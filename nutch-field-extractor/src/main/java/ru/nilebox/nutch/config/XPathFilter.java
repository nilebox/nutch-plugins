package ru.nilebox.nutch.config;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nile
 */
public class XPathFilter {
	private static final Logger logger = LoggerFactory.getLogger(XPathFilter.class);
	
	private static final Map<String, Locale> locales = new ConcurrentHashMap<String, Locale>();
	private XPathFilterConfiguration configuration;
	private final XPath xpath = XPathFactory.newInstance().newXPath();
	private final Map<String, XPathExpression> compiledExpressions = new ConcurrentHashMap<String, XPathExpression>();

	public XPathFilter(XPathFilterConfiguration configuration) {
		this.configuration = configuration;
	}

	public static XPathFilter load(InputStream stream) throws JAXBException {
		// Initialize JAXB
		JAXBContext context = JAXBContext.newInstance(new Class[]{XPathFilterConfiguration.class, XPathFilterSection.class, XPathFilterField.class, FieldType.class});
		Unmarshaller unmarshaller = context.createUnmarshaller();

		// Initialize configuration
		XPathFilterConfiguration configuration = (XPathFilterConfiguration) unmarshaller.unmarshal(stream);

		return new XPathFilter(configuration);
	}

	public static XPathFilter loadFromResource(String path) throws JAXBException, IOException {
		InputStream stream = XPathFilter.class.getClassLoader().getResourceAsStream(path);
		XPathFilter filter = load(stream);
		stream.close();
		return filter;
	}

	public XPathFilterSection findSection(String url) {
		for (XPathFilterSection section : configuration.getSections()) {
			if (section.getUrlFilterPattern().matcher(url).matches()) {
				return section;
			}
		}
		return null;
	}
	
	public XPathFilterFragment findFragment(String url, XPathFilterSection section) {
		for (XPathFilterFragment fragment : section.getFragments()) {
			if (fragment.getUrlRejectPattern()!= null && fragment.getUrlRejectPattern().matcher(url).matches())
				continue;
			if (fragment.getUrlAcceptPattern().matcher(url).matches())
				return fragment;
		}
		return null;
	}

	public Map<String, Object> extractFields(XPathFilterSection section, XPathFilterFragment fragment, Document doc) throws InstantiationException, IllegalAccessException, XPathExpressionException {

		Node root = doc;

		if (fragment.getPageContentFilterXPath() != null) {
			XPathExpression rootExpression = compiledExpressions.get(fragment.getPageContentFilterXPath());
			if (rootExpression == null) {
				rootExpression = xpath.compile(fragment.getPageContentFilterXPath());
				compiledExpressions.put(fragment.getPageContentFilterXPath(), rootExpression);
			}
			root = (Node) rootExpression.evaluate(root, XPathConstants.NODE);
		}

		if (root == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		for (XPathFilterField field : fragment.getFields()) {
			XPathExpression expression = compiledExpressions.get(field.getXPath());
			if (expression == null) {
				expression = xpath.compile(field.getXPath());
				compiledExpressions.put(field.getXPath(), expression);
			}

			if (field.getType() == FieldType.NODE_LIST) {
				NodeList nodeList = (NodeList) expression.evaluate(root, XPathConstants.NODESET);
				if (nodeList != null) {
					map.put(field.getName(), nodeList);
				}
			} else if (field.getType() == FieldType.NODE) {
				Node node = (Node) expression.evaluate(root, XPathConstants.NODE);
				if (node != null) {
					map.put(field.getName(), node);
				}
			} else {
				String value = null;
				if (field.getType() == FieldType.STRING) {
					//Inner xml may contain delimites that can help to split data inside
					//result string later
					Node node = (Node) expression.evaluate(root, XPathConstants.NODE);
					if (node == null)
						continue;
					value = node.getTextContent();
					if (value != null)
						value = StringEscapeUtils.unescapeHtml(value).trim();
				} else {
					value = (String) expression.evaluate(root, XPathConstants.STRING);
				}
				if (value != null && value.length() > 0) {
					map.put(field.getName(), parseFieldValue(value, field));
				}
			}
		}

		if (map.isEmpty()) {
			return null;
		}

		return map;
	}

	private static Object parseFieldValue(String stringValue, XPathFilterField field) {
		Object value = null;
		switch (field.getType()) {
			case STRING:
				value = stringValue;
				break;
			case INTEGER:
				value = Integer.valueOf(stringValue);
				break;
			case LONG:
				value = Long.valueOf(stringValue);
				break;
			case DOUBLE:
				value = Double.valueOf(stringValue);
				break;
			case FLOAT:
				value = Float.valueOf(stringValue);
				break;
			case BOOLEAN:
				value = Boolean.valueOf(stringValue);
				break;
			case DATE:
				// Create SimpleDateFormat object to parse string
				value = null;
				for (XPathFilterDateFormat dateFormat : field.getDateFormats()) {
					String dateFormatString = dateFormat.getFormat() == null ? "dd.MM.yyyy" : dateFormat.getFormat();
					Locale locale = dateFormat.getLocale() == null ? Locale.getDefault() : getLocale(dateFormat.getLocale());
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatString, locale);
					simpleDateFormat.setLenient(true);
					try {
						value = simpleDateFormat.parse(stringValue);
						break;
					} catch (ParseException ex) {
						value = null;
					}
				}
				break;
			default:
				value = stringValue;
				break;
		}

		return value;
	}

	private static Locale getLocale(String localeStr) {
		Locale locale = locales.get(localeStr);
		if (locale != null) {
			return locale;
		}
		String[] parts = StringUtils.split(localeStr, "-");
		locale = new Locale(parts[0], parts[1]);
		locales.put(localeStr, locale);
		return locale;
	}
	
}
