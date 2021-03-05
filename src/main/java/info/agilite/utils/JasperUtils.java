package info.agilite.utils;

import java.math.BigDecimal;
import java.util.List;

import info.agilite.utils.xml.ElementXml;


public class JasperUtils {
	public static BigDecimal decimal(Object xml, String nome) {
		String val = string(xml, nome);
		if(val == null || val.length() == 0)return null;
		return new BigDecimal(val);
	}
	
	public static String string(Object xml, String nome) {
		String val = ((ElementXml)xml).findChildValue(nome);
		return val == null ? "" : val;
	}
	
	public static String string(Object xml, String parent, int index, String valueName) {
		List<ElementXml> elem = ((ElementXml)xml).findChildNodes(parent);
		
		try {
			ElementXml xmlChild = elem.get(index);
			String val = xmlChild.findChildValue(valueName);
			return val == null ? "" : val;
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
}
