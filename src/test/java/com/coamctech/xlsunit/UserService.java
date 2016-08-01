package com.coamctech.xlsunit;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
	public void addUser();
	public void modifyUserInfo(String id,String password);
}
