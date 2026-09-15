# Übung: 2_11 Named Queries mit @NamedQuery

## Lernziel

Wiederverwendbare, vorkompilierte und statisch validierte Abfragen mit `@NamedQuery` (und `@NamedQueries`) an Entity-Klassen deklarieren, Abfrage-Konstanten für refactoringsichere Aufrufe definieren und mit `createNamedQuery()` ausführen.

## Ausgangszustand

In `src/net/rentacar/model/Reservation.java` fehlt die `@NamedQuery`-Deklaration für die Suche nach Start-Shop. In `test/net/rentacar/TestNamedQuery.java` schlägt der Test `testCallNamedQuery()` mit einem Fehler (`IllegalArgumentException: No query defined for that name`) fehl.

## Aufgabe

Bearbeite `src/net/rentacar/model/Reservation.java` und `test/net/rentacar/TestNamedQuery.java`:

1. **Named Query an der Entity annotieren:**
   - Annotiere `Reservation` mit `@NamedQuery`:
     ```java
     @NamedQuery(
         name = Reservation.FIND_BY_START_Shop,
         query = "SELECT r FROM Reservation r WHERE r.startShop = :shop"
     )
     ```
   - Verwende die Konstanten `Reservation.FIND_BY_START_Shop` und `Reservation.PARAM_Shop`.

2. **Named Query aufrufen:**
   - In `testCallNamedQuery()`: Erzeuge die Query mit `manager.createNamedQuery(Reservation.FIND_BY_START_Shop)`.
   - Setze den Parameter `shop` auf die `Shop`-Instanz `muenchen` und führe die Abfrage aus.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_11_JPQL_NamedQueries -am test
```

**Beobachtung beim Start:**
- Beachte, dass Named Queries bereits beim Hochfahren der `EntityManagerFactory` validiert und vorkompiliert werden. Syntaxfehler in JPQL werden somit direkt beim Anwendungsstart erkannt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestNamedQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
Für den Standort München werden genau 2 Reservierungen gefunden.

## Reflexion

1. Welchen Vorteil bietet die Validierung von `@NamedQuery` beim Start der `EntityManagerFactory` gegenüber dynamischen Queries via `manager.createQuery()`?
2. Warum empfiehlt es sich, die Query-Namen und Parameternamen als `public static final String`-Konstanten in der jeweiligen Entity-Klasse zu bündeln?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_11_JPQL_NamedQueries_Loesung`.
