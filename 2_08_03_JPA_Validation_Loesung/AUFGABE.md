# Übung: 2_08_03 Bean Validation mit JPA

## Lernziel

Deklarative Validierungsregeln aus Jakarta/Bean Validation (`@NotBlank`, `@Min`, `@NotNull`) auf JPA-Entities anwenden, automatische Pre-Persist/Pre-Update-Validierungen verstehen und ungültige Zustände (`PersistenceException` / `ConstraintViolationException`) absichern.

## Ausgangszustand

In `src/net/rentacar/model/ValidatedVehicle.java` fehlen die Validierungs-Annotationen auf den Feldern `brand` und `hp`. In `test/net/rentacar/TestValidation.java` schlägt `invalidVehicleTriggersValidation()` fehl, da ungültige Daten ungeprüft persistiert werden.

## Aufgabe

Bearbeite `src/net/rentacar/model/ValidatedVehicle.java`:

1. **Validierungsregeln annotieren:**
   - Annotiere `private String brand;` mit `@NotBlank(message = "Brand must not be blank")`.
   - Annotiere `private int hp;` mit `@Min(value = 1, message = "HP must be at least 1")`.

2. **Validierung testen:**
   - In `test/net/rentacar/TestValidation.java`:
     - Ein Fahrzeug mit `brand = "VW"` und `hp = 120` wird erfolgreich persistiert.
     - Ein Fahrzeug mit leerem Brand (`""`) und `hp = 0` löst beim `flush()` eine `PersistenceException` (mit Ursache `ConstraintViolationException`) aus.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_08_03_JPA_Validation -am test
```

**Beobachtung im Log:**
- Beobachte, dass die Validierung vor dem Absenden des SQL-`INSERT` im Speicher greift und fehlerhafte Datensätze die Datenbank gar nicht erst erreichen.

## Erfolgskriterium

Der Test `test/net/rentacar/TestValidation.java` läuft mit allen 2 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welcher Vorteil entsteht dadurch, dass Validierungsregeln direkt an Entity-Feldern annotiert werden (DRY-Prinzip über DB-, Service- und UI-Schichten)?
2. In welchen JPA-Lebenszyklusphasen führt der Provider standardmäßig Bean Validation aus (`pre-persist`, `pre-update`, `pre-remove`)?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_08_03_JPA_Validation_Loesung`.
