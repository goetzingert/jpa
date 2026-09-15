# Lösungen zu den Reflexionsfragen & Kurs-Quizzes

Dieses Dokument enthält die didaktischen Musterlösungen zu allen Reflexionsfragen aus den `AUFGABE.md`-Dateien der Übungsmodule sowie die Auflösungen zu den Quiz-Folien des Seminars.

---

### Inhaltsübersicht

1. [Tag 1: Grundlagen, Identitätsstrategien & Mapping](#tag-1-grundlagen-identitätsstrategien--mapping)
2. [Tag 2: Assoziationen, Kaskadierung & Vererbung](#tag-2-assoziationen-kaskadierung--vererbung)
3. [Tag 3: JPQL, Locking, Lifecycle, Validation & Criteria API](#tag-3-jpql-locking-lifecycle-validation--criteria-api)
4. [Lösungen zu den Folien-Quizzes (jpa_only.pdf)](#lösungen-zu-den-folien-quizzes-jpa_onlypdf)

---

### Tag 1: Grundlagen, Identitätsstrategien & Mapping

#### Modul `1_1_JPA_DB_CONFIG`
- **Frage 1:** *Welche Konfigurationsparameter gehören zum portablen Jakarta-JPA-Standard (`jakarta.persistence.*`) und welche sind herstellerspezifisch?*
  - **Antwort:**
    - Standard (JPA 2.1+ / 3.x): `jakarta.persistence.jdbc.driver`, `jakarta.persistence.jdbc.url`, `jakarta.persistence.jdbc.user`, `jakarta.persistence.jdbc.password` sowie `jakarta.persistence.schema-generation.database.action` (`none`, `create`, `drop-and-create`, `drop`).
    - Herstellerspezifisch: z. B. `hibernate.show_sql`, `hibernate.format_sql`, `hibernate.dialect`, `hibernate.jdbc.batch_size` bzw. `eclipselink.target-database`, `eclipselink.logging.level`.
- **Frage 2:** *Warum sollte in Produktionsumgebungen `drop-and-create` bzw. `hbm2ddl.auto` deaktiviert sein?*
  - **Antwort:** Automatische DDL-Generierung droppt bei jedem Neustart das Schema oder führt unkontrollierte Tabellenänderungen durch (Datenverlustrisiko). In Produktion gehören Schema-Migrationen in versionierte Migrationswerkzeuge wie Flyway oder Liquibase.

#### Modul `1_2_1_JPA_FirstEntity`
- **Frage 1:** *Warum verlangt die JPA-Spezifikation zwingend einen parameterlosen Konstruktor für Entity-Klassen?*
  - **Antwort:** Der JPA-Provider muss beim Auslesen von Datenbankzeilen via Reflection (`Class.getDeclaredConstructor().newInstance()`) leere Instanzen erzeugen können, bevor er die Attribute per Field- oder Property-Access befüllt.
- **Frage 2:** *Warum ist `access="FIELD"` in modernen Projekten meist dem `access="PROPERTY"` vorzuziehen?*
  - **Antwort:** Field-Access greift direkt auf die Instanzvariablen zu. Getter und Setter können dadurch reine Fachlogik enthalten oder defensive Kopien zurückgeben, ohne dass JPA versehentlich Seiteneffekte beim Hydrieren auslöst.

#### Modul `1_2_2_JPA_ID_AUTO` bis `1_2_4_JPA_ID_SEQ`
- **Frage 1:** *Warum ist `GenerationType.AUTO` in Multi-Database-Projekten oft unberechenbar?*
  - **Antwort:** Der JPA-Provider entscheidet selbst über die Strategie. Hibernate wählt bei PostgreSQL/Oracle Sequenzen, bei MySQL `IDENTITY` und bei älteren Dialekten oft eine langsame Generatortabelle (`TABLE`).
- **Frage 2:** *Welche Vor- und Nachteile hat `GenerationType.TABLE` gegenüber Sequenzen?*
  - **Antwort:** Vorteil: Funktioniert auf jeder relationalen Datenbank. Nachteil: Massiver Performanceverlust durch exklusive Zeilensperren auf der Generator-Tabelle und separate Hilfstransaktionen bei jedem ID-Abruf.
- **Frage 3:** *Warum ermöglicht `GenerationType.SEQUENCE` JDBC-Batching, `GenerationType.IDENTITY` hingegen nicht?*
  - **Antwort:** Bei `SEQUENCE` fragt JPA den nächsten ID-Block vorab per `SELECT NEXT VALUE FOR ...` ab und setzt die ID im Java-Objekt sofort. Das `INSERT` kann verzögert und im Batch gesendet werden. Bei `IDENTITY` erfährt JPA die ID erst **nach** Ausführung des konkreten `INSERT`-Statements über den JDBC-Treiber, wodurch der Flush erzwungen wird.

#### Modul `1_2_5_JPA_ID_Class` vs. `1_2_6_JPA_ID_EmbeddedId`
- **Frage 1:** *Warum wird `@EmbeddedId` in Folie 50 gegenüber `@IdClass` als Standard und Best Practice empfohlen?*
  - **Antwort:** `@EmbeddedId` bündelt den zusammengesetzten Primärschlüssel in einem eigenen, kohärenten `@Embeddable`-Objekt. Das Entitätsmodell bleibt schlank (`@EmbeddedId private BookingId id;`), und Abfragen wie `em.find(Booking.class, bookingId)` sind typsicher. Bei `@IdClass` sind alle Schlüsselfelder flach in der Entity dupliziert.

#### Modul `1_3_1_JPA_AttributeConverter`
- **Frage 1:** *Warum ist `AttributeConverter` für Enums und Value Objects robuster als `@Enumerated(ORDINAL)` oder `@Enumerated(STRING)`?*
  - **Antwort:** `@Enumerated(ORDINAL)` korrumpiert Daten, sobald ein neuer Enum-Wert an den Anfang oder in die Mitte eingefügt wird. `@Enumerated(STRING)` speichert lange Zeichenketten redundant ab. Ein `AttributeConverter` mappt typsicher auf ultrakompakte, feste DB-Codes (z. B. `'E'`, `'D'`, `'P'`).

---

### Tag 2: Assoziationen, Kaskadierung & Vererbung

#### Modul `1_5_JPA_OneToOne` bis `1_8_JPA_ManyToMany`
- **Frage 1:** *Was bedeutet die „Owning Side“ einer Assoziation und wie erkennt man sie?*
  - **Antwort:** Die Owning Side besitzt den physischen Fremdschlüssel (bzw. die Join-Tabelle) in der Datenbank und steuert die SQL-`INSERT`/`UPDATE`-Anweisungen. Man erkennt die Inverse Side am Attribut `mappedBy`. Änderungen werden nur persistiert, wenn sie auf der Owning Side gesetzt werden!
- **Frage 2:** *Warum ist defensive Synchronisation in Hilfsmethoden (`addVehicle`/`removeVehicle`) essenziell?*
  - **Antwort:** JPA synchronisiert den relationalen Zustand in der Datenbank erst beim `flush()`. Im selben Java-Thread/Persistence-Context bleibt eine nicht defensiv gepflegte Gegenseite im Hauptspeicher inkonsistent.
- **Frage 3:** *Warum sollte `CascadeType.REMOVE` bei `@ManyToMany`-Beziehungen strikt vermieden werden?*
  - **Antwort:** Wenn Entity A gelöscht wird, würde `CascadeType.REMOVE` alle verknüpften B-Entities physisch aus der Datenbank löschen – auch wenn diese noch mit Entity C, D und E verknüpft sind!

#### Modul `1_6_1_JPA_CascadeOrphanRemoval`
- **Frage 1:** *Worin liegt der Unterschied zwischen `CascadeType.REMOVE` und `orphanRemoval = true`?*
  - **Antwort:**
    - `CascadeType.REMOVE`: Löscht Kinddatensätze nur, wenn der *Elternknoten selbst* via `em.remove(parent)` gelöscht wird.
    - `orphanRemoval = true`: Löscht ein Kind auch dann per SQL-`DELETE`, wenn es lediglich aus der Java-Collection des Elternknotens entfernt wird (`contract.getDamages().remove(d)`).

#### Modul `1_9_1` bis `1_9_4`: Vererbungsstrategien
- **Frage 1:** *Wann wählt man `SINGLE_TABLE`, wann `JOINED` und wann `TABLE_PER_CLASS`?*
  - **Antwort:**
    - `SINGLE_TABLE`: Maximale Performance (keine Joins, 1 Tabelle, Diskriminatorspalte). Nachteil: Spalten von Unterklassen dürfen keine `NOT NULL`-Constraints auf DB-Ebene haben.
    - `JOINED`: Sauberes, normalisiertes DB-Schema mit `NOT NULL`-Constraints. Nachteil: Polymorphe Abfragen erfordern SQL-`LEFT OUTER JOIN`s über alle Sub-Tabellen.
    - `TABLE_PER_CLASS`: Vollständig isolierte Tabellen. Nachteil: Polymorphe Abfragen über die Basisklasse erfordern teure `UNION`-Queries.
- **Frage 2:** *Warum ist `@MappedSuperclass` keine Vererbungsstrategie im relationalen Sinn?*
  - **Antwort:** Eine `@MappedSuperclass` ist keine eigenständige `@Entity`. Sie besitzt keine eigene Tabelle und kann nicht in polymorphen JPQL-Queries abgefragt werden; sie vererbt lediglich Spalten-Mappings an Sub-Entities.

---

### Tag 3: JPQL, Locking, Lifecycle, Validation & Criteria API

#### Modul `2_07_JPQL_FetchType` (N+1, FetchJoin, Entity Graphs)
- **Frage 1:** *Warum löst `FetchType.EAGER` im Entity-Mapping das N+1-Problem nicht, sondern verschlimmert es?*
  - **Antwort:** `FetchType.EAGER` zwingt JPA, die Relation *immer* mitzuladen – auch bei `em.find()` oder einfachen Abfragen, die die Kinddaten gar nicht benötigen. Bei JPQL (`SELECT s FROM Shop s`) führt EAGER trotzdem zu N separaten Nachlade-Selects!
- **Frage 2:** *Was ist der Unterschied zwischen `fetchgraph` und `loadgraph`?*
  - **Antwort:**
    - `jakarta.persistence.fetchgraph`: Alle im Graphen genannten Attribute werden `EAGER` geladen, **alle anderen Attribute werden auf `LAZY` gesetzt** (überschreibt Mapping strikt).
    - `jakarta.persistence.loadgraph`: Alle im Graphen genannten Attribute werden `EAGER` geladen, alle anderen Attribute **behalten ihr im Mapping definiertes Fetch-Verhalten**.

#### Modul `2_08_01_JPA_TransactionLifecycle` & `2_08_04_JPA_EqualsHashCode`
- **Frage 1:** *Was bewirkt `em.clear()` und welche Gefahr droht bei unbedachtem Einsatz?*
  - **Antwort:** `em.clear()` entkoppelt alle verwalteten Entities vom Persistence Context (Zustand: *Detached*). Nicht geflushte Änderungen gehen verloren! Nachträgliche Zugriffe auf uninitialisierte LAZY-Relationen führen zu `LazyInitializationException`.
- **Frage 2:** *Warum dürfen `equals()` und `hashCode()` bei JPA-Entities nicht auf einer generierten ID basieren?*
  - **Antwort:** Eine neue Entity hat vor dem ersten `persist()` `id = null`. Wird sie in ein `HashSet` eingefügt und anschließend persistiert, ändert sich ihr `hashCode()`. Das Set kann die Entity danach im Hash-Bucket nicht mehr wiederfinden (Set-Invarianz gebrochen!). Lösung: Fachlicher Schlüssel (Business Key) oder feste Konstante für `hashCode()`.

#### Modul `2_08_05_JPA_BulkOperations`
- **Frage 1:** *Warum muss nach `query.executeUpdate()` zwingend `em.clear()` aufgerufen werden?*
  - **Antwort:** Massenoperationen (`UPDATE`/`DELETE`) laufen direkt auf der Datenbank und umgehen den Persistence Context / 1st-Level-Cache vollständig. Bereits geladene Entities im Arbeitsspeicher behalten ihre veralteten Werte (*Stale State*). `em.clear()` erzwingt ein frisches Neuladen aus der DB.

#### Modul `2_08_06_JPA_PessimisticLocking`
- **Frage 1:** *Wann reicht Optimistic Locking (`@Version`) nicht mehr aus?*
  - **Antwort:** Bei hoher Schreibkollisionsrate auf knappen Ressourcen (z. B. Buchung des letzten Sitzplatzes, Ticketkontingente, Zähler). Optimistic Locking führt hier zu ständigen Rollbacks und `OptimisticLockException`s. Pessimistic Locking (`PESSIMISTIC_WRITE`) sichert den Datensatz per `SELECT ... FOR UPDATE` exklusiv auf DB-Ebene ab.

---

### Lösungen zu den Folien-Quizzes (jpa_only.pdf)

| Folie / Thema | Quiz-Frage | Musterlösung & Erklärung |
| :--- | :--- | :--- |
| **Folie 44** (Mapping) | *Welche Annotationen sind zwingend für eine minimale Entity nötig?* | Mindestens `@Entity` und `@Id` auf einem Primärschlüsselfeld sowie ein parameterloser Konstruktor. |
| **Folie 66** (Access Type) | *Was passiert, wenn `@Id` auf dem Feld liegt, aber `@Column` auf dem Getter?* | Der Placement-Ort von `@Id` bestimmt den Standard-Access-Type der gesamten Entity (`FIELD`). `@Column` auf dem Getter wird ignoriert, sofern nicht explizit `@Access(AccessType.PROPERTY)` deklariert ist. |
| **Folie 88** (Collections) | *Warum verwendet man in JPQL `IS EMPTY` statt `IS NULL` für Assoziations-Collections?* | Relationale 1:N- und N:M-Beziehungen sind in Java niemals `null`, sondern leere Collections (`Collection.isEmpty()`). SQL kennt keine Collections – `IS EMPTY` übersetzt in eine `NOT EXISTS (SELECT 1 FROM ...)`-Unterabfrage. |
| **Folie 108** (Path Expressions) | *Warum schlägt die JPQL-Navigation `SELECT s.carpool.type.brand FROM Shop s` fehl?* | `carpool` ist eine Collection (`@OneToMany`). In JPA darf nicht direkt über Collection-Attribute weiter navigiert werden. Es muss explizit ein Join deklariert werden: `SELECT v.type.brand FROM Shop s JOIN s.carpool v`. |
| **Folie 124** (Discriminator) | *Was steht in der Discriminator-Spalte `DTYPE`, wenn kein `@DiscriminatorValue` gesetzt ist?* | JPA verwendet standardmäßig den einfachen Klassennamen der Entität (z. B. `'Car'` oder `'Truck'`). |
| **Folie 150** (JPQL Joins) | *Was ist der Unterschied zwischen `JOIN` und `JOIN FETCH`?* | `JOIN` filtert oder verknüpft Datensätze für die Where-/Select-Klausel, initialisiert aber die Java-Relation nicht. `JOIN FETCH` weist den Provider an, die verknüpften Entitäten im selben SQL-Query vollständig in den Speicher zu laden. |
| **Folie 172** (Criteria API) | *Warum ist `CriteriaBuilder` typsicherer als JPQL?* | Fehler in Attributnamen oder Typen werden bei Nutzung des JPA Static Metamodels (`Shop_.carpool`) bereits zur Compilezeit vom Compiler erkannt, während JPQL-Stringfehler erst zur Laufzeit auftreten. |
