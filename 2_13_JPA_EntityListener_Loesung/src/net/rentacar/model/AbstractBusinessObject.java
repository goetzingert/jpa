package net.rentacar.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractBusinessObject implements Serializable{

	@Id
	private String id;

	public AbstractBusinessObject() {
		this(UUID.randomUUID().toString());
	}

	public AbstractBusinessObject(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
