package com.coamctech.xlsunit;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EmbeddedId;

public class MapperItem {
	private Class clazz;
	private String tableName;
	private Map colItems  = new CaseInsensitiveHashMap();;
	private Mapper mapper;
	private String embedAttr = null;
	private String embeddedIdClass = null;
	private String idClass = null;
	
	public MapperItem(Class clazz,String tableName){
		this.clazz = clazz ;
		this.tableName = tableName;
	}
	
	public void addColMap(String attrName,String colName){
		colItems.put(colName, attrName);
	}
	
	
	
	public String getAttr(String colName){
		
		String attr = (String)colItems.get(colName);
//		if(attr==null){
//			throw new RuntimeException("can not find attr by col "+colName+" in "+clazz);
//		}
		return attr;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, String> getColItems() {
		return colItems;
	}

	public void setColItems(Map<String, String> colItems) {
		this.colItems = colItems;
	}

	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	
	}

	public String getEmbedAttr() {
		return embedAttr;
	}

	public void setEmbedAttr(String embedAttr) {
		this.embedAttr = embedAttr;
	}



	public String getIdClass() {
		return idClass;
	}

	public void setIdClass(String idClass) {
		this.idClass = idClass;
	}

	public String getEmbeddedIdClass() {
		return embeddedIdClass;
	}

	public void setEmbeddedIdClass(String embeddedIdClass) {
		this.embeddedIdClass = embeddedIdClass;
	}

	public boolean isIdClass(){
		return this.idClass!=null;
	}

	
	
}
