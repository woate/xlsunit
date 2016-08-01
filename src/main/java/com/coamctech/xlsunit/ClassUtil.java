package com.coamctech.xlsunit;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;



public class ClassUtil {
	public static Class loadClass(String clsName){
		try{
			Class cls = Class.forName(clsName);
			return cls;
		}catch(Exception ex){
			throw new RuntimeException("load "+clsName+" error",ex);
		}
	}
	
	
	public static Object newInstance(String clsName){
		Class c = loadClass(clsName);
		try {
			return c.newInstance();
		}  catch (Exception e) {
			throw new RuntimeException("instance "+clsName+" error",e);
		}
	}
	
	
	public static Object newInstance(String clsName,String field){
		Class c = loadClass(clsName);
		try {
			Object ins =  c.newInstance();
			Field f = c.getDeclaredField(field);
			f.setAccessible(true);
			Class t = f.getType();
			Object attr = t.newInstance();
			f.set(ins, attr);
			return ins;
			
		}  catch (Exception e) {
			throw new RuntimeException("instance "+clsName+" error",e);
		}
	}
	
	public static InputStream getInputStream(String classpath){
		InputStream ins = null;
		try {
			URL url = ClassUtil.class.getResource(classpath);
			if(url!=null){
				ins = url.openStream();
				return ins ;
			}
			
		} catch (IOException e) {
			//ignore,
		}
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(loader!=null){
			ins = loader.getResourceAsStream(classpath);
			if(ins!=null){
				return ins;
			}
		}
		
		
		throw new RuntimeException("加载excel出错:"+classpath);
	}
	
	
	public static Object valueOf(Object entity,String attr){
		try {
			return  XLSBeanUtil.getProperty(entity, attr);
		} catch (Exception e) {
			throw new RuntimeException("获取属性出错 "+attr+" "+entity,e);
		} 
		
	}
	
	public static void setValue(Object entity,String attr,Object value){
		try {
			 XLSBeanUtil.setProperty(entity, attr,value);
		} catch (Exception e) {
			throw new RuntimeException("获取属性出错 "+attr+" "+entity,e);
		} 
		
	}
	
	public static Field[] getField(Class c ){
		List<Field> list = new ArrayList<Field>();
		Field[] fs = c.getDeclaredFields();
		list.addAll(Arrays.asList(fs));
		while(c.getSuperclass()!=null){
			
			c = c.getSuperclass();
			
			if(c==Object.class||c.isPrimitive()||c.isInterface()){
				continue ;
			}
			fs = c.getDeclaredFields();
			list.addAll(Arrays.asList(fs));
			
		}
		return list.toArray(new Field[0]);
	}
	
	
	public static boolean compare(Object a,Object b,List<String> attrs,CompareInfo info){
		for(String attr:attrs){
			
			try {
				Object o1 = XLSBeanUtil.getProperty(a, attr);
				Object o2 = XLSBeanUtil.getProperty(b, attr);
				if(!isSame(o1,o2)){
					info.setColName(attr);
					info.setExpected(o1);
					info.setReal(o2);
					return false;
				}
		
			} catch (Exception e) {
				throw new RuntimeException("比较数据 出错,未找到方法"+attr,e);
			}
							
		}
		return true;
		
	}
	
	/**
	 * 解析sql,得到变量名字
	 * @param sql
	 * @return
	 */
	public static Object[] parseSql(String sql){
		Object[] ret = new Object[2];
		StringBuilder sb = new StringBuilder();
		int paraIndex = 0;
		int start = 0;
		char[] cs = sql.toCharArray();
		List vars = new ArrayList<String>();
		for(int i = 0;i<cs.length;i++){
			char ch = cs[i];
			if(ch=='$'){
				int end = findSQLEnd(cs,i);
				sb.append(sql.substring(start,i)).append("?");
				vars.add(sql.substring(i+1,end));
				i=end;
				start=end;
			}
		}
		if(start!=cs.length){
			sb.append(sql.substring(start,cs.length));
		}
		ret [0] =  vars;
		ret[1] = sb.toString();
		return ret ;
	}
	
	private static int findSQLEnd(char[] cs,int index){
		int start = index+1;
		for(int i=start;i<cs.length;i++){
			char ch = cs[i];
			if(ch==' '){
				return i;
			}
		}
		return cs.length;
	}
	
	private static boolean isSame(Object a,Object b){
		if(a==null&&b==null){
			return true;
		}else if(a==null&&b!=null){
			return false;
		}else if(a!=null&&b==null){
			return false;
		}else{
			return a.equals(b);
		}
	}
	
	public  static void main(String[] args){
		String sql = "name=$name order by name asc ";
		Object[] rets = parseSql(sql);
		System.out.println(Arrays.asList(rets));
		
		
	}
	

}
