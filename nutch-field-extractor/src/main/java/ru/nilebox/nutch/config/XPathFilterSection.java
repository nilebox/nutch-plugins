package ru.nilebox.nutch.config;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author nile
 */
public class XPathFilterSection {
	private String urlFilterRegex;
	private Pattern urlFilterPattern;
	private List<XPathFilterFragment> fragments = new ArrayList<XPathFilterFragment>(); 

	@XmlAttribute(name="urlFilterRegex", required=false)
	public String getUrlFilterRegex() {
		return urlFilterRegex;
	}

	public void setUrlFilterRegex(String urlFilterRegex) {
		urlFilterRegex = urlFilterRegex.toLowerCase();
		this.urlFilterRegex = urlFilterRegex;
		this.urlFilterPattern = Pattern.compile(urlFilterRegex);
	}

	@XmlElement(name="fragment", nillable=false)
	public List<XPathFilterFragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<XPathFilterFragment> fragments) {
		this.fragments = fragments;
	}

	public Pattern getUrlFilterPattern() {
		return urlFilterPattern;
	}
}
