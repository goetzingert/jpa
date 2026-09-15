# JPA-Kursplan

Der Kurs verwendet ein durchgängiges Rent-a-Car-Modell. Jede Übung besteht aus
einem Aufgabenmodul und, sofern vorhanden, einem gleich aufgebauten Lösungsmodul.
Die Lösung wird erst nach einem eigenen Testlauf geöffnet.

## Voraussetzungen

- Java 17 oder neuer
- Maven über `./mvnw`
- laufender Derby Network Server auf `localhost:1527`
- Datenbank `rentacar`, Benutzer `APP`, Passwort `APP`
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
| 7 | `1_2_6_JPA_ID_EmbeddedId` | zusammengesetzte Schlüssel mit `@EmbeddedId` und `@Embeddable` |
| 8 | `1_3_JPA_TableAndColumnOverride` | Tabellen- und Spaltennamen über Annotationen/XML beeinflussen |
| 9 | `1_3_1_JPA_AttributeConverter` | transparente Typkonvertierung mit `@Converter` & `AttributeConverter` |
| 10 | `1_4_JPA_Embedded` | Wertobjekte mit `@Embeddable` und `@Embedded` |

**Checkpoint:** Eine Entity speichern, nach `flush` wiederfinden und erklären,
welche SQL-Anweisungen sowie Tabellen dabei entstehen.

## Tag 2: Beziehungen und Vererbung

**Ziel:** Ownership, Kardinalitäten, Cascades und Vererbungsstrategien bewusst
auswählen können.

| Reihenfolge | Module | Lernziel |
| --- | --- | --- |
| 1 | `1_5_JPA_OneToOne` | unidirektionale und bidirektionale 1:1-Beziehung |
| 2 | `1_6_JPA_OneToMany` | Collection-Mapping und Persistenz einer Eltern-Kind-Struktur |
| 3 | `1_6_1_JPA_CascadeOrphanRemoval` | `CascadeType.PERSIST`/`REMOVE` vs. `orphanRemoval = true` |
| 4 | `1_7_JPA_ManyToOne` | owning side und `mappedBy` |
| 5 | `1_8_JPA_ManyToMany` | Join-Tabelle und Pflege beider Seiten |
| 6 | `1_9_1_JPA_SingleTable` | Vererbung mit einer gemeinsamen Tabelle |
| 7 | `1_9_2_JPA_TablePerConcrete` | getrennte Tabellen konkreter Klassen |
| 8 | `1_9_3_JPA_Joined` | normalisierte Tabellen mit Join-Vererbung |
| 9 | `1_9_4_JPA_Superclass` | gemeinsame Mapping-Basisklasse ohne Entity-Tabelle |

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
| 4 | `2_07_JPQL_FetchType` | LAZY/EAGER, Fetch Join und Entity Graphs (N+1-Problem) |
| 5 | `2_08_JPQL_GroupBy` | Aggregation, Group By und Having |
| 6 | `2_08_01_JPA_TransactionLifecycle` | Entity-Zustände, Persistence Context, Flush und Clear |
| 7 | `2_08_02_JPA_OptimisticLocking` | Versionierung, paralleler Zugriff und Optimistic Locking |
| 8 | `2_08_03_JPA_Validation` | Bean Validation und fachliche Regeln |
| 9 | `2_08_04_JPA_EqualsHashCode` | Entity-Gleichheit in Sets und Hash-Konsistenz |
| 10 | `2_08_05_JPA_BulkOperations` | Bulk-Updates (`executeUpdate`) & Cache-Synchronisation (`clear`) |
| 11 | `2_08_06_JPA_PessimisticLocking` | pessimistisches Sperren (`PESSIMISTIC_WRITE` / `FOR UPDATE`) |
| 12 | `2_09_JPQL_SubQueries` | Subqueries und abhängige Aggregation |
| 13 | `2_10_JPQL_NativeQueries` | bewusster Wechsel zu SQL und seine Nachteile |
| 14 | `2_11_JPQL_NamedQueries` | wiederverwendbare, benannte Abfragen |
| 15 | `2_12` bis `2_14` | Callbacks, Entity Listener und Default Listener |
| 16 | `2_15_CriteriaAPI_Simple` | typsichere dynamische Abfragen |

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
- **Quiz- & Reflexionslösungen:** [QUIZ_UND_REFLEXIONS_LOESUNGEN.md](QUIZ_UND_REFLEXIONS_LOESUNGEN.md) (Musterlösungen zu allen Modul-Reflexionsfragen und Folien-Quizzes)
- **Folien-Präsentationen (PPTX):**
  - `JPA_Cheat_Sheet.pptx` (23 Folien im 16:9-Format als visuelle Syntax-, Annotations- & Lifecycle-Referenz)
  - `JPA_Troubleshooting_und_Best_Practices.pptx` (22 Folien im 16:9-Format für Seminarvorträge und Troubleshooting-Sessions)

