# Übung: 1_8 Many-to-Many-Beziehung & JoinTable

## Lernziel

Eine N:M-Beziehung (`@ManyToMany`) zwischen Entitäten (`Vehicle` und `Shop` als `locationHistory`) abbilden, die Join-Tabelle (`@JoinTable`, `joinColumns`, `inverseJoinColumns`) explizit konfigurieren, Kaskadierungsentscheidungen treffen und Assoziationsänderungen in Verbindungstabellen nachvollziehen.

## Ausgangszustand

In `src/net/rentacar/model/Vehicle.java` fehlt das Mapping für die Standort-Historie (`locationHistory`). Der Test `test/net/rentacar/TestConnection.java` kann die N:M-Beziehung (`testManyToManyLocationHistory`) nicht überprüfen.

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

4. **Testmethoden implementieren (`TestConnection.java`):**
   - `testFindVehicle()`: Fahrzeug-Laden prüfen.
   - `testOneToManyOfShop()`: 1:N-Bestand am Standort prüfen.
   - `testManyToOneVehicleLocation()`: Aktuellen Standort prüfen.
   - `testManyToManyLocationHistory()`: N:M-Historie verifizieren.
   - `testAddingMultipleLocationsToHistory()`: Mehrfache Standortwechsel dokumentieren und Historie prüfen.
   - `testMultipleVehiclesSharingSameLocationInHistory()`: N:M-Verknüpfung mehrerer Fahrzeuge auf dieselben Standorte und JPQL-Join testen.
   - `testRemovingHistoryEntryDeletesOnlyJoinTableRow()`: Prüfen, dass das Leeren der Historie nur die Zeilen in `location_history` entfernt, die `Shop`-Entitäten aber intakt bleiben.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_8_JPA_ManyToMany -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die DDL-Erstellung: Die Join-Tabelle `location_history` wird mit zwei Fremdschlüsseln auf `tbl_Vehicle` und `tbl_Shop` generiert.
- Beobachte, wann Zeilen in `location_history` eingefügt werden (beim Standortwechsel und gleichzeitigem `flush()`).

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 7 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Wann ist bei einer N:M-Beziehung eine unidirektionale Abbildung ausreichend und wann empfiehlt sich eine bidirektionale Modellierung?
2. Warum sollte `CascadeType.REMOVE` bei `@ManyToMany`-Beziehungen in der Regel zwingend vermieden werden?
3. Wann sollte eine `@ManyToMany`-Beziehung in zwei `@ManyToOne`-Beziehungen mit einer eigenständigen Entity für die Verbindungstabelle aufgelöst werden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_8_JPA_ManyToMany_Loesung`.
