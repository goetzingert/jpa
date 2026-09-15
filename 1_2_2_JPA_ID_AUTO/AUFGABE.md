# Übung: 1_2_2 ID-Strategie AUTO

## Lernziel

Verstehen, wie `GenerationType.AUTO` arbeitet und nach welchen Kriterien der JPA-Provider die konkrete Strategie (Sequence, Identity oder Table) in Abhängigkeit von der Datenbank auswählt.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` ist die Klasse bereits mit `@Entity` annotiert, jedoch fehlt das Primärschlüssel-Mapping auf dem Feld `id`. Der Test `test/net/rentacar/TestConnection.java` kann ohne ID-Mapping nicht ausgeführt werden.

## Aufgabe

Bearbeite `src/net/rentacar/model/VehicleType.java`:

1. **Primärschlüssel-Annotation:**
   Annotiere das Feld `id` mit `@Id`.

2. **Automatische ID-Generierung:**
   Ergänze `@GeneratedValue(strategy = GenerationType.AUTO)` auf dem Feld `id`.

3. **Getter/Setter überprüfen:**
   Stelle sicher, dass `getId()` den generierten Wert zurückgibt.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_2_2_JPA_ID_AUTO -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Beobachte, ob Hibernate für Derby eine Datenbanksequenz (`hibernate_sequence` / Default-Sequenz) oder eine Hilfstabelle anlegt.
- Teste dasselbe Verhalten mit EclipseLink (`./mvnw -Peclipselink -pl 1_2_2_JPA_ID_AUTO -am test`) und vergleiche, welche Strategie EclipseLink bei `AUTO` wählt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testFind`) findet die Entity über die generierte ID:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum ist `GenerationType.AUTO` zwar bequem für schnelle Prototypen, aber in Multi-Provider- oder Multi-Database-Projekten oft unberechenbar?
2. In welchen Fällen sollte man stattdessen explizit `SEQUENCE` oder `IDENTITY` konfigurieren?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_2_2_JPA_ID_AUTO_Loesung`.
