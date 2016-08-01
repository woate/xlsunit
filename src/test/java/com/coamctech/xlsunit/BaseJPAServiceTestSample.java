package com.coamctech.xlsunit;

import javax.persistence.EntityManager;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;




@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-jpa.xml" })
@Transactional
public class BaseJPAServiceTestSample {
	
	protected JPADatabaseAccess dbAccess = null;
	
	
	@Autowired
	protected EntityManager em;
	
	@Autowired
	
	public static JPAMapper mapper  = null;
	public static XLSClassPathLoader loader = null;
	@BeforeClass
	public static void initData(){
		mapper = new  JPAMapper(new ImprovedNamingStrategy(),"com.coamctech.xlsunit");
		loader = new XLSClassPathLoader("/xls");
	}

	public void init() {
		dbAccess = new JPADatabaseAccess(em,mapper);
	}
	
	

}
