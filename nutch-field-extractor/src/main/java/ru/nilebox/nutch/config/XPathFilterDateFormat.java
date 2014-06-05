package ru.nilebox.nutch.config;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author nile
 */
public class XPathFilterDateFormat {
	private String format;
	private String locale;

	@XmlAttribute(name = "format", required = false)
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlAttribute(name = "locale", required = false)
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
}
