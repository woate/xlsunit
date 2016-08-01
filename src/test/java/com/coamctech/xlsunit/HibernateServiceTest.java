package com.coamctech.xlsunit;

import javax.persistence.criteria.Order;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateServiceTest extends BaseHibernateServiceTestSample {

	XLSParser orderParser = null;
	
	
	@Before
	public void init() {
		super.init();
		orderParser = new XLSParser(BaseHibernateServiceTestSample.loader, "测试三.xlsx", dbAccess,new RowHolderFacotoy.RowHibernateHolderFactory());
	}

//	 @Test
//	 @Rollback(false)
	public void testEnv() throws Exception {

		SysOrder order = new SysOrder();
		order.setName("ok");
		dbAccess.save(Order.class, order);
	
		Long id = order.getId();
		
		// History his = new History();
		// his.setName("abc");
		// his.setHistoryPk(new HistoryPk("a",1));
		//
		// em.persist(his);

	}
	
	@Test
	public void testOrder1() {
		VariableTable vars = new VariableTable();
		orderParser.init(vars);
		orderParser.prepare("场景1", vars);
		
		orderParser.test("场景1", vars);
	}
	
	
	@Test
	public void testOrder2() {
		VariableTable vars = new VariableTable();
		orderParser.init(vars);
		orderParser.prepare("场景2", vars);
		
		orderParser.test("场景2", vars);
	}
	
	
	

////	@Test
//	public void testUser2() {
//		VariableTable vars = new VariableTable();
//		userParser.init(vars);
//		
//		userService.addUser();
//
//		userParser.test("场景2", vars);
//	}
//
////	@Test
//	public void testHistory1() { 
//		VariableTable vars = new VariableTable();
//		historyParser.init(vars);
//		historyParser.test("场景1", vars);
//	}
//	
////	@Test
//	public void testHistory2() {
//		VariableTable vars = new VariableTable();
//		historyParser.init(vars);
//		historyParser.test("场景2", vars);
//	}

	
}
