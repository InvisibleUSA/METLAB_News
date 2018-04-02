package me.metlabnews.Model.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table
public class Organisation
{
	public Organisation(String name)
	{
		this.name = name;
	}

	public Organisation()
	{
		this(null);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}


	private long    id;
	private String name;
}
