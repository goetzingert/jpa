# Übung: 1_2_3 ID-Strategie TABLE

## Lernziel

Tabellen-basierte ID-Generierung mit `@TableGenerator` und `GenerationType.TABLE` konfigurieren, gemeinsame Generatortabellen strukturieren und den High/Low-Mechanismus (`allocationSize`) verstehen.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` und `src/net/rentacar/model/User.java` fehlen die Primärschlüssel-Annotationen und Generatordefinitionen. Der Test `test/net/rentacar/TestConnection.java` kann beide Entitäten nicht speichern.

## Aufgabe

Bearbeite `VehicleType.java` und `User.java`:

1. **TableGenerator für `VehicleType` definieren:**
   - Name: `MY_GEN1`
   - Tabellenname: `tbl_MyGen`
   - Primärschlüssel-Spaltenname: `PK_COLUMN`
   - Wert-Spaltenname: `VALUE_COLUMN`
   - Segmentwert (`pkColumnValue`): `"Vehicle_ID"`
   - `allocationSize`: `10`
   - ID-Annotation: `@GeneratedValue(strategy = GenerationType.TABLE, generator = "MY_GEN1")`

2. **TableGenerator für `User` definieren:**
   - Name: `MY_GEN`
   - dieselbe Generatortabelle: `tbl_MyGen`
   - identische Spaltennamen: `PK_COLUMN` und `VALUE_COLUMN`
   - eigener Segmentwert (`pkColumnValue`): `"USER_ID"`
   - `allocationSize`: `10`
   - ID-Annotation: `@GeneratedValue(strategy = GenerationType.TABLE, generator = "MY_GEN")`

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_2_3_JPA_ID_TABLE -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Beobachte die SQL-Statements zur Abfrage und Aktualisierung der Tabelle `tbl_MyGen` (`SELECT ... FOR UPDATE` bzw. `UPDATE tbl_MyGen SET VALUE_COLUMN = ...`).
- Prüfe in der Derby-Datenbank, wie viele Zeilen in `tbl_MyGen` entstehen (eine Zeile pro Entitätstyp/Segment).

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testFind`) speichert und lädt beide Entitäten erfolgreich:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Vor- und Nachteile hat `GenerationType.TABLE` gegenüber datenbankspezifischen Sequenzen (`GenerationType.SEQUENCE`) im Hinblick auf Performance und Portabilität?
2. Wie verhindert die `allocationSize` häufige Tabellensperren und Roundtrips zur Datenbank?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_2_3_JPA_ID_TABLE_Loesung`.
