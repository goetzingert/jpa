# Übung: 2_03 Paging mit setFirstResult und setMaxResults

## Lernziel

Ergebnismengen von Datenbankabfragen mittels `setFirstResult()` (Offset) und `setMaxResults()` (Limit) seitenweise abrufen (*Pagination*), deterministisches Paging mit `ORDER BY` implementieren, Gesamtseitenzahlen mittels `COUNT` berechnen und Randfälle sicher behandeln.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig vorbereitet (`TODO`). Im Test-Setup wurden 7 `User`-Datensätze angelegt.

## Aufgabe

Bearbeite die Testmethoden in `test/net/rentacar/TestQuery.java`:

1. **Seitenweises Durchblättern (`testPagingIteratesThroughAllPagesWithFixedPageSize`):**
   - Erstelle eine Query mit `SELECT n FROM User n ORDER BY n.id` und `setMaxResults(3)`.
   - Implementiere eine Schleife, die mit `setFirstResult()` den Offset schrittweise erhöht, bis die Seite leer ist, und alle 7 User einsammelt.

2. **Deterministisches Paging mit `ORDER BY` (`testDeterministicPagingRequiresOrderBy`):**
   - Formuliere die Query mit `ORDER BY n.person.lastName ASC, n.person.firstName ASC`.
   - Prüfe die exakten Nachnamen für:
     - Seite 1 (Offset 0, Limit 3): *Anstaedt*, *Gross*, *Mayer*
     - Seite 2 (Offset 3, Limit 3): *Meyer*, *Mueller*, *Mustermann*
     - Seite 3 (Offset 6, Limit 3): *Schmitt*

3. **Gesamtzahl und Seitenberechnung (`testCountTotalElementsAndCalculatePageCount`):**
   - Führe `SELECT COUNT(n) FROM User n` aus (`Long`).
   - Berechne die Gesamtanzahl der Seiten bei einer `pageSize` von 3 (`Math.ceil((double) totalCount / pageSize) = 3`).

4. **Randfall: Offset außerhalb des Datenbestands (`testPagingBeyondTotalElementsReturnsEmptyList`):**
   - Prüfe mit `setFirstResult(100).setMaxResults(3)`, dass keine Exception geworfen wird, sondern eine leere Liste zurückkommt.

5. **Randfall: Limit 0 (`testPagingWithZeroMaxResultsReturnsEmptyList`):**
   - Prüfe mit `setMaxResults(0)`, dass eine leere Ergebnisliste geliefert wird.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_03_JPQL_SimpleQueryPaging -am test
```

**Beobachtung im SQL-Log:**
- Beobachte, wie der JPA-Provider die datenbankspezifischen Paging-Befehle (z. B. `OFFSET ... ROWS FETCH NEXT ... ROWS ONLY` in Derby oder `LIMIT / OFFSET` in PostgreSQL/MySQL) automatisch generiert.

## Erfolgskriterium

Der Test `test/net/rentacar/TestQuery.java` läuft erfolgreich durch:
```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum ist Pagination auf Datenbankebene (`setFirstResult`/`setMaxResults`) dem Laden aller Datensätze mit anschließender Filterung im Java-Speicher (`subList()`) drastisch überlegen?
2. Warum ist bei verlässlicher Pagination in Produktionsanwendungen eine explizite `ORDER BY`-Klausel unerlässlich?
3. Warum ist `setFirstResult`/`setMaxResults` in Kombination mit `JOIN FETCH` auf Collection-Beziehungen (1:N) eine bekannte Performance-Falle (Hibernate InMemory-Paging-Warnung)?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_03_JPQL_SimpleQueryPaging_Loesung`.
