package com.coamctech.xlsunit;

import java.util.ArrayList;
import java.util.List;

/** 数据库统一的回调
 * @author lijiazhi
 *
 */
public class DBCallBackItem {
	String colName;
	String varRef ;
	public DBCallBackItem(String colName, String varRef) {
		super();
		this.colName = colName;
		this.varRef = varRef;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getVarRef() {
		return varRef;
	}
	public void setVarRef(String varRef) {
		this.varRef = varRef;
	}
	
	
	
	
	
}
