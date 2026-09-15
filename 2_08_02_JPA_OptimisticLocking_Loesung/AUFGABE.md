# Übung: 2_08_02 Optimistic Locking mit @Version

## Lernziel

Nebenläufige Datenbankzugriffe mittels optimistischer Sperren absichern, Versionsspalten mit `@Version` konfigurieren und das Verhalten bei Versionskonflikten (`OptimisticLockException`) testen.

## Ausgangszustand

In `src/net/rentacar/model/LockableVehicle.java` fehlt die `@Version`-Annotation. In `test/net/rentacar/TestOptimisticLocking.java` schlägt `optimisticLockingRejectsStaleVersions()` fehl, da parallele Änderungen die Daten gegenseitig überschreiben (*Lost Update*), ohne eine Exception auszulösen.

## Aufgabe

Bearbeite `src/net/rentacar/model/LockableVehicle.java` und `test/net/rentacar/TestOptimisticLocking.java`:

1. **Versionsfeld annotieren:**
   - Ergänze in `LockableVehicle` das Feld:
     ```java
     @Version
     private Long version;
     ```
   - Füge Getter und Setter hinzu.

2. **Parallele Transaktionen testen:**
   - In `TestOptimisticLocking`: Öffne zwei getrennte `EntityManager` (`firstManager`, `secondManager`) und lade dasselbe Fahrzeug.
   - Ändere und committe die Entity im ersten `EntityManager` (erhöht die Versionsnummer in der Datenbank).
   - Ändere die Entity im zweiten `EntityManager` (besitzt noch die alte Versionsnummer) und prüfe, dass `secondManager.flush()` eine `OptimisticLockException` wirft.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_08_02_JPA_OptimisticLocking -am test
```

**Beobachtung im SQL-Log:**
- Beobachte die generierten SQL-`UPDATE`-Statements:
  `UPDATE tbl_LockableVehicle SET brand = ?, version = ? WHERE id = ? AND version = ?`
- Beachte, wie die Datenbank über die `WHERE ... AND version = ?`-Bedingung prüft, ob die Zeile seit dem Auslesen verändert wurde (Update-Count == 0 löst die Exception aus).

## Erfolgskriterium

Der Test `test/net/rentacar/TestOptimisticLocking.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```
Die veraltete Transaktion wird durch eine `OptimisticLockException` abgewiesen.

## Reflexion

1. Warum sind optimistische Sperren (`@Version`) in modernen Webanwendungen dem pessimistischen Sperren (`PESSIMISTIC_WRITE`) meist vorzuziehen?
2. Welche Datentypen sind für JPA-`@Version`-Felder zulässig (z. B. `int`, `Integer`, `long`, `Long`, `Timestamp`)?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_08_02_JPA_OptimisticLocking_Loesung`.
