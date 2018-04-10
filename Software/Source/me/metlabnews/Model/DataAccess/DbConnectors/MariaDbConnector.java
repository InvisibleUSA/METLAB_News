package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.DataAccess.Exceptions.DataUpdateFailedException;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Properties;



public class MariaDbConnector implements AutoCloseable
{
	public static MariaDbConnector getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new MariaDbConnector();
		}
		return m_instance;
	}

	private MariaDbConnector()
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


	public List<Object> getEntityList(String property, String value)
	{
		List<Object>resultSet = null;
		String table = Object.class.getName();
		connect();
		try
		{
			Query query = m_session.get().createQuery("from " + table
					                                          + "where " + property
					                                          + " = :val");
			query.setParameter("val", value);
			resultSet = query.getResultList();
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
		}
		finally
		{
			disconnect();
		}
		return resultSet;
	}

	public Object getUniqueEntity(String property, String value)
			throws RequestedDataDoesNotExistException, UnexpectedNonUniqueDataException
	{
		Object result = null;
		String table = Object.class.getName();
		connect();
		try
		{
			Query query = m_session.get().createQuery("from " + table
					                                          + "where " + property
					                                          + " = :val");
			query.setParameter("val", value);
			result = query.getSingleResult();
			m_transaction.get().commit();
		}
		catch(NoResultException e)
		{
			throw new RequestedDataDoesNotExistException(e);
		}
		catch(NonUniqueResultException e)
		{
			throw new UnexpectedNonUniqueDataException(e);
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
		}
		finally
		{
			disconnect();
		}
		return result;
	}


	public Long addEntity(Object entity)
			throws DataCouldNotBeAddedException
	{
		Long id;
		connect();
		try
		{
			id = (Long)m_session.get().save(entity);
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
			throw new DataCouldNotBeAddedException(e);
		}
		finally
		{
			disconnect();
		}
		return id;
	}

	public void updateEntity(Object entity)
			throws DataUpdateFailedException
	{
		connect();
		try
		{
			m_session.get().update(entity);
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
			throw new DataUpdateFailedException(e);
		}
		finally
		{
			disconnect();
		}
	}

	public void deleteEntity(Object entity)
			throws RequestedDataDoesNotExistException
	{
		connect();
		try
		{
			m_session.get().delete(entity);
			m_transaction.get().commit();
		}
		catch(HibernateException e)
		{
			if(m_transaction != null)
			{
				m_transaction.get().rollback();
			}
			throw new RequestedDataDoesNotExistException(e);
		}
		finally
		{
			disconnect();
		}
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


	@Override
	public void close()
	{
		disconnect();
	}



	private static MariaDbConnector m_instance = null;
	private SessionFactory m_sessionFactory;
	private ThreadLocal<Session> m_session = new ThreadLocal<>();
	private ThreadLocal<Transaction> m_transaction = new ThreadLocal<>();
}
