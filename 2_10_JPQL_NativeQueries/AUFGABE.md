# Übung: 2_10 Native SQL-Queries (Entity-Mapping, Joins, Aggregationen & DML)

## Lernziel

Native SQL-Abfragen mit `EntityManager.createNativeQuery()` ausführen, wenn datenbankspezifische Funktionen oder komplexe relationale Tabellenstrukturen benötigt werden, relationale Ergebnismengen direkt auf Entity-Klassen (`Customer.class`) mappen, skalare Aggregatfunktionen abfragen, Paging auf Native Queries anwenden und native DML-Massenoperationen (`UPDATE`) unter Synchronisation des Persistence Contexts (`clear()`) durchführen.

## Ausgangszustand

In `test/net/rentacar/TestNativeQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestNativeQuery.java`:

1. **Native SQL für Entity-Ergebnisse (`testNativeQueryOfKunde`):**
   - Formuliere ein natives SQL-Statement: `SELECT * FROM tbl_User WHERE DTYPE = 'Customer' ORDER BY id`.
   - Mappe das Ergebnis direkt auf die Entity-Klasse `Customer.class`.
   - Prüfe die 7 geladenen Kunden inklusive ihrer `@OneToOne`-Assoziation zu `Person`.

2. **Komplexe relationale Tabellen-Joins (`testNativeQueryOfVehicleUndItem`):**
   - Führe ein natives SQL-Statement über Vererbungs- und Assoziationstabellen aus (`tbl_VehicleType`, `TBL_Car`, `TBL_TRUCK`, `tbl_Vehicle`).
   - Verarbeite die zurückgegebene `List<?>` und prüfe die Zeilenanzahl.

3. **Positions-Parameter in Native Queries (`nativeQuerySupportsBoundParameters`):**
   - Binde Positions-Parameter mit `?1` (`SELECT COUNT(*) FROM tbl_VehicleType WHERE HP > ?1`) und setze den Parameterwert `130`.

4. **Skalare Aggregatfunktionen (`testNativeQueryScalarAggregates`):**
   - Berechne `COUNT(*)`, `AVG(HP)`, `MAX(maxKph)`, `MIN(HP)` über `tbl_VehicleType` mit Native SQL und werte das `Object[]` aus.

5. **Paging mit Native Queries (`testNativeQueryPagination`):**
   - Wende `setFirstResult(2)` und `setMaxResults(3)` auf ein natives SQL-Statement an.

6. **Native DML-Updates & Persistence Context (`testNativeQueryDmlUpdateAndClearSync`):**
   - Führe ein natives `UPDATE tbl_VehicleType SET HP = HP + 10 WHERE brand = 'BMW'` mit `executeUpdate()` aus.
   - Synchronisiere den Persistence Context via `manager.clear()`, damit nachfolgende JPQL-Queries den aktualisierten Wert (`hp == 160`) aus der Datenbank laden.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_10_JPQL_NativeQueries -am test
```

**Beobachtung im SQL-Log:**
- Beachte, dass bei Native Queries das angegebene SQL-Statement 1:1 unverändert an die Datenbank geschickt wird – ohne Übersetzung von Entity-Namen oder Attributen.
- Beachte auch: Native DML-Operationen (`UPDATE`/`DELETE`) umgehen den Persistence Context – daher ist `manager.clear()` zwingend erforderlich.

## Erfolgskriterium

Der Test `test/net/rentacar/TestNativeQuery.java` läuft mit allen Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Risiken und Nachteile bringen Native Queries bezüglich Portabilität, Refactoring-Sicherheit und Typsicherheit mit sich?
2. Warum müssen bei Native Queries exakte relationale Spalten- und Tabellennamen verwendet werden, während JPQL auf dem Java-Domänenmodell arbeitet?
3. Warum führt ein natives `UPDATE` via `createNativeQuery().executeUpdate()` zu veralteten Werten (Stale Data) im Persistence Context, wenn `manager.clear()` vergessen wird?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_10_JPQL_NativeQueries_Loesung`.
