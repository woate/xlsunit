package com.coamctech.xlsunit;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public  abstract class XLSSheetReader {
	 protected abstract RowHolder  getRowHolder(XLSParser parser,VariableTable vars,String sheetName);
	 
	 
	 public void readSheet(XLSParser parser,Workbook book,String sheetName,VariableTable vars,int content){
		 	Sheet sheet = book.getSheet(sheetName);
		 	if(sheet==null){
		 		throw new RuntimeException("找不到sheet "+sheetName);
		 	}
			int rowNum=sheet.getLastRowNum();//获得总行数
		
		
			RowHolder holder = null;
			for(int i=0;i<=rowNum;i++){
				Row row = sheet.getRow(i);
							
				if(holder==null){
					//判断是否是空行，不知道有没有更好的办法
					if(row==null||row.getLastCellNum()==0) {
						continue ;
					}
					Cell cell =  row.getCell(0);
					if(parser.isEmptyCell(cell)) continue ;
					
					String value = parser.getCellValue(cell);
					if(StringUtils.isEmpty(value)){
						//应该是个空行，忽略
						continue ;
					}
					String stat = parser.removeStatementPreffix(value);
					if(parser.isVarDeclare(stat)){
						if(content==2){
							i=i+2;
							continue ;
						}
						//变量申明
						holder = new RowVarHolder(parser,vars);
						holder.addRow(row);
					}else if(parser.isComment(stat)){
						//注释，忽略此行
						continue;
					}else{
						
						if(content==1){
							//ingnore table ,just init 
							break;
						}
						//表数据
						if(row.getLastCellNum()==2){
							String cmd = row.getCell(1).getStringCellValue();
							if(cmd.startsWith("include")){
								XLSInclude excel = new XLSInclude(parser,cmd);
								excel.doInclude(vars);
							}else{
								throw new UnsupportedOperationException("不支持的cmd"+cmd+" at "+parser.file);
							}
							
						}else{
							holder = getRowHolder(parser,vars,sheetName);
							holder.addRow(row);
						}
						
					
					}
					
				}else {
					//TODO
					if(parser.isEmptyRow(row)){
						if(holder!=null){
							holder.runData();
						}
						holder = null;
						continue ;
					}
					
					boolean hasMore = holder.addRow(row);
					if(!hasMore){
						holder.runData();
						holder = null;
					}
					
				}
			
				
			}
			
			if(holder!=null){
				holder.runData();
			}
	 }
	 
	
}
