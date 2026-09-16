# Übung: 2_09 JPQL Subqueries (Unterabfragen, EXISTS, ALL & Skalar-Subqueries)

## Lernziel

Korrelierte und unkorrelierte Unterabfragen (*Subqueries*) in der `WHERE`-Klausel und Projektion von JPQL formulieren, Quantoren (`EXISTS`, `NOT EXISTS`, `ALL`, `IN`) einsetzen und Skalar-Subqueries in `SELECT` zur aggregierten Anreicherung nutzen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Korrelierte Subquery mit Aggregatfunktion (`testSubQuery`):**
   - Selektiere alle Kunden (`Customer`), deren Gesamtsumme der Reservierungspreise über 200 GE liegt:
     `SELECT k FROM Customer k WHERE (SELECT SUM(res.price) FROM k.reservations res) > 200 ORDER BY k.person.lastName`

2. **Randfall unerreichbarer Schwellenwert (`returnsNoCustomerAboveAnUnreachableTotal`):**
   - Prüfe mit Schwellenwert > 1000, dass eine leere Liste zurückgegeben wird.

3. **`EXISTS`-Operator (`testSubqueryWithExistsOperator`):**
   - Finde Kunden mit mindestens einer Buchung >= 400 GE:
     `SELECT k FROM Customer k WHERE EXISTS (SELECT res FROM k.reservations res WHERE res.price >= 400)`

4. **`NOT EXISTS`-Operator (`testSubqueryWithNotExistsOperator`):**
   - Finde alle Kunden ohne Reservierungen:
     `SELECT k FROM Customer k WHERE NOT EXISTS (SELECT res FROM k.reservations res)`

5. **`IN`-Subquery (`testSubqueryWithInOperator`):**
   - Finde Fahrzeuge, deren Typ in der Menge der Typen mit > 130 PS liegt:
     `SELECT v FROM Vehicle v WHERE v.type IN (SELECT t FROM VehicleType t WHERE t.hp > 130)`

6. **`ALL`-Quantor (`testSubqueryWithAllOperator`):**
   - Finde den/die Fahrzeugtypen, deren PS-Zahl größer-gleich aller anderen Typen ist:
     `SELECT v FROM VehicleType v WHERE v.hp >= ALL (SELECT t.hp FROM VehicleType t)`

7. **Skalar-Subquery im `SELECT` (`testScalarSubqueryInSelectClause`):**
   - Zähle die Reservierungen pro Kunde direkt in der `SELECT`-Projektion:
     `SELECT k.person.lastName, (SELECT COUNT(res) FROM k.reservations res) FROM Customer k WHERE k.person.lastName = 'Mustermann'`

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_09_JPQL_SubQueries -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie der Provider korrelierte JPQL-Subqueries in relationale Subselects mit `EXISTS`, `NOT EXISTS` und `>= ALL(...)` übersetzt.
- Achte auf die SQL-Ausführung von Skalar-Subqueries im `SELECT`.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 7 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. In welchen Teilen einer JPQL-Query sind Subqueries erlaubt (`WHERE`, `HAVING`, `SELECT`) und wo waren sie in älteren JPA-Spezifikationen eingeschränkt (z. B. nicht in `FROM`)?
2. Warum ist `EXISTS (...)` oft performanter als `IN (SELECT ...)`, wenn die Unterabfrage auf NULL-Werte oder große Datenmengen trifft?
3. Warum sind korrelierte Subqueries bei sehr großen Datenmengen performancekritisch, und wie könnten sie alternativ mit Joins und `GROUP BY` umformuliert werden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_09_JPQL_SubQueries_Loesung`.
