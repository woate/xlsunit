package com.coamctech.xlsunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class RowOutputHolder implements RowHolder {
	int line = 0;
	XLSParser parser;
	VariableTable vars;
	String tableName = null;
	String sheetName = null;

	boolean findByPk = true;
	// 列
	List<String> colNames = new ArrayList<String>();
	// 主键
	List<String> pks = new ArrayList<String>();
	// 需要比较的列
	List<String> compare = new ArrayList<String>();
	// 需要按照sql查询
	String sql = null;
	// 或者按照主键来查询
	Map<String, Object> queryKeys = new HashMap<String, Object>();
	
	List<Map<String, Object>> allRecords = new ArrayList<Map<String, Object>>();
	int begingRow = 0;
	
	List<DBCallBack> cbs = new ArrayList<DBCallBack>();
	

	public RowOutputHolder(XLSParser parser, VariableTable vars, String sheetName) {
		this.parser = parser;
		this.vars = vars;
		this.sheetName = sheetName;
	}

	@Override
	public boolean addRow(Row row) {
		if (line == 0) {
			begingRow = row.getRowNum(); // 用于错误提示
			Cell cell = row.getCell(0);
			String value = parser.getCellValue(cell);
			tableName = parser.removeStatementPreffix(value);
			if (row.getLastCellNum() > 2) {
				 
				 sql = row.getCell(1).getStringCellValue();
				 if(!StringUtils.isEmpty(sql)){
					 findByPk = false;
				 }
				 
			}
			line++;
			return true;
		} else if (line == 1) {
			// key
			int count = row.getLastCellNum();
			for (int i = 0; i < count; i++) {
				Cell cell = row.getCell(i);
				if(parser.isEmptyCell(cell)){
					break;
				}
				String value = cell.getStringCellValue();

				if (StringUtils.isEmpty(value)) {
					break;
				}

				if (value.startsWith("$")) {
					value = parser.removeVarPreffix(value);
					// 主键
					pks.add(value);
				}

				XSSFColor co = (XSSFColor) cell.getCellStyle().getFillForegroundColorColor();
				// 需要比较的列
				if (co != null && "FFFF0000".equals(co.getARGBHex())) {
					compare.add(value);
				}

				colNames.add(value);
			}
			line++;
		
			return true;
		} else {
			// colValues
			if (parser.isEmptyRow(row)) {
				return false;
			}

			Map<String, Object> record = new HashMap<String, Object>();
			DBCallBack cb = new DBCallBack();
			cbs.add(cb);
			for (int i = 0; i < colNames.size(); i++) {
				Cell cell = row.getCell(i);
				
				Object value = parser.getCellValue(cell);
				String colName = colNames.get(i);
				if (value instanceof String && ((String) value).startsWith("$")) {
					String varRef = parser.removePKPreffix((String) value);
					if(vars.contain(varRef)){
						Object refValue = this.vars.find(varRef);
						if (findByPk && this.pks.contains(colName)) {
							queryKeys.put(colName, refValue);
						}
						record.put(colName, refValue);
					}else{
						cb.addVarRef(new DBCallBackItem(colName, varRef));
					}
					

				} else {
					record.put(colName, value);
				}
				
				

			}

			if (findByPk) {
				// 测试数据是否与数据库里的数据匹配
				XLSPoistion p = new XLSPoistion(parser.file, sheetName, row.getRowNum());

				fetchAndCompareOne(record, queryKeys, compare, cb,p);

			} else {
				// 暂存,最后比较
				allRecords.add(record);
			}

			return true;
		}
	}

	@Override
	public void runData() {
		if (allRecords.size() == 0)
			return;
		XLSPoistion p = new XLSPoistion(parser.file, sheetName, this.begingRow);
		fetchAndCompareRecords(allRecords,  compare, sql, cbs,p);

	}

	protected void fetchAndCompareRecords(List<Map<String, Object>> allRecords,
			List<String> compare, String sql, List<DBCallBack> cbs ,XLSPoistion p) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 按照主键
	 * 
	 * @param record
	 * @param queryKeys
	 * @param line
	 * @param file
	 */
	protected void fetchAndCompareOne(Map<String, Object> record, Map<String, Object> queryKeys, List<String> compare,
			DBCallBack cb ,XLSPoistion p) {
		throw new UnsupportedOperationException();

	}

}
