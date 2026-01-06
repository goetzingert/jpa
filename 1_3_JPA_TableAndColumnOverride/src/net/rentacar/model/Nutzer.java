package net.rentacar.model;

import java.io.Serializable;

public class Nutzer implements Serializable{

	public String firstName;
	public String lastName;
	
	public Nutzer()
	{
		super();
	}
	
	public Nutzer(String firstName, String lastName)
	{

		this.firstName = firstName;
		this.lastName = lastName;
	}

}
