# JPA Praxisseminar: Jakarta Persistence & Hibernate

Herzlich willkommen zum praktischen Schulungsprojekt für **Jakarta Persistence API (JPA)** und **Hibernate**. 

Dieses Repository enthält ein vollständiges, modular aufgebautes Schulungscurriculum anhand der durchgängigen Fachdomäne einer Autovermietung (*Rent-a-Car*). Jedes Thema ist in praxisnahe, isolierte Maven-Teilprojekte mit konkreten Aufgabenstellungen (`AUFGABE.md`) und korrespondierenden Referenzlösungen (`_Loesung`) unterteilt.

---

## 1. Schnellstart & Voraussetzungen

### Systemvoraussetzungen
- **Java:** OpenJDK / Oracle JDK 17 oder neuer
- **Build-Tool:** Apache Maven (über den enthaltenen Maven Wrapper `./mvnw`)
- **Datenbank:** Apache Derby Network Server

### Schritt-für-Schritt-Inbetriebnahme

1. **Repository & Java-Version prüfen:**
   ```bash
   ./mvnw -version
   ```

2. **Apache Derby Network Server starten:**
   Starten Sie den Derby Network Server in einem separaten Terminalfenster:
   - **Host & Port:** `localhost:1527`
   - **Datenbank:** `rentacar`
   - **User / Passwort:** `APP` / `APP`
   - **JDBC-URL:** `jdbc:derby://localhost:1527/rentacar;create=true`

3. **Verbindung & Basis-Setup validieren:**
   Führen Sie den Test des ersten Lösungsmoduls aus:
   ```bash
   ./mvnw -pl 1_1_JPA_DB_CONFIG_Loesung -am test
   ```

---

## 2. Kursaufbau & Modulübersicht

Das Seminar ist didaktisch in 4 Schwerpunkte unterteilt:

### Tag 1: Grundlagen, Konfiguration & Basis-Mappings
Fokus: Persistenzkontext, Schema-Generierung, ID-Generierungsstrategien und Value Objects.

| Modul | Thema & Lernziel |
| :--- | :--- |
| `1_0_Basic_Project` | **Vorbereitung:** Setup-Check, Domänenmodell & Test-Basisklasse `AbstractJPATestCase` |
| `1_1_JPA_DB_CONFIG` | `persistence.xml`, JDBC-Treiber, Schema-Generierung (`drop-and-create`) |
| `1_2_1_JPA_FirstEntity` | Erste Entity `@Entity`, Primärschlüssel `@Id`, Default-Konstruktor & `find()` |
| `1_2_2_JPA_ID_AUTO` | ID-Strategie `GenerationType.AUTO` und Provider-Auswahl |
| `1_2_3_JPA_ID_TABLE` | Tabellengestützte Sequenzen mit `@TableGenerator` |
| `1_2_4_JPA_ID_SEQ` | Datenbank-Sequenzen mit `@SequenceGenerator` |
| `1_2_5_JPA_ID_Class` | Zusammengesetzte Primärschlüssel mit `@IdClass` |
| `1_2_6_JPA_ID_EmbeddedId` | Zusammengesetzte Primärschlüssel mit `@EmbeddedId` und `@Embeddable` |
| `1_3_JPA_TableAndColumnOverride` | Tabellen- und Spaltenmapping mit `@Table`, `@Column` und XML-Overrides |
| `1_3_1_JPA_AttributeConverter` | Transparente Typkonvertierung mit `@Converter` & `AttributeConverter` |
| `1_4_JPA_Embedded` | Value Objects mit `@Embeddable`, `@Embedded` und `@AttributeOverride` |

### Tag 2: Assoziationen & Vererbungsstrategien
Fokus: Beziehungs-Ownership (`mappedBy`), Kaskadierung (`cascade`), Join-Tabellen und relationale Vererbung.

