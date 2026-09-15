# Übung: 1_1 Datenbankkonfiguration

## Lernziel

Konfiguration einer JPA-Persistence-Unit (`projectUnit`) und Verbindungsaufbau zu einem externen Apache Derby Network Server.

Dabei unterscheidest du:
- Standardisierte JPA-Properties vs. Provider-spezifische Einstellungen (Hibernate/EclipseLink)
- JDBC-Verbindungsdaten (Treiberklasse, URL, Credentials)
- DDL-Schema-Generierung für isolierte Schulungsumgebungen

## Ausgangszustand

In `src/META-INF/persistence.xml` ist lediglich eine leere Persistence-Unit `projectUnit` deklariert. Der Test `test/net/rentacar/TestConnection.java` schlägt fehl, da weder JDBC-Treiber noch Provider-Eigenschaften hinterlegt sind.

## Aufgabe

Öffne `src/META-INF/persistence.xml` und konfiguriere die Persistence-Unit `projectUnit`:

1. **JPA-Provider festlegen:**
   ```xml
   <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
   ```

2. **Standard-JDBC-Properties ergänzen:**
   ```xml
   <property name="jakarta.persistence.jdbc.driver" value="org.apache.derby.jdbc.ClientDriver" />
   <property name="jakarta.persistence.jdbc.url" value="jdbc:derby://localhost:1527/rentacar;create=true" />
   <property name="jakarta.persistence.jdbc.user" value="APP" />
   <property name="jakarta.persistence.jdbc.password" value="APP" />
   ```

3. **Schema-Generierung aktivieren:**
   ```xml
   <property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create" />
   ```

4. **SQL-Logging für die Fehlersuche einschalten:**
   ```xml
   <property name="hibernate.show_sql" value="true" />
   ```

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_1_JPA_DB_CONFIG -am test
```

Für den alternativen EclipseLink-Provider (`src/META-INF/persistence-eclipselink.xml`):
```bash
./mvnw -Peclipselink -pl 1_1_JPA_DB_CONFIG -am test
```

**Beobachtung:**
- Beobachte im Log, wann der Verbindungsaufbau zur Datenbank erfolgt.
- Prüfe, welche DDL-Aktionen die Schema-Generierung beim Start ausführt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testConnection`) läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Konfigurationsparameter gehören zum portablen Jakarta-JPA-Standard (`jakarta.persistence.*`) und welche sind herstellerspezifisch (`hibernate.*` / `eclipselink.*`)?
2. Warum eignet sich `drop-and-create` für Schulungs- und Integrationsumgebungen, birgt jedoch in Produktionssystemen erhebliche Risiken?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_1_JPA_DB_CONFIG_Loesung`.