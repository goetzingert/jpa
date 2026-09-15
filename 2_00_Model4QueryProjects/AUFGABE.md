# Übung: 2_00 Gemeinsames Abfragemodell

## Lernziel

Das für alle JPQL- und Criteria-Abfragen der Folgemodule genutzte gemeinsame Rent-a-Car-Domänenmodell verstehen, Objektbeziehungen analysieren und Navigationspfade für Abfragen identifizieren.

## Ausgangszustand

Im Modul `2_00_Model4QueryProjects` liegt das erweiterte Domänenmodell unter `src/net/rentacar/model/` (`User`, `Person`, `Customer`, `Vehicle`, `VehicleType`, `Car`, `Truck`, `Shop`, `Reservation`). Vor dem Schreiben komplexer Abfragen müssen die Assoziationen, Kardinalitäten und Property-Namen klar sein.

## Aufgabe

Analysiere die Modellklassen in `src/net/rentacar/model/`:

1. **Beziehungsstrukturen und Pfade nachvollziehen:**
   - Welche Entitäten bilden 1:N- und N:1-Beziehungen (`Shop` -> `Vehicle` via `carpool` / `location`, `Customer` -> `Reservation` via `reservations`)?
   - Wie sind Vererbungshierarchien modelliert (`VehicleType` -> `Car`, `Truck`)?
   - Welche Attribute sind Embedded Objects (`Model` in `VehicleType`)?

2. **Navigationspfade formulieren:**
   - Ermittle die JPQL-Pfade von `Customer` über `Reservation` zu `Vehicle` und `Shop`.
   - Notiere, welche Attribute (`brand`, `hp`, `price`, `startDate`) für Filterungen und Sortierungen zur Verfügung stehen.

## Test und Beobachtung

Führe den Test aus, um die Modellvalidierung und Datenbankschema-Erstellung zu prüfen:

```bash
./mvnw -pl 2_00_Model4QueryProjects -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Beobachte die erzeugten Tabellen und Fremdschlüssel für `Customer`, `Reservation`, `Vehicle` und `Shop`.
- Prüfe, wie die Vererbung von `VehicleType` in Tabellen abgebildet ist.

## Erfolgskriterium

Das Modul kompiliert und alle vorbereiteten Modellprüfungen laufen erfolgreich durch. Du kannst für jede Entität die navigierbaren Assoziationspfade für spätere JPQL-Queries benennen.

## Reflexion

1. Warum navigiert JPQL über Entity-Attributnamen (`reservation.vehicle.location`) statt über relationale Tabellen- und Fremdschlüsselspalten?
2. Warum ist das Verständnis von `mappedBy` und der *owning side* entscheidend für die Formulierung korrekter JPQL-Joins?

## Lösungshinweis

Das Modell in `2_00_Model4QueryProjects` dient als Referenzbibliothek für alle nachfolgenden Abfrageübungen.
