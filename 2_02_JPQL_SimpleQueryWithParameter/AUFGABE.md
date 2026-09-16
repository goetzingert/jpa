# Übung: 2_02 JPQL-Abfragen mit Parametern

## Lernziel

Benannte (`:param`) und positionsbezogene (`?1`) Parameter in JPQL einsetzen, SQL-Injection-Sicherheit verstehen, logische Verknüpfungen (AND), String-Mustervergleiche mit `LIKE`, Entity-Parameter sowie den Umgang mit `NULL`-Werten beherrschen.

## Ausgangszustand

In `test/net/rentacar/TestQuery.java` sind die Testmethoden noch unvollständig (`TODO`).

## Aufgabe

Bearbeite die Testmethoden in `test/net/rentacar/TestQuery.java`:

1. **Benannte Parameter (`testQueryForVehicleTypesMoreThan130HPWithNamedParameter`):**
   - Verwende `:hp` im JPQL-String: `SELECT f FROM VehicleType f WHERE f.hp > :hp`.
   - Setze den Wert mit `.setParameter("hp", 130)`.

2. **Positionsbezogene Parameter (`testQueryWithPositionalParameter`):**
   - Verwende den 1-basierten JPA-Standard-Parameter `?1`: `SELECT f FROM VehicleType f WHERE f.hp > ?1`.
   - Binde den Wert mit `.setParameter(1, 130)`.

3. **Mehrere Parameter & logisches AND (`testQueryWithMultipleNamedParametersAndLogicalAnd`):**
   - Verknüpfe zwei Parameter: `WHERE f.hp >= :minHp AND f.model.brand = :brand`.
   - Setze `minHp = 120` und `brand = "VW"` und prüfe das Ergebnis.

4. **Case-insensitiver Mustervergleich mit `LIKE` (`testQueryWithCaseInsensitiveLikePattern`):**
   - Nutze `LOWER(f.model.modell) LIKE LOWER(:pattern)` mit Wildcard `%ol%` für Teilstring-Suchen.

5. **Entity-Objekte als Parameter (`testQueryWithEntityAsParameter`):**
   - Übergebe eine geladene Entity-Instanz direkt als Parameter (`WHERE v.type = :type`).
   - Beobachte, dass JPA im generierten SQL automatisch den Primärschlüssel vergleicht.

6. **Randfall: NULL-Werte und 3-wertige SQL-Logik (`testQueryNullParameterDemonstratesIsNullRequirement`):**
   - Prüfe, warum `:brand = null` mit `WHERE f.model.brand = :brand` 0 Treffer liefert (`NULL = NULL` ist in SQL unbestimmt/FALSE).
   - Vergleiche dies mit der expliziten Syntax `WHERE f.model.brand IS NOT NULL`.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_02_JPQL_SimpleQueryWithParameter -am test
```

## Erfolgskriterium

Der Test läuft mit 6 erfolgreichen Testfällen durch:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum dürfen Parameter niemals durch String-Konkatenation (`"WHERE f.hp > " + ps`) in JPQL eingebaut werden (SQL/JPQL-Injection, Statement-Caching)?
2. Welche Vorteile bieten benannte Parameter (`:ps`) gegenüber Positions-Parametern (`?1`) bei vielen Parametern in komplexen Abfragen?
3. Warum kann man in SQL/JPQL nicht `WHERE f.location = NULL` schreiben, sondern muss `IS NULL` verwenden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_02_JPQL_SimpleQueryWithParameter_Loesung`.
