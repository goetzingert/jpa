# Übung: 2_04 Erweiterte WHERE-Klauseln (IN, NOT IN, IS EMPTY)

## Lernziel

Erweiterte Filterbedingungen in JPQL formulieren: Mengenprüfungen mit `IN` und `NOT IN`, Collection-Prüfungen mit `IS EMPTY` sowie die Navigation über eingebettete Attribute (`n.person.firstName`).

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die drei Testmethoden `testQueryForNutzerWithPersonnameISMichaelOrMathias()`, `testQueryForShopMitKeinemVehicle()` und `testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator()` noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **`IN`-Operator mit Collection-Parameter:**
   - In `testQueryForNutzerWithPersonnameISMichaelOrMathias()`: Selektiere alle `User`, deren `person.firstName` in der übergebenen Liste `["Michael", "Mathias"]` liegt (`WHERE n.person.firstName IN :FIRSTNAME`).

2. **Collection-Zustand mit `IS EMPTY` abfragen:**
   - In `testQueryForShopMitKeinemVehicle()`: Finde alle `Shop`-Instanzen, deren Fahrzeugpool leer ist (`WHERE f.carpool IS EMPTY`).

3. **Negation mit `NOT IN` anwenden:**
   - In `testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator()`: Selektiere alle `Shop`-Instanzen, deren Standort nicht in `["Muenchen", "Stuttgart"]` liegt (`WHERE f.location NOT IN :orte`).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_04_JPQL_WhereClause -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie JPA `IS EMPTY` auf relationaler Ebene (z. B. via `NOT EXISTS (SELECT 1 FROM tbl_Vehicle ... WHERE ...)`) übersetzt.
- Prüfe, wie Parameterlisten bei `IN (:liste)` in dynamische SQL-Platzhalter `IN (?, ?)` aufgelöst werden.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 3 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum bietet JPQL spezielle Operatoren wie `IS EMPTY` / `IS NOT EMPTY` für Collections statt einfacher Vergleiche wie `size() = 0`?
2. Wie verhält sich ein `IN`-Ausdruck, wenn die übergebene Java-Collection leer ist?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_04_JPQL_WhereClause_Loesung`.
