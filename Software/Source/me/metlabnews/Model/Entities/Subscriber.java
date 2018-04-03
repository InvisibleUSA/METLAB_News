package me.metlabnews.Model.Entities;

import javax.persistence.*;



@Entity
@Table
public class Subscriber extends User
{
	public Subscriber(String email, String password, String firstName, String lastName,
	                  Organisation organisation, boolean isOrganisationAdministrator)
	{
		super(email, password, firstName, lastName);
		this.organisationId = organisation;
		this.isOrganisationAdministrator = isOrganisationAdministrator;
	}

	public Subscriber()
	{
		super("", "", "", "");
		this.organisationId = null;
		this.isOrganisationAdministrator = false;
	}



	@JoinColumn(name = "organisationId")
	@ManyToOne(cascade = CascadeType.ALL)
	public Organisation getOrganisationId()
	{
		return organisationId;
	}

	public void setOrganisationId(Organisation organisation)
	{
		this.organisationId = organisation;
	}


	@Column(name = "isOrganisationAdministrator")
	//@org.hibernate.annotations.Type(type="true_false")
	public boolean isOrganisationAdministrator()
	{
		return isOrganisationAdministrator;
	}

	public void setOrganisationAdministrator(boolean organisationAdministrator)
	{
		isOrganisationAdministrator = organisationAdministrator;
	}



	protected Organisation organisationId;
	protected boolean isOrganisationAdministrator = false;
}
