# Übung: 2_08_01 JPA Entity-Lebenszyklus und Persistence Context

## Lernziel

Die vier fundamentalen Zustände einer JPA-Entity (*New/Transient*, *Managed*, *Detached*, *Removed*) verstehen und die Steuerungsbefehle des `EntityManager` (`persist`, `flush`, `clear`, `contains`, `find`, `remove`) gezielt anwenden.

## Ausgangszustand

In `test/net/rentacar/TestLifecycle.java` ist die Testmethode `implementEntityLifecycleExercise()` noch ein unvollständiger Rumpf.

## Aufgabe

Bearbeite `test/net/rentacar/TestLifecycle.java`:

1. **Entity instanziieren (Transient):**
   - Erstelle eine neue Entity `VehicleType vehicleType = new Car(new Model("VW", "Golf"), 120, 200, 2);`.
   - Prüfe mit `assertFalse(manager.contains(vehicleType))`, dass das Objekt noch nicht verwaltet wird.

2. **Entity persistieren (Managed):**
   - Rufe `manager.persist(vehicleType)` auf.
   - Prüfe mit `assertTrue(manager.contains(vehicleType))`, dass die Entity nun im Persistence Context liegt.

3. **Synchronisieren und Kontext leeren (Flush & Clear):**
   - Führe `manager.flush()` aus und stelle fest, dass die ID generiert wurde (`assertNotNull(vehicleType.getId())`).
   - Leere den Kontext mit `manager.clear()`.
   - Prüfe mit `assertFalse(manager.contains(vehicleType))`, dass die Entity nun im Zustand *Detached* ist.

4. **Wiederladen über Primary Key (Find):**
   - Lade die Entity mit `manager.find(VehicleType.class, vehicleType.getId())` frisch aus der Datenbank und validiere die Attribute.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_08_01_JPA_TransactionLifecycle -am test
```

**Beobachtung im SQL-Log:**
- Beobachte genau, zu welchem Zeitpunkt das SQL-`INSERT` an die Datenbank gesendet wird (beim `flush()` bzw. Transaktionsende, nicht zwingend beim `persist()`).
- Beobachte, dass `manager.find()` nach einem `clear()` ein echtes `SELECT` ausführt, während `find()` vor einem `clear()` die Entity direkt aus dem First-Level-Cache zurückgibt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestLifecycle.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Was ist der Unterschied zwischen `manager.persist()` und `manager.flush()`?
2. Was passiert mit Änderungen an einer *Detached*-Entity, wenn kein `manager.merge()` aufgerufen wird?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_08_01_JPA_TransactionLifecycle_Loesung`.
