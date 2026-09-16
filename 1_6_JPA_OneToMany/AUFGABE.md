# Übung: 1_6 One-to-Many-Beziehung

## Lernziel

Eine 1:N-Beziehung (`@OneToMany`) zwischen zwei Entitäten (`Shop` und `Vehicle`) mit Collection-Mapping (`Set<Vehicle>`) abbilden, den Unterschied zwischen Join-Tabelle und Fremdschlüssel-Spalte (`@JoinColumn`) verstehen, Collections manipulieren und das Löschverhalten ohne Kaskadierung nachvollziehen.

## Ausgangszustand

In `src/net/rentacar/model/Shop.java` ist die Collection `vehicles` noch mit `@Transient` markiert. Der Test `test/net/rentacar/TestConnection.java` schlägt bei `testOneToManyOfShop()` fehl, da Fahrzeuge nicht persistent mit dem Shop verknüpft werden.

## Aufgabe

Bearbeite `src/net/rentacar/model/Shop.java` und `test/net/rentacar/TestConnection.java`:

1. **1:N-Collection annotieren (`Shop.java`):**
   - Entferne `@Transient` von `private Set<Vehicle> vehicles`.
   - Annotiere das Feld mit `@OneToMany`.
   - Ergänze `@JoinColumn(name = "shop_id")`, um eine direkte Fremdschlüsselspalte in der Zieltabelle `tbl_Vehicle` zu verwenden (statt einer Standard-Join-Tabelle).

2. **Testmethoden implementieren (`TestConnection.java`):**
   - `testFindShop()`: Lade den Shop und prüfe den Standort.
   - `testOneToManyOfShop()`: Prüfe, dass die Collection geladen wird und 1 Fahrzeug enthält.
   - `testAddingVehicleToShopCollection()`: Füge ein 2. Fahrzeug zur Collection hinzu und prüfe die Speicherung nach `flush()`.
   - `testRemovingVehicleFromShopCollectionSetsForeignKeyToNull()`: Leere die Collection und prüfe, dass der Fremdschlüssel gelöst wird, das Fahrzeug selbst aber erhalten bleibt.
   - `testEmptyShopReturnsEmptyCollectionNotNull()`: Verifiziere, dass Shops ohne Fahrzeuge ein leeres Set (nicht `null`) liefern.
   - `testShopLocationUpdatePropagatesOnFlush()`: Prüfe Dirty Checking auf dem Shop-Standort.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_6_JPA_OneToMany -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche, welche Tabellenstruktur ohne `@JoinColumn` entstehen würde (eine separate Verbindungstabelle `tbl_Shop_tbl_Vehicle`) und wie `@JoinColumn(name = "shop_id")` dies vereinfacht.
- Beobachte die SQL-`UPDATE`-Statements, die Hibernate nach dem `INSERT` des Fahrzeugs ausführt, um den Fremdschlüssel `shop_id` zu setzen.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 6 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum erzeugt ein reines `@OneToMany` ohne `@JoinColumn` und ohne `mappedBy` standardmäßig eine Join-Tabelle?
2. Warum erfordert ein unidirektionales `@OneToMany` mit `@JoinColumn` beim Speichern ein zusätzliches `UPDATE`-Statement für den Fremdschlüssel?
3. Was ist der Unterschied zwischen dem Leeren einer `@OneToMany`-Collection mit und ohne `orphanRemoval = true`?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_6_JPA_OneToMany_Loesung`.
