package com.coamctech.xlsunit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HistoryPk implements Serializable{
	
	@Column
	String id ;
	@Column
	Integer ver;
	
	
	public HistoryPk(String id, Integer version) {
		super();
		this.id = id;
		this.ver = version;
	}
	
	
	public HistoryPk() {
		super();
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
