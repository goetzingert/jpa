package net.rentacar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractJPATestCase {

	/** Verbindungsdaten des Derby Network Servers - identisch zu den persistence.xml der Module. */
	protected static final String JDBC_URL = "jdbc:derby://localhost:1527/rentacar;create=true";
	protected static final String JDBC_USER = "APP";
	protected static final String JDBC_PASSWORD = "APP";

	/**
	 * Inspektionsmodus: wird ueber das Maven-Profil "-Pinspect" (System-Property jpa.inspect=true)
	 * aktiviert. Dann bleibt das Schema nach dem Testlauf bestehen und die Testdaten werden
	 * committet, damit sie im Derby-Client (ij / IDE-Datenbankfenster) sichtbar sind.
	 */
	protected static final boolean INSPECT = Boolean.parseBoolean(System.getProperty("jpa.inspect", "false"));

	protected static EntityManagerFactory managerFactory;
	protected EntityManager manager;

	@BeforeAll
	public static void setUpEMFactory() {
		cleanDatabase();
		java.util.Map<String, Object> overrides = new java.util.HashMap<>();
		if (INSPECT) {
			// "create" statt "drop-and-create": der Provider legt die Tabellen an und raeumt
			// beim Schliessen der Factory nicht wieder auf.
			overrides.put("jakarta.persistence.schema-generation.database.action", "create");
		}
		managerFactory = Persistence.createEntityManagerFactory("projectUnit", overrides);
	}

	private static void cleanDatabase() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			try (java.sql.Connection conn = java.sql.DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
				java.sql.DatabaseMetaData meta = conn.getMetaData();
				try (java.sql.ResultSet rs = meta.getExportedKeys(null, JDBC_USER, "%")) {
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
				java.util.List<String> tables = new java.util.ArrayList<>();
				try (java.sql.ResultSet rs = meta.getTables(null, JDBC_USER, "%", new String[]{"TABLE"})) {
					while (rs.next()) {
						tables.add(rs.getString("TABLE_NAME"));
					}
				}
				// Mehrere Durchlaeufe: Tabellen, die wegen verbliebener Abhaengigkeiten
				// nicht geloescht werden koennen, werden im naechsten Durchlauf erneut versucht.
				for (int pass = 0; pass < 3 && !tables.isEmpty(); pass++) {
					java.util.List<String> remaining = new java.util.ArrayList<>();
					for (String t : tables) {
						try (java.sql.Statement stmt = conn.createStatement()) {
							stmt.execute("DROP TABLE \"" + t + "\"");
						} catch (Exception retryLater) {
							remaining.add(t);
						}
					}
					tables = remaining;
				}
				dropSequences(conn);
			}
		} catch (Exception ignored) {
		}
	}

	/** Derby-Sequenzen der Module mit @GeneratedValue(strategy = SEQUENCE) entfernen. */
	private static void dropSequences(java.sql.Connection conn) {
		java.util.List<String> sequences = new java.util.ArrayList<>();
		try (java.sql.Statement stmt = conn.createStatement();
			 java.sql.ResultSet rs = stmt.executeQuery("SELECT SEQUENCENAME FROM SYS.SYSSEQUENCES")) {
			while (rs.next()) {
				sequences.add(rs.getString(1));
			}
		} catch (Exception ignored) {
			return;
		}
		for (String s : sequences) {
			try (java.sql.Statement stmt = conn.createStatement()) {
				stmt.execute("DROP SEQUENCE \"" + s + "\" RESTRICT");
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Loescht im Inspektionsmodus vor jedem Test alle Datensaetze (nicht die Tabellen!),
	 * damit die Tests trotz Commit voneinander unabhaengig bleiben. Die Reihenfolge der
	 * Fremdschluessel ist unbekannt, deshalb wird die Liste mehrfach durchlaufen.
	 */
	private static void deleteAllRows() {
		try (java.sql.Connection conn = java.sql.DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
			java.util.List<String> tables = new java.util.ArrayList<>();
			try (java.sql.ResultSet rs = conn.getMetaData().getTables(null, JDBC_USER, "%", new String[]{"TABLE"})) {
				while (rs.next()) {
					tables.add(rs.getString("TABLE_NAME"));
				}
			}
			for (int pass = 0; pass < 3 && !tables.isEmpty(); pass++) {
				java.util.List<String> remaining = new java.util.ArrayList<>();
				for (String t : tables) {
					try (java.sql.Statement stmt = conn.createStatement()) {
						stmt.executeUpdate("DELETE FROM \"" + t + "\"");
					} catch (Exception retryLater) {
						remaining.add(t);
					}
				}
				tables = remaining;
			}
		} catch (Exception ignored) {
		}
	}

	@BeforeEach
	public void setUpEM() throws Exception {
		if (INSPECT)
			deleteAllRows();
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
		if(manager.getTransaction().isActive()) {
			if (INSPECT)
				manager.getTransaction().commit();
			else
				manager.getTransaction().rollback();
		}
		if(manager.isOpen())
			manager.close();
	}

	@AfterAll
	public static void tearDownEMFactory(){
		if(managerFactory != null && managerFactory.isOpen())
			managerFactory.close();
		if (INSPECT)
			System.out.println("[inspect] Schema und Daten bleiben erhalten: " + JDBC_URL
					+ " (Benutzer " + JDBC_USER + ")");
	}

}
