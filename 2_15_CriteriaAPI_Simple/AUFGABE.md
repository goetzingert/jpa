# Übung: 2_15 Criteria API (Prädikate, Joins, Aggregationen & Dynamische Filter)

## Lernziel

Dynamische, typsichere und refactoringsichere Datenbankabfragen mit der JPA Criteria API (`CriteriaBuilder`, `CriteriaQuery`, `Root`, `Join`, `Predicate`) erstellen: Mengenoperationen (`in`), Assoziationsprüfungen (`isEmpty`), Negationen (`not`), deterministische Sortierung (`orderBy`), numerische Vergleiche (`and`, `ge`, `equal`), Entity-Joins (`join`), relationale Aggregationen (`multiselect`) und flexible, dynamisch zusammengesetzte Suchfilter (`List<Predicate>`).

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **CriteriaQuery mit `in()`-Prädikat (`testQueryForUserWithPersonnameISMichaelOrMathias`):**
   - Filter auf `user.get("person").get("firstName").in(list)`.

2. **Collection-Prüfung mit `cb.isEmpty()` (`testQueryForShopMitKeinemVehicle`):**
   - Finde Shops mit leerem Fahrzeugpool mittels `cq.where(cb.isEmpty(shop.get("carpool")))`.

3. **Negiertes Prädikat mit `cb.not()` (`testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator`):**
   - Finde Shops, deren Standort nicht in `["Muenchen", "Stuttgart"]` liegt (`cq.where(cb.not(shop.get("location").in(orte)))`).

4. **Sortierung mit `cb.asc()` (`criteriaQueryCanApplyStableOrdering`):**
   - Sortiere die Benutzer alphabetisch nach Vornamen (`cq.orderBy(cb.asc(user.get("person").get("firstName")))`).

5. **Numerische Vergleiche und Konjunktion (`testCriteriaNumericComparisonAndConjunction`):**
   - Kombiniere `cb.equal(root.get("model").get("brand"), "VW")` und `cb.ge(root.get("hp"), 120L)` über `cb.and(...)`.

6. **Explizite Entity-Joins (`testCriteriaJoinAcrossEntities`):**
   - Joine `Vehicle` auf `VehicleType` mit `Join<Vehicle, VehicleType> typeJoin = vehicleRoot.join("type")` und filtere auf `brand == "VW"`.

7. **Skalare Aggregationen (`testCriteriaAggregateCalculations`):**
   - Berechne `cb.count(root)`, `cb.avg(root.get("hp"))` und `cb.max(root.get("maxKpH"))` via `cq.multiselect(...)`.

8. **Dynamischer Query Builder mit Predicate-Liste (`testCriteriaDynamicQueryWithPredicateList`):**
   - Baue eine `List<Predicate>` auf, die optionale Filter (z. B. `filterBrand` und `minHp`) dynamisch hinzufügt und per `cq.where(predicates.toArray(new Predicate[0]))` übergibt.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_15_CriteriaAPI_Simple -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, dass die Criteria API exakt denselben optimierten SQL-Code erzeugt wie die entsprechenden JPQL-Abfragen.
- Beachte das Pattern der dynamischen Predicate-Liste: Es vermeidet String-Konkatenationen und schützt vor SQL-Syntaxfehlern bei optionalen Suchkriterien in Web-Masken.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 8 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Vor- und Nachteile hat die Criteria API gegenüber JPQL bezüglich Typsicherheit, dynamischem Zusammenbau von Suchfiltern und Lesbarkeit?
2. Warum eignet sich das `List<Predicate>`-Muster ideal für variable Benutzeroberflächen-Suchmasken im Vergleich zu dynamischen JPQL-Stringverkettungen?
3. Wie hilft das JPA Static Metamodel (`User_.person`, etc.) dabei, auch Attributnamen zur Compilezeit vollständig abzusichern?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_15_CriteriaAPI_Simple_Loesung`.
