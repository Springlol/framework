package com.zt.frame.util;

import com.zt.frame.entity.Action;
import com.zt.frame.entity.Result;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoutao on 2016/11/28.
 */
public class ActionMapping {
	public static final String config = "struts.xml";
	public static Map<String,Object> context = new HashMap<String, Object>();

	public static void parseXML() throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(ActionMapping.class.getClassLoader().getResourceAsStream(config));
		Element root = document.getRootElement();
		List<Element> elements = root.elements();
		for (Element element : elements) {
			Action action = new Action();
			String name = element.attributeValue("name");
			String className = element.attributeValue("class");
			String method = element.attributeValue("method");
			action.setName(name);
			action.setClasses(className);
			if (method != null) {
				action.setMethod(method);
			}

			List<Element> results = element.elements();
			for (Element ele : results) {
				Result result = new Result();
				String rName = ele.attributeValue("name");
				String rType = ele.attributeValue("type");
				String rLocation = ele.getText();
				if(rName != null) {
					result.setName(rName);
				}
				if (rType != null) {
					result.setType(rType);
				}
				result.setLocation(rLocation);
				action.getResult().put(result.getName(),result);
			}

			context.put(name,action);

		}

	}

	public static Map<String, Object> getContext() {
		return context;
	}

}
