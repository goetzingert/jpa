# Übung: 1_8 Many-to-Many-Beziehung

## Lernziel

Eine N:M-Beziehung (`@ManyToMany`) zwischen Entitäten (`Vehicle` und `Shop` als `locationHistory`) abbilden, die Join-Tabelle (`@JoinTable`, `joinColumns`, `inverseJoinColumns`) explizit konfigurieren und Kaskadierungsentscheidungen (`CascadeType.PERSIST`, `CascadeType.MERGE`) verstehen.

## Ausgangszustand

In `src/net/rentacar/model/Vehicle.java` fehlt das Mapping für die Standort-Historie (`locationHistory`). Der Test `test/net/rentacar/TestConnection.java` kann die N:M-Beziehung (`testManyToManyVehicle`) nicht überprüfen.

## Aufgabe

Bearbeite `src/net/rentacar/model/Vehicle.java` und `test/net/rentacar/TestConnection.java`:

1. **N:M-Collection in `Vehicle.java` definieren:**
   - Deklariere `private List<Shop> locationHistory = new ArrayList<Shop>();` mit passendem Getter und Setter.

2. **`@ManyToMany` und `@JoinTable` annotieren:**
   - Annotiere das Feld mit `@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })`.
   - Konfiguriere die Verbindungstabelle mit `@JoinTable`:
     ```java
     @JoinTable(
         name = "location_history",
         joinColumns = { @JoinColumn(name = "vehicle_id") },
         inverseJoinColumns = { @JoinColumn(name = "shop_id") }
     )
     ```

3. **Historie bei Standortwechsel pflegen:**
   - Ergänze in `setLocation(Shop location)`: Wird ein neues Fahrzeug einem Standort zugewiesen, wird der vorherige Standort automatisch in `locationHistory` übernommen.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_8_JPA_ManyToMany -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die DDL-Erstellung: Die Join-Tabelle `location_history` wird mit zwei Fremdschlüsseln auf `tbl_Vehicle` und `tbl_Shop` generiert.
- Beobachte, wann Zeilen in `location_history` eingefügt werden (beim Standortwechsel und gleichzeitigem `flush()`).

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 8 Testmethoden (insb. `testManyToManyVehicle`) erfolgreich durch:
```text
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Wann ist bei einer N:M-Beziehung eine unidirektionale Abbildung ausreichend und wann empfiehlt sich eine bidirektionale Modellierung?
2. Warum sollte `CascadeType.REMOVE` bei `@ManyToMany`-Beziehungen in der Regel vermieden werden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_8_JPA_ManyToMany_Loesung`.
