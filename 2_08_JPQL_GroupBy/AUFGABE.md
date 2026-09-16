# Übung: 2_08 JPQL Group By, Having, Aggregation & DTO-Mapping

## Lernziel

Aggregatfunktionen (`COUNT`, `SUM`, `AVG`, `MIN`, `MAX`, `COUNT(DISTINCT ...)`) in JPQL anwenden, Abfrageergebnisse mit `GROUP BY` gruppieren, Gruppenfilterungen mit `HAVING` auf Zeilen- und Umsatzebene durchführen und aggregierte Kennzahlen direkt in typsichere DTOs mappen.

## Ausgangszustand

In `src/net/rentacar/dto/ReservationStatsDTO.java` existiert eine DTO-Klasse für Aggregatkennzahlen. In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **`GROUP BY` mit `COUNT` (`testGroupBy`):**
   - Zähle die Anzahl der Reservierungen pro Fahrzeugmodell:
     `SELECT res.vehicle.type.model.modell, COUNT(res) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell ORDER BY res.vehicle.type.model.modell`

2. **Gruppenfilterung mit `HAVING` (`testGroupByAndHaving`):**
   - Filtere nur diejenigen Fahrzeugmodelle heraus, die mehr als 2 Reservierungen aufweisen (`HAVING COUNT(res) > 2`).

3. **Mehrere Aggregatfunktionen gleichzeitig (`testMultipleAggregatesSumMinMaxAvg`):**
   - Ermittle `COUNT(res)`, `SUM(res.price)`, `AVG(res.price)`, `MIN(res.price)` und `MAX(res.price)` in einer einzigen Query gruppiert nach Fahrzeugmodell.

4. **Kardinalitäten mit `COUNT(DISTINCT ...)` (`testCountDistinctModels`):**
   - Zähle die Anzahl eindeutig reservierter Modelle:
     `SELECT COUNT(DISTINCT res.vehicle.type.model.modell) FROM Customer k, IN (k.reservations) res`

5. **DTO-Mapping mit Constructor Expression (`testGroupByWithDtoProjection`):**
   - Mappe `res.vehicle.type.model.modell`, `COUNT(res)` und `SUM(res.price)` direkt in `ReservationStatsDTO`:
     `SELECT new net.rentacar.dto.ReservationStatsDTO(...) ... GROUP BY ...`

6. **`HAVING` mit Umsatzkriterium (`testHavingWithSumPriceCondition`):**
   - Finde Kunden, deren Gesamtumsatz 500 GE überschreitet (`GROUP BY k.person.lastName HAVING SUM(res.price) > 500`).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_08_JPQL_GroupBy -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie der JPQL-Query-Parser die Aggregat- und Gruppenfunktionen direkt in relationale SQL-`GROUP BY`- und `HAVING`-Klauseln übersetzt.
- Prüfe, wie `SUM` und `AVG` in der Datenbank ausgeführt werden und welche Java-Typen (`Double`, `Long`, `Float`) JPA standardmäßig zurückliefert.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 6 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Was ist der Unterschied zwischen der Filterung in der `WHERE`-Klausel (Filterung einzelner Zeilen vor Aggregation) und der `HAVING`-Klausel (Filterung aggregierter Gruppen)?
2. Warum liefert `COUNT(...)` in JPA immer ein `Long` und `AVG(...)` immer ein `Double`, unabhängig vom Typ des Tabellenattributs?
3. Welche Vorteile bietet das direkte Mappen aggregierter Ergebnisse in DTOs (`ReservationStatsDTO`) gegenüber `List<Object[]>`?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_08_JPQL_GroupBy_Loesung`.
