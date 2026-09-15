# Übung: 2_06 Explizite Joins (INNER JOIN, LEFT JOIN, IN-Operator)

## Lernziel

Explizite Assoziations-Joins in JPQL formulieren (`INNER JOIN`, `LEFT JOIN`), den Unterschied zu impliziten Pfad-Joins verstehen und Outer Joins zur Erfassung unvollständiger Beziehungen einsetzen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden `testInnerJoin_SearchShopWithVehicleOfBrandVW()`, `testIn_SearchShopWithVehicleOfBrandVW()` und `leftJoinAlsoReturnsShopsWithoutVehicles()` noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **`INNER JOIN` über Assoziationspfad:**
   - In `testInnerJoin_SearchShopWithVehicleOfBrandVW()`: Selektiere alle `Shop`-Instanzen mit einem Fahrzeug der Marke `'VW'`:
     `SELECT f FROM Shop f INNER JOIN f.carpool fz WHERE fz.type.model.brand = 'VW'`

2. **`IN()`-Syntax als Join-Alternative:**
   - In `testIn_SearchShopWithVehicleOfBrandVW()`: Erreiche dasselbe Ergebnis mit der alternativen JPQL-Syntax:
     `SELECT f FROM Shop f, IN (f.carpool) fz WHERE fz.type.model.brand = 'VW'`

3. **`LEFT JOIN` für unvollständige Relationen:**
   - In `leftJoinAlsoReturnsShopsWithoutVehicles()`: Selektiere mit `SELECT DISTINCT f FROM Shop f LEFT JOIN f.carpool fz` alle Shops, auch diejenigen ohne zugeordnete Fahrzeuge (Ergebnis: 3 Shops).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_06_JPQL_InnerJoins -am test
```

**Beobachtung im SQL-Log:**
- Vergleiche das erzeugte SQL eines `INNER JOIN` (schließt Shops ohne Fahrzeuge aus) mit dem eines `LEFT OUTER JOIN` (liefert alle Shops, auch `Koeln`).
- Beobachte, warum bei Joins über Collections im JPQL oft `SELECT DISTINCT` erforderlich ist, um duplizierte Eltern-Entities im Abfrageergebnis zu verhindern.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 3 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Wann sollte ein `LEFT JOIN` statt eines impliziten Joins (wie `s.carpool`) verwendet werden?
2. Warum führt ein Join über eine 1:N-Collection ohne `DISTINCT` bei mehreren Kind-Elementen zu mehrfach referenzierten Eltern-Objekten in der Ergebnisliste?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_06_JPQL_InnerJoins_Loesung`.
