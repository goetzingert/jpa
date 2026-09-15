package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "tbl_User")
public class User implements Serializable {

	@TableGenerator(name = "MY_GEN", table = "tbl_MyGen", pkColumnName = "PK_COLUMN", valueColumnName = "VALUE_COLUMN", pkColumnValue = "USER_ID", allocationSize = 10)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="MY_GEN")
	private Long id;
	private String firstName;
	private String lastName;

	public User() {
		super();
	}

	public User(String firstName, String lastName) {

		this.setFirstName(firstName);
		this.setLastName(lastName);
	}

	public Long getId() {
		return this.id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

}
