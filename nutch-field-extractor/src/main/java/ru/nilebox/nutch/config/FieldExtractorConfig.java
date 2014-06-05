package ru.nilebox.nutch.config;

/**
 *
 * @author nile
 */
public class FieldExtractorConfig {
	private final RegexFilter regexFilter;
	private final XPathFilter xPathFilter;
	
	public FieldExtractorConfig(RegexFilter regexFilter, XPathFilter xPathFilter) {
		this.regexFilter = regexFilter;
		this.xPathFilter = xPathFilter;
	}
	
	public RegexFilter getRegexFilter() {
		return regexFilter;
	}

	public XPathFilter getXPathFilter() {
		return xPathFilter;
	}
}
