package ru.nilebox.nutch.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXBException;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author
 * nile
 */
public class FieldExtractorConfig {

	private static final Logger logger = LoggerFactory.getLogger(FieldExtractorConfig.class);
	private static final String URL_CONFIG_PROPERTY = "filter.url.path";
	private static final String XPATH_CONFIG_PROPERTY = "filter.xpath.path";
	private final RegexUrlFilter urlFilter;
	private final XPathFilter xPathFilter;

	public FieldExtractorConfig(RegexUrlFilter urlFilter, XPathFilter xPathFilter) {
		this.urlFilter = urlFilter;
		this.xPathFilter = xPathFilter;
	}

	public static FieldExtractorConfig getInstance(Configuration configuration) throws UnsupportedEncodingException, IOException, JAXBException {
		// Get URL filter configution from Nutch /conf folder
		InputStream urlConfigStream = configuration.getConfResourceAsInputStream(configuration.get(URL_CONFIG_PROPERTY));
		RegexUrlFilter urlFilter = RegexUrlFilter.load(urlConfigStream);
		urlConfigStream.close();

		// Get XPath filter configuration from Nutch /conf folder
		InputStream xpathConfigStream = configuration.getConfResourceAsInputStream(configuration.get(XPATH_CONFIG_PROPERTY));
		XPathFilter xpathFilter = XPathFilter.load(xpathConfigStream);
		xpathConfigStream.close();

		return new FieldExtractorConfig(urlFilter, xpathFilter);
	}

	public RegexUrlFilter getRegexFilter() {
		return urlFilter;
	}

	public XPathFilter getXPathFilter() {
		return xPathFilter;
	}
}
