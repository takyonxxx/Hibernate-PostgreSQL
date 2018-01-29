package com.biliyor.entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnector {

	private static HibernateConnector me;
	private Configuration cfg;
	private SessionFactory sessionFactory;
	private static String user;
	private static String password;
	private static int port;

	public HibernateConnector(String user, String password, int port) throws HibernateException {

		HibernateConnector.user = user;
		HibernateConnector.password = password;
		HibernateConnector.port = port;		   

		cfg = new Configuration();
		cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/testdb");
		cfg.setProperty("hibernate.connection.username", user);
		cfg.setProperty("hibernate.connection.password", password);
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.format_sql", "true");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");

		cfg.addResource("com/biliyor/entity/Person.hbm.xml");

		sessionFactory = cfg.buildSessionFactory();
	}

	public static synchronized HibernateConnector getInstance() throws HibernateException {
		if (me == null) {
			me = new HibernateConnector(user,password,port);
		}

		return me;
	}

	public Session getSession() throws HibernateException {
		Session session = sessionFactory.openSession();

		
		System.out.println("User info " + user + " " + password);		
	
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