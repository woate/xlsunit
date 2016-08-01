package com.coamctech.xlsunit;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
/**
 * 使用简单的sql来操作excel
 * @author lijiazhi
 * 
 */
public class HibernateAccess implements DBAccess {
	
	SessionFactory sessionFactory = null;
	Mapper mapper;
	public HibernateAccess(SessionFactory sessionFactory,Mapper mapper){
		this.sessionFactory = sessionFactory;
		this.mapper = mapper;
	}

	@Override
	public void save(Class z,Object o) {
		
		
		sessionFactory.getCurrentSession().save(o);

	}

	@Override
	public Object findById(Class clz, Object id) {
		return sessionFactory.getCurrentSession().get(clz, (Serializable)id);
	}

	@Override
	public List<Object> query(Class clz,String tableName,String where,VariableTable vars){
		StringBuilder sb = new StringBuilder(" select * from ");
		sb.append(tableName).append(" where ").append(where);
		String sql =sb.toString();
		Object[] ret = ClassUtil.parseSql(sql);
		List<String> para = (List<String>)ret[0];
		String jdbcSql = (String)ret[1];
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(jdbcSql);
		for(int i=0;i<para.size();i++){
			query.setParameter(i, vars.find(para.get(i)));
//			query.setParameter(para.get(i), vars.find(para.get(i)));
		}
		return query.addEntity(clz).list();
	}

	@Override
	public Mapper getMapper() {
		// TODO Auto-generated method stub
		return mapper;
	}
	
	
															  
}
