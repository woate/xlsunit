package com.coamctech.xlsunit;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

/**
 * JPA implementation，可以测试jpa，但目前配置要讲求jpa 简单的orm到数据库
 * @author lijiazhi
 *
 */
public class JPADatabaseAccess implements DBAccess {

	EntityManager em;
	JPAMapper mapper = null;
	public JPADatabaseAccess(EntityManager em,JPAMapper mapper){
		this.em = em;
		this.mapper = mapper;
		
	}
	
	@Override
	public void save(Class z,Object o) {
		em.persist(o);
		em.flush();
		

	}

	@Override
	public Object findById(Class clz,Object id) {
		return em.find(clz, id);
	}

	@Override
	public List<Object> query(Class clz,String tableName,String where,VariableTable vars) {
		
		StringBuilder sb = new StringBuilder(" select * from ");
		sb.append(tableName).append(" where ").append(where);
		String sql =sb.toString();
		Object[] ret = ClassUtil.parseSql(sql);
		List<String> para = (List<String>)ret[0];
		String jdbcSql = (String)ret[1];
		Query query = em.createNativeQuery(jdbcSql, clz);
		for(int i=0;i<para.size();i++){
			query.setParameter(i+1, vars.find(para.get(i)));
//			query.setParameter(para.get(i), vars.find(para.get(i)));
		}
		return query.getResultList();
	}
	@Override
	public Mapper getMapper() {
		return this.mapper;
	}
	
	

}
