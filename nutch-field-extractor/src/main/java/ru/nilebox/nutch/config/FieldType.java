package ru.nilebox.nutch.config;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nile
 */
@XmlEnum
@XmlRootElement(name="type")
public enum FieldType {
	
	@XmlEnumValue("BOOLEAN")
	BOOLEAN, 
	
	@XmlEnumValue("FLOAT")
	FLOAT, 
	
	@XmlEnumValue("DOUBLE")
	DOUBLE, 
	
	@XmlEnumValue("STRING")
	STRING, 
	
	@XmlEnumValue("INTEGER")
	INTEGER,
	
	@XmlEnumValue("LONG")
	LONG,
	
	@XmlEnumValue("DATE")
	DATE,
	
	@XmlEnumValue("NODE")
	NODE,
	
	@XmlEnumValue("NODE_LIST")
	NODE_LIST,
}
