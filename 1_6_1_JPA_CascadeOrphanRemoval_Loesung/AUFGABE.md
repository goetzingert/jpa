# Übung: 1_6_1 Cascade und orphanRemoval

## Lernziel

Den exakten Unterschied zwischen `CascadeType.PERSIST`, `CascadeType.REMOVE` und `orphanRemoval = true` in `@OneToMany`-Beziehungen verstehen und in Unittests nachweisen.

## Ausgangszustand

In `RentalContract.java` ist `@OneToMany` ohne Kaskadierung und ohne `orphanRemoval` konfiguriert. Die Tests in `TestCascadeOrphanRemoval.java` schlagen fehl.

## Aufgabe

1. **Beziehung in `RentalContract` konfigurieren:**
   - Ergänze `@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)`.
   - Untersuche die Hilfsmethoden `addDamageRecord` und `removeDamageRecord`, die beide Seiten der bidirektionalen Beziehung synchron halten.

2. **Drei Szenarien testen:**
   - **Kaskadiertes Persistieren:** Speichern des Vertrags speichert automatisch alle Schadensberichte.
   - **Orphan Removal:** Entfernen eines Schadensberichts aus der Liste des Vertrags löscht den Datensatz physisch in der Datenbank.
   - **Kaskadiertes Löschen:** Löschen des Vertrags löscht alle zugehörigen Schadensberichte.

## Test und Beobachtung

```bash
./mvnw -pl 1_6_1_JPA_CascadeOrphanRemoval -am test
```

## Erfolgskriterium

Alle 3 Testmethoden in `TestCascadeOrphanRemoval` laufen erfolgreich durch:
```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Was ist der funktionale Unterschied zwischen `CascadeType.REMOVE` und `orphanRemoval = true`?
2. Warum kann unbedachtes `CascadeType.ALL` auf `@ManyToOne`- oder `@ManyToMany`-Beziehungen fatale Datenverluste verursachen?

## Lösungshinweis

Vergleiche deine Lösung mit `1_6_1_JPA_CascadeOrphanRemoval_Loesung`.
