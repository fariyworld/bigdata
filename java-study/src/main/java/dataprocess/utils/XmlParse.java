package dataprocess.utils;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * DOM4J 解析xml
 * @author 15257
 *
 */
public class XmlParse {

	public static void readXml(String pathname){
		try {
			SAXReader saxReader= new SAXReader();
			Document document = saxReader.read(new File(pathname));
			Element rootElement= document.getRootElement();
			Element senceElement;
			for( Iterator<?> elementIterator = rootElement.elementIterator("scence"); elementIterator.hasNext();){
				senceElement = (Element) elementIterator.next();
				System.out.println(senceElement.getName().trim());
				System.out.println(senceElement.getText());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
