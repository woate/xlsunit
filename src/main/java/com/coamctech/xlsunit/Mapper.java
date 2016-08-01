package com.coamctech.xlsunit;

import java.util.List;
import java.util.Map;

/**
 * 定义表和类之间的映射，以及列名，主键等基本信息
 * @author Administrator
 *
 */
public interface Mapper {
	public Object mapper(Map<String,Object> src,String tableName);
	public Object mapperId(Map<String,Object> src,String tableName);	
	public String getClassName(String tableName);
	public String getAttrName(String tableName,String colName);
	public List<String> getAttrName(String tableName,List<String> cols);
	public void initMapItem(MapperItem item);
}
