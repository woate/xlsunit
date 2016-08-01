package com.coamctech.xlsunit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(HistoryPk2.class)
@Table(name = "History2")
public class History2 implements java.io.Serializable{
	
	@Id
	String id ;
	@Id
	Integer ver;
	@Column
	String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getVer() {
		return ver;
	}
	public void setVer(Integer ver) {
		this.ver = ver;
	}

	
	
}
