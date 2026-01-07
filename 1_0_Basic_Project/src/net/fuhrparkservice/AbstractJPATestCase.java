package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractJPATestCase {

	protected static EntityManagerFactory managerFactory;
	protected EntityManager manager;

	@BeforeAll
	public static void setUpEMFactory() {
		managerFactory = Persistence.createEntityManagerFactory("projectUnit");
	}

	@BeforeEach
	public void setUpEM() throws Exception {
		//Session zur DB Öffnen
		manager = managerFactory.createEntityManager();
		manager.isOpen();
		setUPTransaction();
		setUpTestData();
	}
	
	private void setUPTransaction(){
		manager.getTransaction().begin();
	}
	
	public void setUpTestData() throws Exception{
		this.setUp();
	}
	
	protected abstract void setUp() throws Exception;

	@AfterEach
	public void transactionRollback(){
		if(manager.getTransaction().isActive())
			manager.getTransaction().rollback();
	}

}
