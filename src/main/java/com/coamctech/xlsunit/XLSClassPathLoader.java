package com.coamctech.xlsunit;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XLSClassPathLoader implements XLSLoader {
	
	Map<String,Workbook> cache = new HashMap<String,Workbook>();
	String root="";
	public XLSClassPathLoader(){
		
	}
	
	public XLSClassPathLoader(String root){
		this.root = root ;
		
	}
	public Workbook  getWorkbook(String file){
		if(cache.containsKey(file)){
			return cache.get(file);
		}
		
		InputStream ins = ClassUtil.getInputStream(root+(file.startsWith("/")?"":"/")+file);
		try {
			Workbook workbook = WorkbookFactory.create(ins);
			cache.put(file, workbook);
			return workbook;
		} catch (InvalidFormatException e) {
			throw new RuntimeException("加载"+file+"失败",e);
		} catch (IOException e) {
			throw new RuntimeException("加载"+file+"失败",e);
		}
		
		
	}
	
}
