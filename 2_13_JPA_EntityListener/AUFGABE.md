# Übung: 2_13 Entity Listeners mit @EntityListeners

## Lernziel

Lebenszyklus-Logik aus Entity-Klassen auslagern, dedizierte Listener-Klassen mit `@EntityListeners` registrieren und Querschnittsaufgaben (wie Auditing oder Logging) modular umsetzen.

## Ausgangszustand

In `src/net/rentacar/model/listener/VehicleTypeListener.java` existiert eine Listener-Klasse mit `@PostPersist public void doInListener(VehicleType vehicle)`. In `src/net/rentacar/model/VehicleType.java` fehlt jedoch die Verknüpfung über `@EntityListeners`. In `test/net/rentacar/TestCallback.java` schlägt `testCallback()` fehl, da `VehicleTypeListener.gewonnen` nicht auf `true` gesetzt wird.

## Aufgabe

Bearbeite `src/net/rentacar/model/VehicleType.java`:

1. **Entity Listener an der Entity registrieren:**
   - Annotiere die Entity-Klasse `VehicleType` mit `@EntityListeners(VehicleTypeListener.class)`.

2. **Listener-Ausführung prüfen:**
   - In `test/net/rentacar/TestCallback.java` wird ein Fahrzeug persistiert und geflusht.
   - Verifiziere, dass sowohl der interne Callback der Entity als auch der externe `VehicleTypeListener` ausgeführt werden.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_13_JPA_EntityListener -am test
```

## Erfolgskriterium

Der Test `test/net/rentacar/TestCallback.java` läuft mit allen 2 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```
`VehicleTypeListener.gewonnen` ist nach dem `flush()` `true`.

## Reflexion

1. Welcher Architekturvorteil entsteht, wenn Auditing- und Logging-Logik in externe Entity Listener statt in die fachlichen Entity-Klassen ausgelagert wird?
2. Wie unterscheidet sich die Signatur einer Callback-Methode in einer Entity von einer Methode in einer externen Listener-Klasse (Übergabe des Entity-Objekts als Methodenparameter)?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_13_JPA_EntityListener_Loesung`.
