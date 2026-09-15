# Übung: 1_4 Embedded Value Objects

## Lernziel

Strukturierung von Fachdaten mit Wertobjekten (*Value Objects*) über `@Embeddable` und `@Embedded`. Verstehen, wie eingebettete Attribute direkt in die Tabelle der besitzenden Entity integriert werden, ohne eigene Fremdschlüssel oder Zusatztabellen anzulegen.

## Ausgangszustand

In `src/net/rentacar/model/` sind `VehicleType`, `User` und `Shop` als flache Entitäten ohne strukturierte Wertobjekte definiert. Die Auslagerung von Modell- und Markeninformationen in separate `@Embeddable`-Klassen (`Model`, `VehicleModel`) ist noch nicht vollzogen. In `test/net/rentacar/TestConnection.java` sind Teile der Testabdeckung (`testFindModel`) auskommentiert.

## Aufgabe

Bearbeite die Modellklassen und die Testklasse:

1. **Embeddable-Klasse `Model` erstellen/ergänzen:**
   - Markiere `Model` mit `@Embeddable`.
   - Felder: `brand` (String) und `modell` (String) inklusive Konstruktoren und Getter.

2. **Verschachteltes Embeddable `VehicleModel` definieren:**
   - Markiere `VehicleModel` mit `@Embeddable`.
   - Felder: `series` (String) und `@Embedded private Model model;`.

3. **Entity `VehicleType` anpassen:**
   - Ersetze die Einzelattribute durch `@Embedded private VehicleModel model;` (oder `@Embedded private Model model;`).

4. **Testklasse `TestConnection.java` vervollständigen:**
   - Erzeuge in `setUp()` eine `VehicleType`-Instanz mit eingebetteten Objekten.
   - Aktiviere die Testmethode `testFindModel()` und verifiziere, dass Marke und Modell über die eingebetteten Objekte geladen werden.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_4_JPA_Embedded -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Prüfe die Spalten der Tabelle `tbl_VehicleType`: Die Felder von `VehicleModel` und `Model` werden als gewöhnliche Spalten in derselben Tabelle angelegt.
- Es existiert keine separate Tabelle für das Value Object und kein Fremdschlüssel (`FK`).

## Erfolgskriterium

Alle Testmethoden in `test/net/rentacar/TestConnection.java` laufen ohne Fehler durch:
```text
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Was unterscheidet ein `@Embeddable` Value Object grundlegend von einer `@Entity` (Identität, Lebenszyklus, Tabellenstruktur)?
2. Wie können Namenskonflikte gelöst werden, wenn dieselbe `@Embeddable`-Klasse mehrfach in einer Entity eingebettet wird (z. B. Rechnungsadresse und Lieferadresse) -> Stichwort `@AttributeOverride`?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_4_JPA_Embedded_Loesung`.
