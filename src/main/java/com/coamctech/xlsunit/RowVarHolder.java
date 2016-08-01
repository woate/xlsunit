package com.coamctech.xlsunit;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public   class RowVarHolder implements RowHolder {
	 int line = 0;
	 XLSParser parser;
	 VariableTable vars ;
	 String scope = null;
	List<String> keys = new ArrayList<String>();
	List<Object> values = new ArrayList<Object>();
	public RowVarHolder (XLSParser parser,VariableTable vars){
		this.parser = parser;
		this.vars = vars;
	}
	@Override
	public boolean addRow(Row row) {
		if(line==0){
			Cell cell =  row.getCell(0);
			String value = parser.getCellValue(cell);				
			scope = parser.removeStatementPreffix(value);
			line++;
			return true;
		}else if(line==1){
			//key
			int count = row.getLastCellNum();
			for(int i=0;i<count;i++){
				Cell cell = row.getCell(i);
				keys.add(parser.getCellValue(cell));
			}
			line++;
			return true;
		}else if(line==2){
			//key
			
			for(int i=0;i<keys.size();i++){
				Cell cell = row.getCell(i);
				Object value = parser.getCellValue(cell);
				
				if(value instanceof String && ((String)value).startsWith("$")){
					String varRef  = parser.removePKPreffix((String)value);
					values.add(vars.find(varRef));
					
				}else{
					values.add(parser.getCellValue(cell));
				}
				
			}
			line++;
			return false;
		}else{
			//结束，应该是空白row
			return false;
		}
		
		
	}
	
	@Override
	public void runData() {
		// TODO Auto-generated method stub
		for(int i =0;i<keys.size();i++){
			String key = keys.get(i);
			Object value = values.get(i);
			if(value instanceof String){
				String val = (String)value;
				if(val.startsWith("$")){
					val = parser.removeVarPreffix(val);
					Object realValue = vars.find(val);
					vars.add(scope+"."+key, realValue);
				}else{
					vars.add(scope+"."+key, value);
				}
			}else{
				vars.add(scope+"."+key, value);
			}
			
		}
		
	}
}	
	
	 