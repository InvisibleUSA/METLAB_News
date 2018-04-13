package me.metlabnews.Model.DataAccess.DbConnectors;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.DataAccess.Exceptions.DataUpdateFailedException;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedNonUniqueDataException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import me.metlabnews.Model.ResourceManagement.IResource;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Properties;




public class RelationalDbConnector implements IResource
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
	}


	@Override
	public void initialize()
	{
		try
		{
			Properties properties = new Properties();
			// properties.put("hibernate.id.new_generator_mappings","false");

			ConfigurationManager configManager = ConfigurationManager.getInstance();

			properties.setProperty("hibernate.connection.driver_class",
			                       configManager.getRdbmsDriver());
			properties.setProperty("dialect",
			                       configManager.getRdbmsSqlDialect());

			if(configManager.getRdbmsUseLocalDB())
			{
				properties.setProperty("hibernate.connection.url",
				                       configManager.getRdbmsLocalUrl());
			}
			else
			{
				properties.setProperty("hibernate.connection.url",
				                       configManager.getRdbmsRemoteUrl());
			}

			properties.setProperty("hibernate.connection.username",
			                       configManager.getRdbmsUsername());
			properties.setProperty("hibernate.connection.password",
			                       configManager.getRdbmsPassword());

			Configuration configuration = new Configuration();
			configuration.addProperties(properties);

			// Just to be sure
			configuration.addAnnotatedClass(Subscriber.class);
			configuration.addAnnotatedClass(SystemAdministrator.class);
			configuration.addAnnotatedClass(Organisation.class);

			m_sessionFactory = configuration.buildSessionFactory();
		}
		catch (Throwable e)
		{
			throw new ExceptionInInitializerError(e);
		}
	}


	@Override
	public void close()
	{
		disconnect();
	}


	public List<Object> getEntityList(String entity)
	{
		List<Object>resultSet = null;
		connect();
		try
		{
			String queryString = "from " + entity;
			Query query = m_session.get().createQuery(queryString);
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


	public List<Object> getEntityList(String entity, String property, Object value)
	{
		List<Object>resultSet = null;
		connect();
		try
		{
			String queryString = "from " + entity + " where " + property + " = :val";
			Query query = m_session.get().createQuery(queryString);
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


	public Object getUniqueEntity(String entity, String property, String value)
			throws RequestedDataDoesNotExistException, UnexpectedNonUniqueDataException
	{
		Object result = null;
		connect();
		try
		{
			String queryString = "from " + entity + " where " + property + " = :val";
			Query query = m_session.get().createQuery(queryString);
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



	private static RelationalDbConnector m_instance = null;
	private SessionFactory m_sessionFactory;
	private ThreadLocal<Session> m_session = new ThreadLocal<>();
	private ThreadLocal<Transaction> m_transaction = new ThreadLocal<>();
}
