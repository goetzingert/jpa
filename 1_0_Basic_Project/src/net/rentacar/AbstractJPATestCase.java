package net.rentacar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractJPATestCase {

	protected static EntityManagerFactory managerFactory;
	protected EntityManager manager;

	@BeforeAll
	public static void setUpEMFactory() {
		cleanDatabase();
		managerFactory = Persistence.createEntityManagerFactory("projectUnit");
	}

	private static void cleanDatabase() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:derby://localhost:1527/rentacar;create=true", "APP", "a")) {
				java.sql.DatabaseMetaData meta = conn.getMetaData();
				try (java.sql.ResultSet rs = meta.getExportedKeys(null, "APP", "%")) {
					while (rs.next()) {
						String fkName = rs.getString("FK_NAME");
						String fkTable = rs.getString("FKTABLE_NAME");
						if (fkName != null && fkTable != null) {
							try (java.sql.Statement stmt = conn.createStatement()) {
								stmt.execute("ALTER TABLE \"" + fkTable + "\" DROP CONSTRAINT \"" + fkName + "\"");
							} catch (Exception ignored) {}
						}
					}
				}
				try (java.sql.ResultSet rs = meta.getTables(null, "APP", "%", new String[]{"TABLE"})) {
					java.util.List<String> tables = new java.util.ArrayList<>();
					while (rs.next()) {
						tables.add(rs.getString("TABLE_NAME"));
					}
					for (String t : tables) {
						try (java.sql.Statement stmt = conn.createStatement()) {
							stmt.execute("DROP TABLE \"" + t + "\"");
						} catch (Exception ignored) {}
					}
				}
			}
		} catch (Exception ignored) {
		}
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
		if(manager.isOpen())
			manager.close();
	}

	@AfterAll
	public static void tearDownEMFactory(){
		if(managerFactory != null && managerFactory.isOpen())
			managerFactory.close();
	}

}
