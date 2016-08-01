package com.coamctech.xlsunit;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 *  同jpa，配置来源于hibernate的xml配置，其中不支持idclass。因为目前实际项目没有用到，如果需要，参考JPAMapper
 * @author lijiazhi
 *
 */
public class HibernateMapper implements Mapper {
	Log log = LogFactory.getLog(HibernateMapper.class);
	Map items = new CaseInsensitiveHashMap();
	String configFile;
	SAXBuilder jdomBuilder = null;
	String nodePath = null;
	
	public HibernateMapper(String configFile,String nodePath){
		this.configFile = configFile;
		this.nodePath = nodePath;
		try {
			
			jdomBuilder = new SAXBuilder(XMLReaders.NONVALIDATING);
			jdomBuilder.setFeature(
					  "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			jdomBuilder.setEntityResolver(new NoOpEntityResolver());
			
			init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
		
	}
	
	@Override
	public String getClassName(String tableName) {
		MapperItem item = getItem(tableName);
		return item.getClazz().getName();
	}

	@Override
	public String getAttrName(String tableName,String colName) {
		MapperItem item = getItem(tableName);
		String attr =  item.getAttr(colName);
		if(attr==null){
			return colName;
		}else{
			return attr;
		}
		
	}
	
	public List<String> getAttrName(String tableName,List<String> cols){
		List<String> attrs = new ArrayList<String>(cols.size());
		for(String col:cols){
			attrs.add(getAttrName(tableName,col));
		}
		return attrs;
	}
	
	private MapperItem getItem(String tableName){
		MapperItem item = (MapperItem)items.get(tableName);
		if(item==null){
			throw new RuntimeException("找不到 "+tableName+"定义的类");
		}
		return item;
	}
	
	private void init() throws JDOMException, IOException{
		 Document doc =  jdomBuilder.build(ClassUtil.getInputStream(configFile));
		 Element root = doc.getRootElement();
		 List<Element> list = root.getChildren();
		 for(Element el:list){
			 String id = el.getAttributeValue("id");
			 if(id.equals(nodePath)){
				 for(Element ps :el.getChildren()){
					 if("mappingResources".equals(ps.getAttributeValue("name"))){
						 Element files =  ps.getChildren().get(0);
						 for(Element file :files.getChildren()){
							 String f = file.getText();
							 initFile(f);
						 }
					 }
				 }
			 }
		 }
	}
	
	private void initFile(String f) throws JDOMException, IOException{
		 Document doc =  jdomBuilder.build(ClassUtil.getInputStream(f));
		 Element root = doc.getRootElement();
		 String pkg = root.getAttributeValue("package");
		 for(Element clsEl:root.getChildren()){
			 String name = clsEl.getAttributeValue("name");
			 String clsName = pkg+"."+name;
			 Class cls = ClassUtil.loadClass(clsName);
			 String table = clsEl.getAttributeValue("table");
			 MapperItem item = new MapperItem(cls,table);
			 items.put(table, item);
			 
			 // 寻找id
			 
			 for(Element colEl:clsEl.getChildren()){
				 String tagName = colEl.getName();
				 if(tagName.equals("id")){
					 initId(item,colEl);
				 }else if(tagName.equals("property")){
					 intiCol(item,colEl);
				 }else if(tagName.equals("composite-id ")){
					 initIds(item,colEl);
				 }
				 
				 else{
					 throw new RuntimeException("不支持的hibernate 配置");
				 }
			 }
			 
		 }
	}
	
	private void initIds(MapperItem item,Element idEle){
		String clsName = idEle.getAttributeValue("class");
		item.setEmbeddedIdClass(clsName);
		String pkAttr = idEle.getAttributeValue("name");
		item.setEmbedAttr(pkAttr);
		for(Element e:idEle.getChildren()){
			String attrName = e.getAttributeValue("name");
			String colName = e.getAttributeValue("column");
			item.addColMap(attrName, colName);
			
		}
	
		
	}
	
	private void initId(MapperItem item,Element idEle){
		String name = idEle.getAttributeValue("name");
		String colName = idEle.getAttributeValue("column");
		item.addColMap(name, colName);
		
	}
	
	private void intiCol(MapperItem item,Element colEle){
		String attrName = colEle.getAttributeValue("name");
		String colName = colEle.getAttributeValue("column");
		item.addColMap(attrName, colName);
	}
	

	
	@Override
	public void initMapItem(MapperItem item) {

		// 已经初始化了，本来用来延迟初始化以提高性能，留在以后做
		
	}
	
	



	@Override
	public Object mapper(Map<String, Object> src, String tableName)  {
		MapperItem item = this.getItem(tableName);
		Class c = item.getClazz();
		String pk = item.getEmbedAttr();
		Object ins = null;
		if(pk==null){
			ins = ClassUtil.newInstance(c.getName());
		}else{
			ins = ClassUtil.newInstance(c.getName(), pk);
		}
		try {
			XLSBeanUtil.copyPropesrties(src, ins);
		} catch (Exception e) {
			throw new RuntimeException("tableName "+tableName,e);
		}
		
		return ins;
	}

	@Override
	public Object mapperId(Map<String, Object> src, String tableName) {
		Map<String, Object> orig = new HashMap<String, Object>();
		MapperItem item = this.getItem(tableName);
		for(Entry<String,Object> entry:src.entrySet()){
			String col = entry.getKey();
			Object value = entry.getValue();
			String attr = item.getAttr(col);
			int index = attr.indexOf(".");
			String realAttr = attr.substring(index+1);
			orig.put(realAttr,value);
		}
		
		
		Object ins = ClassUtil.newInstance(item.getEmbeddedIdClass());
		try {
			XLSBeanUtil.copyPropesrties(orig, ins);
		} catch (Exception e) {
			throw new RuntimeException("赋值出错:"+item.getEmbeddedIdClass()+" with "+src);
		} 
		return ins;
		
	}

	public static class NoOpEntityResolver implements EntityResolver {
		  public InputSource resolveEntity(String publicId, String systemId) {
		    return new InputSource(new StringBufferInputStream(""));
		  }
		}

}
