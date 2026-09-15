# Übung: 2_07 Fetch Types und JOIN FETCH (N+1-Problem)

## Lernziel

Den Unterschied zwischen `FetchType.LAZY` und `FetchType.EAGER` verstehen, das klassische N+1-Select-Problem identifizieren und Assoziationen gezielt und effizient mittels `JOIN FETCH` in einem einzigen SQL-Statement vorab laden (*eager fetching per query*).

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` schlägt `testForVehicleInShop()` fehl oder erzeugt nach dem Schließen des Persistence Contexts eine `LazyInitializationException`, da die Fahrzeug-Collection `Shop.carpool` lazy geladen wird.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **`JOIN FETCH`-Abfrage formulieren:**
   - Lade die Shops zusammen mit ihrem Fahrzeugpool in einer einzigen Abfrage:
     `SELECT f FROM Shop f LEFT JOIN FETCH f.carpool`
   - Beachte das Schlüsselwort `FETCH`, das den JPA-Provider anweist, die verknüpften `Vehicle`-Objekte direkt im selben Abfrageergebnis zu initialisieren.

2. **Verarbeitungslogik testen:**
   - Iteriere über alle geladenen Shops und deren Fahrzeuge (`shop2.getCarpool()`).
   - Stelle sicher, dass der Zugriff auf Fahrzeug- und Modelldaten fehlerfrei gelingt.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_07_JPQL_FetchType -am test
```

**Beobachtung im SQL-Log:**
- Zähle die ausgeführten SQL-Statements: Ohne `JOIN FETCH` entsteht für jeden einzelnen Shop ein separates `SELECT`-Statement beim ersten Zugriff auf `getCarpool()` (N+1-Problem).
- Beobachte mit `JOIN FETCH`: Es wird exakt **1** SQL-Statement mit einem `LEFT OUTER JOIN` ausgeführt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft fehlerfrei durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
Alle Fahrzeuge in den Shops werden ohne zusätzliche Nachlade-Queries ausgelesen.

## Reflexion

1. Warum gilt die Faustregel: "Globale Mappings immer `LAZY`, bei Bedarf gezielt per `JOIN FETCH` oder Entity Graph laden"?
2. Was ist der Unterschied zwischen einem regulären `LEFT JOIN` und einem `LEFT JOIN FETCH` in JPQL?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit den Referenzständen in `2_07_JPQL_FetchType_Loesung_FetchJoin` (bester Ansatz) und `2_07_JPQL_FetchType_Loesung_Eager` (statisches Eager-Mapping).
