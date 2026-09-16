# Übung: 1_5 One-to-One-Beziehung & Kaskadierung

## Lernziel

Eine 1:1-Beziehung (`@OneToOne`) zwischen zwei JPA-Entities (`User` und `Person`) modellieren, Fremdschlüsselspalten verstehen, den Lebenszyklus verknüpfter Instanzen mittels Kaskadierung (`CascadeType.PERSIST`) steuern und Dirty Checking über Beziehungsgrenzen hinweg anwenden.

## Ausgangszustand

Die Entitäten `User` und `Person` existieren bereits. In `src/net/rentacar/model/User.java` ist die Relation zu `Person` jedoch noch nicht als JPA-Assoziation abgebildet oder kaskadiert. Der Test `test/net/rentacar/TestConnection.java` schlägt beim Laden der Person über `testFindUserAndNavigateToPerson()` fehl.

## Aufgabe

Bearbeite `src/net/rentacar/model/User.java` und `test/net/rentacar/TestConnection.java`:

1. **1:1-Beziehung annotieren (`User.java`):**
   - Annotiere das Feld `person` in `User` mit `@OneToOne(cascade = CascadeType.PERSIST)`.
   - Konfiguriere `@PrimaryKeyJoinColumn` oder `@JoinColumn(name = "person_id")`.

2. **Testmethoden implementieren (`TestConnection.java`):**
   - `testFindUserAndNavigateToPerson()`: Lade den `User` und prüfe, dass die verknüpfte `Person` geladen wird.
   - `testCascadePersistPropagatesToPerson()`: Erzeuge einen neuen `User` mit neuer `Person` und persistiere nur den User (`manager.persist(newUser)`). Verifiziere, dass die Person durch `CascadeType.PERSIST` automatisch in `tbl_Person` gespeichert wird.
   - `testUpdatingPersonThroughManagedUserPropagatesOnFlush()`: Ändere ein Attribut der Person über das gemanagte User-Objekt (`user.getPerson().setFirstName(...)`) und prüfe die Persistierung nach `flush()`.
   - `testRemovingUserDoesNotRemovePersonWithoutCascadeRemove()`: Lösche den User (`manager.remove(user)`) und prüfe, dass die Person in der Datenbank erhalten bleibt.
   - `testFindNonExistingUserReturnsNull()`: Prüfe den Randfall nicht existierender IDs.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_5_JPA_OneToOne -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Beobachte, in welcher Reihenfolge die SQL-`INSERT`-Statements für `Person` und `User` abgesetzt werden.
- Untersuche das Tabellenschema: In welcher Tabelle liegt der Fremdschlüssel?

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 6 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Seite ist bei einer 1:1-Beziehung die *owning side* und woran erkennt man das im relationalen Datenbankschema?
2. Was bewirkt `CascadeType.PERSIST` beim Aufruf von `manager.persist(user)` – und warum werden Änderungen nach dem Laden (`merge`/`flush`) ohne passende Cascade-Typen nicht automatisch propagiert?
3. Warum sollte `CascadeType.REMOVE` bei 1:1-Beziehungen nur verwendet werden, wenn die abhängige Entity eine echte Teil-Existenz (Komposition) darstellt?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_5_JPA_OneToOne_Loesung`.
