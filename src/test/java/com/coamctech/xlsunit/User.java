package com.coamctech.xlsunit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "SYS_USER")
public class User implements Serializable {
	
	static {
		System.out.println("hello go");
	}
	
	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@GenericGenerator(name = "uuidGenerator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(length = 36)
	private String id;
	
	@Column(length = 30, nullable = false)
	private String name; // 姓名
	
	@Column(length = 40, nullable = false)
	private String password; // 加密密码
	
	@Column(length = 3, nullable = false)
	private Long gender; // 性别
	
	
	@Column(nullable = false)
	private BigDecimal money; // 薪水
	
	@Column(nullable = false)
	private Timestamp bir; // 生日
	
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getGender() {
		return gender;
	}

	public void setGender(Long gender) {
		this.gender = gender;
	}

	

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Timestamp getBir() {
		return bir;
	}

	public void setBir(Timestamp bir) {
		this.bir = bir;
	}

	
	
	
}