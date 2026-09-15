# Übung: 2_05 Constructor Expressions (DTO Projection)

## Lernziel

Abfrageergebnisse nicht als gemanagte Entities, sondern direkt als unmanaged Data Transfer Objects (DTOs) über JPA Constructor Expressions (`SELECT new net.rentacar.dto.VehicleDTO(...)`) instanziieren und Read-Only-Datenströme performant abfragen.

## Ausgangszustand

In `src/net/rentacar/dto/VehicleDTO.java` existiert bereits eine DTO-Klasse mit passendem Konstruktor (`VehicleDTO(String modell, int maxKph)`). In `test/net/rentacar/TestQuery.java` ist die Testmethode `testQueryWithMappingInSelect()` noch leer.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Constructor Expression formulieren:**
   - Erstelle eine JPQL-Abfrage mit dem `new`-Operator:
     `SELECT new net.rentacar.dto.VehicleDTO(f.model.modell, f.maxKpH) FROM VehicleType f`
   - Verwende den vollqualifizierten Klassennamen des DTOs in der Query oder baue ihn via `VehicleDTO.class.getName()` dynamisch zusammen.

2. **DTO-Ergebnis prüfen:**
   - Führe die Query mit `TypedQuery<VehicleDTO>` aus.
   - Validiere die Attribute des ersten DTOs (`modell == "Golf"`, `maxKph == 200`).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_05_JPQL_MappingInQuery -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, dass die Datenbank nur die tatsächlich benötigten Spalten (`modell`, `maxKpH`) im `SELECT` abfragt, statt alle Entity-Spalten zu laden.
- Prüfe im Persistence Context: DTOs sind *unmanaged Objects* und verbrauchen keinen Snapshot-Speicher im First-Level-Cache.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
Das instanziierte DTO enthält die korrekten Werte für `modell` und `maxKph`.

## Reflexion

1. Welche Performance-Vorteile bieten DTO-Projections bei reinen Leseoperationen gegenüber dem Laden vollständiger Entity-Graphen?
2. Warum erfordert eine JPA Constructor Expression zwingend einen passenden, öffentlichen Konstruktor mit exakt übereinstimmenden Parametertypen in der DTO-Klasse?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_05_JPQL_MappingInQuery_Loesung`.
