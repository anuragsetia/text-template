package org.texttemplate;

public class TTag {
	
	public static final int FIELD_TYPE_DATA=1;
	public static final int FIELD_TYPE_NESTED_OBJECT=2;
	public static final int FIELD_TYPE_COLLECTION=3;
	public static final int	FIELD_TYPE_LIST_COMPLETE=4;

	private String tagName;
	private String tagObjectClass;
	private String tagObjectField;
	private int tagObjectFieldType;
	
	private String tagNestedClass;
	private String tagNestedField;
	private String tagNestedField2;
	
	/**
	 * @return the tagNestedClass
	 */
	public String getTagNestedClass() {
		return tagNestedClass;
	}
	/**
	 * @param tagNestedClass the tagNestedClass to set
	 */
	public void setTagNestedClass(String tagNestedClass) {
		this.tagNestedClass = tagNestedClass;
	}
	/**
	 * @return the tagNestedField
	 */
	public String getTagNestedField() {
		return tagNestedField;
	}
	/**
	 * @param tagNestedField the tagNestedField to set
	 */
	public void setTagNestedField(String tagNestedField) {
		this.tagNestedField = tagNestedField;
	}
	/**
	 * @return the tagNestedField2
	 */
	public String getTagNestedField2() {
		return tagNestedField2;
	}
	/**
	 * @param tagNestedField2 the tagNestedField2 to set
	 */
	public void setTagNestedField2(String tagNestedField2) {
		this.tagNestedField2 = tagNestedField2;
	}
	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}
	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	/**
	 * @return the tagObjectClass
	 */
	public String getTagObjectClass() {
		return tagObjectClass;
	}
	/**
	 * @param tagObjectClass the tagObjectClass to set
	 */
	public void setTagObjectClass(String tagObjectClass) {
		this.tagObjectClass = tagObjectClass;
	}
	/**
	 * @return the tagObjectField
	 */
	public String getTagObjectField() {
		return tagObjectField;
	}
	/**
	 * @param tagObjectField the tagObjectField to set
	 */
	public void setTagObjectField(String tagObjectField) {
		this.tagObjectField = tagObjectField;
	}
	/**
	 * @return the tagObjectFieldType
	 */
	public int getTagObjectFieldType() {
		return tagObjectFieldType;
	}
	/**
	 * @param tagObjectFieldType the tagObjectFieldType to set
	 */
	public void setTagObjectFieldType(int tagObjectFieldType) {
		this.tagObjectFieldType = tagObjectFieldType;
	}
	
	

}
