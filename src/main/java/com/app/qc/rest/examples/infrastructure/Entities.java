package com.app.qc.rest.examples.infrastructure;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Entities")
public class Entities {

	protected String TotalResults;

	protected List<Entity> Entity;

	public String getTotalResults() {
		return TotalResults;
	}

	public void setTotalResults(String totalResults) {
		TotalResults = totalResults;
	}

	public List<Entity> getEntity() {
		return Entity;
	}

	public void setEntity(List<Entity> entity) {
		Entity = entity;
	}

}
