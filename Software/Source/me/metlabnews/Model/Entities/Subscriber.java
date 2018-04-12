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
	public boolean isOrganisationAdministrator()
	{
		return isOrganisationAdministrator;
	}

	public void setOrganisationAdministrator(boolean organisationAdministrator)
	{
		isOrganisationAdministrator = organisationAdministrator;
	}

	@Column(name = "verificationPending")
	public boolean isVerificationPending()
	{
		return verificationPending;
	}

	public void setVerificationPending(boolean verificationPending)
	{
		this.verificationPending = verificationPending;
	}



	protected Organisation organisationId;
	protected boolean isOrganisationAdministrator;
	protected boolean verificationPending = true;
}
