# Übung: 2_03 Paging mit setFirstResult und setMaxResults

## Lernziel

Ergebnismengen von Datenbankabfragen mittels `setFirstResult()` (Offset) und `setMaxResults()` (Limit) seitenweise abrufen (*Pagination*), Speicherauslastung minimieren und datenbankunabhängige Pagination-Mechanismen verstehen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` ist die Testmethode `testPagingBySelectNutzer()` ein leerer Rumpf. Im Test-Setup wurden 7 `User`-Datensätze angelegt.

## Aufgabe

Bearbeite `test/net/rentacar/TestQuery.java`:

1. **Paging-Query formulieren:**
   - Erstelle eine `TypedQuery<User>` mit `SELECT n FROM User n`.
   - Setze die Seitengröße auf 3 (`setMaxResults(3)`).

2. **Seitenweise durchblättern:**
   - Implementiere eine Schleife, die mit `setFirstResult()` den Offset bei jedem Schritt um die Seitengröße erhöht, bis eine leere Ergebnisliste zurückgegeben wird.

3. **Gezielte Offset-Abfrage verifizieren:**
   - Prüfe mit `assertEquals(2, createQuery.setFirstResult(5).setMaxResults(3).getResultList().size())`, dass ab Index 5 bei 7 Datensätzen genau die verbleibenden 2 Elemente geladen werden.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_03_JPQL_SimpleQueryPaging -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie der JPA-Provider die datenbankspezifischen Paging-Befehle (z. B. `OFFSET ... ROWS FETCH NEXT ... ROWS ONLY` in Derby oder `LIMIT / OFFSET` in anderen Datenbanken) automatisch generiert.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum ist Pagination auf Datenbankebene (`setFirstResult`/`setMaxResults`) dem Laden aller Datensätze mit anschließender Filterung im Java-Speicher (`subList()`) drastisch überlegen?
2. Warum ist bei verlässlicher Pagination in Produktionsanwendungen eine explizite `ORDER BY`-Klausel unerlässlich?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_03_JPQL_SimpleQueryPaging_Loesung`.
