# Übung: 2_08_06 Pessimistisches Sperren (Pessimistic Locking)

## Lernziel

Den Einsatz von pessimistischen Datenbanksperren (`LockModeType.PESSIMISTIC_WRITE`, `LockModeType.PESSIMISTIC_READ`) zur Verhinderung paralleler Schreibkonflikte verstehen. Erkennen, wie JPA ein `SELECT ... FOR UPDATE` generiert und wann pessimistisches Sperren gegenüber optimistischem Sperren (`@Version`) vorzuziehen ist.

## Ausgangszustand

In `PessimisticVehicle.java` ist die Entity definiert. In `TestPessimisticLocking.java` wird das Sperren via `find(..., LockModeType)` und `manager.lock(...)` trainiert.

## Aufgabe

1. **Pessimistic Lock beim Laden anfordern:**
   - Nutze `manager.find(PessimisticVehicle.class, id, LockModeType.PESSIMISTIC_WRITE)`.
   - Beobachte im SQL-Log das generierte SQL-Statement mit Sperrklausel (`FOR UPDATE`).

2. **Nachträgliches Sperren:**
   - Lade eine Entity mit normalem `find(...)` und sperre sie anschließend explizit mit `manager.lock(entity, LockModeType.PESSIMISTIC_WRITE)`.

## Test und Beobachtung

```bash
./mvnw -pl 2_08_06_JPA_PessimisticLocking -am test
```

## Erfolgskriterium

Alle Tests in `TestPessimisticLocking` laufen fehlerfrei durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Wann ist Optimistic Locking (hohe Lesehäufigkeit, seltene Konflikte) und wann Pessimistic Locking (hohe Kollisionswahrscheinlichkeit, z. B. Ticket- oder Fahrzeugreservierung) die bessere Wahl?
2. Welche Gefahr (z. B. Deadlocks, Performance-Engpässe) bergen pessimistische Sperren bei langen Transaktionen?

## Lösungshinweis

Vergleiche deine Lösung mit `2_08_06_JPA_PessimisticLocking_Loesung`.
