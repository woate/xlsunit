package com.coamctech.xlsunit;

import org.apache.poi.ss.usermodel.Row;

/**
 *  用来处理excel 输入
 * @author lijiazhi
 *
 */
public interface RowHolder {
	 public boolean addRow(Row row );
	 public void runData();
}