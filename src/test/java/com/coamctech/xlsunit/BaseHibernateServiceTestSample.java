package com.coamctech.xlsunit;

import java.util.HashMap;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;




@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-hibernate.xml" })
@Transactional
public class BaseHibernateServiceTestSample {
	
	protected HibernateAccess dbAccess = null;
	
	@Resource
	protected SessionFactory sessionFactory;

	public static XLSClassPathLoader loader = null;
	
	@BeforeClass
	public static void initData(){
		loader = new XLSClassPathLoader("/xls");
	}

	@Before
	public void init() {
		// hiberate 配置文件位置
		dbAccess = new HibernateAccess(sessionFactory,new HibernateMapper("applicationContext-hibernate.xml","mySessionFactory"));
	}
	
	
	

}
