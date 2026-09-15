# Übung: 2_12 Lifecycle Callbacks mit @PrePersist und @PostPersist

## Lernziel

JPA-Lebenszyklus-Methoden direkt in Entity-Klassen deklarieren (`@PrePersist`, `@PostPersist`, `@PreUpdate`, `@PostUpdate`, etc.), automatische Vor- und Nachbereitungen beim Persistieren ausführen und den genauen Zeitpunkt des Aufrufs verstehen.

## Ausgangszustand

In `src/net/rentacar/model/VehicleType.java` fehlt die Callback-Annotation für die Methode `doCallback()`. In `test/net/rentacar/TestCallback.java` schlägt `testCallback()` fehl, da `Vehicle.isCalled()` nach dem `flush()` weiterhin `false` liefert.

## Aufgabe

Bearbeite `src/net/rentacar/model/VehicleType.java`:

1. **Lifecycle-Callback annotieren:**
   - Annotiere die Callback-Methode `public void doCallback()` mit `@PostPersist` (oder `@PrePersist` je nach Anforderung).
   - In dieser Methode wird das Flag `this.called = true;` gesetzt.

2. **Aufrufzeitpunkt im Test prüfen:**
   - In `test/net/rentacar/TestCallback.java`:
     - Nach `manager.persist(vehicle)` vor dem Flush ist `isCalled()` noch `false` (bei `@PostPersist`).
     - Nach `manager.flush()` wurde der Datensatz in die Datenbank geschrieben und `isCalled()` ist `true`.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_12_JPA_Callbacks -am test
```

**Beobachtung im Log:**
- Beobachte den genauen Zeitpunkt, an dem die Callback-Methode ausgeführt wird (direkt nach dem SQL-`INSERT` bei `@PostPersist`).

## Erfolgskriterium

Der Test `test/net/rentacar/TestCallback.java` läuft mit allen 2 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Lifecycle-Phasen stehen in JPA zur Verfügung (`@PrePersist`, `@PostPersist`, `@PreUpdate`, `@PostUpdate`, `@PreRemove`, `@PostRemove`, `@PostLoad`)?
2. Warum dürfen in Lifecycle-Callback-Methoden keine `EntityManager`-Aufrufe oder Transaktionsoperationen durchgeführt werden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_12_JPA_Callbacks_Loesung`.
