# Übung: 1_2_6 Zusammengesetzter Schlüssel mit @EmbeddedId

## Lernziel

Zusammengesetzte Primärschlüssel (*Composite Keys*) mit `@EmbeddedId` und `@Embeddable` abbilden. Die Unterschiede zu `@IdClass` (objektorientierte Kapselung vs. flache Attribute) verstehen und Entities über die eingebettete Schlüsselinstanz verwalten.

## Ausgangszustand

In `BookingId.java` fehlt `@Embeddable`. In `Booking.java` fehlen `@Entity` und `@EmbeddedId`. Der Test `TestEmbeddedId.java` kann die Entity noch nicht laden.

## Aufgabe

1. **Schlüsselklasse `BookingId` konfigurieren:**
   - Annotiere `BookingId` mit `@Embeddable`.
   - Stelle sicher, dass `Serializable` implementiert ist und `equals`/`hashCode` definiert sind.

2. **Entity `Booking` konfigurieren:**
   - Annotiere `Booking` mit `@Entity`.
   - Markiere das Attribut `private BookingId id;` mit `@EmbeddedId`.

3. **Test ausführen:**
   - Führe den Test `TestEmbeddedId` aus und beobachte die DDL-Generierung (Primary Key auf beiden Spalten `contractNumber` und `branchCode`).

## Test und Beobachtung

```bash
./mvnw -pl 1_2_6_JPA_ID_EmbeddedId -am test
```

## Erfolgskriterium

Der Test läuft fehlerfrei durch:
```text
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum wird `@EmbeddedId` in Folie 50 als objektorientierter und wartbarer gegenüber `@IdClass` eingestuft?
2. Wie unterscheidet sich eine JPQL-Abfrage auf den Schlüssel bei `@EmbeddedId` (`b.id.contractNumber`) von `@IdClass` (`b.contractNumber`)?

## Lösungshinweis

Vergleiche deine Lösung mit `1_2_6_JPA_ID_EmbeddedId_Loesung`.
