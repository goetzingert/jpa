# Übung: 2_15 Criteria API Typsichere Abfragen

## Lernziel

Dynamische, typsichere und refactoringsichere Datenbankabfragen mit der JPA Criteria API (`CriteriaBuilder`, `CriteriaQuery`, `Root`, `Predicate`) erstellen und mit JPQL-Äquivalenten vergleichen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden `testQueryForUserWithPersonnameISMichaelOrMathias()`, `testQueryForShopMitKeinemVehicle()`, `testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator()` und `criteriaQueryCanApplyStableOrdering()` noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **CriteriaQuery mit `in()`-Prädikat:**
   - In `testQueryForUserWithPersonnameISMichaelOrMathias()`:
     ```java
     CriteriaBuilder cb = manager.getCriteriaBuilder();
     CriteriaQuery<User> cq = cb.createQuery(User.class);
     Root<User> user = cq.from(User.class);
     cq.select(user).where(user.get("person").get("firstName").in(list));
     ```

2. **Collection-Prüfung mit `cb.isEmpty()`:**
   - In `testQueryForShopMitKeinemVehicle()`: Finde Shops mit leerem Fahrzeugpool mittels `cq.where(cb.isEmpty(shop.get("carpool")))`.

3. **Negiertes Prädikat mit `cb.not()`:**
   - In `testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator()`: Finde Shops, deren Standort nicht in `["Muenchen", "Stuttgart"]` liegt (`cq.where(cb.not(shop.get("location").in(orte)))`).

4. **Sortierung mit `cb.asc()`:**
   - In `criteriaQueryCanApplyStableOrdering()`: Sortiere die Beuser alphabetisch nach Vornamen (`cq.orderBy(cb.asc(user.get("person").get("firstName")))`).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_15_CriteriaAPI_Simple -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, dass die Criteria API exakt denselben optimierten SQL-Code erzeugt wie die entsprechenden JPQL-Abfragen.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft mit allen 4 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Vor- und Nachteile hat die Criteria API gegenüber JPQL bezüglich Typsicherheit, dynamischem Zusammenbau von Suchfiltern und Lesbarkeit?
2. Wie hilft das JPA Static Metamodel (`User_.person`, etc.) dabei, auch Attributnamen zur Compilezeit vollständig abzusichern?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_15_CriteriaAPI_Simple_Loesung`.
