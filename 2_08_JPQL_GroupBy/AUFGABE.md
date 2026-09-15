# Übung: 2_08 JPQL Group By, Having und Aggregation

## Lernziel

Aggregatfunktionen (`COUNT`, `SUM`, `AVG`, `MIN`, `MAX`) in JPQL anwenden, Abfrageergebnisse mit `GROUP BY` gruppieren, Gruppenfilterungen mit `HAVING` durchführen und polymorphe Objektarrays (`Object[]`) auswerten.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden `testGroupBy()` und `testGroupByAndHaving()` noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **`GROUP BY` mit Aggregatfunktion:**
   - In `testGroupBy()`: Zähle die Anzahl der Reservierungen pro Fahrzeugmodell:
     `SELECT res.vehicle.type.model.modell, count(res) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell`
   - Werte die Ergebnisliste `List<Object[]>` aus.

2. **Gruppenfilterung mit `HAVING`:**
   - In `testGroupByAndHaving()`: Filtere nur diejenigen Fahrzeugmodelle heraus, die mehr als 2 Reservierungen aufweisen:
     `SELECT res.vehicle.type.model.modell, count(res) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell HAVING count(res) > 2`
   - Validiere, dass genau 1 Modell (`"323"`) dieses Kriterium erfüllt.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_08_JPQL_GroupBy -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie der JPQL-Query-Parser die Aggregat- und Gruppenfunktionen direkt in die entsprechenden SQL-`GROUP BY` und `HAVING`-Klauseln übersetzt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 2 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Was ist der Unterschied zwischen der Filterung in der `WHERE`-Klausel (Filterung einzelner Zeilen vor Aggregation) und der `HAVING`-Klausel (Filterung aggregierter Gruppen)?
2. Warum liefert eine JPQL-Abfrage mit mehreren selektierten Ausdrücken (`SELECT a, b, count(c)`) eine `List<Object[]>` statt einer Liste von Entity-Objekten?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_08_JPQL_GroupBy_Loesung`.
