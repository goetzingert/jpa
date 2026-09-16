# Übung: 2_04 Erweiterte WHERE-Klauseln (IN, NOT IN, IS EMPTY, BETWEEN, MEMBER OF)

## Lernziel

Erweiterte Filterbedingungen in JPQL formulieren: Mengenprüfungen mit `IN` und `NOT IN`, Collection-Prüfungen mit `IS EMPTY`, Bereichsabfragen mit `BETWEEN`, Assoziationsprüfungen mit `MEMBER OF`, Skalarfunktionen wie `LENGTH()` und sichere `NULL`-Prüfungen mit `IS NULL` / `IS NOT NULL`.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig vorbereitet (`TODO`).

## Aufgabe

Bearbeite die Testmethoden in `test/net/rentacar/TestQuery.java`:

1. **`IN`-Operator mit Collection-Parameter (`testQueryForUserWithPersonnameISMichaelOrMathias`):**
   - Selektiere alle `User`, deren `person.firstName` in der Liste `["Michael", "Mathias"]` liegt (`WHERE n.person.firstName IN :FIRSTNAME`).

2. **Collection-Zustand mit `IS EMPTY` abfragen (`testQueryForShopMitKeinemVehicle`):**
   - Finde alle `Shop`-Instanzen, deren Fahrzeugpool leer ist (`WHERE f.carpool IS EMPTY`).

3. **Negation mit `NOT IN` anwenden (`testQueryForShopsNichtInStuttgartUndMuenchen_MitNOTINOperator`):**
   - Selektiere alle `Shop`-Instanzen, deren Standort nicht in `["Muenchen", "Stuttgart"]` liegt (`WHERE f.location NOT IN :orte`).

4. **Bereichsabfrage mit `BETWEEN` (`testQueryBetweenOperatorInclusiveBoundaries`):**
   - Filtere `VehicleType` mit PS-Zahlen zwischen 120 und 140 (`WHERE f.hp BETWEEN :minHp AND :maxHp`).
   - Beobachte, dass in SQL beide Grenzwerte inklusiv sind.

5. **Assoziations-Zugehörigkeit mit `MEMBER OF` (`testQueryMemberOfCollectionOperator`):**
   - Prüfe, welcher `Shop` das gegebene `Vehicle`-Objekt im Fuhrpark (`carpool`) hält (`WHERE :veh MEMBER OF s.carpool`).

6. **String-Skalarfunktionen in WHERE (`testQueryStringLengthFunctionInWhereClause`):**
   - Selektiere alle `User`, deren Nachname mehr als 7 Zeichen hat (`WHERE LENGTH(u.person.lastName) > 7`).

7. **Sichere NULL-Prüfungen (`testQueryIsNullAndIsNotNullOperators`):**
   - Selektiere Fahrzeuge mit gesetztem Standort (`WHERE v.location IS NOT NULL`) und ohne Standort (`WHERE v.location IS NULL`).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_04_JPQL_WhereClause -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie JPA `IS EMPTY` auf relationaler Ebene via `NOT EXISTS (SELECT 1 FROM tbl_Vehicle ... WHERE ...)` übersetzt.
- Sieh dir an, wie `MEMBER OF` zu einem Subquery/Existenz-Check gegen die Fremdschlüsselspalte expandiert wird.
- Prüfe, wie Parameterlisten bei `IN (:liste)` in dynamische SQL-Platzhalter `IN (?, ?)` aufgelöst werden.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 7 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum bietet JPQL spezielle Operatoren wie `IS EMPTY` / `IS NOT EMPTY` für Collections statt einfacher Vergleiche wie `size() = 0`?
2. Warum ist `MEMBER OF` in JPQL oft lesbarer und wartbarer als ein manueller `INNER JOIN` mit `WHERE`-Vergleich?
3. Wie verhält sich ein `IN`-Ausdruck, wenn die übergebene Java-Collection leer ist (Hibernate vs. JPA-Standard)?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_04_JPQL_WhereClause_Loesung`.
