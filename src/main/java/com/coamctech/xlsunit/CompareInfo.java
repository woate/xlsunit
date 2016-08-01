package com.coamctech.xlsunit;

/**
 * 简单保留了哪一个列比较有问题
 * @author Administrator
 *
 */
public class CompareInfo {
	String colName;
	Object expected;
	Object real;

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Object getExpected() {
		return expected;
	}

	public void setExpected(Object expected) {
		this.expected = expected;
	}

	public Object getReal() {
		return real;
	}

	public void setReal(Object real) {
		this.real = real;
	}

	@Override
	public String toString() {
		return "CompareInfo [colName=" + colName + ", expected=" + expected + ", real=" + real + "]";
	}
	
}
