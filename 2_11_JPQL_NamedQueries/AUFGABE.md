# Übung: 2_11 Named Queries mit @NamedQuery und orm.xml

## Lernziel

Wiederverwendbare, vorkompilierte und statisch validierte Abfragen mit `@NamedQuery` (und `@NamedQueries`) an Entity-Klassen deklarieren, Abfrage- und Parameter-Konstanten für refactoringsichere Aufrufe definieren, typsichere `TypedQuery<T>`-Ausführungen verwenden, skalare Aggregatabfragen durchführen, Named Queries extern in `orm.xml` definieren und die Fehlerbehandlung bei ungültigen Query-Namen (`IllegalArgumentException`) verstehen.

## Ausgangszustand

In `test/net/rentacar/TestNamedQuery.java` sind die Testmethoden noch unvollständig.

## Aufgabe

Bearbeite `test/net/rentacar/TestNamedQuery.java`:

1. **Einfache Named Query aufrufen (`testCallNamedQuery`):**
   - Erzeuge die Query mit `manager.createNamedQuery(Reservation.FIND_BY_START_Shop)`.
   - Binde den Parameter `Reservation.PARAM_Shop` an die `Shop`-Instanz `muenchen` und prüfe die 2 Treffer.

2. **Typsichere Named Query (`testTypedNamedQuery`):**
   - Führe die Abfrage typsicher mit `manager.createNamedQuery(Reservation.FIND_BY_START_Shop, Reservation.class)` aus.

3. **Named Query mit mehreren Parametern (`testNamedQueryWithMultipleParameters`):**
   - Rufe `Reservation.FIND_BY_START_SHOP_AND_MIN_PRICE` mit `PARAM_Shop = muenchen` und `PARAM_MIN_PRICE = 200.0f` auf.
   - Prüfe die gefundene Reservierung mit Preis `440.0`.

4. **Skalare Zählabfrage via Named Query (`testNamedQueryScalarCount`):**
   - Führe `Reservation.COUNT_BY_START_SHOP` mit Rückgabetyp `Long.class` aus.

5. **Externe Named Query aus `orm.xml` (`testNamedQueryDefinedInOrmXml`):**
   - Rufe die in `src/META-INF/orm.xml` deklarierte Named Query `"Shop.findWithNoVehicles"` auf.
   - Prüfe die 2 leeren Standorte (Köln und München).

6. **Fehlerbehandlung bei ungültigem Query-Namen (`testUndefinedNamedQueryThrowsException`):**
   - Prüfe mit `assertThrows`, dass ein Aufruf von `manager.createNamedQuery("Reservation.nonExistentQuery")` eine `IllegalArgumentException` auslöst.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_11_JPQL_NamedQueries -am test
```

**Beobachtung beim Start:**
- Beachte, dass Named Queries bereits beim Hochfahren der `EntityManagerFactory` validiert und vorkompiliert werden. Syntaxfehler in JPQL werden somit direkt beim Anwendungsstart erkannt.

## Erfolgskriterium

Der Test `test/net/rentacar/TestNamedQuery.java` läuft mit allen 6 Testmethoden erfolgreich durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Welchen Vorteil bietet die Validierung von `@NamedQuery` beim Start der `EntityManagerFactory` gegenüber dynamischen Queries via `manager.createQuery()`?
2. Warum empfiehlt es sich, die Query-Namen und Parameternamen als `public static final String`-Konstanten in der jeweiligen Entity-Klasse zu bündeln?
3. Wann ist die Definition von Named Queries in `orm.xml` gegenüber Code-Annotationen vorzuziehen?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_11_JPQL_NamedQueries_Loesung`.
