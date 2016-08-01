package com.coamctech.xlsunit;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XLSParser {
	DBAccess db;
	Workbook book ;
	XLSClassPathLoader loader;
	String file;
	RowHolderFacotoy rowFactory = null;
	
	public XLSParser(XLSClassPathLoader loader,String file,DBAccess db){
		this.loader = loader;
		this.file = file;
		this.db = db;
		rowFactory = new RowHolderFacotoy.RowJPAHolderFacotoy();
		
	}
	
	
	public XLSParser(XLSClassPathLoader loader,String file,DBAccess db,RowHolderFacotoy rowFactory){
		this.loader = loader;
		this.file = file;
		this.db = db;
		this.rowFactory = rowFactory;
		
	}
	
	
	
	
	
	
	/**
	 * 设置初始化数据库,并
	 */
	public void init(VariableTable vars){
		book = loader.getWorkbook(file);
		//读取第二个sheet
		Sheet sheet = book.getSheetAt(1);
		String name = sheet.getSheetName();
		
		XLSSheetReader rowIterator = new XLSSheetReader(){

			@Override
			protected RowHolder getRowHolder(XLSParser parser, VariableTable vars, String sheetName) {
				return rowFactory.getInputHolder(parser, vars, sheetName);
			}
			
		};
		
		rowIterator.readSheet(this, book, name, vars,3);
		
		
		
	}
	
	
	public void prepare(String name,VariableTable vars){
		
		XLSSheetReader rowIterator = new XLSSheetReader(){

			@Override
			protected RowHolder getRowHolder(XLSParser parser, VariableTable vars, String sheetName) {
				return rowFactory.getOutputHolder(parser, vars, sheetName);
			}
			
		};
		
		rowIterator.readSheet(this, book, name, vars,1);
		
	}
	
	
	public void test(String name,VariableTable vars){
		
		XLSSheetReader rowIterator = new XLSSheetReader(){

			@Override
			protected RowHolder getRowHolder(XLSParser parser, VariableTable vars, String sheetName) {
				return rowFactory.getOutputHolder(parser, vars, sheetName);
			}
			
		};
		
		rowIterator.readSheet(this, book, name, vars,2);
		
	}
	
	
	
	protected boolean isVarDeclare(String stat){
		char c = stat.charAt(0);
		if(Character.isLowerCase(c)||Character.isDigit(c)){
			return true;
		}else{
			return false;
		}
	}
	
	protected boolean isComment(String stat){
		return stat.charAt(0)==':';
	}
	
	
	protected String removeStatementPreffix(String str){
		 return str.substring(2);
	 }
	 
	 protected String removeVarPreffix(String str){
		 return str.substring(1);
	 }
	 
	 protected String removePKPreffix(String str){
		 return str.substring(1);
	 }
	 
	 protected boolean isEmptyRow(Row row){
		//判断是否是空行，不知道有没有更好的办法
		if( row==null||row.getLastCellNum()<1){
			return true;
		}else{
			Cell c = row.getCell(0);
			String str = getCellValue(c);
			if(StringUtils.isEmpty(str)){
				c = row.getCell(0);
				str = getCellValue(c);
				if(StringUtils.isEmpty(str)){
					return true;
				}
			}
			return false;
		}
		
	 }
	 
	 protected boolean isEmptyCell(Cell cell){
			if(cell==null){
				return true;
			}else{
				String str = getCellValue(cell);
				return StringUtils.isEmpty(str);
			}
	  }
	 
	 protected  String getCellValue(Cell cell) {
		if(cell==null) return null;
		 switch(cell.getCellType()){
		 case Cell.CELL_TYPE_NUMERIC:{
			 if (HSSFDateUtil.isCellDateFormatted(cell)) {
		    		Date  date = cell.getDateCellValue();
		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    		return sdf.format(date);
		        }else{
		        	 Number number =  cell.getNumericCellValue();
					 return trimZero(number);
		        }
		 }
		 case Cell.CELL_TYPE_STRING:{
			 return cell.getStringCellValue();
		 }
		 case Cell.CELL_TYPE_BLANK:{
			 return null;
		 }
		 case Cell.CELL_TYPE_FORMULA:{
			 //TODO 待验证
			 FormulaEvaluator evaluator = book.getCreationHelper().createFormulaEvaluator();
			 CellValue cellValue = evaluator.evaluate(cell);
			 switch (cellValue.getCellType()) {
			    case Cell.CELL_TYPE_BOOLEAN:
			        return cellValue.getBooleanValue()+"";
			    case Cell.CELL_TYPE_NUMERIC:
			    	if (HSSFDateUtil.isCellDateFormatted(cell)) {
			    		Date  date = cell.getDateCellValue();
			    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    		return sdf.format(date);
			        }else{
			        	 Number number =  cell.getNumericCellValue();
						 return trimZero(number);
			        }
			    	
			    case Cell.CELL_TYPE_STRING:
			        return cellValue.getStringValue();
			    case Cell.CELL_TYPE_BLANK:
			        break;
			    case Cell.CELL_TYPE_ERROR:
			        break;
			    // CELL_TYPE_FORMULA will never happen
			    case Cell.CELL_TYPE_FORMULA: 
			        break;
			}		
		 }
		 }
		 throw new UnsupportedOperationException("cell type "+cell.getCellType());
	 }

	 private String trimZero(Number n){
		 BigDecimal b = new BigDecimal(n.toString());
		 BigDecimal newData = b.stripTrailingZeros();
		 return newData.toString();
	 }

	public DBAccess getDb() {
		return db;
	}


	public void setDb(DBAccess db) {
		this.db = db;
	}





	public Workbook getBook() {
		return book;
	}


	public void setBook(Workbook book) {
		this.book = book;
	}


	public XLSClassPathLoader getLoader() {
		return loader;
	}


	public void setLoader(XLSClassPathLoader loader) {
		this.loader = loader;
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public RowHolderFacotoy getRowFactory() {
		return rowFactory;
	}


	public void setRowFactory(RowHolderFacotoy rowFactory) {
		this.rowFactory = rowFactory;
	}
		

	
	 
	
	 
	  
	
	
}
