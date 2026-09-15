package net.rentacar.model;

import java.io.Serializable;

public class User implements Serializable{

	public String firstName;
	public String lastName;
	
	public User()
	{
		super();
	}
	
	public User(String firstName, String lastName)
	{

		this.firstName = firstName;
		this.lastName = lastName;
	}

}