| Modul | Thema & Lernziel |
| :--- | :--- |
| `1_5_JPA_OneToOne` | 1:1-Beziehungen (uni- und bidirektional, `CascadeType.PERSIST`) |
| `1_6_JPA_OneToMany` | 1:N-Collection-Mappings und Fremdschlüsselabbildung |
| `1_6_1_JPA_CascadeOrphanRemoval` | Eltern-Kind-Lifecycle: `CascadeType` vs. `orphanRemoval = true` |
| `1_7_JPA_ManyToOne` | N:1-Beziehungen, Fremdschlüssel-Ownership und defensive Synchronisation |
| `1_8_JPA_ManyToMany` | N:M-Beziehungen mit Join-Tabelle (`@JoinTable`) und beidseitiger Pflege |
| `1_9_1_JPA_SingleTable` | Vererbung: `SINGLE_TABLE` mit Diskriminatorspalte (`@DiscriminatorColumn`) |
| `1_9_2_JPA_TablePerConcrete` | Vererbung: `TABLE_PER_CLASS` mit separaten Tabellen pro konkreter Klasse |
| `1_9_3_JPA_Joined` | Vererbung: `JOINED` mit normalisierten Tabellen und Primärschlüssel-Joins |
| `1_9_4_JPA_Superclass` | Gemeinsame Basisfunktionalität mit `@MappedSuperclass` ohne eigene Tabelle |

### Tag 3 & 4: JPQL, Abfragen, Entity-Lifecycle & Fortgeschrittene Konzepte
Fokus: Typsichere Abfragen, Performance (N+1-Problem), Transaktionen, Locking, Callbacks und Criteria API.

| Modul | Thema & Lernziel |
| :--- | :--- |
| `2_00_Model4QueryProjects` | Zentrales Testdatenmodell für alle Abfrageprojekte |
| `2_01_JPQL_SimpleQuery` | Grundlegende JPQL-Abfragen (`SELECT`, `createQuery`, `getResultList`) |
| `2_02_JPQL_SimpleQueryWithParameter` | Parameter-Binding (benannte `:param` und positionale `?1` Parameter) |
| `2_03_JPQL_SimpleQueryPaging` | Pagination mit `setFirstResult()` und `setMaxResults()` |
| `2_04_JPQL_WhereClause` | Bedingungen (`WHERE`, `LIKE`, `IN`, `BETWEEN`, Operatoren) |
| `2_05_JPQL_MappingInQuery` | DTO-Constructor-Expressions (`SELECT NEW com.example.DTO(...)`) |
| `2_06_JPQL_InnerJoins` | Explizite Joins (`INNER JOIN`, `LEFT JOIN`) in JPQL |
| `2_07_JPQL_FetchType` | `FetchType.LAZY` vs. `EAGER`, N+1-Problem, `JOIN FETCH` & Entity Graphs |
| `2_08_01_JPA_TransactionLifecycle` | Entity-Zustände (New, Managed, Detached, Removed), `flush()` und `clear()` |
| `2_08_02_JPA_OptimisticLocking` | Optimistisches Locking mit `@Version` gegen Lost Updates |
| `2_08_03_JPA_Validation` | Declarative Bean Validation (`@NotNull`, `@Size`, `@Min`, `@Pattern`) |
| `2_08_04_JPA_EqualsHashCode` | Konsistente `equals()` und `hashCode()` Implementierung für Entities in Sets |
| `2_08_05_JPA_BulkOperations` | Bulk-Updates (`executeUpdate()`) und Cache-Invalidierung (`clear()`) |
| `2_08_06_JPA_PessimisticLocking` | Pessimistisches Sperren mit `PESSIMISTIC_WRITE` (`FOR UPDATE`) |
| `2_08_JPQL_GroupBy` | Aggregatfunktionen (`COUNT`, `AVG`, `SUM`, `MAX`) und `GROUP BY` / `HAVING` |
| `2_09_JPQL_SubQueries` | Verschachtelte Unterabfragen (Subqueries) in JPQL |
| `2_10_JPQL_NativeQueries` | Native SQL-Abfragen mit `createNativeQuery()` und ResultSet-Mapping |
| `2_11_JPQL_NamedQueries` | Vorab kompilierte, benannte Abfragen (`@NamedQuery`) |
| `2_12_JPA_Callbacks` | Lifecycle-Annotations-Callbacks (`@PrePersist`, `@PostLoad` etc.) |
| `2_13_JPA_EntityListener` | Auslagerung von Lifecycle-Logik in dedizierte Listener (`@EntityListeners`) |
| `2_14_JPA_DefaultEntityListener` | Globale Default-Listener über `orm.xml` für alle Entities |
| `2_15_CriteriaAPI_Simple` | Programmatische, typsichere Abfrageerstellung mit `CriteriaBuilder` |

---

## 3. Testausführung & Multi-Provider-Unterstützung

Das Projekt unterstützt standardmäßig den Referenz-Provider **Hibernate** sowie das Profil für **EclipseLink**.

### Einzelnes Modul testen
```bash
# Aufgabe testen
./mvnw -pl 1_2_1_JPA_FirstEntity -am test

# Lösung testen
./mvnw -pl 1_2_1_JPA_FirstEntity_Loesung -am test
```

