package com.coamctech.xlsunit;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class SysOrder {
	@Id
	@SequenceGenerator(name = "generator", sequenceName="SYS_ORDER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@Column
	private Long id ;
	
	@Column
	private String name;
	
	BigDecimal bg = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBg() {
		return bg;
	}

	public void setBg(BigDecimal bg) {
		this.bg = bg;
	}
	
	
	
}
