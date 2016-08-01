package com.coamctech.xlsunit;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MyTest {

	@Test
	public void generalTest() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Map map = new HashMap();
//		map.put("bg", "1.22");
//		SysOrder order = new SysOrder();
//		XLSBeanUtil.copyPropesrties(map, order);
//		order.setName("ok");
//	
//		BigDecimal b = new BigDecimal("2.001");
//		 BigDecimal newData = b.stripTrailingZeros();
//		 
//		
//		System.out.println(newData);
		
		VariableTable table = new VariableTable();
		table.add("accc.dddb", 1);
		Map map = table.findScope("accc");
		System.out.println(map);

	}

}
