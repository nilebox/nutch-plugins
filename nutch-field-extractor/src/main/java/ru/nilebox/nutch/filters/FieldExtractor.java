package ru.nilebox.nutch.filters;

import java.util.Map;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import ru.nilebox.nutch.config.FieldExtractorConfig;
import ru.nilebox.nutch.config.XPathFilterFragment;
import ru.nilebox.nutch.config.XPathFilterSection;

/**
 *
 * @author nile
 */
public class FieldExtractor {
	
	private FieldExtractorConfig config;
	
	public FieldExtractor(FieldExtractorConfig config) {
		config = config;
	}
	
	public boolean shouldVisit(String url) {
		String lowerUrl = url.toLowerCase();
		return getFilterFragment(lowerUrl) != null;		
	}
	
	public Map<String, Object> extractFields(String url, Document doc) throws InstantiationException, IllegalAccessException, XPathExpressionException {
		XPathFilterSection section = getFilterSection(url);
		XPathFilterFragment fragment = getFilterFragment(url, section);
		Map<String, Object> fields = config.getXPathFilter().extractFields(section, fragment, doc);
		return fields;
	}
	
	private XPathFilterSection getFilterSection(String url) {
		XPathFilterSection section = config.getXPathFilter().findSection(url);
		return section;
	}
	
	private XPathFilterFragment getFilterFragment(String url, XPathFilterSection section) {
		XPathFilterFragment fragment = config.getXPathFilter().findFragment(url, section);
		return fragment;
	}
	
	private XPathFilterFragment getFilterFragment(String url) {
		XPathFilterSection section = getFilterSection(url);
		if (section == null)
			return null;
		XPathFilterFragment fragment = getFilterFragment(url, section);
		return fragment;	
	}
	
}
