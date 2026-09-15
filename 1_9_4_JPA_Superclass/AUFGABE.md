# Übung: 1_9_4 Mapped Superclass

## Lernziel

Gemeinsame persistente Attribute (wie ID, Versionsfelder für Optimistic Locking oder Timestamps) in einer Basisklasse mittels `@MappedSuperclass` bündeln und an konkrete Entitäten vererben, ohne dass für die Basisklasse eine eigene Datenbanktabelle erzeugt wird.

## Ausgangszustand

Die Entitäten (`Person`, `User`, etc.) deklarieren ihre ID- und Versionsfelder bisher redundant. Eine gemeinsame Basisklasse `AbstractBusinessObject` fehlt in `1_9_4_JPA_Superclass` bzw. ist noch nicht mit `@MappedSuperclass` eingebunden.

## Aufgabe

Bearbeite bzw. erstelle `AbstractBusinessObject.java` und passe `Person.java` sowie `User.java` an:

1. **Mapped Superclass `AbstractBusinessObject` erstellen/konfigurieren:**
   - Erstelle `src/net/rentacar/model/AbstractBusinessObject.java` (implementiert `Serializable`).
   - Annotiere die Klasse mit `@MappedSuperclass`.
   - Deklariere `@Id private String id;` mit Standard-UUID-Generierung und Getter.
   - Ergänze optional ein Versionsfeld für optimistisches Sperren: `@Version @Column(name = "optimisticLocking") private long version;`.

2. **Entitäten von `AbstractBusinessObject` ableiten:**
   - Passe `Person` und `User` so an, dass sie `AbstractBusinessObject` erweitern (`extends AbstractBusinessObject`).
   - Entferne die redundanten `@Id`-Deklarationen in den Kindklassen.

3. **Vererbung und Persistenz testen:**
   - Führe die Tests in `test/net/rentacar/TestConnection.java` aus.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_9_4_JPA_Superclass -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die erzeugten Tabellen: Es existieren nur `tbl_person`, `tbl_user` etc. Für `AbstractBusinessObject` wird **keine** eigene Tabelle angelegt.
- Prüfe, dass die Spalten `id` und `optimisticLocking` (bzw. `version`) in jeder einzelnen konkreten Entitätstabelle vorhanden sind.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 9 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum ist eine `@MappedSuperclass` selbst keine Entität und kann daher nicht in JPQL-Abfragen (`SELECT a FROM AbstractBusinessObject a`) oder `manager.find(...)` verwendet werden?
2. In welchen Architektur-Szenarien ist `@MappedSuperclass` einer echten Entity-Vererbung (`@Inheritance`) vorzuziehen?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_9_4_JPA_Superclass_Loesung`.
