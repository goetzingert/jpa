# Übung: 1_9_2 Vererbung mit Table per Concrete Class

## Lernziel

Eine Vererbungshierarchie mit `InheritanceType.TABLE_PER_CLASS` auf getrennte Tabellen pro konkreter Klasse abbilden. Die Auswirkungen auf polymorphe Abfragen (`UNION`), Primärschlüsselgenerierung und Schema-Design verstehen.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` fehlt die Vererbungsstrategie `TABLE_PER_CLASS`. Die konkreten Unterklassen `Truck` und `Car` besitzen noch keine eigenständigen Tabellendefinitionen. Der Test `test/net/rentacar/TestConnection.java` kann Unterklassen nicht getrennt persistieren.

## Aufgabe

Bearbeite `VehicleType.java`, `Truck.java` und `Car.java`:

1. **Basisklasse `VehicleType` konfigurieren:**
   - Annotiere die Basisklasse mit `@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`.
   - Setze `@Table(name = "tbl_VehicleType")`.

2. **Konkrete Unterklassen konfigurieren:**
   - Annotiere `Truck` mit `@Entity` und `@Table(name = "TBL_TRUCK")`.
   - Annotiere `Car` mit `@Entity` und `@Table(name = "TBL_CAR")`.
   - Beachte: Bei `TABLE_PER_CLASS` werden keine `@DiscriminatorColumn` / `@DiscriminatorValue`-Annotationen verwendet.

3. **Primärschlüssel-Hinweis:**
   - Da jede Tabelle eigenständig ist, müssen Primärschlüssel über alle Tabellen der Hierarchie hinweg eindeutig bleiben (z. B. über GUIDs/Strings oder gemeinsame `TABLE`/`SEQUENCE`-Generatoren).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_9_2_JPA_TablePerConcrete -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die erzeugten Tabellen `tbl_VehicleType`, `TBL_TRUCK` und `TBL_CAR`. Jede Tabelle enthält alle Attribute der Basisklasse plus die eigenen Attribute.
- Beobachte die polymorphe Abfrage beim Laden über `manager.find(VehicleType.class, id)` bzw. JPQL `SELECT v FROM VehicleType v`: Der JPA-Provider generiert ein `UNION`-Statement über alle beteiligten Tabellen.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 9 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum führt `InheritanceType.TABLE_PER_CLASS` bei polymorphen Abfragen über viele Unterklassen oft zu komplexen und teuren `UNION [ALL]` SQL-Abfragen?
2. Warum ist `GenerationType.IDENTITY` mit `TABLE_PER_CLASS` in der Regel nicht zulässig oder problematisch?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_9_2_JPA_TablePerConcrete_Loesung`.
