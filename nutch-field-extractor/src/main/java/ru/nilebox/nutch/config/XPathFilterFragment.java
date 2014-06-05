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
public class XPathFilterFragment {
	private String urlAcceptRegex;
	private Pattern urlAcceptPattern;
	private String urlRejectRegex;
	private Pattern urlRejectPattern;
	private String pageContentFilterXPath;
	private String handlerClassName;
	private List<XPathFilterField> fields = new ArrayList<XPathFilterField>(); 

	@XmlAttribute(name="urlAcceptRegex", required=false)
	public String getUrlAcceptRegex() {
		return urlAcceptRegex;
	}

	public void setUrlAcceptRegex(String urlAcceptRegex) {
		urlAcceptRegex = urlAcceptRegex.toLowerCase();
		this.urlAcceptRegex = urlAcceptRegex;
		this.urlAcceptPattern = Pattern.compile(urlAcceptRegex);
	}

	@XmlAttribute(name="urlRejectRegex", required=false)
	public String getUrlRejectRegex() {
		return urlRejectRegex;
	}

	public void setUrlRejectRegex(String urlRejectRegex) {
		urlRejectRegex = urlRejectRegex.toLowerCase();
		this.urlRejectRegex = urlRejectRegex;
		this.urlRejectPattern = Pattern.compile(urlRejectRegex);
	}

	@XmlAttribute(name="pageContentFilterXPath", required=false)
	public String getPageContentFilterXPath() {
		return pageContentFilterXPath;
	}

	public void setPageContentFilterXPath(String pageContentFilterXPath) {
		this.pageContentFilterXPath = pageContentFilterXPath;
	}

	@XmlAttribute(name="handlerClass", required=false)
	public String getHandlerClassName() {
		return handlerClassName;
	}

	public void setHandlerClass(String handlerClassName) {
		this.handlerClassName = handlerClassName;
	}

	@XmlElement(name="field", nillable=false)
	public List<XPathFilterField> getFields() {
		return fields;
	}

	public void setFields(List<XPathFilterField> fields) {
		this.fields = fields;
	}
	
	public Pattern getUrlAcceptPattern() {
		return urlAcceptPattern;
	}
	
	public Pattern getUrlRejectPattern() {
		return urlRejectPattern;
	}
}
