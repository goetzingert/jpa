# Übung: 1_7 Many-to-One und mappedBy (Bidirektionale Konsistenz)

## Lernziel

Eine bidirektionale 1:N / N:1-Beziehung zwischen `Shop` und `Vehicle` sowie eine unidirektionale N:1-Beziehung von `Vehicle` zu `VehicleType` modellieren. Das fundamentale JPA-Konzept der *owning side* (Fremdschlüsselträger) versus *inverse side* mit `mappedBy` verstehen und defensive Hilfsmethoden zur In-Memory-Synchronisation implementieren.

## Ausgangszustand

In `src/net/rentacar/model/Vehicle.java` fehlen die `@ManyToOne`-Annotationen auf den Feldern `type` und `location`. In `src/net/rentacar/model/Shop.java` ist `vehicles` noch nicht korrekt mit `mappedBy = "location"` als inverse Seite konfiguriert. Der Test `test/net/rentacar/TestConnection.java` schlägt beim Laden der Beziehungen fehl.

## Aufgabe

Bearbeite `Vehicle.java`, `Shop.java` und `TestConnection.java`:

1. **Unidirektionales N:1 in `Vehicle.java`:**
   - Annotiere `private VehicleType type` mit `@ManyToOne`.

2. **Bidirektionales N:1 in `Vehicle.java` (Owning Side):**
   - Annotiere `private Shop location` mit `@ManyToOne`.
   - `Vehicle` ist die besitzende Seite und hält die Fremdschlüsselspalte `location_id`.

3. **Inverse 1:N-Seite in `Shop.java`:**
   - Annotiere `vehicles` mit `@OneToMany(mappedBy = "location", cascade = CascadeType.PERSIST)`.
   - `mappedBy = "location"` verweist auf das Feld `location` in `Vehicle`.

4. **Testmethoden implementieren (`TestConnection.java`):**
   - `testManyToOneVehicleToType()`: Unidirektionalen Zugriff prüfen.
   - `testManyToOneVehicleToLocation()`: N:1-Zugriff von Fahrzeug auf Standort prüfen.
   - `testOneToManyOfShop()`: 1:N-Navigation von Shop auf Fahrzeuge prüfen.
   - `testOwningSidePersistsRelation()`: Standort über `vehicle.setLocation(stuttgart)` zuweisen und verifizieren, dass der Fremdschlüssel in `tbl_Vehicle` geschrieben wird.
   - `testDefensiveHelperMethodMaintainsBothSides()`: Prüfen, dass `shop.addVehicle()` beide Seiten im Speicher konsistent hält.
   - `testMovingVehicleUpdatesBothShopCollections()`: Prüfen, dass das Versetzen eines Fahrzeugs die Collections beider Shops aktualisiert.
   - `testRemovingLocationSetsNullForeignKey()`: Prüfen, dass `setLocation(null)` den Fremdschlüssel auf NULL setzt.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_7_JPA_ManyToOne -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Untersuche die Tabelle `tbl_Vehicle`: Sie enthält den Fremdschlüssel auf `tbl_Shop`.
- Beobachte, dass dank `mappedBy` keine zusätzliche Join-Tabelle erzeugt wird und keine nachträglichen `UPDATE`-Statements für Fremdschlüssel nötig sind.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` läuft mit allen 7 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum bezeichnet `mappedBy` immer den Namen des Java-Attributs auf der Gegenseite und niemals einen Datenbankspaltennamen?
2. Was passiert, wenn eine neue Verknüpfung nur in `shop.getVehicles().add(vehicle)` eingetragen wird, `vehicle.setLocation(shop)` aber nicht aufgerufen wird? Wird die Beziehung in der Datenbank gespeichert?
3. Warum sind defensive Helper-Methoden (wie `addVehicle`/`removeVehicle`) bei bidirektionalen Beziehungen für die Anwendungslogik unverzichtbar?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_7_JPA_ManyToOne_Loesung`.
