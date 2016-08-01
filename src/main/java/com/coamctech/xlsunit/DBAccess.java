package com.coamctech.xlsunit;

import java.util.List;

public interface DBAccess {
	public void save(Class clz,Object o);
	public Object findById(Class clz,Object id);
	public List<Object> query(Class clz,String tableName,String where,VariableTable vars);
	public Mapper getMapper();
}
