package com.coamctech.xlsunit;

/**
 * 用来标示xls执行的位置，当出现错误的时候，用于显示具体错误位置
 * @author Administrator
 *
 */
public class XLSPoistion {
	String file ;
	String sheet;
	int line;
	public XLSPoistion(String file,String sheet,int line){
		this.file = file;
		this.sheet = sheet;
		this.line = line;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	@Override
	public String toString() {
		return "XLSPoistion [file=" + file + ", sheet=" + sheet + ", line=" + line + "]";
	}
	
}
