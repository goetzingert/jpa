# Übung: 1_6 One-to-Many-Beziehung

## Lernziel

Eine 1:N-Beziehung (`@OneToMany`) zwischen zwei Entitäten mit Collection-Mapping (`Set<Vehicle>`) abbilden, den Unterschied zwischen Join-Tabelle und Fremdschlüssel-Spalte (`@JoinColumn`) verstehen und verknüpfte Entitäten über die Eltern-Entity persistieren und laden.

## Ausgangszustand

In `src/net/rentacar/model/Shop.java` ist die Collection `vehicles` noch mit `@Transient` markiert. Der Test `test/net/rentacar/TestConnection.java` schlägt bei `testOneToManyOfShop()` fehl, da Fahrzeuge nicht persistent mit dem Shop verknüpft werden.

## Aufgabe

Bearbeite `src/net/rentacar/model/Shop.java` und `test/net/rentacar/TestConnection.java`:

1. **1:N-Collection annotieren:**
   - Entferne `@Transient` von `private Set<Vehicle> vehicles`.
   - Annotiere das Feld mit `@OneToMany`.
   - Ergänze `@JoinColumn(name = "shop_id")`, um eine direkte Fremdschlüsselspalte in der Zieltabelle `tbl_Vehicle` zu verwenden (statt einer Standard-Join-Tabelle).

2. **Testdaten in `TestConnection.java` pflegen:**
   - Erzeuge in `setUp()` ein `Vehicle`, ordne es dem `Shop` zu und füge es der Collection `Shop.getVehicles().add(fz)` hinzu.
   - Persistiere beide Instanzen und prüfe die Datenhaltung.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_6_JPA_OneToMany -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche, welche Tabellenstruktur ohne `@JoinColumn` entstehen würde (eine separate Verbindungstabelle `tbl_Shop_tbl_Vehicle`) und wie `@JoinColumn(name = "shop_id")` dies vereinfacht.
- Beobachte die SQL-`UPDATE`-Statements, die Hibernate nach dem `INSERT` des Fahrzeugs ausführt, um den Fremdschlüssel `shop_id` zu setzen.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 6 Testmethoden (insb. `testOneToManyOfShop`) fehlerfrei durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum erzeugt ein reines `@OneToMany` ohne `@JoinColumn` und ohne `mappedBy` standardmäßig eine Join-Tabelle?
2. Warum erfordert ein unidirektionales `@OneToMany` mit `@JoinColumn` beim Speichern ein zusätzliches `UPDATE`-Statement für den Fremdschlüssel?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_6_JPA_OneToMany_Loesung`.
