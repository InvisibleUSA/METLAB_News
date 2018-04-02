package me.metlabnews.Model.Entities;

import javax.persistence.*;



@Entity
@Table
public class Subscriber extends User
{
	public Subscriber(String email, String password, String firstName, String lastName,
	                  Organisation organisation)
	{
		super(email, password, firstName, lastName);
		this.organisation = organisation;
	}

	public Subscriber()
	{
		super("", "", "", "");
		this.organisation = null;
	}



	@JoinColumn(name = "ORGANISATION_ID")
	@ManyToOne(cascade = CascadeType.ALL)
	public Organisation getOrganisation()
	{
		return organisation;
	}

	public void setOrganisation(Organisation organisation)
	{
		this.organisation = organisation;
	}


	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isOrganisationAdministrator()
	{
		return isOrganisationAdministrator;
	}

	public void setOrganisationAdministrator(boolean organisationAdministrator)
	{
		isOrganisationAdministrator = organisationAdministrator;
	}



	protected Organisation organisation;
	protected boolean isOrganisationAdministrator = false;
}
