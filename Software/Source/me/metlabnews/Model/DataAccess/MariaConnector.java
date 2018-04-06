package me.metlabnews.Model.DataAccess;

import me.metlabnews.Model.DataAccess.Exceptions.DataCouldNotBeAddedException;
import me.metlabnews.Model.DataAccess.Exceptions.DataUpdateFailedException;
import me.metlabnews.Model.DataAccess.Exceptions.RequestedDataDoesNotExistException;
import me.metlabnews.Model.DataAccess.Exceptions.UnexpectedDataException;
import me.metlabnews.Model.Entities.Organisation;
import me.metlabnews.Model.Entities.Subscriber;
import me.metlabnews.Model.Entities.SystemAdministrator;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Properties;



public class MariaConnector implements AutoCloseable
{
	public static MariaConnector getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new MariaConnector();
		}
		return m_instance;
	}


	private MariaConnector()
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
			throws DataCouldNotBeAddedException
	{
		Long id = addEntity(subscriber);
		subscriber.setId(id);
	}


	public void addOrganisation(Organisation organisation)
			throws DataCouldNotBeAddedException
	{
		Long id = addEntity(organisation);
		organisation.setId(id);
	}


	public void deleteSubscriber(Subscriber subscriber)
			throws RequestedDataDoesNotExistException
	{
		deleteEntity(subscriber);
	}


	public void deleteOrganisation(Organisation organisation)
			throws RequestedDataDoesNotExistException
	{
		deleteEntity(organisation);
	}


	public void updateSubscriber(Subscriber subscriber)
			throws DataUpdateFailedException
	{
		updateEntity(subscriber);
	}


	public void updateOrganisation(Organisation organisation)
			throws DataUpdateFailedException
	{
		updateEntity(organisation);
	}


	public Subscriber getSubscriberByEmail(String email)
			throws RequestedDataDoesNotExistException, UnexpectedDataException
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
			throw new RequestedDataDoesNotExistException(e);
		}
		catch(NonUniqueResultException e)
		{
			throw new UnexpectedDataException(e);
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
		return subscriber;
	}

	// TODO: merge with getSubscriberByEmail()
	public SystemAdministrator getSystemAdministratorByEmail(String email)
			throws RequestedDataDoesNotExistException, UnexpectedDataException
	{
		SystemAdministrator admin = null;
		connect();
		try
		{
			Query query = m_session.get().createQuery("from " +
					"SystemAdministrator where email = :email");
			query.setParameter("email", email);
			admin = (SystemAdministrator)query.getSingleResult();
			m_transaction.get().commit();
		}
		catch(NoResultException e)
		{
			throw new RequestedDataDoesNotExistException(e);
		}
		catch(NonUniqueResultException e)
		{
			throw new UnexpectedDataException(e);
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
		return admin;
	}


	public Subscriber[] getAllSubscribersOfOrganisation(Organisation organisation)
	{
		List<Subscriber> subscribers = null;
		connect();
		try
		{
			Query query = m_session.get().createQuery("from Subscriber " +
					                                          "where organisationId = :id");
			query.setParameter("id", organisation.getId());
			subscribers = query.getResultList();
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
		return subscribers.toArray(new Subscriber[subscribers.size()]);
	}


	public Organisation getOrganisationByName(String name)
			throws RequestedDataDoesNotExistException
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
		catch(NoResultException e)
		{
			throw new RequestedDataDoesNotExistException(e);
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
		return organisation;
	}



	private Long addEntity(Object entity)
			throws DataCouldNotBeAddedException
	{
		//TODO Find out why the fuck IntelliJ marks the following initialisation as redundant
		//Klar ist das redundant wenn du das 3 Zeilen weiter sowieso wieder ueberschreibst

		Long id = -1L;
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

	private void updateEntity(Object entity)
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

	private void deleteEntity(Object entity)
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



	private static MariaConnector m_instance = null;
	private SessionFactory m_sessionFactory;
	private ThreadLocal<Session> m_session = new ThreadLocal<>();
	private ThreadLocal<Transaction> m_transaction = new ThreadLocal<>();
}
