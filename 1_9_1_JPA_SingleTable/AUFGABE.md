# Übung: 1_9_1 Vererbung mit Single Table

## Lernziel

Eine objektorientierte Vererbungshierarchie mit `InheritanceType.SINGLE_TABLE` in einer einzigen Datenbanktabelle abbilden. Das Zusammenspiel von `@Inheritance`, `@DiscriminatorColumn` und `@DiscriminatorValue` verstehen sowie polymorphe Abfragen über die Basisklasse durchführen.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` fehlen die Vererbungs- und Diskriminator-Annotationen. Die Unterklasse `Truck` ist zwar mit `@Entity` annotiert, wird aber ohne expliziten Diskriminatorwert gemappt. Der Test `test/net/rentacar/TestConnection.java` schlägt beim Testen der Vererbung (`testInheritance`) fehl.

## Aufgabe

Bearbeite `VehicleType.java`, `Truck.java` und `Car.java`:

1. **Basisklasse `VehicleType` konfigurieren:**
   - Annotiere die Basisklasse mit `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`.
   - Definiere eine Diskriminatorspalte: `@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)`.
   - Setze den Basis-Diskriminatorwert: `@DiscriminatorValue("Vehicle")`.
   - Lege den Tabellennamen fest: `@Table(name = "tbl_Vehicle_Hierarchy")`.

2. **Unterklassen `Truck` und `Car` annotieren:**
   - Annotiere `Truck` mit `@DiscriminatorValue("Truck")`.
   - Annotiere `Car` mit `@DiscriminatorValue("Car")`.

3. **Testfälle prüfen:**
   - In `test/net/rentacar/TestConnection.java` wird ein `Truck` persistiert und sowohl über `VehicleType.class` als auch über `Truck.class` mittels `manager.find(...)` geladen.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_9_1_JPA_SingleTable -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die Tabelle `tbl_Vehicle_Hierarchy`: Alle Attribute der Basisklasse sowie aller Unterklassen (`maxLoad`, etc.) befinden sich in dieser einen Tabelle.
- Beobachte die Diskriminatorspalte `type` und wie Hibernate das `SELECT`-Statement filtert, wenn gezielt nach `Truck.class` gesucht wird.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 9 Testmethoden (insb. `testInheritance`) erfolgreich durch:
```text
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Performance-Vorteile bietet `SINGLE_TABLE` gegenüber normalisierten Strategien (z. B. Vermeidung von SQL-Joins)?
2. Warum müssen alle Spalten von Unterklassen bei `SINGLE_TABLE` in der Datenbank zwingend `NULL`-fähig (*nullable*) sein?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_9_1_JPA_SingleTable_Loesung`.
