# Übung: 1_3_1 JPA AttributeConverter (@Converter)

## Lernziel

Verstehen, wie mit JPA 2.1+ `@Converter` und `AttributeConverter<X, Y>` eigene Typkonvertierungen transparent zwischen Java-Modell und Datenbankspalte geschaltet werden. Vermeidung der bekannten Fallstricke von `@Enumerated(ORDINAL)` (Zerbrechen bei Umsortierung) und `@Enumerated(STRING)` (redundanter Speicherplatz).

## Ausgangszustand

In `FuelTypeConverter.java` sind die Methoden noch unvollständig. In `TestAttributeConverter.java` schlägt der Test fehl, weil die Konvertierung `null` liefert.

## Aufgabe

1. **`FuelTypeConverter` implementieren:**
   - Annotiere die Klasse mit `@Converter(autoApply = true)`.
   - Implementiere `convertToDatabaseColumn`: Wandelt `FuelType` in den kompakten `dbCode` ("P", "D", "E", "H").
   - Implementiere `convertToEntityAttribute`: Wandelt den DB-String via `FuelType.fromDbCode(...)` zurück in das Enum.

2. **Entity `ConvertedVehicle` prüfen:**
   - Durch `autoApply = true` wird der Converter automatisch für alle Attribute vom Typ `FuelType` aktiv.

3. **Test ausführen und DB-Zustand verifizieren:**
   - Der Test prüft sowohl die geladene Entity als auch den physischen Spaltenwert in Derby per Native Query.

## Test und Beobachtung

```bash
./mvnw -pl 1_3_1_JPA_AttributeConverter -am test
```

## Erfolgskriterium

Der Test läuft fehlerfrei durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum ist `AttributeConverter` für Enums und Value Objects (z. B. Geldbeträge, Postleitzahlen) robuster als Standard-Mappings?
2. Kann ein `AttributeConverter` auch für Abfragen mit JPQL in der `WHERE`-Klausel verwendet werden?

## Lösungshinweis

Vergleiche deine Lösung mit `1_3_1_JPA_AttributeConverter_Loesung`.
