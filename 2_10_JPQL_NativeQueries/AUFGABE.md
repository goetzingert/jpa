# Übung: 2_10 Native SQL-Queries und SqlResultSetMapping

## Lernziel

Native SQL-Abfragen mit `EntityManager.createNativeQuery()` ausführen, wenn datenbankspezifische Funktionen benötigt werden, und relationale Ergebnismengen über `@SqlResultSetMapping` oder direkte Spaltenlisten auf Entity- oder DTO-Strukturen abbilden.

## Ausgangszustand

In `test/net/rentacar/TestNativeQuery.java` sind die Testmethoden `testNativeQueryOfKunde()` und `testNativeQueryOfVehicleUndItem()` noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestNativeQuery.java`:

1. **Native SQL für Entity-Ergebnisse:**
   - In `testNativeQueryOfKunde()`: Formuliere eine native SQL-Abfrage über Tabellennamen (`tbl_Customer`, `tbl_Person`), um Kundendaten abzufragen.
   - Binde das Ergebnis an `Customer.class` als Entity-Ergebnis.

2. **Komplexe Tabellen-Joins mit Native SQL:**
   - In `testNativeQueryOfVehicleUndItem()`: Führe ein natives SQL-Statement über mehrere Tabellen aus (`tbl_VehicleType`, `TBL_Car`, `TBL_TRUCK`, `tbl_Vehicle`).
   - Verarbeite die zurückgegebene `List<?>` und prüfe, dass die Spaltenwerte korrekt geladen werden.

3. **Positions-Parameter in Native Queries:**
   - Prüfe `nativeQuerySupportsBoundParameters()`, wo Parameter mit `?1` gebunden werden (`SELECT COUNT(*) FROM tbl_VehicleType WHERE HP > ?1`).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_10_JPQL_NativeQueries -am test
```

**Beobachtung im SQL-Log:**
- Beachte, dass bei Native Queries das angegebene SQL-Statement 1:1 unverändert an die Datenbank geschickt wird – ohne Übersetzung von Entity-Namen oder Attributen.

## Erfolgskriterium

Der Test `test/net/rentacar/TestNativeQuery.java` läuft mit allen Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Risiken und Nachteile bringen Native Queries bezüglich Portabilität, Refactoring-Sicherheit und Typsicherheit mit sich?
2. Warum müssen bei Native Queries exakte relationale Spalten- und Tabellennamen verwendet werden, während JPQL auf dem Java-Domänenmodell arbeitet?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_10_JPQL_NativeQueries_Loesung`.