### Alle Module mit Hibernate testen (Standard)
```bash
./mvnw test
```

### Alle Module mit EclipseLink testen (Multi-Provider-Profil)
```bash
./mvnw -Peclipselink test
```

### Schema und Daten nach dem Testlauf inspizieren (`-Pinspect`)

Im Normalbetrieb ist die Datenbank nach einem Testlauf leer: Die Schemagenerierung läuft mit
`drop-and-create` und jeder Test endet mit einem Rollback. Wer die erzeugten Tabellen, Fremdschlüssel
und Datensätze anschließend im Derby-Client oder im Datenbankfenster der IDE ansehen möchte,
verwendet das Profil `inspect`:

```bash
./mvnw -Pinspect -pl 1_6_JPA_OneToMany_Loesung -am test
```

In diesem Modus gilt:

- Die Schemagenerierung läuft mit `create` statt `drop-and-create`, das Schema bleibt also erhalten.
- Jeder Test **committet** statt zurückzurollen; die Testdaten stehen danach in der Datenbank.
- Vor jedem Test werden nur die Datensätze gelöscht (nicht die Tabellen), damit die Tests trotzdem
  unabhängig voneinander bleiben. In der Datenbank steht am Ende der Stand des zuletzt gelaufenen Tests.
- Beim Start eines Laufs wird das Schema wie gewohnt vollständig neu aufgebaut — ein Inspektionslauf
  ist also wiederholbar und hinterlässt keine Altlasten aus anderen Modulen.

> **Hinweis:** `-Pinspect` ist ein Werkzeug zum Nachsehen, kein alternativer Testmodus. Für den
> normalen Übungsablauf bleibt `./mvnw -pl <modul> -am test` die richtige Variante.

---

## 4. Begleitende Kursmaterialien & Nachschlagewerke

Für Trainer und Teilnehmer stehen umfassende Nachschlagewerke und Foliensätze bereit:

- **[KURSPLAN.md](KURSPLAN.md):** Detaillierter Ablaufplan, Zeitraster, didaktische Leitlinien und Modul-Zuordnungsmatrix.
- **[JPA_CHEAT_SHEET.md](JPA_CHEAT_SHEET.md):** Kompaktes Syntax- und Annotations-Nachschlagewerk (Mappings, JPQL, Lifecycle, Criteria API).
- **[TROUBLESHOOTING_UND_BEST_PRACTICES.md](TROUBLESHOOTING_UND_BEST_PRACTICES.md):** Umfassender Praxis-Guide zu typischen Fehlern (`LazyInitializationException`, N+1-Problem, Optimistic Locking, Derby-Locks, equals/hashCode).
- **[QUIZ_UND_REFLEXIONS_LOESUNGEN.md](QUIZ_UND_REFLEXIONS_LOESUNGEN.md):** Vollständige Musterlösungen zu allen Reflexionsfragen der Übungen und Folien-Quizzes.
- **[VERBESSERUNGSIDEEN.md](VERBESSERUNGSIDEEN.md):** Strukturierte Ideen, Modernisierungspotenziale (JPA 3.1 / Hibernate 6) und Roadmap für zukünftige Übungserweiterungen.
- **PowerPoint-Präsentationen (16:9 Format):**
  - `JPA_Cheat_Sheet.pptx` (23 Folien als visuelle Syntax- & Lifecycle-Referenz)
  - `JPA_Troubleshooting_und_Best_Practices.pptx` (22 Folien für Vorträge und Troubleshooting-Sessions)

---

## 5. Didaktischer Übungsablauf für Teilnehmer

Jedes Aufgabenmodul folgt einem festen Ablauf:

1. **`AUFGABE.md` lesen:** Lernziel, Ausgangszustand und Anforderungen erfassen.
2. **Initialen Test ausführen:** Ausgangszustand prüfen (Test schlägt ggf. erwartungsgemäß fehl oder Daten fehlen).
3. **Entity- / Mapping-Code implementieren:** Nur die in der Aufgabe genannten Klassen bzw. XML-Dateien anpassen.
4. **Test wiederholen & SQL-Logs beobachten:** Erzeugte DDL- und DML-Statements in der Konsole prüfen.
5. **Erfolgskriterium & Reflexion:** Sicherstellen, dass alle Tests grün sind und die Reflexionsfragen beantworten können.
6. **Lösungsvergleich:** Erst nach erfolgreicher eigener Implementierung mit dem `_Loesung`-Modul abgleichen.
