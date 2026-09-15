# Übung: 1_2_4 ID-Strategie SEQUENCE

## Lernziel

Datenbank-Sequenzen mit `@SequenceGenerator` und `GenerationType.SEQUENCE` anbinden, `sequenceName` vs. Generator-Logik verstehen und das Zusammenspiel mit `allocationSize` analysieren.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` und `src/net/rentacar/model/User.java` fehlen die Primärschlüssel-Annotationen für Sequenzen. Der Test `test/net/rentacar/TestConnection.java` schlägt fehl.

## Aufgabe

Bearbeite `VehicleType.java` und `User.java`:

1. **SequenceGenerator für `VehicleType` definieren:**
   - Generator-Name: `Vehicle_SEQ`
   - DB-Sequenzname (`sequenceName`): `VehicleSeq`
   - `allocationSize`: `1`
   - ID-Annotation: `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Vehicle_SEQ")`

2. **SequenceGenerator für `User` definieren:**
   - Generator-Name: `User_SEQ`
   - DB-Sequenzname (`sequenceName`): `Vehicle_SEQ` (User nutzt dieselbe Sequenz)
   - ID-Annotation: `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ")`

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_2_4_JPA_ID_SEQ -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Beobachte, wann der Provider die Sequenzwerte abfragt (`VALUES NEXT VALUE FOR ...` in Derby).
- Prüfe, ob die Sequenzabfrage bereits beim `persist()` (vor dem `flush()`) stattfindet.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testFind`) speichert und findet beide Entitäten über die Sequenz-IDs:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum ermöglicht `GenerationType.SEQUENCE` dem JPA-Provider (im Gegensatz zu `GenerationType.IDENTITY`), die ID bereits vor dem tatsächlichen `INSERT` im Speicher zuzuweisen (wichtig für JDBC-Batching)?
2. Welche Datenbanken unterstützen native Sequenzen (z. B. PostgreSQL, Oracle, Derby) und welche nicht (z. B. MySQL bis Version 8)?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_2_4_JPA_ID_SEQ_Loesung`.
