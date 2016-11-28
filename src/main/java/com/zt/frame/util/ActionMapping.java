package com.zt.frame.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * Created by zhoutao on 2016/11/28.
 */
public class ActionMapping {
	public static final String config = "struts.xml";

	public static Document parseXML() throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(ActionMapping.class.getClassLoader().getResourceAsStream(config));
		return document;
	}

}
