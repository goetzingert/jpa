# Übung: 2_06 Explizite Joins (INNER JOIN, LEFT JOIN, ON-Klausel & Theta-Joins)

## Lernziel

Explizite Assoziations-Joins in JPQL formulieren (`INNER JOIN`, `LEFT JOIN`), den Unterschied zwischen `ON`- und `WHERE`-Klauseln bei Outer Joins verstehen, mehrstufige Assoziationsketten verknüpfen und unverbundene Entities per Theta-Join abfragen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **`INNER JOIN` über Assoziationspfad (`testInnerJoin_SearchShopWithVehicleOfBrandVW`):**
   - Selektiere alle `Shop`-Instanzen mit einem Fahrzeug der Marke `'VW'`:
     `SELECT f FROM Shop f INNER JOIN f.carpool fz WHERE fz.type.model.brand = 'VW'`

2. **`IN()`-Syntax als Join-Alternative (`testIn_SearchShopWithVehicleOfBrandVW`):**
   - Erreiche dasselbe Ergebnis mit der alternativen JPQL-Syntax:
     `SELECT f FROM Shop f, IN (f.carpool) fz WHERE fz.type.model.brand = 'VW'`

3. **`LEFT JOIN` für unvollständige Relationen (`leftJoinAlsoReturnsShopsWithoutVehicles`):**
   - Selektiere mit `SELECT DISTINCT f FROM Shop f LEFT JOIN f.carpool fz` alle Shops, auch diejenigen ohne zugeordnete Fahrzeuge (Ergebnis: 3 Shops).

4. **JPA 2.1 `ON`-Klausel vs. `WHERE`-Klausel (`testLeftJoinWithOnClauseVsWhereClause`):**
   - Zeige den Unterschied:
     - `LEFT JOIN s.carpool v ON v.type.model.brand = 'BMW'` schränkt nur den Join ein (3 Shops bleiben erhalten).
     - `LEFT JOIN s.carpool v WHERE v.type.model.brand = 'BMW'` filtert NULL-Werte aus und verhält sich wie ein `INNER JOIN` (0 Treffer).

5. **Mehrstufiger Join (`testMultipleJoinsChainAcrossAssociations`):**
   - Verknüpfe `Shop -> Vehicle -> VehicleType`: `SELECT DISTINCT s FROM Shop s INNER JOIN s.carpool v INNER JOIN v.type t WHERE t.hp >= 120`.

6. **Theta-Join (`testThetaJoinWithoutDirectAssociation`):**
   - Verknüpfe zwei unverbundene Entities `Shop` und `User` über `FROM Shop s, User u WHERE s.location LIKE 'M%' AND u.person.lastName LIKE 'M%'`.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_06_JPQL_InnerJoins -am test
```

**Beobachtung im SQL-Log:**
- Vergleiche das erzeugte SQL eines `INNER JOIN` (schließt Shops ohne Fahrzeuge aus) mit dem eines `LEFT OUTER JOIN`.
- Beobachte, warum bei Joins über Collections im JPQL oft `SELECT DISTINCT` erforderlich ist, um duplizierte Eltern-Entities im Abfrageergebnis zu verhindern.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 6 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Wann sollte ein `LEFT JOIN` statt eines impliziten Joins (wie `s.carpool`) verwendet werden?
2. Warum macht eine `WHERE`-Bedingung auf eine `LEFT JOIN`-Tabelle den Outer Join zunichte, und wie löst die JPA 2.1 `ON`-Klausel dieses Problem?
3. Warum führt ein Join über eine 1:N-Collection ohne `DISTINCT` bei mehreren Kind-Elementen zu mehrfach referenzierten Eltern-Objekten in der Ergebnisliste?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_06_JPQL_InnerJoins_Loesung`.
