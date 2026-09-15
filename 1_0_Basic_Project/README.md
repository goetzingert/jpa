# 1_0_Basic_Project: Vorbereitung und Orientierung

Dieses Modul ist das **Basis- und Vorbereitungsmodul** für das gesamte JPA-Seminar. Es dient der technischen Validierung Ihrer Entwicklungsumgebung und der Einführung in die Fachdomäne der Schulung.

> **Wichtig:** Dieses Modul ist keine klassische JPA-Übung mit Aufgaben- und Lösungsstand. Die praktischen JPA-Übungen beginnen mit Modul `1_1_JPA_DB_CONFIG`.

---

## 1. Voraussetzungen und Setup-Check

### Java und Maven

Das Projekt setzt **Java 17** oder neuer voraus. Verwenden Sie den bereitgestellten Maven Wrapper, um Versionskonflikte zu vermeiden:

```bash
# Maven Wrapper und Java-Version prüfen
./mvnw -version
```

### Datenbank (Apache Derby Network Server)

Alle Integrations- und Modultests nutzen standardmäßig einen extern laufenden Apache Derby Network Server. Dadurch können erzeugte Tabellen, Fremdschlüssel und Datensätze während der Übungen live inspiziert werden.

- **Host & Port:** `localhost:1527`
- **Datenbankname:** `rentacar`
- **Beuser / Passwort:** `APP` / `APP`
- **JDBC-URL:** `jdbc:derby://localhost:1527/rentacar;create=true`

Starten Sie den Derby Network Server vor dem Ausführen der Tests in einem separaten Terminal.

---

## 2. Die Fachdomäne: Rent-a-Car

Das Seminar verwendet ein durchgängiges Domänenmodell einer Autovermietung (*Rent-a-Car*).

### Kernentitäten und Beziehungen:
- **VehicleType:** Fahrzeugtypen (Marke, Modell, PS, Höchstgeschwindigkeit, Mietsatz).
- **Vehicle (Car / Truck):** Konkrete Einzelfahrzeuge mit Fahrgestellnummer und Werkstattstatus; spezialisiert in PKW (Türen) und LKW (Zuladung).
- **Shop:** Mietstationen/Filialen an verschiedenen Standorten.
- **User / Customer:** Beuser und Kundenstammdaten (Referenznummer, Bankverbindung).
- **Reservation:** Buchungen von Fahrzeugen an Mietstationen durch Kunden für definierte Zeiträume.

### Relationaler Blick vs. JPA-Objektblick

- `create.sql` und `insert.sql` im Projektstamm dienen ausschließlich der **fachlichen Orientierung und Analyse** des relationalen Schemas.
- In JPA modellieren wir diese Domäne objektorientiert als Entities mit Assoziationen (`@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`) und Vererbungsstrukturen (`@Inheritance`). Die Tabellen werden in den Übungen schema-generiert oder über JPA-Mappings abgebildet.

---

## 3. Die Test-Basisklasse `AbstractJPATestCase`

In `src/net/rentacar/AbstractJPATestCase.java` ist das zentrale Testgerüst definiert, das in vielen nachfolgenden Modulen genutzt wird:

- **`setUpEMFactory()` (`@BeforeAll`):** Erstellt einmalig die `EntityManagerFactory` für die Persistence Unit `projectUnit`.
- **`setUpEM()` (`@BeforeEach`):** Öffnet einen neuen `EntityManager`, startet eine Transaktion und ruft die abstrakte Template-Methode `setUp()` für modulspezifische Testdaten auf.
- **`transactionRollback()` (`@AfterEach`):** Führt nach jedem Test einen Rollback durch und schließt den `EntityManager`, damit Tests isoliert und seiteneffektfrei bleiben.

---

## 4. Testlauf zur Verifikation

Nachdem der Derby-Server läuft, kann die Umgebung mit einem Referenztest überprüft werden:

```bash
./mvnw -pl 1_1_JPA_DB_CONFIG_Loesung -am test
```

Erfolgreicher Durchlauf signalisiert: Java, Maven Wrapper, Derby Network Server und JPA-Provider sind einsatzbereit für die erste Übung in `1_1_JPA_DB_CONFIG`.
