package com.coamctech.xlsunit;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class VariableTable {
	Map<String,Object> vars = new HashMap<String,Object>();
	Map<String,Function> fns = new HashMap<String,Function>();
	
	public VariableTable(){
		initDefaultFns();
	}
	public  Object find(String name){
		
		return find(name,false);
	}
	
	public  Object find(String name,boolean allowNoExit){
		if(name.startsWith("fn.")){
			return getFnValue(name);
		}
		boolean contain = vars.containsKey(name);
		if(!allowNoExit&&!contain){
			throw new RuntimeException("no value "+name+" in Variable Table");
		}
		Object value = vars.get(name);
		return value;
		
	}
	
	public boolean contain(String name){
		if(name.startsWith("fn.")){
			return true;
		}else{
			return vars.containsKey(name);
		}
		
	}
	
	
	
	private Object getFnValue(String name){
		String fnName = name.substring(3);
		Function fn = fns.get(fnName);
		if(fn==null){
			throw new RuntimeException("未找到方法名"+fnName);
			
		}
		return fn.value(fnName);
	}
	
	public void add(String name,Object value){
		vars.put(name, value);
	}
	
	public Map<String,Object> findScope(String scope){
		if(StringUtils.isEmpty(scope)){
			return this.vars;
		}
		String prefix = scope+".";
		Map<String,Object> map = new HashMap<String,Object>();
		for(Entry<String,Object> entry:vars.entrySet()){
			String key = entry.getKey();
			if(key.startsWith(prefix)){
//				int index = key.indexOf(prefix);
				String attrName = key.substring(prefix.length());
				map.put(attrName,entry.getValue());
			}
		}
		return map;
		
	}
	
	public String toString(){
		return vars.toString();
	}
	
	private void initDefaultFns(){
		DateFunction df = new DateFunction();
		SeqFunction seqFn = new SeqFunction();
		fns.put("date", df);
		fns.put("timestamp", df);
		fns.put("sqlDate", df);
		fns.put("sqlDate", df);
		fns.put("seq", seqFn);
		
		
	}
	
	static public  class DateFunction implements Function{

		@Override
		public Object value(String name) {
			if(name.equals("date")){
				return new Date();
			}else if(name.equals("timestamp")){
				return new Timestamp(System.currentTimeMillis());
			}else if(name.equals("sqlDate")){
				return new java.sql.Date(System.currentTimeMillis());
			}else{
				throw new RuntimeException("不支持的方法调用"+name);
			}
		}
		
	}
	
	
	static public class SeqFunction implements Function{
		public static long seq = System.currentTimeMillis();
		@Override
		public Object value(String name) {
			return seq++;
		}
		
	}


	public Map<String, Function> getFns() {
		return fns;
	}
	
	public String findString(String name ){
		Object o = this.find(name);
		if(o==null){
			return null;
		}else if(o instanceof String){
			return (String)o;
		}else{
			return o.toString();
		}
	}
	
	public Long findLong(String name ){
		Object o = this.find(name);
		if(o==null){
			return null;
		}else if(o instanceof Long){
			return (Long)o;
		}else if(o instanceof Number){
			return ((Number)o).longValue();
		}else{
			throw new RuntimeException("Long expectd,but "+o);
		}
	}
	
	public Integer findInteger(String name ){
		Object o = this.find(name);
		if(o==null){
			return null;
		}else if(o instanceof Integer){
			return (Integer)o;
		}else if(o instanceof Number){
			return ((Number)o).intValue();
		}else{
			throw new RuntimeException("Integer expectd,but "+o);
		}
	}
	
	
	
}
