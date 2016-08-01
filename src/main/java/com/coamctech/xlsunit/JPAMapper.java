package com.coamctech.xlsunit;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.NamingStrategy;
import org.springframework.beans.BeanUtils;



public class JPAMapper implements Mapper {
	Log log = LogFactory.getLog(JPAMapper.class);
	Map items = new CaseInsensitiveHashMap();
	NamingStrategy ns = null;
	String basePkg;
	public JPAMapper(NamingStrategy ns,String basePkg){
		this.basePkg = basePkg;
		this.ns = ns ;
		init();
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
	
	private void init(){
		 for (Class<?> c : new ClassScaner(basePkg,
	                new JPAEntityScanerFilter()).getClasses()) {
	          
			 MapperItem item = null;
			  Table table = c.getAnnotation(Table.class);
			  if(table!=null){
				  String name = table.name();
				  item = new MapperItem(c,name);
				  item.setMapper(this);
				  this.initMapItem(item);
				  items.put(name, item);
			  }else{
				  String name = c.getSimpleName();
				  String tableName = ns.classToTableName(name);
				  item = new MapperItem(c,name);
				  this.initMapItem(item);
				  item.setMapper(this);
				  items.put(tableName, item);
			  }
			  
			  IdClass idClass = c.getAnnotation(IdClass.class);
			  if(idClass!=null){
				  Class pkClass= idClass.value();
				  item.setIdClass(pkClass.getName());
			  }
			
	        }
	}
	
	private void addEmdId(Class c,MapperItem item,String preffix){
		//TODO 假设emdId只在属性里，而不是getter/setter 里
		Field[] fs = ClassUtil.getField(c);
		for(Field f:fs){
			Column colAnno = f.getAnnotation(Column.class);
			if(colAnno==null){
				continue;
			}
			
			String colName = colAnno.name();
			String attrName = f.getName();
			if(StringUtils.isEmpty(colName)){
				colName = this.ns.propertyToColumnName(attrName);
			}
			item.addColMap(preffix+"."+attrName, colName);
			
		}
		
	}
	
	@Override
	public void initMapItem(MapperItem item) {
		Class c = item.getClazz();
		Field[] fs = ClassUtil.getField(c);
		for(Field f:fs){
			Column colAnno = getAnnotation(f,Column.class);
			if(colAnno!=null){
				String colName = colAnno.name();
				String attrName = f.getName();
				if(StringUtils.isEmpty(colName)){
					colName = this.ns.propertyToColumnName(attrName);
				}
				item.addColMap(attrName, colName);
				continue ;
			}
			
			Id idAnno = f.getAnnotation(Id.class);
			if(colAnno==null&&idAnno!=null){
				String attrName = f.getName();
				item.addColMap(attrName, this.ns.propertyToColumnName(attrName));
				continue ;
			}
			
				
			EmbeddedId eid = getAnnotation(f,EmbeddedId.class);
			if(eid==null){
				continue ;
			}else{
				String name = f.getName();
				addEmdId(f.getType(),item,name);
				item.setEmbedAttr(name);
				item.setEmbeddedIdClass(f.getType().getName());
			}
			continue ;
			
		}
		
		
		PropertyDescriptor[] ps =  BeanUtils.getPropertyDescriptors(c);
		
		
		for(PropertyDescriptor p:ps){
			Column colAnno = null;
			Id idAnno = null;
			if( p.getReadMethod()!=null){
				colAnno = p.getReadMethod().getAnnotation(Column.class);
				if(colAnno!=null){
					String colName = colAnno.name();
					String attrName = p.getName();
					if(StringUtils.isEmpty(colName)){
						colName = this.ns.propertyToColumnName(attrName);
					}
					item.addColMap(attrName,colName );
					continue ;
				}
				
				idAnno =  p.getReadMethod().getAnnotation(Id.class);
				if(colAnno==null&&idAnno!=null){
					String attrName = p.getName();
					item.addColMap(attrName, this.ns.propertyToColumnName(attrName));
				}
				
			}
			
			if(p.getWriteMethod()!=null){
				colAnno = p.getWriteMethod().getAnnotation(Column.class);
				if(colAnno!=null){
					String colName = colAnno.name();
					String attrName = p.getName();
					if(StringUtils.isEmpty(colName)){
						colName = this.ns.propertyToColumnName(attrName);
					}
					item.addColMap(attrName,colName );
					continue ;
				}
				
				idAnno =  p.getWriteMethod().getAnnotation(Id.class);
				if(colAnno==null&&idAnno!=null){
					String attrName = p.getName();
					item.addColMap(attrName, this.ns.propertyToColumnName(attrName));
				}
			}
			
				
			// 忽略这个属性
		}
		
		
		
	}
	
	
	
	private <T extends Annotation> T  getAnnotation(Object o,Class<T> annotationClass){
		if(o instanceof Method){
			return ((Method)o).getAnnotation(annotationClass);
		}else if( o instanceof Field){
			return ((Field)o).getAnnotation(annotationClass);
		}else {
			throw new RuntimeException("不支持类型");
		}
	}
	
	
	
	
	 static class JPAEntityScanerFilter implements ClassScanerFilter{
		 Log log = LogFactory.getLog(JPAEntityScanerFilter.class);
		  @Override
          public boolean accept(Class<?> clazz) {
              if (clazz == null) {
                  return false;
              }
              
              if (clazz.isInterface()) {
                  return false;
              }
              if (Modifier.isAbstract(clazz.getModifiers())) {
                  return false;// 抽象
              }
              
              Entity entity = clazz.getAnnotation(Entity.class);
              if(entity!=null){
            	  return true;
              }else{
            	  return false;
              }
             
              
          }
		 
	 }






	@Override
	public Object mapper(Map<String, Object> src, String tableName)  {
		MapperItem item = this.getItem(tableName);
		Class c = item.getClazz();
	
		Object ins = null;
		if(item.getEmbeddedIdClass()!=null){
			ins = ClassUtil.newInstance(c.getName(),item.getEmbedAttr());
			
		}else{
			ins = ClassUtil.newInstance(c.getName());
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
			if(index!=-1){
				//通过是否有. 来判断是idClass 或者 EmbeddedIdClass
				String realAttr = attr.substring(index+1);
				orig.put(realAttr,value);
			}else{
				orig.put(attr,value);
			}
			
		}
		Object ins = null;
		if(item.isIdClass()){
			ins = ClassUtil.newInstance(item.getIdClass());
		}else{
			ins = ClassUtil.newInstance(item.getEmbeddedIdClass());
		}
		try {
			XLSBeanUtil.copyPropesrties(orig, ins);
		} catch (Exception e) {
			throw new RuntimeException("赋值出错:"+item.getEmbeddedIdClass()+"or"+item.getIdClass()+" with "+src);
		} 
		return ins;
		
	}



}
