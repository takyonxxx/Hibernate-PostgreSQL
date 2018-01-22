package com.biliyor.entity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnector {

	private static HibernateConnector me;
	private Configuration cfg;
	private SessionFactory sessionFactory;

	private HibernateConnector() throws HibernateException {

		// build the config
		cfg = new Configuration();

		/**
		 * Connection Information..
		 */

		cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/testdb");
		cfg.setProperty("hibernate.connection.username", "user1");
		cfg.setProperty("hibernate.connection.password", "0000");
		cfg.setProperty("hibernate.show_sql", "true");    
		cfg.setProperty("hibernate.format_sql", "true");    
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");    

		cfg.addResource("com/biliyor/entity/Person.hbm.xml");

		sessionFactory = cfg.buildSessionFactory();
	}

	public static synchronized HibernateConnector getInstance() throws HibernateException {
		if (me == null) {
			me = new HibernateConnector();
		}

		return me;
	}

	public Session getSession() throws HibernateException {
		Session session = sessionFactory.openSession();
		if (!session.isConnected()) {
			this.reconnect();
		}
		return session;
	}

	private void reconnect() throws HibernateException {
		this.sessionFactory = cfg.buildSessionFactory();
	}

	public void shutdown() {
		sessionFactory.close();
	}
}