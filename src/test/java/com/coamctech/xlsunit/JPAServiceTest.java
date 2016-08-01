package com.coamctech.xlsunit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class JPAServiceTest extends BaseJPAServiceTestSample {

	XLSParser userParser = null;
	XLSParser historyParser = null;
	XLSParser historyParser2 = null;
	

	@Autowired
	UserService userService;

	@Before
	public void init() {
		super.init();
		userParser = new XLSParser(BaseJPAServiceTestSample.loader, "测试一.xlsx", dbAccess,new RowHolderFacotoy.RowJPAHolderFacotoy());
		historyParser = new XLSParser(BaseJPAServiceTestSample.loader, "测试二.xlsx", dbAccess,new RowHolderFacotoy.RowJPAHolderFacotoy());
		historyParser2 = new XLSParser(BaseJPAServiceTestSample.loader, "测试四.xlsx", dbAccess,new RowHolderFacotoy.RowJPAHolderFacotoy());
	
	}

//	 @Test
//	 @Rollback(false)
	public void testEnv() throws Exception {

//		SysOrder order = new SysOrder();
//		order.setName("ok");
//		em.persist(order);
//		Long id = order.getId();
		
		// History his = new History();
		// his.setName("abc");
		// his.setHistoryPk(new HistoryPk("a",1));
		//
		// em.persist(his);
		
//		History2 key = new History2();
//		key.setId("3");
//		key.setVer(1);
	
		HistoryPk2 key = new HistoryPk2();
		key.setId("3");
		key.setVer(1);
		
		History2 value = em.find(History2.class,key);
	
		
		Assert.assertEquals(value.getName(), "hello");
		
		
		

	}
	
	@Test
	public void testUser1() {
		VariableTable vars = new VariableTable();
		userParser.init(vars);
		userParser.prepare("场景1", vars);
		String id = (String) vars.find("id");
		userService.modifyUserInfo(id, "newPassword");
		userParser.test("场景1", vars);
	}
	
	
	

//	@Test
	public void testUser2() {
		VariableTable vars = new VariableTable();
		userParser.init(vars);
		userParser.prepare("场景2", vars);
		userService.addUser();
		userParser.test("场景2", vars);
		String str = (String)vars.find("mustknow1");
	}

//	@Test
	public void testHistory1() { 
		VariableTable vars = new VariableTable();
		historyParser.init(vars);
		historyParser.prepare("场景1", vars);
		historyParser.test("场景1", vars);
	}
	
//	@Test
	public void testHistory2() {
		VariableTable vars = new VariableTable();
		historyParser.init(vars);
		historyParser.prepare("场景2", vars);
		historyParser.test("场景2", vars);
	}
	
//	@Test
	public void testHistoryIdClass() {
		VariableTable vars = new VariableTable();
		historyParser2.init(vars);
		historyParser2.prepare("场景一", vars);
		historyParser2.test("场景一", vars);
	}

	
}
