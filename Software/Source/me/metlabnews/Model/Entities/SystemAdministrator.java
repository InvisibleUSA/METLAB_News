package me.metlabnews.Model.Entities;

import javax.persistence.Entity;
import javax.persistence.Table;



@Entity
@Table
public class SystemAdministrator extends User
{
	public SystemAdministrator(String email, String password, String firstName, String lastName)
	{
		super(email, password, firstName, lastName);
	}
}
