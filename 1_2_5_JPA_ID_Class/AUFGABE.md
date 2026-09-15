# Übung: 1_2_5 Zusammengesetzter Schlüssel mit IdClass

## Lernziel

Zusammengesetzte Primärschlüssel (*Composite Keys*) mit `@IdClass` abbilden, Anforderungen an die Schlüsselklasse (Serializable, parameterloser Konstruktor, `equals`/`hashCode`) erfüllen und Entities anhand einer Schlüsselinstanz mit `manager.find(...)` laden.

## Ausgangszustand

In `src/net/rentacar/model/Kunde.java` fehlen `@Entity`, `@IdClass` sowie die `@Id`-Annotationen auf den zusammengesetzten Schlüsselfeldern. In `test/net/rentacar/TestConnection.java` übergibt `testFind()` bisher `null` als ID-Parameter.

## Aufgabe

Bearbeite `Kunde.java` und `TestConnection.java`:

1. **Schlüsselklasse `Nutzer` analysieren:**
   Prüfe `src/net/rentacar/model/Nutzer.java`: Die Klasse besitzt `firstName` und `lastName`, implementiert `Serializable` sowie korrekte `equals`/`hashCode`-Methoden.

2. **Entity `Kunde` konfigurieren:**
   - Annotiere `Kunde` mit `@Entity` und `@IdClass(Nutzer.class)`.
   - Markiere beide Felder `firstName` und `lastName` jeweils mit `@Id`.
   - Biete Getter/Setter für `kundennummer` an.

3. **Test anpassen:**
   Ersetze in `test/net/rentacar/TestConnection.java` in `testFind()` das `null` durch eine konkrete Schlüsselinstanz:
   ```java
   assertNotNull(super.manager.find(Kunde.class, new Nutzer("a", "b")));
   ```

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 1_2_5_JPA_ID_Class -am test
```

**Beobachtung im SQL-Log und Derby-Schema:**
- Prüfe die DDL-Erstellung: Der generierte Primary Key von `Kunde` (`PRIMARY KEY (firstname, lastname)`) besteht aus beiden Spalten.
- Beobachte das generierte `SELECT`-Statement bei `find(...)`: Es enthält in der `WHERE`-Klausel beide Schlüsselkomponenten.

## Erfolgskriterium

Der Test `test/net/rentacar/TestConnection.java` (`testFind`) läuft ohne Fehler durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum müssen die Feldnamen in der Schlüsselklasse exakt mit den `@Id`-Feldnamen in der Entity übereinstimmen?
2. Was unterscheidet `@IdClass` konzeptionell von `@EmbeddedId`?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `1_2_5_JPA_ID_Class_Loesung`.
