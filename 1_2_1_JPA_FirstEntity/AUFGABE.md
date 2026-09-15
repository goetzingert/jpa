# Übung: 1_2_1 Erste Entity

## Lernziel

Eine Java-POJO-Klasse als JPA-Entity abbilden und elementare Lebenszyklusoperationen (`persist`, `find`) über den `EntityManager` ausführen.

## Ausgangszustand

Die Klasse `src/net/rentacar/model/VehicleType.java` ist ein einfaches Java-Objekt ohne JPA-Annotationen. Der Test `test/net/rentacar/TestConnection.java` kann das Objekt nicht persistieren bzw. über `find(...)` laden.

## Aufgabe

Bearbeite `src/net/rentacar/model/VehicleType.java` und `test/net/rentacar/TestConnection.java`:

1. **Entity-Annotation:**
   Annotiere die Klasse `VehicleType` mit `@Entity` (Package `jakarta.persistence`).

2. **Primärschlüssel festlegen:**
   Markiere das Feld `id` mit `@Id` und `@GeneratedValue(strategy = GenerationType.AUTO)`.

3. **Konstruktor-Anforderung:**
   Stelle sicher, dass ein parameterloser Standard-Konstruktor (`public` oder `protected`) vorhanden ist.

4. **Test implementieren:**
   Ergänze in `test/net/rentacar/TestConnection.java` die Testmethode `testFind()`:
   - Ein `VehicleType` wird in `setUp()` erzeugt, persistiert und seine generierte ID gemerkt.
   - Lade das Objekt in `testFind()` mittels `manager.find(VehicleType.class, id)`.
   - Validiere mit Assertions (`assertNotNull`, `assertEquals`), dass das geladene Objekt der gespeicherten Instanz entspricht.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_2_1_JPA_FirstEntity -am test
```

**Beobachtung im SQL-Log:**
- Prüfe, welche Tabellen-DDL beim Start erzeugt wird (`CREATE TABLE tbl_vehicletype ...` oder `CREATE TABLE VehicleType ...`).
- Beobachte die Reihenfolge von `INSERT` beim `flush()` und `SELECT` beim `find()`.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testFind`) läuft ohne Fehler durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum verlangt die JPA-Spezifikation zwingend einen parameterlosen Konstruktor für Entity-Klassen?
2. Zu welchem exakten Zeitpunkt (beim `persist()`, beim `flush()` oder beim Transaktions-Commit) vergibt der Provider die ID?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_2_1_JPA_FirstEntity_Loesung`.
