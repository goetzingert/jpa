# Übung: 1_2_1 Erste Entity & Entity-Lifecycle

## Lernziel

Eine Java-POJO-Klasse als JPA-Entity abbilden und die Kernzustände des JPA-Entity-Lebenszyklus (*Transient*, *Managed*, *Detached*, *Removed*) über die `EntityManager`-Operationen (`persist`, `find`, `detach`, `merge`, `remove`, `contains`) sowie automatisches Dirty Checking verstehen und anwenden.

## Ausgangszustand

Die Klasse `src/net/rentacar/model/VehicleType.java` ist ein einfaches Java-Objekt ohne JPA-Annotationen. Der Test `test/net/rentacar/TestConnection.java` kann das Objekt nicht persistieren bzw. über `find(...)` laden.

## Aufgabe

Bearbeite `src/net/rentacar/model/VehicleType.java` und `test/net/rentacar/TestConnection.java`:

1. **Entity-Annotation:**
   - Annotiere die Klasse `VehicleType` mit `@Entity` (Package `jakarta.persistence`).
   - Markiere das Feld `id` mit `@Id`.
   - Stelle sicher, dass der parameterlose Standard-Konstruktor vorhanden ist.

2. **Testmethoden implementieren (`test/net/rentacar/TestConnection.java`):**
   - `testFind()`: Lade das in `setUp()` erzeugte `VehicleType`-Objekt mittels `manager.find(VehicleType.class, "1")` und prüfe die Attribute.
   - `testDirtyCheckingTriggersAutomaticUpdate()`: Ändere die PS-Zahl (`setHp(150)`) auf der gemanagten Entity und synchronisiere (`flush()`, `clear()`). Prüfe, dass JPA den Wert automatisch ohne Aufruf einer Update-Methode aktualisiert hat.
   - `testContainsReflectsPersistenceContextState()`: Überprüfe mit `manager.contains()`, wie sich der Zustand von gemanagt zu detached nach `manager.detach()` und `manager.clear()` ändert.
   - `testDetachAndMergeLifecycle()`: Modifiziere eine detached Entity im Java-Speicher (`setHp(180)`) und führe sie mit `manager.merge()` wieder in den Persistence Context zurück. Verifiziere, dass `merge()` eine neue gemanagte Instanz zurückliefert.
   - `testRemoveDeletesEntityFromDatabase()`: Lösche eine gemanagte Entity mit `manager.remove()` und prüfe nach `flush()`, dass sie nicht mehr auffindbar ist.
   - `testFindNonExistingIdReturnsNull()`: Beweise, dass `manager.find()` bei nicht existierender ID `null` liefert (keine Exception).

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_2_1_JPA_FirstEntity -am test
```

**Beobachtung im SQL-Log:**
- Beobachte die Tabellen-DDL beim Start (`CREATE TABLE VehicleType ...`).
- Beobachte, dass beim Ändern von Attributen auf gemanagten Entities beim `flush()` automatisch ein SQL-`UPDATE` generiert wird (Dirty Checking).
- Beobachte, dass `manager.merge()` bei Bedarf zunächst ein `SELECT` ausführt, um den aktuellen Zustand in den Context zu laden, bevor das `UPDATE` erfolgt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 6 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum verlangt die JPA-Spezifikation zwingend einen parameterlosen Konstruktor für Entity-Klassen?
2. Warum erfordert Dirty Checking in JPA keine explizite `update()`-Methode am `EntityManager`?
3. Was ist der genaue Unterschied zwischen der ursprünglichen detached Instanz und dem Rückgabewert von `manager.merge()`?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_2_1_JPA_FirstEntity_Loesung`.
