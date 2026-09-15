# Übung: 2_09 JPQL Subqueries (Unterabfragen)

## Lernziel

Korrelierte und unkorrelierte Unterabfragen (*Subqueries*) in der `WHERE`- oder `HAVING`-Klausel von JPQL formulieren, um komplexe Aggregatvergleiche über verschachtelte Beziehungsstrukturen durchzuführen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` ist die Testmethode `testSubQuery()` noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Subquery mit Aggregatfunktion formulieren:**
   - Selektiere alle Kunden (`Customer`), deren Gesamtsumme der Reservierungspreise über 200 liegt:
     `SELECT k FROM Customer k WHERE 200 < (SELECT sum(res.price) FROM k.reservations res)`
   - Beachte die korrelierte Unterabfrage, die direkt auf die Collection `k.reservations` des äußeren Kunden zugreift.

2. **Ergebnis validieren:**
   - Führe die Query aus und prüfe, dass exakt 2 Kunden diesen Schwellenwert überschreiten.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_09_JPQL_SubQueries -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie der Provider die korrelierte JPQL-Subquery in ein relationales SQL mit `WHERE 200 < (SELECT SUM(...) FROM ... WHERE fk = customer.id)` übersetzt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. In welchen Teilen einer JPQL-Query sind Subqueries erlaubt (`WHERE`, `HAVING`) und wo waren sie in älteren JPA-Spezifikationen eingeschränkt (z. B. nicht in `FROM`)?
2. Warum sind korrelierte Subqueries bei sehr großen Datenmengen performancekritisch, und wie könnten sie alternativ mit Joins und `GROUP BY` umformuliert werden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_09_JPQL_SubQueries_Loesung`.
