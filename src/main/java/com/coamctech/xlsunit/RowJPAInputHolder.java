package com.coamctech.xlsunit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public  class RowJPAInputHolder extends RowInputHolder{
	
	static Log log = LogFactory.getLog(RowJPAInputHolder.class);
	
	 public RowJPAInputHolder (XLSParser parser,VariableTable vars,String sheetName){
			super(parser,vars,sheetName);
		
	 }
	 

	@Override
	protected void saveDb(Map<String,Object> record,DBCallBack cb,XLSPoistion p){
		//开始存入数据库
		try{
			Mapper mapper =  parser.db.getMapper() ;
			String clsName = mapper.getClassName(tableName);
			Object entity = null;
			
			Map<String,Object> propertis = new HashMap<String,Object> ();
			
			for(Entry<String,Object> entry:record.entrySet()){
				String attr = mapper.getAttrName(tableName, entry.getKey());
				propertis.put(attr, entry.getValue());
			
			}
			
			
			try {
//				
				entity = mapper.mapper(propertis, tableName);
				
			} catch (Exception e) {
				throw new RuntimeException("属性映射出错 "+tableName+" in "+propertis+" at "+p,e);
			} 
			
			
			DBAccess access = parser.db;
			try{
				access.save(entity.getClass(),entity);
			}catch(RuntimeException ex){
				throw new RuntimeException(" 保存数据"+this.tableName+" 出错，在"+p,ex);
			}
			
			
			if(cb.hasData()){
				
				for(DBCallBackItem col:cb.getVarRef()){
					String varRef = col.getVarRef();
					String write = mapper.getAttrName(tableName, col.getColName());
					if(!vars.contain(varRef)){
						Object o = ClassUtil.valueOf(entity, write);
						vars.add(col.getVarRef(), o);
						
					}else{
						//已经赋值过了
						Object o = vars.find(varRef);
						ClassUtil.setValue(entity, write, o);
					}
					
		
					
				}
			}
			
			
		}catch(RuntimeException ex){
			log.fatal("保存数据出错,位置是"+p,ex);
			throw ex;
		}
		
		
		
	}
	
	 
}