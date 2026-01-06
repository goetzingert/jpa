package net.rentacar.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public class AbstractBusinessObject implements Serializable{

	@Id
	private String id;
	
	@Column(name="optimisticLocking")
	@Version
	private long version;

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
