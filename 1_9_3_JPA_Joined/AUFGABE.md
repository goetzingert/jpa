# Übung: 1_9_3 Vererbung mit Joined Strategy

## Lernziel

Eine vollständig normalisierte Vererbungshierarchie mit `InheritanceType.JOINED` abbilden. Das Zusammenspiel von Basistabelle und Unterklassentabellen (Fremdschlüssel-Beziehung auf den gemeinsamen Primärschlüssel) sowie die generierten SQL-Joins verstehen.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` fehlt die Vererbungsstrategie `JOINED`. Die Unterklassen `Truck` und `Car` sind noch nicht als Subtabellen mit Join-Primärschlüssel abgebildet. Der Test `test/net/rentacar/TestConnection.java` schlägt fehl.

## Aufgabe

Bearbeite `VehicleType.java`, `Truck.java` und `Car.java`:

1. **Basisklasse `VehicleType` konfigurieren:**
   - Annotiere die Basisklasse mit `@Inheritance(strategy = InheritanceType.JOINED)`.
   - Setze `@Table(name = "tbl_VehicleType")`.

2. **Unterklassen `Truck` und `Car` konfigurieren:**
   - Annotiere `Truck` mit `@Entity` und `@Table(name = "TBL_TRUCK")`.
   - Annotiere `Car` mit `@Entity` und `@Table(name = "TBL_CAR")`.
   - Optional: Mit `@PrimaryKeyJoinColumn` kann der Name der Fremdschlüssel-Primärschlüssel-Spalte in der Untertabelle explizit gesteuert werden (Standard: Name der `@Id`-Spalte der Basisklasse).

3. **Vererbung testen:**
   - Prüfe `testInheritance()` in `TestConnection.java`, wo ein `Truck` gespeichert und geladen wird.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_9_3_JPA_Joined -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die Tabellenstruktur: `tbl_VehicleType` enthält die gemeinsamen Spalten (`id`, `hp`, `maxKph`), während `TBL_TRUCK` nur `id` (als PK und FK) und `maxLoad` enthält.
- Beobachte beim Laden von `Truck` das generierte SQL: Hibernate führt einen `INNER JOIN` (oder `OUTER JOIN` bei polymorphen Abfragen über `VehicleType`) zwischen `tbl_VehicleType` und `TBL_TRUCK` aus.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 9 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Vorteile bietet `JOINED` gegenüber `SINGLE_TABLE` hinsichtlich Datenintegrität (`NOT NULL`-Constraints auf Subklassen-Spalten) und Normalisierung?
2. Welcher Performance-Trade-off entsteht bei tiefen Vererbungshierarchien durch mehrfache Tabellen-Joins?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_9_3_JPA_Joined_Loesung`.
