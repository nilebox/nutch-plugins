package ru.nilebox.nutch.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nile
 */
@XmlRootElement(name = "xpathFilterConfiguration")
public class XPathFilterConfiguration {

	private List<XPathFilterSection> sections = new ArrayList<XPathFilterSection>();

	@XmlElement(name="section", nillable=false)
	public List<XPathFilterSection> getSections() {
		return sections;
	}

	public void setSections(List<XPathFilterSection> sections) {
		this.sections = sections;
	}
	
}
