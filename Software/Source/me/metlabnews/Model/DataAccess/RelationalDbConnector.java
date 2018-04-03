package me.metlabnews.Model.DataAccess;

import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.Query;



public class RelationalDbConnector
{
	public static RelationalDbConnector getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new RelationalDbConnector();
		}
		return m_instance;
	}


	private RelationalDbConnector()
	{
		try
		{
			Configuration configuration = new Configuration();
			// Resources/hibernate.cfg.xml contains IP and PortNr of MariaDB
			configuration.configure("hibernate.cfg.xml");
			m_sessionFactory = configuration.buildSessionFactory();
		} catch (Throwable e)
		{
			System.err.println("[ERROR] Failed to create sessionFactory object." + e);
			throw new ExceptionInInitializerError(e);
		}
	}


	// just ignore some of Oracle's ridiculous bullshit
	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable
	{
		disconnect();
		super.finalize();
	}


	private void connect()
	{
		try
		{
			m_session = m_sessionFactory.openSession();
			m_transaction = m_session.beginTransaction();
		}
		catch(HibernateException e)
		{
			System.err.println("[ERROR] Connection attempt to database failed:");
			System.err.println(e.getMessage());
		}
	}

	private void disconnect()
	{
		try
		{
			m_transaction = null;
			m_session.close();
			m_session = null;
		}
		catch(Exception e)
		{
			System.err.println("Failed to disconnect from database:");
			System.err.println(e.getMessage());
		}
	}


	public void addSubscriber(Subscriber subscriber)
	{
		connect();
		try
		{
			Long id = (Long)m_session.save(subscriber);
			subscriber.setId(id);
			m_transaction.commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.rollback();
			}
			System.err.println("[ERROR] Failed to add new user:");
			System.err.println(e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}

	public Subscriber getSubscriberByEmail(String email) throws RequestedDataDoesNotExistException
	{
		Subscriber subscriber = null;
		connect();
		try
		{
			Query query = m_session.createQuery("from Subscriber where email = :email");
			query.setParameter("email", email);
			subscriber = (Subscriber)query.getSingleResult();
			//subscriber = (Subscriber)query.getResultList().get(0);
			m_transaction.commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.rollback();
			}
			System.err.println("[ERROR] Failed to query user by email:");
			System.err.println(e.getMessage());
		}
		catch(NoResultException e)
		{
			throw new RequestedDataDoesNotExistException();
		}
		finally
		{
			disconnect();
		}
		return subscriber;
	}


	public Organisation getOrganisationByName(String name) throws RequestedDataDoesNotExistException
	{
		Organisation organisation = null;
		connect();
		try
		{
			Query query = m_session.createQuery("from Organisation where name= :name");
			query.setParameter("name", name);
			organisation = (Organisation)query.getSingleResult();
			m_transaction.commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.rollback();
			}
			System.err.println("Failed to query organisation by name:");
			System.err.println(e.getMessage());
		}
		catch(NoResultException e)
		{
			throw new RequestedDataDoesNotExistException();
		}
		finally
		{
			disconnect();
		}
		return organisation;
	}


	private static RelationalDbConnector m_instance = null;
	private SessionFactory m_sessionFactory;
	private Session m_session;
	private Transaction m_transaction;
}
