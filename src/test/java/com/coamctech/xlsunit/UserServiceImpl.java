package com.coamctech.xlsunit;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	protected EntityManager em;
	@Transactional
	public void addUser(){
		User user = new User();
		user.setBir(new Timestamp(System.currentTimeMillis()));
		user.setName("joelli");
		user.setPassword("d00");
		user.setMoney(new BigDecimal("1.1"));
		user.setGender(1l);
		em.persist(user);
	}
	
	
	@Transactional
	public void modifyUserInfo(String id,String password){
		User user = em.find(User.class, id);
		user.setPassword(password);
		
	}
}
