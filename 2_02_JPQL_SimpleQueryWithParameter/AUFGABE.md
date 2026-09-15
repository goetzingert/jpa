# Übung: 2_02 JPQL-Abfrage mit Parametern

## Lernziel

JPQL-Abfragen mit benannten Parametern (`:paramName`) oder Positions-Parametern (`?1`) absichern, dynamische Werte über `setParameter()` binden und SQL-Injection-Gefahren vermeiden.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` ist die Testmethode `testQueryForVehicleTypesMoreThen130HPWithParameter()` noch nicht implementiert (`int numberOfVehicleTypes = -1;`). Der Test schlägt bei der Assertion fehl.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Parameterisierte JPQL-Abfrage definieren:**
   - Erstelle eine Abfrage auf `VehicleType`, die die PS-Zahl über einen Parameter filtert:
     `SELECT f FROM VehicleType f WHERE f.hp > :ps` (oder `WHERE f.hp > ?1`).
   
2. **Parameter binden und ausführen:**
   - Setze den Parameterwert mittels `query.setParameter("ps", 130)` bzw. `query.setParameter(1, 130)`.
   - Ermittle die Anzahl der Treffer (`resultList.size()`) und weise sie `numberOfVehicleTypes` zu.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_02_JPQL_SimpleQueryWithParameter -am test
```

**Beobachtung im SQL-Log:**
- Beobachte im JDBC-Log, dass JPA Prepared Statements mit Bind-Variablen (`?`) an die Derby-Datenbank sendet.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft fehlerfrei durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
`numberOfVehicleTypes` ist exakt 1.

## Reflexion

1. Warum dürfen Parameter niemals durch String-Konkatenation (`"WHERE f.hp > " + ps`) in JPQL eingebaut werden (SQL/JPQL-Injection, Statement-Caching)?
2. Welche Vorteile bieten benannte Parameter (`:ps`) gegenüber Positions-Parametern (`?1`) bei vielen Parametern in komplexen Abfragen?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_02_JPQL_SimpleQueryWithParameter_Loesung`.
