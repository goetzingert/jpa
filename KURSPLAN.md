# JPA-Kursplan

Der Kurs verwendet ein durchgängiges Rent-a-Car-Modell. Jede Übung besteht aus
einem Aufgabenmodul und, sofern vorhanden, einem gleich aufgebauten Lösungsmodul.
Die Lösung wird erst nach einem eigenen Testlauf geöffnet.

## Voraussetzungen

- Java 17 oder neuer
- Maven über `./mvnw`
- laufender Derby Network Server auf `localhost:1527`
- Datenbank `rentacar`, Beuser `APP`, Passwort `APP`
- eigener Datenbankstand pro Teilnehmer oder Kursgruppe

Integrationstests werden mit dem externen Derby-Server ausgeführt, damit Tabellen,
Sequenzen und Daten während der Übungen beobachtet werden können.

## Arbeitsrhythmus

Für jede Übung gilt derselbe Ablauf:

1. Lernziel und Ausgangszustand lesen.
2. Test zuerst ausführen und die Ausgangslage beobachten.
3. Nur die in der Aufgabe genannten Dateien ändern.
4. Test erneut ausführen.
5. SQL-Log und Datenbankstruktur prüfen.
6. Erst danach die Lösung vergleichen.

Standardbefehle:

```bash
./mvnw -pl <modul> -am test
./mvnw -Peclipselink -pl <modul> -am test
```

## Tag 1: Grundlagen und Mapping

**Ziel:** Entwicklungsumgebung verifizieren, eine JPA-Anwendung starten, Entities speichern und Beziehungen sowie
Identitätsstrategien lesen und erklären können.

| Reihenfolge | Module | Lernziel |
| --- | --- | --- |
| 0 | `1_0_Basic_Project` | **Vorbereitung:** Setup-Check (Java, Maven, Derby), Domäne kennenlernen & Testbasis verstehen |
| 1 | `1_1_JPA_DB_CONFIG` | `persistence.xml`, Provider, JDBC und Schema-Generierung konfigurieren |
| 2 | `1_2_1_JPA_FirstEntity` | Entity, Primärschlüssel, Konstruktor und `find` |
| 3 | `1_2_2_JPA_ID_AUTO` | `GenerationType.AUTO` und Providerentscheidung |
| 4 | `1_2_3_JPA_ID_TABLE` | Table-Generator und zusätzliche Generatortabelle |
| 5 | `1_2_4_JPA_ID_SEQ` | Sequence-Generator und Datenbankabhängigkeit |
| 6 | `1_2_5_JPA_ID_Class` | zusammengesetzte Schlüssel mit `@IdClass` |
| 7 | `1_3_JPA_TableAndColumnOverride` | Tabellen- und Spaltennamen über Annotationen/XML beeinflussen |
| 8 | `1_4_JPA_Embedded` | Wertobjekte mit `@Embeddable` und `@Embedded` |

**Checkpoint:** Eine Entity speichern, nach `flush` wiederfinden und erklären,
welche SQL-Anweisungen sowie Tabellen dabei entstehen.

## Tag 2: Beziehungen und Vererbung

**Ziel:** Ownership, Kardinalitäten, Cascades und Vererbungsstrategien bewusst
auswählen können.

| Reihenfolge | Module | Lernziel |
| --- | --- | --- |
| 1 | `1_5_JPA_OneToOne` | unidirektionale und bidirektionale 1:1-Beziehung |
| 2 | `1_6_JPA_OneToMany` | Collection-Mapping und Persistenz einer Eltern-Kind-Struktur |
| 3 | `1_7_JPA_ManyToOne` | owning side und `mappedBy` |
| 4 | `1_8_JPA_ManyToMany` | Join-Tabelle und Pflege beider Seiten |
| 5 | `1_9_1_JPA_SingleTable` | Vererbung mit einer gemeinsamen Tabelle |
| 6 | `1_9_2_JPA_TablePerConcrete` | getrennte Tabellen konkreter Klassen |
| 7 | `1_9_3_JPA_Joined` | normalisierte Tabellen mit Join-Vererbung |
| 8 | `1_9_4_JPA_Superclass` | gemeinsame Mapping-Basisklasse ohne Entity-Tabelle |

**Checkpoint:** Für dieselbe Domänenbeziehung die owning side, den erwarteten
Fremdschlüssel und das Verhalten von `cascade` erklären.

## Tag 3: Abfragen und Lifecycle

**Ziel:** Daten mit JPQL und Criteria API abfragen und Provider-/Lifecycle-
Verhalten anhand von SQL-Ausgaben beurteilen können.

