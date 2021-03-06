package org.texttemplate;

import org.texttemplate.util.XMLHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TTEngine {
	
	private static HashMap<String, TTag> smartTags = new HashMap<String, TTag>();
	
	private static String getDataForTag(TTag tag, Object[] dataObjs, String key){
		String result = null;
		try {
			Class c1 = Class.forName(tag.getTagObjectClass());
			Method f1 = c1.getMethod(tag.getTagObjectField(),null);
			Object dataObj = null;
			for (int i = 0; i < dataObjs.length; i++) {
				try {
					dataObj = c1.cast(dataObjs[i]);
					break;
				} catch (Exception e) {}
			}
			Object returnObj = f1.invoke(dataObj,null);
			switch (tag.getTagObjectFieldType()) {
			case TTag.FIELD_TYPE_DATA:
				result = returnObj.toString();
				return result;
			case TTag.FIELD_TYPE_NESTED_OBJECT:
				Object nestedObj = returnObj;
				Class c3 = Class.forName(tag.getTagNestedClass());
				Method f3 = c3.getMethod(tag.getTagNestedField());
				result = f3.invoke(nestedObj,null).toString();
				return result;
			case TTag.FIELD_TYPE_COLLECTION:
				HashMap hm1 = (HashMap) returnObj;
				result = hm1.get(key).toString();
				return result;
			case TTag.FIELD_TYPE_LIST_COMPLETE:
				List l = (List) returnObj;
				for (Object nestedListObj : l) {
					Class c4 = Class.forName(tag.getTagNestedClass());
					Method f4 = c4.getMethod(tag.getTagNestedField());
					result += f4.invoke(nestedListObj,null).toString()+":";
					if(tag.getTagNestedField2()!=null && !"".equalsIgnoreCase(tag.getTagNestedField2())){
						Method f5 = c4.getMethod(tag.getTagNestedField2());
						Object obj5 = f5.invoke(nestedListObj,null);
						String temp5 = obj5==null?"":obj5.toString();
						result += temp5;
					}else
						result += "N/A";
						
					result += "|";
				}
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void initEngine() throws FileNotFoundException {
		
		ResourceBundle rb = ResourceBundle.getBundle("resources.folder");
		String smartTagFile = rb.getString("email.smarttag.xml");
		XMLHelper xmlHelper = new XMLHelper();
		xmlHelper.load(new FileInputStream(new File(smartTagFile)));
		Node[] smartTags1 = xmlHelper.getElements();
		Node rootTag = smartTags1[0];
		NodeList nodes = rootTag.getChildNodes();
		for(int i = 0; i<nodes.getLength();i++){
			Node smartTagNode = nodes.item(i);
			if(smartTagNode.getNodeType()==Node.ELEMENT_NODE){
				// its a smart tag
				TTag objSmartTag = new TTag();
				objSmartTag.setTagName(smartTagNode.getAttributes().item(0).getNodeValue());

				NodeList childNodes = smartTagNode.getChildNodes();
				Node tagClass = childNodes.item(1);
				Node tagField = childNodes.item(3);
				
				objSmartTag.setTagObjectClass(tagClass.getChildNodes().item(0).getNodeValue());
				objSmartTag.setTagObjectField(tagField.getChildNodes().item(0).getNodeValue());
				objSmartTag.setTagObjectFieldType(Integer.parseInt(tagField.getAttributes().item(0).getNodeValue()));
				if(objSmartTag.getTagObjectFieldType()==TTag.FIELD_TYPE_NESTED_OBJECT || objSmartTag.getTagObjectFieldType()==TTag.FIELD_TYPE_LIST_COMPLETE){
					Node tagNestedClass = childNodes.item(5);
					Node tagNestedField = childNodes.item(7);
					Node tagNestedField2 = childNodes.item(9);
					objSmartTag.setTagNestedClass(tagNestedClass.getChildNodes().item(0).getNodeValue());
					objSmartTag.setTagNestedField(tagNestedField.getChildNodes().item(0).getNodeValue());
					if(tagNestedField2!=null)
						objSmartTag.setTagNestedField2(tagNestedField2.getChildNodes().item(0).getNodeValue());
				}
				smartTags.put(objSmartTag.getTagName(), objSmartTag);
			}
		}
	}
	
	//replace tags with data in the template content
	/*
	 * This would look through the template content for regular expressions
	 * and all encountered tags would be looked up in the hashmap for a match
	 * and invoke the getDataForTag method to replace it with data.
	 */
	public static String doMerge(String templateContent, Object[] dataObjects) throws Exception{
		if(smartTags.size()==0)
			throw new Exception("Engine not initialized");
        String regex = "(?<=##)[.[^##]]+(?=##)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(templateContent);
        HashMap<String, String> replacementMap = new HashMap<String, String>();
        String tagFound = "";
        while (matcher.find() ) {
          tagFound = matcher.group();
          String keyFound = "";
		  TTag tag = (TTag) smartTags.get(tagFound);
		  if(tag!=null){
			  String tagData = getDataForTag(tag, dataObjects, keyFound);
	          replacementMap.put("##"+tagFound+"##", tagData);
		  }
        }
        matcher.reset();
        
        StringBuffer actualContent = new StringBuffer(templateContent);
        StringBuffer tempContent = new StringBuffer();
        int lastFound = 0;
        for (Iterator<String> itr =  replacementMap.keySet().iterator(); itr.hasNext();) {
            pattern = Pattern.compile(itr.next());
            matcher = pattern.matcher(actualContent);
            while (matcher.find()) {
              if (replacementMap.get(pattern.pattern()) != null)
                matcher.appendReplacement(tempContent, replacementMap.get(pattern.pattern()));
              else
                 matcher.appendReplacement(tempContent, "");
              lastFound = matcher.end();
            }

            for(int i = lastFound, j = actualContent.length(); i < j; i++) {
               tempContent.append(actualContent.charAt(i));
            }

            actualContent.setLength(0);
            actualContent.append(tempContent);
            tempContent.setLength(0);
        }
		return actualContent.toString();
	}

//	public static void main(String[] args) throws FileNotFoundException {
//		TTEngine.initTags();
//		String returnValue = TTEngine.replaceTagsWithData("Dear Human, \n##Check.Fields##", new Object[]{check});
//		System.out.println(returnValue);
//	}

}
