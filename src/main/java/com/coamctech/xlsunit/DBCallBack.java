package com.coamctech.xlsunit;

import java.util.ArrayList;
import java.util.List;

/** 数据库统一的回调
 * @author lijiazhi
 *
 */
public class DBCallBack {
	int rowNum;
	List<DBCallBackItem> varRef = new ArrayList<DBCallBackItem>();
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public List<DBCallBackItem> getVarRef() {
		return varRef;
	}
		
	public void setVarRef(List<DBCallBackItem> varRef) {
		this.varRef = varRef;
	}
	
	public void addVarRef(DBCallBackItem ref){
		this.varRef.add(ref);
	}
	public boolean hasData(){
		return varRef.size()!=0;
	}
	
	
	
}