| Reihenfolge | Module | Lernziel |
| --- | --- | --- |
| 1 | `2_00_Model4QueryProjects` | gemeinsames Abfragemodell verstehen |
| 2 | `2_01` bis `2_04` | einfache JPQL-Abfragen, Parameter, Paging und Where-Klauseln |
| 3 | `2_05` bis `2_06` | Entity-Mapping in Ergebnissen und Joins |
| 4 | `2_07_JPQL_FetchType` | LAZY/EAGER, Fetch Join und N+1-Problem |
| 5 | `2_08_JPQL_GroupBy` | Aggregation, Group By und Having |
| 6 | `2_08_01_JPA_TransactionLifecycle` | Entity-Zustände, Persistence Context, Flush und Clear |
| 7 | `2_08_02_JPA_OptimisticLocking` | Versionierung, paralleler Zugriff und Optimistic Locking |
| 8 | `2_08_03_JPA_Validation` | Bean Validation und fachliche Regeln |
| 9 | `2_08_04_JPA_EqualsHashCode` | Entity-Gleichheit in Sets und Hash-Konsistenz |
| 10 | `2_09_JPQL_SubQueries` | Subqueries und abhängige Aggregation |
| 11 | `2_10_JPQL_NativeQueries` | bewusster Wechsel zu SQL und seine Nachteile |
| 12 | `2_11_JPQL_NamedQueries` | wiederverwendbare, benannte Abfragen |
| 13 | `2_12` bis `2_14` | Callbacks, Entity Listener und Default Listener |
| 14 | `2_15_CriteriaAPI_Simple` | typsichere dynamische Abfragen |

**Checkpoint:** Eine Abfrage als JPQL, Native Query und Criteria Query vergleichen
und die Unterschiede in Portabilität, Lesbarkeit und Kontrolle benennen.

### Schulungsüberblick für die neuen Fachthemen

- **Lifecycle/Transaktion:** Erst Zustände am Whiteboard sortieren, dann `contains`,
  `persist`, `flush`, `clear` und `find` einzeln im SQL-Log beobachten.
- **Cascade/Orphan Removal:** Mit einer Eltern-Kind-Beziehung starten und jeweils
  genau eine Cascade-Option ändern. Die Teilnehmer formulieren vorab, welches SQL
  entstehen muss.
- **Optimistic Locking:** Zwei `EntityManager` laden dieselbe Entity. Ein Teilnehmer
  speichert zuerst, der zweite provoziert die Konfliktexception.
- **Bulk-Updates:** JPQL-Update ausführen, den Persistence Context bewusst nicht
  leeren und den Unterschied zwischen In-Memory- und Datenbankzustand erklären.
- **Bean Validation:** Gültige und ungültige Entities gegenüberstellen und die
  `ConstraintViolationException` als fachliches Ergebnis testen.

## Einheitliches Aufgabenformat

Jede Übung sollte künftig diese Abschnitte enthalten:

- **Lernziel:** genau ein fachlicher Schwerpunkt
- **Ausgangszustand:** was ist bereits vorhanden, was fehlt?
- **Aufgabe:** konkrete Dateien und erwartete Änderung
- **Beobachtung:** welches SQL- oder Datenbankverhalten soll geprüft werden?
- **Erfolgskriterium:** konkreter Testname und erwartetes Ergebnis
- **Reflexion:** zwei kurze Fragen zum Warum, nicht nur zum Was
- **Lösungshinweis:** nur nach dem eigenen Versuch öffnen

## Begleitende Kursmaterialien & Nachschlagewerke

- **Setup & Domänenmodell:** [1_0_Basic_Project/README.md](1_0_Basic_Project/README.md)
- **JPA-Spickzettel (Cheat Sheet):** [JPA_CHEAT_SHEET.md](JPA_CHEAT_SHEET.md) (Kompakte Syntax- & Annotationsreferenz für Entities, JPQL, Criteria API und EntityManager)
- **Troubleshooting & Best Practices:** [TROUBLESHOOTING_UND_BEST_PRACTICES.md](TROUBLESHOOTING_UND_BEST_PRACTICES.md) (Lösungsstrategien für N+1, LazyLoading, Locking, Validation und Cache-Verhalten)
- **Folien-Präsentationen (PPTX):**
  - `JPA_Cheat_Sheet.pptx` (17 Folien im 16:9-Format als visuelle Syntax-, Annotations- & Lifecycle-Referenz)
  - `JPA_Troubleshooting_und_Best_Practices.pptx` (17 Folien im 16:9-Format für Seminarvorträge und Troubleshooting-Sessions)

## Didaktische Leitplanken

- Providerunabhängige JPA-Konzepte zuerst, Hibernate-/EclipseLink-Details danach.
- `cascade` nicht pauschal vorgeben; Ursache und Lebenszyklus erklären lassen.
- Bei jeder Beziehung owning side und `mappedBy` ausdrücklich benennen.
- Bei jeder Query-Aufgabe einen erwarteten Datensatz und ein erwartetes Ergebnis nennen.
- Hibernate-spezifische APIs wie `Session` oder Statistics in eigene Vertiefungen auslagern.
- `create-drop` beziehungsweise `drop-and-create` als destruktive Kursdatenbank-
  Einstellung markieren.