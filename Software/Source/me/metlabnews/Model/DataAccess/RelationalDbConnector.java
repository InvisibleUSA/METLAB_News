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
import java.util.Properties;



public class RelationalDbConnector implements AutoCloseable
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
			Properties properties = new Properties();
			properties.put("hibernate.id.new_generator_mappings","false");
			configuration.addProperties(properties);
			m_sessionFactory = configuration.buildSessionFactory();
		} catch (Throwable e)
		{
			System.err.println("[ERROR] Failed to create sessionFactory object." + e);
			throw new ExceptionInInitializerError(e);
		}
	}


	@Override
	public void close()
	{
		disconnect();
	}


	private void connect()
	{
		try
		{
			m_session.set(m_sessionFactory.openSession());
			m_transaction.set(m_session.get().beginTransaction());
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
			m_transaction.remove();
			m_session.get().close();
			m_session.remove();
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
			Long id = (Long)m_session.get().save(subscriber);
			subscriber.setId(id);
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
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
			Query query = m_session.get().createQuery("from Subscriber where email = :email");
			query.setParameter("email", email);
			subscriber = (Subscriber)query.getSingleResult();
			m_transaction.get().commit();
		}
		catch(NoResultException e)
		{
			throw new RequestedDataDoesNotExistException();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
			System.err.println("[ERROR] Failed to query user by email:");
			System.err.println(e.getMessage());
		}
		finally
		{
			disconnect();
		}
		return subscriber;
	}


	public void addOrganisation(Organisation organisation)
	{
		connect();
		try
		{
			Long id = (Long)m_session.get().save(organisation);
			organisation.setId(id);
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
			System.err.println("[ERROR] Failed to add new organisation:");
			System.err.println(e.getMessage());
		}
		finally
		{
			disconnect();
		}
	}

	public Organisation getOrganisationByName(String name) throws RequestedDataDoesNotExistException
	{
		Organisation organisation = null;
		connect();
		try
		{
			Query query = m_session.get().createQuery("from Organisation where name= :name");
			query.setParameter("name", name);
			organisation = (Organisation)query.getSingleResult();
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
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
	private ThreadLocal<Session> m_session = new ThreadLocal<>();
	private ThreadLocal<Transaction> m_transaction = new ThreadLocal<>();
}
