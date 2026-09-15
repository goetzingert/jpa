# Übung: 1_5 One-to-One-Beziehung

## Lernziel

Eine unidirektionale 1:1-Beziehung (`@OneToOne`) zwischen zwei JPA-Entities modellieren, Fremdschlüsselspalten verstehen und den Lebenszyklus verknüpfter Instanzen mittels Kaskadierung (`CascadeType.PERSIST`) steuern.

## Ausgangszustand

Die Entitäten `User` und `Person` existieren bereits. In `src/net/rentacar/model/User.java` ist die Relation zu `Person` jedoch noch nicht vollständig als JPA-Assoziation abgebildet oder kaskadiert. Der Test `test/net/rentacar/TestConnection.java` schlägt beim Laden der Person über `testFindPersonByUser()` fehl.

## Aufgabe

Bearbeite `src/net/rentacar/model/User.java` und `test/net/rentacar/TestConnection.java`:

1. **1:1-Beziehung annotieren:**
   Annotiere das Feld `person` in `User` mit `@OneToOne(cascade = CascadeType.PERSIST)`.

2. **Fremdschlüssel-Mapping prüfen:**
   Stelle sicher, dass `User` als besitzende Seite (*owning side*) die Referenz auf `Person` hält.

3. **Testfälle vervollständigen:**
   In `test/net/rentacar/TestConnection.java` in `setUp()` wird ein `User` mit zugehöriger `Person` erzeugt und persistiert (`manager.persist(user)`). Durch `CascadeType.PERSIST` wird die `Person` automatisch mitspeichert, ohne dass ein separates `manager.persist(user.getPerson())` nötig ist.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_5_JPA_OneToOne -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Prüfe im DDL-Log, in welcher Tabelle der Fremdschlüssel angelegt wird (`tbl_User` erhält eine Fremdschlüsselspalte `PERSON_ID`).
- Beobachte die `INSERT`-Reihenfolge: Warum muss `Person` vor `User` in die Datenbank geschrieben werden?

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welche Seite ist bei einer 1:1-Beziehung die *owning side* und woran erkennt man das im relationalen Datenbankschema?
2. Was bewirkt `CascadeType.PERSIST` beim Aufruf von `manager.persist(user)` – und warum werden Änderungen nach dem Laden (`merge`/`flush`) ohne passende Cascade-Typen nicht automatisch propagiert?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_5_JPA_OneToOne_Loesung`.
