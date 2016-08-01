package com.coamctech.xlsunit;

public abstract class   RowHolderFacotoy {
	
	public  abstract RowInputHolder getInputHolder(XLSParser parser,VariableTable vars,String sheetName);
	
	public  abstract RowOutputHolder getOutputHolder(XLSParser parser,VariableTable vars,String sheetName);
	
	public static class RowJPAHolderFacotoy extends RowHolderFacotoy{
		public  RowInputHolder getInputHolder(XLSParser parser,VariableTable vars,String sheetName){
			return new RowJPAInputHolder(parser,vars,sheetName);
		}
		
		public  RowOutputHolder getOutputHolder(XLSParser parser,VariableTable vars,String sheetName){
			return new RowJPAOutputHolder(parser,vars,sheetName);
		}
	}
	
	public static class RowHibernateHolderFactory extends RowHolderFacotoy{

		@Override
		public RowInputHolder getInputHolder(XLSParser parser, VariableTable vars, String sheetName) {
			return new RowHibernateInputHolder(parser,vars,sheetName);
		}

		@Override
		public RowOutputHolder getOutputHolder(XLSParser parser, VariableTable vars, String sheetName) {
			return new RowHibernateOutputHolder(parser,vars,sheetName);
		}
		
	}
	
	
}
