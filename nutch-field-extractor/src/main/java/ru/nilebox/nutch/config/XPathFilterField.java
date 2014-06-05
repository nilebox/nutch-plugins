package ru.nilebox.nutch.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author nile
 */
public class XPathFilterField {
	private String name;
    private String xPath;
    private FieldType type;
    private List<XPathFilterDateFormat> dateFormats = new ArrayList<XPathFilterDateFormat>();
    private Boolean trimXPathData;

	@XmlAttribute(name = "name", required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "xPath", required = true)
	public String getXPath() {
		return xPath;
	}

	public void setXPath(String xPath) {
		this.xPath = xPath;
	}

	@XmlAttribute(name = "type", required = true)
	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	@XmlAttribute(name = "trimXPathData", required = false)
	public Boolean getTrimXPathData() {
		return trimXPathData;
	}

	public void setTrimXPathData(Boolean trimXPathData) {
		this.trimXPathData = trimXPathData;
	}

	@XmlElement(name="dateFormat", nillable=true)
	public List<XPathFilterDateFormat> getDateFormats() {
		return dateFormats;
	}

	public void setDateFormats(List<XPathFilterDateFormat> dateFormats) {
		this.dateFormats = dateFormats;
	}
	
}
