# Übung: 1_3 Tabellen- und Spaltennamen überschreiben

## Lernziel

Das relationale Datenbank-Mapping (Tabellen- und Spaltennamen, Längenbeschränkungen, Nullability, Precision) unabhängig von Java-Klassen- und Feldnamen über JPA-Annotationen (`@Table`, `@Column`) oder XML steuern.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` fehlen die expliziten Mapping-Annotationen für `@Table` und `@Column`. Der Test `test/net/rentacar/TestConnection.java` kann `VehicleType` nicht ordnungsgemäß gegen das angepasste relationale Schema testen.

## Aufgabe

Bearbeite `src/net/rentacar/model/VehicleType.java`:

1. **Tabellennamen überschreiben:**
   Annotiere die Klasse mit `@Table(name = "tbl_VehicleType")`.

2. **Spalten-Mapping konfigurieren:**
   - `id`: Markiere das Feld mit `@Id`.
   - `brand`: `@Column(length = 30, nullable = false)`
   - `modell`: `@Column(length = 50, nullable = false)`
   - `hp`: `@Column(precision = 4)`
   - `maxKpH`: `@Column(precision = 3)`

3. **Testdaten in `TestConnection.java` prüfen:**
   Führe den Test `testFindVehicle()` aus und verifiziere das Laden der Entity.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_3_JPA_TableAndColumnOverride -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Beobachte im DDL-Log den erzeugten `CREATE TABLE tbl_VehicleType`-Befehl mit den definierten VARCHAR-Längen und NOT-NULL-Constraints.
- Vergleiche die abweichenden Java-Klassennamen mit den physischen Datenbank-Bezeichnern.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testFindVehicle`) läuft ohne Fehler durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Wann ist ein explizites Überschreiben von Tabellen- und Spaltennamen gegenüber den Default-Mapping-Konventionen zwingend erforderlich (z. B. Legacy-Datenbanken, DB-Namenskonventionen)?
2. Welche Mapping-Information hat Vorrang, wenn dieselbe Entity sowohl über Java-Annotationen als auch in einer `orm.xml` definiert ist?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_3_JPA_TableAndColumnOverride_Loesung`.
