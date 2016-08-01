package com.coamctech.xlsunit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public class HistoryPk2 implements Serializable{
	
	
	String id ;
	Integer ver;
	
	
	public HistoryPk2() {
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ver == null) ? 0 : ver.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryPk2 other = (HistoryPk2) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ver == null) {
			if (other.ver != null)
				return false;
		} else if (!ver.equals(other.ver))
			return false;
		return true;
	}

	
}
