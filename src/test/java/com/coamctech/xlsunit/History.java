package com.coamctech.xlsunit;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity // 参考History2, 测试俩种主键方式
public class History {
	
	@EmbeddedId
	private HistoryPk historyPk;
	@Column
	String name;
	
	
	
	public HistoryPk getHistoryPk() {
		return historyPk;
	}
	public void setHistoryPk(HistoryPk historyPk) {
		this.historyPk = historyPk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}