---

## Modul- und Material-Zuordnungsmatrix

Diese Matrix ordnet jedes Übungsmodul den entsprechenden Folienkapiteln, dem Cheat Sheet und dem Troubleshooting-Guide zu:

| Modul | Schwerpunkt & Lernziel | Folien (`jpa_only.pdf` / PPTX) | Cheat Sheet (Abschnitt) | Troubleshooting Guide |
| :--- | :--- | :--- | :--- | :--- |
| `1_1_JPA_DB_CONFIG` | `persistence.xml`, Schema-Action, Driver | `jpa_only.pdf` Folien 18–33 | 1. `persistence.xml` & DB | DDL-Warnungen beim Start |
| `1_2_1_JPA_FirstEntity` | `@Entity`, `@Id`, Konstruktor, `em.find()` | `jpa_only.pdf` Folien 34–48 | 2.1 Entity Basis-Mapping | Transiente Instanzen |
| `1_2_2_JPA_ID_AUTO` | `GenerationType.AUTO` | `jpa_only.pdf` Folien 49–51 | 2.1 ID-Generierung | ID-Strategien & Batching |
| `1_2_3_JPA_ID_TABLE` | `GenerationType.TABLE` & Generator-Tabelle | `jpa_only.pdf` Folien 52–54 | 2.1 ID-Generierung | Tabellen-Locks & Skalierung |
| `1_2_4_JPA_ID_SEQ` | `GenerationType.SEQUENCE` & AllocationSize | `jpa_only.pdf` Folien 55–58 | 2.1 ID-Generierung | Sequence Allocation Size |
| `1_2_5_JPA_ID_Class` | Zusammengesetzte Keys (`@IdClass`) | `jpa_only.pdf` Folien 59–62 | 2.2 Zusammengesetzte Keys | Composite Key Mappings |
| `1_2_6_JPA_ID_EmbeddedId` | Zusammengesetzte Keys (`@EmbeddedId`) | `jpa_only.pdf` Folie 50 / Troubleshooting Slide 18 | 2.2 `@EmbeddedId` (Cheat Sheet Slide 4) | 2.8 `@EmbeddedId` vs `@IdClass` |
| `1_3_JPA_TableAndColumnOverride` | `@Table`, `@Column` Overrides | `jpa_only.pdf` Folien 63–75 | 2.1 Entity Basis-Mapping | DDL-Generierung & Naming |
| `1_3_1_JPA_AttributeConverter` | Custom Types (`@Converter`) | Troubleshooting Slide 19 | 2.4 `@Converter` (Cheat Sheet Slide 6) | 2.9 Custom Type Mapping |
| `1_4_JPA_Embedded` | Wertobjekte (`@Embeddable`, `@Embedded`) | `jpa_only.pdf` Folien 76–82 | 2.3 Value Objects | Embedded & AttributeOverrides |
| `1_5_JPA_OneToOne` | `@OneToOne` uni-/bidirektional | `jpa_only.pdf` Folien 83–90 | 3.1 1:1 Assoziation | 2.2 Defensive Assoziation |
| `1_6_JPA_OneToMany` | `@OneToMany` Eltern-Kind-Struktur | `jpa_only.pdf` Folien 91–98 | 3.2 1:N Assoziation | 2.1 FetchType-Regeln |
| `1_6_1_JPA_CascadeOrphanRemoval` | `CascadeType` vs. `orphanRemoval = true` | `jpa_only.pdf` Folien 99–102 / Troubleshooting Slide 13 | 3.4 Cascade & Orphan (Cheat Sheet Slide 10) | 2.3 Cascade & Orphan Removal |
| `1_7_JPA_ManyToOne` | `@ManyToOne`, Owning Side, `mappedBy` | `jpa_only.pdf` Folien 103–108 | 3.2 Owning Side | 1.6 TransientPropertyValue |
| `1_8_JPA_ManyToMany` | `@ManyToMany` & Join-Tabelle | `jpa_only.pdf` Folien 109–116 | 3.3 N:M Assoziation | 2.2 Assoziations-Pflege |
| `1_9_1_JPA_SingleTable` | Vererbung: `SINGLE_TABLE` (`DTYPE`) | `jpa_only.pdf` Folien 117–125 | 4.1 Vererbungsstrategien | 1.9 MappedSuperclass vs Entity |
| `1_9_2_JPA_TablePerConcrete` | Vererbung: `TABLE_PER_CLASS` | `jpa_only.pdf` Folien 126–129 | 4.1 Vererbungsstrategien | Vererbung & Polymorphie |
| `1_9_3_JPA_Joined` | Vererbung: `JOINED` | `jpa_only.pdf` Folien 130–134 | 4.1 Vererbungsstrategien | Vererbung & Join Overhead |
| `1_9_4_JPA_Superclass` | `@MappedSuperclass` Basis-Mapping | `jpa_only.pdf` Folien 135–139 | 4.2 `@MappedSuperclass` | 1.9 MappedSuperclass vs Entity |
| `2_00_Model4QueryProjects` | Gemeinsames Query-Domänenmodell | `jpa_only.pdf` Folien 140–143 | 5.1 JPQL Grundlagen | Domänenmodell-Referenz |
| `2_01_JPQL_SimpleQuery` | Einfache JPQL-Abfragen (`SELECT`) | `jpa_only.pdf` Folien 144–149 | 5.1 JPQL Syntax | Query Syntax & Typisierung |
| `2_02_JPQL_SimpleQueryWithParameter` | Named Parameters (`:param`) | `jpa_only.pdf` Folien 150–152 | 5.1 Parameter | SQL-Injection Vermeidung |
| `2_03_JPQL_SimpleQueryPaging` | Paging (`setFirstResult`, `setMaxResults`) | `jpa_only.pdf` Folien 153–155 | 5.1 Paging | Paging & FirstResult |
| `2_04_JPQL_WhereClause` | Where-Filter, `BETWEEN`, `LIKE`, `IN` | `jpa_only.pdf` Folien 156–160 | 5.1 Where-Operatoren | NULL- & Empty-Handling |
| `2_05_JPQL_MappingInQuery` | DTO-Projektion (`SELECT new ...`) | `jpa_only.pdf` Folien 161–164 | 5.2 DTO-Projektionen | 2.7 DTO Constructor Expr. |
| `2_06_JPQL_InnerJoins` | Inner Joins & Navigation | `jpa_only.pdf` Folien 165–170 | 5.1 Joins | Path Expressions & Joins |
| `2_07_JPQL_FetchType` | N+1, `JOIN FETCH`, Entity Graphs | Troubleshooting Slides 4–5 | 5.3 Entity Graphs (Cheat Sheet Slide 14) | 1.2 N+1 Problem & 1.3 Graphs |
| `2_08_JPQL_GroupBy` | Aggregation (`COUNT`, `AVG`, `HAVING`) | `jpa_only.pdf` Folien 171–176 | 5.1 Aggregation | Group By & Having |
| `2_08_01_JPA_TransactionLifecycle` | Entity-Zustände, `flush()`, `clear()` | Troubleshooting Slide 14 | 7. Lifecycle & States (Cheat Sheet Slide 16) | 2.5 Persistence Context |
| `2_08_02_JPA_OptimisticLocking` | `@Version`, OptimisticLockException | Troubleshooting Slide 6 | 8.1 Optimistic Locking (Cheat Sheet Slide 18) | 1.4 OptimisticLockException |
| `2_08_03_JPA_Validation` | Jakarta Bean Validation (`@NotNull`) | Troubleshooting Slide 10 | 6. Bean Validation (Cheat Sheet Slide 15) | 1.7 ConstraintViolation |
| `2_08_04_JPA_EqualsHashCode` | Invarianz in Sets & Hash-Konsistenz | Troubleshooting Slide 15 | 2.1 equals / hashCode (Cheat Sheet Slide 3) | 2.4 equals/hashCode Regeln |
| `2_08_05_JPA_BulkOperations` | `executeUpdate()` & Cache-Invalidierung | Troubleshooting Slide 16 | 5.4 Bulk Operations (Cheat Sheet Slide 15) | 2.6 Bulk Operations & Cache |
| `2_08_06_JPA_PessimisticLocking` | `PESSIMISTIC_WRITE` (`FOR UPDATE`) | Troubleshooting Slide 7 | 8.2 Pessimistic Locking (Cheat Sheet Slide 19) | 1.5 Pessimistisches Locking |
| `2_09_JPQL_SubQueries` | Subqueries (`EXISTS`, `IN`) | `jpa_only.pdf` Folien 177–180 | 5.1 Subqueries | Korrelierte Unterabfragen |
| `2_10_JPQL_NativeQueries` | Native SQL & `createNativeQuery` | `jpa_only.pdf` Folien 181–184 | 5.5 Native SQL | Native Queries & Portabilität |
| `2_11_JPQL_NamedQueries` | `@NamedQuery` & Vorkompilierung | `jpa_only.pdf` Folien 185–188 | 5.1 Named Queries | Query Startup Validation |
| `2_12_JPA_Callbacks` | `@PrePersist`, `@PostLoad` etc. | `jpa_only.pdf` Folien 189–192 | 7.2 Callback Annotations | 3.2 Callback Lifecycle |
| `2_13_JPA_EntityListener` | Dedizierte Listener-Klassen | `jpa_only.pdf` Folien 193–195 | 7.2 Entity Listener | Auditing & Listener |
| `2_14_JPA_DefaultEntityListener` | XML-Default-Listener für alle Entities | `jpa_only.pdf` Folien 196–199 | 7.2 Default Listener | Globales Entity-Auditing |
| `2_15_CriteriaAPI_Simple` | `CriteriaBuilder`, `CriteriaQuery`, `Root` | `jpa_only.pdf` Folien 165–176 | 9. Criteria API | Criteria API & Metamodel |

## Didaktische Leitplanken

- Providerunabhängige JPA-Konzepte zuerst, Hibernate-/EclipseLink-Details danach.
- `cascade` nicht pauschal vorgeben; Ursache und Lebenszyklus erklären lassen.
- Bei jeder Beziehung owning side und `mappedBy` ausdrücklich benennen.
- Bei jeder Query-Aufgabe einen erwarteten Datensatz und ein erwartetes Ergebnis nennen.
- Hibernate-spezifische APIs wie `Session` oder Statistics in eigene Vertiefungen auslagern.
- `create-drop` beziehungsweise `drop-and-create` als destruktive Kursdatenbank-
  Einstellung markieren.