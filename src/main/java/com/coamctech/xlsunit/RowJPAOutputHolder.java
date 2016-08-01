package com.coamctech.xlsunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RowJPAOutputHolder extends  RowOutputHolder {
	static Log log = LogFactory.getLog(RowJPAInputHolder.class);

	public RowJPAOutputHolder(XLSParser parser, VariableTable vars, String sheetName) {
		super(parser,vars,sheetName);
	}

	
	@Override
	protected void fetchAndCompareRecords(List<Map<String, Object>> allRecords,
			List<String> compare, String sql, List<DBCallBack> cbs,XLSPoistion p) {
		// 开始存入数据库
		try{
			Mapper mapper = parser.db.getMapper() ;
			String clsName = mapper.getClassName(tableName);

			List<Object> expectedList = new ArrayList<Object>();

			for (Map<String, Object> record : allRecords) {
				// Object entity = ClassUtil.newInstance(clsName);
				Map<String, Object> propertis = new HashMap<String, Object>();
				for (Entry<String, Object> entry : record.entrySet()) {
					String attr = mapper.getAttrName(tableName, entry.getKey());
					propertis.put(attr, entry.getValue());
				}

				try {
					//
					Object entity = mapper.mapper(propertis, tableName);
					expectedList.add(entity);
				} catch (Exception e) {
					throw new RuntimeException("找不到属性 " + tableName + " in " + propertis + " at " + p, e);
				}
				

			}

		

		

			DBAccess access = parser.db;

			List<Object> dbList = null;
			try {

				dbList = access.query(ClassUtil.loadClass(clsName), tableName,sql,vars);
			} catch (RuntimeException ex) {
				throw new RuntimeException(" 查询数据" + this.tableName + " 出错，在" + p, ex);
			}

			org.junit.Assert.assertEquals("查询结果总数不匹配 "+p.toString(), expectedList.size(), dbList.size());
			if (expectedList.size() == dbList.size()) {
				for (int i = 0; i < expectedList.size(); i++) {
					Object exp = expectedList.get(i);
					Object db = dbList.get(i);
					CompareInfo info = new CompareInfo();
					boolean isSame = ClassUtil.compare(exp, db, mapper.getAttrName(tableName, compare),info);
					if (!isSame) {
						org.junit.Assert.fail("比较数据 出错，在第"+(i+1)+"行，位于"+ p+",错误信息："+info.toString());
					}
					DBCallBack cb = cbs.get(i);
					if(cb.hasData()){
						for(DBCallBackItem item:cb.varRef){
							String colName = item.getColName();
							String attr = mapper.getAttrName(tableName, colName);
							Object o = ClassUtil.valueOf(db, attr);
							vars.add(item.getVarRef(), o);
						}
					}
					

				}
			}

		}catch(RuntimeException ex){
			log.fatal("保存数据出错,位置是"+p,ex);
			throw ex;
		}
		
	}

	/**
	 * 按照主键
	 * 
	 * @param record
	 * @param queryKeys
	 * @param line
	 * @param file
	 */
	@Override
	protected void fetchAndCompareOne(Map<String, Object> record, Map<String, Object> queryKeys, List<String> compare,
			DBCallBack cb,XLSPoistion p) {
		// 开始存入数据库
		try{
			Mapper mapper =  parser.db.getMapper() ;
			Object entity = null;

			Map<String, Object> propertis = new HashMap<String, Object>();

			for (Entry<String, Object> entry : record.entrySet()) {
				String attr = mapper.getAttrName(tableName, entry.getKey());
				propertis.put(attr, entry.getValue());

			}
			Object id = null;
			if (queryKeys.size() > 1) {
				id = mapper.mapperId(queryKeys, tableName);
			} else {
				id = queryKeys.values().toArray()[0];
			}

			try {
				entity = mapper.mapper(propertis, tableName);
				// XLSBeanUtil.copyPropesrties(propertis, entity);
			} catch (Exception e) {
				throw new RuntimeException("找不到属性 " + entity + " in " + propertis + " at " + p, e);
			}

			DBAccess access = parser.db;

			Object dbEntity = null;
			try {
				dbEntity = access.findById(entity.getClass(), id);
				if (dbEntity == null) {
					org.junit.Assert.fail("比较数据 出错，在" + p);
					return;
				}

			} catch (RuntimeException ex) {
				throw new RuntimeException(" 查询数据" + this.tableName + " 出错，在" + p, ex);
			}
			CompareInfo info = new CompareInfo();
			boolean isSame = ClassUtil.compare(entity, dbEntity, mapper.getAttrName(tableName, compare),info);
			if (!isSame) {
				org.junit.Assert.fail("比较数据 出错，在" + p+" 错误信息："+info.toString());
			}
			
			if(cb.hasData()){
				for(DBCallBackItem item:cb.varRef){
					String colName = item.getColName();
					String attrName = mapper.getAttrName(tableName, colName);
					Object o = ClassUtil.valueOf(dbEntity, attrName);
					vars.add(item.getVarRef(), o);
				}
			}
			
		}catch(RuntimeException ex){
			log.fatal("保存数据出错,位置是"+p,ex);
			throw ex;
		}
		

	}

}
