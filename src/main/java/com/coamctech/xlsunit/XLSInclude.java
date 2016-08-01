package com.coamctech.xlsunit;

import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XLSInclude {
	 private VariableTable thisVars = new  VariableTable();
	 String file = null;
	 String sheetName = null;
	 String scope = null;
	 XLSParser parser;
	 public XLSInclude(XLSParser parser,String paras){
		 this.parser = parser;
		 String[] array = paras.split(" ");
		 String cmd = array[0];
		 file = array[1];
		 if(array.length>2){
			 sheetName = array[2];
		 }
		 if(array.length>3){
			 scope = array[3];
		 }
		 
	 }
	 public void doInclude(VariableTable parent){
		 Workbook book = parser.loader.getWorkbook(file);
		 Sheet sheet = null;
		 if(sheetName==null){
			 sheet =  book.getSheetAt(0);
		 }else{
			 sheet =  book.getSheet(sheetName);
		 }
		 
		 RowJPAInputHolder hanlder = new RowJPAInputHolder(parser,thisVars,sheetName);
		 int rowNum=sheet.getLastRowNum();//获得总行数
		 for(int i=0;i<=rowNum;i++){
			Row row = sheet.getRow(i);
			boolean success = hanlder.addRow(row);
			if(!success){
				break;
			}
			
		 }
		 
		 if(scope==null){
			 parent.vars.putAll(thisVars.vars);
		 }else{
			 //加上前缀
			 for(Entry<String,Object> entry:thisVars.vars.entrySet()){
				 String key =scope+"."+entry.getKey();
				 parent.add(key, entry.getValue());
			 }
		 }
		 
	 }
}
