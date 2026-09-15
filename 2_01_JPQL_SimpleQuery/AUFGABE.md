# Übung: 2_01 Einfache JPQL-Abfrage

## Lernziel

Eine typisierte JPQL-Abfrage mit `EntityManager.createQuery()` und `TypedQuery<T>` formulieren, Entity-Attribute filtern und Ergebnisse typsicher verarbeiten.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` ist die Testmethode `testQueryForVehicleTypeMoreThan130HP()` noch unvollständig (`TypedQuery<VehicleType> query = null;`). Der Test schlägt mit einer `NullPointerException` fehl.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Typisierte JPQL-Abfrage erstellen:**
   - Erstelle eine `TypedQuery<VehicleType>` über `manager.createQuery(...)`.
   - JPQL-Statement: `SELECT f FROM VehicleType f WHERE f.hp > 130` (unter Angabe der Ergebnisklasse `VehicleType.class`).

2. **Ergebnis ausführen:**
   - Führe die Query mit `query.getResultList()` aus.
   - Überprüfe, dass genau der BMW mit 150 PS gefunden wird.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_01_JPQL_SimpleQuery -am test
```

**Beobachtung im SQL-Log:**
- Beobachte das generierte relationale `SELECT`-Statement.
- Beachte, wie JPA den JPQL-Klassennamen `VehicleType` auf den tatsächlichen Tabellennamen und `f.hp` auf die Spalte abbildet.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
Es wird genau 1 Fahrzeugtyp (`BMW`, 150 PS) zurückgegeben.

## Reflexion

1. Welchen Sicherheits- und Lesbarkeitsvorteil bietet `manager.createQuery(jpql, VehicleType.class)` (`TypedQuery`) gegenüber der klassischen `Query` ohne Typangabe?
2. Warum lautet die Klausel `FROM VehicleType f` und nicht `FROM tbl_VehicleType`?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_01_JPQL_SimpleQuery_Loesung`.
