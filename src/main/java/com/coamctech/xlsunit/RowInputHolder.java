package com.coamctech.xlsunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public  class RowInputHolder implements RowHolder{
	 int line = 0;
	 XLSParser parser;
	 VariableTable vars ;
	 String tableName = null;
	 List<String> colNames = new ArrayList<String>();
	 List<String> pks = new ArrayList<String>();
	 String sheetName = null;
	 int rowBegin = 0;
	 public RowInputHolder (XLSParser parser,VariableTable vars,String sheetName){
			this.parser = parser;
			this.vars = vars;
			this.sheetName = sheetName;
			
	 }
	 
	@Override
	public boolean addRow(Row row) {
		if(line==0){
			Cell cell =  row.getCell(0);
			String value = parser.getCellValue(cell);				
			tableName = parser.removeStatementPreffix(value);
			line++;
			rowBegin = row.getRowNum();
			return true;
		}else if(line==1){
			//key
			int count = row.getLastCellNum();
			for(int i=0;i<count;i++){
				Cell cell = row.getCell(i);
				String value = cell.getStringCellValue();
				if(StringUtils.isEmpty(value)){
					break ;
				}
				if(value.startsWith("$")){
					value = parser.removeVarPreffix(value);
					//主键
					pks.add(value);
				}
				colNames.add(value);
			}
			line++;
			return true;
		}else {
			//colValues
			Map  record = new HashMap<String,Object>();
			int count = row.getLastCellNum();
			if(count==0){
				//结束
				return false ;
			}
			
			DBCallBack cb = new DBCallBack();
			for(int i=0;i<colNames.size();i++){
				Cell cell = row.getCell(i);
				Object value = parser.getCellValue(cell);
				
				if(value instanceof String && ((String)value).startsWith("$")){
					//变量
					String key = colNames.get(i);
					value = parser.removePKPreffix((String)value);
					//普通值
					String varRef = (String)value;
					if(vars.contain(varRef)){
						record.put(colNames.get(i), vars.find(varRef));
					}else{
						//不存在此变量,需要数据库插入后赋值的变量
						cb.addVarRef(new DBCallBackItem(key,varRef));
					}
					
					
				}else{
					record.put(colNames.get(i), value);
				}
				
			}
			
			//测试数据，保存到数据库里
			XLSPoistion p = new XLSPoistion(parser.file,sheetName,rowBegin);
			saveDb(record,cb,p); //后面俩个参数用于报错显示详细情况
			
			return true;
		}
	}

	@Override
	public void runData() {
		// TODO Auto-generated method stub
		
	}
	
	protected void saveDb(Map<String,Object> record,DBCallBack cb,XLSPoistion p){
		throw new UnsupportedOperationException("不支持");
		
	}
	
	 
}