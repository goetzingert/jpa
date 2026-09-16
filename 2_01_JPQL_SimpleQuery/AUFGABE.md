# Übung: 2_01 Einfache JPQL-Abfrage

## Lernziel

Eine typisierte JPQL-Abfrage mit `EntityManager.createQuery()` und `TypedQuery<T>` formulieren, Entity-Attribute filtern, Vererbung und Polymorphie in JPQL verstehen, sowie das Verhalten und die Ausnahmen von `getSingleResult()` im Vergleich zu `getResultList()` beherrschen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig vorbereitet (`TODO`).

## Aufgabe

Bearbeite die Testmethoden in `test/net/rentacar/TestQuery.java`:

1. **Typisierte JPQL-Abfrage (`testQueryForVehicleTypeMoreThan130HP`):**
   - Erstelle eine `TypedQuery<VehicleType>` über `manager.createQuery(...)`.
   - JPQL-Statement: `SELECT f FROM VehicleType f WHERE f.hp > 130` (Ergebnisklasse: `VehicleType.class`).
   - Führe die Query mit `getResultList()` aus und prüfe, dass genau der BMW (150 PS) gefunden wird.

2. **Polymorphe Abfragen (`testPolymorphicQueryReturnsAllSubtypes`):**
   - JPQL `SELECT v FROM VehicleType v` ist automatisch polymorph und liefert sowohl `Car`- als auch `Truck`-Instanzen.
   - Sortiere nach `v.model.brand` und überprüfe die Typen per `assertInstanceOf`.

3. **Subtyp-Filterung mit `TYPE()` (`testFilterBySpecificSubtypeUsingTypeFunction`):**
   - Nutze die JPQL-Funktion `TYPE(v) = Car`, um gezielt nur PKWs abzufragen.

4. **Eindeutiges Einzelergebnis mit `getSingleResult()` (`testFindSingleVehicleTypeByExactBrand`):**
   - Frage gezielt nach dem Fahrzeug mit Marke `'BMW'` und hole die Entity direkt über `query.getSingleResult()`.

5. **Fehlerbehandlung: `NoResultException` (`testGetSingleResultThrowsNoResultExceptionWhenNotFound`):**
   - Was passiert, wenn eine Query mit `getSingleResult()` keinen Treffer findet (z. B. Marke `'Porsche'`)?
   - Verifiziere, dass JPA nicht `null` zurückgibt, sondern eine `jakarta.persistence.NoResultException` wirft.

6. **Fehlerbehandlung: `NonUniqueResultException` (`testGetSingleResultThrowsNonUniqueResultExceptionWhenMultipleMatches`):**
   - Was passiert bei `getSingleResult()`, wenn mehr als ein Treffer existiert (z. B. `TYPE(v) = Car`)?
   - Verifiziere, dass eine `jakarta.persistence.NonUniqueResultException` geworfen wird.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_01_JPQL_SimpleQuery -am test
```

**Beobachtung im SQL-Log:**
- Beobachte das generierte relationale `SELECT`-Statement.
- Beachte, wie JPA den JPQL-Klassennamen `VehicleType` auf den tatsächlichen Tabellennamen und `f.hp` auf die Spalte abbildet.
- Sieh dir an, wie `TYPE(v) = Car` im SQL auf die Discriminator-Spalte (`DTYPE`) übersetzt wird.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welchen Sicherheits- und Lesbarkeitsvorteil bietet `manager.createQuery(jpql, VehicleType.class)` (`TypedQuery`) gegenüber der klassischen `Query` ohne Typangabe?
2. Warum lautet die Klausel `FROM VehicleType f` und nicht `FROM tbl_VehicleType`?
3. Warum wirft `getSingleResult()` bei 0 Treffern eine `NoResultException` anstatt `null` zurückzugeben — und wie geht man in modernem Java (z. B. mit Streams / `getResultStream().findFirst()`) eleganter damit um?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_01_JPQL_SimpleQuery_Loesung`.
