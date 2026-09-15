# Übung: 2_08_05 Bulk-Updates & Persistence Context Synchronisation

## Lernziel

Den Unterschied zwischen Einzeloperationen (`find`/`persist`/Dirty Checking) und Massenoperationen (`Query.executeUpdate()`) verstehen. Erkennen, dass Bulk-Updates direkt auf der Datenbank ausgeführt werden und den First-Level-Cache (Persistence Context) **umgehen**, sowie die Notwendigkeit von `em.clear()` oder `em.refresh()` beherrschen.

## Ausgangszustand

In `TestBulkOperations.java` wird demonstriert, warum nach einem `executeUpdate()` verwaltete Entities im Persistence Context veraltete Daten enthalten.

## Aufgabe

1. **Entity `BulkVehicle` untersuchen:**
   - Entity mit `brand`, `dailyRate` und `active`.

2. **Bulk-Update ausführen:**
   - Führe ein `UPDATE BulkVehicle v SET v.dailyRate = 120.0 WHERE v.brand = :brand` mit `executeUpdate()` aus.
   - Prüfe vor und nach `manager.clear()` den Zustand der geladenen Entity.

3. **Bulk-Delete testen:**
   - Führe ein `DELETE FROM BulkVehicle v WHERE v.brand = :brand` aus und prüfe die Anzahl verbleibender Datensätze.

## Test und Beobachtung

```bash
./mvnw -pl 2_08_05_JPA_BulkOperations -am test
```

## Erfolgskriterium

Alle Tests in `TestBulkOperations` laufen fehlerfrei durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum werden bei Bulk-Updates keine Entity-Lifecycle-Callbacks (`@PreUpdate`, `@PostUpdate`) oder EntityListener aufgerufen?
2. Welches Risiko entsteht, wenn man nach einem Bulk-Update eine veraltete Managed Entity ändert und die Transaktion committet?

## Lösungshinweis

Vergleiche deine Lösung mit `2_08_05_JPA_BulkOperations_Loesung`.
