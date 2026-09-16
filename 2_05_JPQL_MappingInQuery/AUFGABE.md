# Übung: 2_05 Projektionen & Constructor Expressions (DTOs, Skalare & Tuples)

## Lernziel

Abfrageergebnisse nicht nur als gemanagte Entities laden, sondern performant als unmanaged Data Transfer Objects (DTOs) über JPA Constructor Expressions (`SELECT new ...`), als typsichere Skalar-Listen (`List<String>`), generische Zeilenarrays (`List<Object[]>`) oder über das JPA 2.0+ `Tuple`-Interface abfragen.

## Ausgangszustand

In `src/net/rentacar/dto/VehicleDTO.java` existiert eine DTO-Klasse mit passendem Konstruktor (`VehicleDTO(String modell, long maxKph)`). In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Constructor Expression (`testQueryWithMappingInSelect`):**
   - Erstelle eine JPQL-Abfrage mit dem `new`-Operator:
     `SELECT new net.rentacar.dto.VehicleDTO(f.model.modell, f.maxKpH) FROM VehicleType f ORDER BY f.model.modell`
   - Verwende `VehicleDTO.class.getName()` dynamisch im Query-String.
   - Validiere die Attribute des ersten DTOs.

2. **Skalar-Projektion (`testScalarProjectionReturnsSingleColumnList`):**
   - Frage gezielt nur die Marken als `TypedQuery<String>` ab:
     `SELECT DISTINCT f.model.brand FROM VehicleType f ORDER BY f.model.brand`
   - Prüfe die 3 Marken (`BMW`, `Mercedes`, `VW`).

3. **Mehrere Spalten als `Object[]` (`testMultipleScalarFieldsReturnObjectArrayList`):**
   - Frage Marke und PS ab: `SELECT f.model.brand, f.hp FROM VehicleType f ORDER BY f.hp DESC`
   - Werte die `List<Object[]>` aus.

4. **Typsichere Tuple-Projektion (`testTupleProjectionWithAlias`):**
   - Frage mit Aliasen ab: `SELECT f.model.brand AS brand, f.hp AS hp FROM VehicleType f ORDER BY f.hp DESC` (Ergebnisklasse: `Tuple.class`).
   - Greife über `tuple.get("brand", String.class)` und `tuple.get("hp", Integer.class)` zu.

5. **Gefilterte DTO-Projektion (`testDtoProjectionWithFilter`):**
   - Kombiniere die Constructor Expression mit einer `WHERE f.hp > :minHp`-Bedingung.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_05_JPQL_MappingInQuery -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, dass die Datenbank nur die tatsächlich benötigten Spalten im `SELECT` abfragt, statt alle Entity-Spalten zu laden.
- Prüfe im Persistence Context: DTOs und Tuples sind *unmanaged Objects* und verbrauchen keinen Snapshot-Speicher im First-Level-Cache.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Performance-Vorteile bieten DTO-Projections bei reinen Leseoperationen gegenüber dem Laden vollständiger Entity-Graphen?
2. Warum erfordert eine JPA Constructor Expression zwingend einen passenden, öffentlichen Konstruktor mit exakt übereinstimmenden Parametertypen in der DTO-Klasse?
3. Warum ist die `Tuple`-Projektion wartungsfreundlicher als der unstrukturierte Zugriff auf `Object[]` per numerischem Index?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_05_JPQL_MappingInQuery_Loesung`.
