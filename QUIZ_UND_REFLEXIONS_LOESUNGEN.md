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
- **Frage 2:** *Warum erfordert Dirty Checking in JPA keine explizite `update()`-Methode am `EntityManager`?*
  - **Antwort:** Gemanagte Entities werden beim Laden durch einen Snapshot im Persistence Context überwacht. Vor dem `flush()` vergleicht JPA den aktuellen Objektzustand mit dem Snapshot und erzeugt bei festgestellten Abweichungen automatisch die minimal erforderlichen SQL-`UPDATE`-Statements.
- **Frage 3:** *Was ist der genaue Unterschied zwischen der ursprünglichen detached Instanz und dem Rückgabewert von `manager.merge()`?*
  - **Antwort:** `manager.merge(detachedEntity)` kopiert den Zustand des übergebenen detached Objekts in eine *neue* oder bereits existierende gemanagte Entity-Instanz im Persistence Context und gibt diese zurück. Das ursprüngliche Objekt bleibt unmanaged (`manager.contains(original) == false`), während die zurückgegebene Kopie gemanagt wird.

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

#### Modul `1_5_JPA_OneToOne`
- **Frage 1:** *Welche Seite ist bei einer 1:1-Beziehung die Owning Side und woran erkennt man das im relationalen Datenbankschema?*
  - **Antwort:** Die Owning Side besitzt die physische Fremdschlüsselspalte (z. B. `person_id` in `tbl_User`). In Java erkennt man die inverse Seite am Attribut `mappedBy`. Nur Änderungen auf der Owning Side werden per SQL in die Fremdschlüsselspalte geschrieben.
- **Frage 2:** *Was bewirkt `CascadeType.PERSIST` beim Aufruf von `manager.persist(user)` – und warum werden Änderungen nach dem Laden ohne passende Cascade-Typen nicht automatisch propagiert?*
  - **Antwort:** `CascadeType.PERSIST` delegiert das initiale Persistieren transitiv an noch ungespeicherte (`transient`) Kind-Objekte. Das automatische Erkennen von Änderungen nach dem Laden (Dirty Checking) funktioniert für bereits gemanagte abhängige Entities jedoch auch ohne `CascadeType.MERGE`.
- **Frage 3:** *Warum sollte `CascadeType.REMOVE` bei 1:1-Beziehungen nur verwendet werden, wenn die abhängige Entity eine echte Teil-Existenz (Komposition) darstellt?*
  - **Antwort:** Wenn eine `Person` auch unabhängig von einem `User` existieren darf (z. B. als Ansprechpartner, Mitarbeiter oder Fahrer), würde `CascadeType.REMOVE` beim Löschen des Users unbeabsichtigt die gesamte Person physisch löschen.

#### Modul `1_6_JPA_OneToMany`
- **Frage 1:** *Warum erzeugt ein reines `@OneToMany` ohne `@JoinColumn` und ohne `mappedBy` standardmäßig eine Join-Tabelle?*
  - **Antwort:** Relational kann eine 1:N-Beziehung ohne Fremdschlüsselspalte auf der N-Seite nur über eine eigenständige Zuordnungstabelle (`tbl_Shop_tbl_Vehicle`) abgebildet werden. Erst `@JoinColumn(name = "shop_id")` verlagert den Fremdschlüssel direkt in die Zieltabelle.
- **Frage 2:** *Warum erfordert ein unidirektionales `@OneToMany` mit `@JoinColumn` beim Speichern ein zusätzliches `UPDATE`-Statement für den Fremdschlüssel?*
  - **Antwort:** JPA persistiert zuerst das Kind (`Vehicle`) per `INSERT` ohne Kenntnis des Eltern-Schlüssels und setzt den Fremdschlüssel `shop_id` anschließend über ein separates SQL-`UPDATE`. Eine bidirektionale Beziehung mit `mappedBy` vermeidet dieses zusätzliche Update.
- **Frage 3:** *Was ist der Unterschied zwischen dem Leeren einer `@OneToMany`-Collection mit und ohne `orphanRemoval = true`?*
  - **Antwort:** Ohne `orphanRemoval` setzt JPA beim Leeren der Collection lediglich den Fremdschlüssel `shop_id` auf `NULL` (die Kind-Entities verbleiben in der Datenbank). Mit `orphanRemoval = true` wird für jedes entfernte Kind ein physisches SQL-`DELETE` ausgeführt.

#### Modul `1_7_JPA_ManyToOne`
- **Frage 1:** *Warum bezeichnet `mappedBy` immer den Namen des Java-Attributs auf der Gegenseite und niemals einen Datenbankspaltennamen?*
  - **Antwort:** JPA arbeitet objektorientiert. `mappedBy = "location"` signalisiert dem Provider, dass das Java-Feld `location` in der Klasse `Vehicle` die Metadaten (`@ManyToOne`, `@JoinColumn`) für diese Relation definiert.
- **Frage 2:** *Was passiert, wenn eine neue Verknüpfung nur in `shop.getVehicles().add(vehicle)` eingetragen wird, `vehicle.setLocation(shop)` aber nicht aufgerufen wird?*
  - **Antwort:** Die Verknüpfung wird **nicht** in der Datenbank gespeichert! Da `Shop.vehicles` mit `mappedBy` die inverse Seite ist, ignoriert JPA Änderungen an dieser Collection beim Schreiben. Der Fremdschlüssel wird nur persistiert, wenn die Owning Side (`Vehicle.location`) gesetzt ist.
- **Frage 3:** *Warum sind defensive Helper-Methoden (wie `addVehicle`/`removeVehicle`) bei bidirektionalen Beziehungen unverzichtbar?*
  - **Antwort:** Ohne synchrone Pflege beider Seiten im Java-Code weicht der Hauptspeicherzustand bis zum nächsten Neuladen aus der DB vom Datenbankzustand ab. Ruft Geschäftscode im selben Request `shop.getVehicles()` auf, fehlen neu zugeordnete Fahrzeuge.

#### Modul `1_8_JPA_ManyToMany`
- **Frage 1:** *Wann ist bei einer N:M-Beziehung eine unidirektionale Abbildung ausreichend und wann empfiehlt sich eine bidirektionale Modellierung?*
  - **Antwort:** Unidirektional reicht aus, wenn Abfragen und Navigation fachlich immer nur von einer Seite ausgehen (z. B. `Vehicle` kennt seine `locationHistory`, `Shop` muss die Historie nicht direkt als Collection halten). Bidirektional lohnt sich nur, wenn beide Seiten im Objektmodell traversiert werden müssen.
- **Frage 2:** *Warum sollte `CascadeType.REMOVE` bei `@ManyToMany`-Beziehungen strikt vermieden werden?*
  - **Antwort:** Wenn Fahrzeug A gelöscht wird, würde `CascadeType.REMOVE` alle Standorte aus seiner Historie (`Shop`) physisch aus der Datenbank löschen – und damit auch die Standorte für alle anderen existierenden Fahrzeuge vernichten!
- **Frage 3:** *Wann sollte eine `@ManyToMany`-Beziehung in zwei `@ManyToOne`-Beziehungen mit einer eigenständigen Entity für die Verbindungstabelle aufgelöst werden?*
  - **Antwort:** Sobald die Beziehung eigene fachliche Attribute trägt (z. B. `ankunftsDatum`, `kilometerStandBeimWechsel`, `bemerkung`). In JPA können Join-Tabellen von `@ManyToMany` keine zusätzlichen Nutzdaten-Spalten enthalten.

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

#### Modul `2_01_JPQL_SimpleQuery`
- **Frage 1:** *Welchen Sicherheits- und Lesbarkeitsvorteil bietet `manager.createQuery(jpql, VehicleType.class)` (`TypedQuery`) gegenüber der klassischen `Query` ohne Typangabe?*
  - **Antwort:** `TypedQuery<T>` erspart manuelle Type-Casts (`(List<VehicleType>) resultList`) und fängt Typinkompatibilitäten direkt an der Aufrufstelle ab. Compiler und IDEs bieten sofort Code-Completion und Typensicherheit.
- **Frage 2:** *Warum lautet die Klausel `FROM VehicleType f` und nicht `FROM tbl_VehicleType`?*
  - **Antwort:** JPQL operiert ausschließlich auf dem objektorientierten Domänenmodell (Entity-Namen und Java-Klasseneigenschaften), nicht auf physischen Tabellen- oder Spaltennamen. JPA übersetzt dies anhand des Mappings automatisch in das passende relationale SQL.
- **Frage 3:** *Warum wirft `getSingleResult()` bei 0 Treffern eine `NoResultException` anstatt `null` zurückzugeben — und wie geht man in modernem Java eleganter damit um?*
  - **Antwort:** Die Spezifikation definiert `getSingleResult()` als strikten Kontrakt („erwarte genau 1 Ergebnis“). Ab JPA 2.2 / modernem Java nutzt man stattdessen `query.getResultStream().findFirst()`, was ein sauberes `Optional<T>` liefert und try-catch-Blöcke vermeidet.

#### Modul `2_02_JPQL_SimpleQueryWithParameter`
- **Frage 1:** *Warum dürfen Parameter niemals durch String-Konkatenation (`"WHERE f.hp > " + ps`) in JPQL eingebaut werden?*
  - **Antwort:** 1. **SQL/JPQL-Injection:** Angreifer können bösartigen Code einschleusen. 2. **Performance & Prepared Statement Caching:** Datenbanken und JPA-Provider kompilieren parametrisierte Abfragen (`?` / `:param`) einmalig und cachen den Ausführungsplan. String-Konkatenation erzwingt bei jedem Aufruf ein neues Parsing.
- **Frage 2:** *Welche Vorteile bieten benannte Parameter (`:ps`) gegenüber Positions-Parametern (`?1`)?*
  - **Antwort:** Benannte Parameter sind selbstdokumentierend, widerstandsfähig gegen spätere Umstellungen in der WHERE-Klausel und können in derselben Query mehrfach wiederverwendet werden, ohne den Wert doppelt zu binden.
- **Frage 3:** *Warum kann man in SQL/JPQL nicht `WHERE f.location = NULL` schreiben, sondern muss `IS NULL` verwenden?*
  - **Antwort:** Relationale Datenbanken verwenden 3-wertige Logik (TRUE, FALSE, UNKNOWN). Jeder direkte Vergleich mit `NULL` (`= NULL`, `!= NULL`) ergibt immer `UNKNOWN` (wird als `FALSE` ausgewertet). Zur Prüfung auf Vorhandensein von Nullwerten ist der Operator `IS NULL` bzw. `IS NOT NULL` zwingend vorgeschrieben.

#### Modul `2_03_JPQL_SimpleQueryPaging`
- **Frage 1:** *Warum ist Pagination auf Datenbankebene (`setFirstResult`/`setMaxResults`) dem Laden aller Datensätze mit anschließender Filterung im Java-Speicher (`subList()`) drastisch überlegen?*
  - **Antwort:** Bei 1 Million Datensätzen würde ein Java-seitiges `subList()` alle 1 Million Zeilen über das Netzwerk übertragen, instanziieren und den Heap überlasten (Out of Memory). Datenbank-Pagination lädt exakt nur die 10–50 Zeilen der aktuellen Seite.
- **Frage 2:** *Warum ist bei verlässlicher Pagination in Produktionsanwendungen eine explizite `ORDER BY`-Klausel unerlässlich?*
  - **Antwort:** Ohne `ORDER BY` garantiert relationale Datenbanken keine feste Zeilenreihenfolge (Sortierung kann sich zwischen zwei Requests oder bei Indexänderungen ändern). Dies führt dazu, dass Datensätze auf Folgeseiten doppelt auftauchen oder übersprungen werden.
- **Frage 3:** *Warum ist `setFirstResult`/`setMaxResults` in Kombination mit `JOIN FETCH` auf Collection-Beziehungen (1:N) eine bekannte Performance-Falle?*
  - **Antwort:** Ein SQL-Join auf eine 1:N-Tabelle multipliziert die Zeilen der Eltern-Tabelle. Würde die Datenbank `LIMIT 10` anwenden, würden 10 Join-Zeilen geladen, was ggf. nur 2 Elternknoten entspricht. Hibernate muss daher **alle** Zeilen in den Java-Speicher laden und das Paging im RAM durchführen (*HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!*).

#### Modul `2_04_JPQL_WhereClause`
- **Frage 1:** *Warum bietet JPQL spezielle Operatoren wie `IS EMPTY` / `IS NOT EMPTY` für Collections statt einfacher Vergleiche wie `size() = 0`?*
  - **Antwort:** Collections existieren relational als Fremdschlüssel in einer separaten Tabelle. `IS EMPTY` wird hochoptimiert in `NOT EXISTS (SELECT 1 FROM ...)` übersetzt, ohne dass alle Zeilen aggregiert oder gezählt werden müssen.
- **Frage 2:** *Warum ist `MEMBER OF` in JPQL oft lesbarer und wartbarer als ein manueller `INNER JOIN` mit `WHERE`-Vergleich?*
  - **Antwort:** `WHERE :entity MEMBER OF parent.children` drückt die fachliche Absicht direkt aus („ist das Kind im Elternteil enthalten?“). JPA generiert die notwendige Unterabfrage oder den Join automatisch.
- **Frage 3:** *Wie verhält sich ein `IN`-Ausdruck, wenn die übergebene Java-Collection leer ist?*
  - **Antwort:** Nach JPA-Standard führt ein leeres `IN ()` zu einem SQL-Syntaxfehler (`WHERE id IN ()` ist ungültiges SQL). Moderne Provider (wie Hibernate 6) fangen leere Collections ab und generieren ein sicheres `WHERE 1=0` (keine Treffer), dennoch sollte vor dem Absetzen geprüft werden, ob die Liste leer ist.

#### Modul `2_05_JPQL_MappingInQuery`
- **Frage 1:** *Welche Performance-Vorteile bieten DTO-Projections bei reinen Leseoperationen gegenüber dem Laden vollständiger Entity-Graphen?*
  - **Antwort:** DTO-Projektionen fordern auf der Datenbank nur die exakt benötigten Spalten an (kein `SELECT *`), sparen Netzwerkbandbreite und instanziieren unmanaged Java-Objekte. Der Persistence Context muss weder Snapshots für das Dirty-Checking vorhalten noch den 1st-Level-Cache belasten.
- **Frage 2:** *Warum erfordert eine JPA Constructor Expression zwingend einen passenden, öffentlichen Konstruktor mit exakt übereinstimmenden Parametertypen in der DTO-Klasse?*
  - **Antwort:** JPA nutzt Standard-Reflection zur Instanziierung (`new MyDTO(...)`). Passen Parametertypen nicht exakt (z. B. `int` vs. `long` oder `double` vs. `BigDecimal`), wirft der Provider eine `IllegalArgumentException` oder kann keinen passenden Konstruktor auflösen.
- **Frage 3:** *Warum ist die `Tuple`-Projektion wartungsfreundlicher als der unstrukturierte Zugriff auf `Object[]` per numerischem Index?*
  - **Antwort:** Das `Tuple`-Interface ermöglicht den typsicheren Zugriff über Alias-Namen (`tuple.get("brand", String.class)`). Wird die Spaltenreihenfolge in der Query später geändert oder eine neue Spalte eingefügt, bricht bestehender Code bei `Object[]`-Indexzugriffen (`row[0]`), während `Tuple`-Zugriffe stabil bleiben.

#### Modul `2_06_JPQL_InnerJoins`
- **Frage 1:** *Wann sollte ein `LEFT JOIN` statt eines impliziten Joins (wie `s.carpool`) verwendet werden?*
  - **Antwort:** Ein impliziter Pfadausdruck oder ein `INNER JOIN` schließt Elternknoten ohne verknüpfte Kinddatensätze (z. B. Standorte ohne Fahrzeuge) komplett aus dem Abfrageergebnis aus. Ein `LEFT JOIN` stellt sicher, dass alle Eltern-Entities erhalten bleiben, auch wenn keine Beziehung existiert.
- **Frage 2:** *Warum macht eine `WHERE`-Bedingung auf eine `LEFT JOIN`-Tabelle den Outer Join zunichte, und wie löst die JPA 2.1 `ON`-Klausel dieses Problem?*
  - **Antwort:** Eine `WHERE`-Klausel wird nach dem Join ausgewertet und filtert alle Zeilen heraus, bei denen Attribute der rechten Seite `NULL` sind (der `LEFT JOIN` wird relational zum `INNER JOIN`). Die JPA 2.1 `ON`-Klausel (`LEFT JOIN s.carpool v ON v.brand = 'BMW'`) schränkt hingegen nur die Zuordnungsbedingung des Joins ein, sodass der linke Datensatz mit `NULL`-Werten im Resultset verbleibt.
- **Frage 3:** *Warum führt ein Join über eine 1:N-Collection ohne `DISTINCT` bei mehreren Kind-Elementen zu mehrfach referenzierten Eltern-Objekten in der Ergebnisliste?*
  - **Antwort:** Ein SQL-Join erzeugt ein kartesisches Produkt aus Eltern- und Kindzeilen. Besitzt ein Standort 5 Fahrzeuge, liefert die Datenbank 5 Ergebniszeilen. Ohne `SELECT DISTINCT` instanziiert oder referenziert JPA denselben `Shop` fünfmal in der Ergebnisliste.

#### Modul `2_08_JPQL_GroupBy`
- **Frage 1:** *Was ist der Unterschied zwischen der Filterung in der `WHERE`-Klausel und der `HAVING`-Klausel?*
  - **Antwort:** `WHERE` filtert einzelne Rohdatensätze *vor* der Gruppierung und Aggregation (z. B. nur Reservierungen aus dem Jahr 2024). `HAVING` filtert die bereits aggregierten Gruppen *nach* der Gruppenbildung (z. B. nur Fahrzeugmodelle mit `COUNT(res) > 2` oder Gesamtumsatz `SUM(res.price) > 500`).
- **Frage 2:** *Warum liefert `COUNT(...)` in JPA immer ein `Long` und `AVG(...)` immer ein `Double`, unabhängig vom Typ des Tabellenattributs?*
  - **Antwort:** Die JPA-Spezifikation schreibt feste Rückgabetypen für Standard-Aggregatfunktionen vor, um Überläufe bei großen Datenmengen zu verhindern (`COUNT` liefert `Long`, `SUM` über Ganzzahlen liefert `Long`, `AVG` liefert immer Fließkommazahlen als `Double`).
- **Frage 3:** *Welche Vorteile bietet das direkte Mappen aggregierter Ergebnisse in DTOs (`ReservationStatsDTO`) gegenüber `List<Object[]>`?*
  - **Antwort:** DTOs machen fachliche Kennzahlen (`getCount()`, `getTotalPrice()`) explizit, typsicher und refactoring-resistent. Ungetypte `Object[]`-Arrays erfordern fehleranfällige manuelle Casts und numerische Indexzugriffe.

#### Modul `2_09_JPQL_SubQueries`
- **Frage 1:** *In welchen Teilen einer JPQL-Query sind Subqueries erlaubt (`WHERE`, `HAVING`, `SELECT`) und wo waren sie in älteren JPA-Spezifikationen eingeschränkt?*
  - **Antwort:** Nach JPA 2.x-Standard sind Subqueries in `WHERE`, `HAVING` und `SELECT` (als Skalar-Subquery) erlaubt. Subqueries in der `FROM`-Klausel waren im JPA-Standard lange verboten (Hibernate 6 unterstützt abgeleitete Tabellen in `FROM` inzwischen herstellerspezifisch).
- **Frage 2:** *Warum ist `EXISTS (...)` oft performanter als `IN (SELECT ...)`, wenn die Unterabfrage auf NULL-Werte oder große Datenmengen trifft?*
  - **Antwort:** `EXISTS` bricht auf DB-Ebene sofort ab, sobald der erste passende Datensatz gefunden wird (Short-Circuit Evaluation). Zudem ist `EXISTS` immun gegen unerwartetes Verhalten bei `NULL`-Werten, während `IN` bei vorhandenen `NULL`-Einträgen in der Unterabfrage die 3-wertige SQL-Logik auslöst und Indizes oft nicht optimal nutzen kann.
- **Frage 3:** *Warum sind korrelierte Subqueries bei sehr großen Datenmengen performancekritisch, und wie könnten sie alternativ mit Joins und `GROUP BY` umformuliert werden?*
  - **Antwort:** Bei korrelierten Subqueries muss die Unterabfrage konzeptionell für jede Zeile der äußeren Abfrage erneut ausgeführt werden (O(N)-Komplexität). Durch Umformulierung in einen `LEFT JOIN` mit anschließendem `GROUP BY` und `HAVING` kann die Datenbank Hash- oder Merge-Joins nutzen und die Berechnung in einem einzigen Scan durchführen.

#### Modul `2_10_JPQL_NativeQueries`
- **Frage 1:** *Welche Risiken und Nachteile bringen Native Queries bezüglich Portabilität, Refactoring-Sicherheit und Typsicherheit mit sich?*
  - **Antwort:** Native Queries binden die Anwendung fest an einen spezifischen SQL-Dialekt (z. B. Derby, Oracle, PostgreSQL) und brechen die Datenbank-Unabhängigkeit. Bei Refactorings von Java-Attributen oder Entity-Klassennamen schlägt die automatische IDE-Umbenennung für reine SQL-Strings fehl. Syntax- und Tippfehler werden erst zur Laufzeit bemerkt.
- **Frage 2:** *Warum müssen bei Native Queries exakte relationale Spalten- und Tabellennamen verwendet werden, während JPQL auf dem Java-Domänenmodell arbeitet?*
  - **Antwort:** Native SQL wird vom JPA-Provider 1:1 direkt an den JDBC-Treiber durchgereicht. Die relationale Datenbank kennt weder Java-Klassen noch Entity-Attribute, sondern ausschließlich physische Tabellen, Views und Spalten (`tbl_User`, `DTYPE`, `tbl_VehicleType`).
- **Frage 3:** *Warum führt ein natives `UPDATE` via `createNativeQuery().executeUpdate()` zu veralteten Werten (Stale Data) im Persistence Context, wenn `manager.clear()` vergessen wird?*
  - **Antwort:** Native DML-Operationen modifizieren Tabellenzeilen direkt auf der Datenbank und umgehen das Entity-Lifecycle-Management und den 1st-Level-Cache vollständig. Bereits geladene Instanzen im Persistence Context behalten ihre alten Feldwerte, solange der Cache nicht per `manager.clear()` invalidiert oder per `manager.refresh()` nachgeladen wird.

#### Modul `2_11_JPQL_NamedQueries`
- **Frage 1:** *Welchen Vorteil bietet die Validierung von `@NamedQuery` beim Start der `EntityManagerFactory` gegenüber dynamischen Queries via `manager.createQuery()`?*
  - **Antwort:** Named Queries werden beim Hochfahren der EMF einmalig geparst, semantisch validiert und als AST (Abstract Syntax Tree) vorkompiliert. Tippfehler in Entity-Pfaden oder Syntaxfehler werden als Startup-Fehler sofort beim Serverstart aufgedeckt (Fail-Fast-Prinzip) und verlangsamen keine späteren Requests.
- **Frage 2:** *Warum empfiehlt es sich, die Query-Namen und Parameternamen als `public static final String`-Konstanten in der jeweiligen Entity-Klasse zu bündeln?*
  - **Antwort:** Konstanten binden Aufrufe (`manager.createNamedQuery(Reservation.FIND_BY_START_Shop)`) an Compiler-Prüfungen. Ändert sich der Query-Name, findet die IDE alle Verwendungsstellen zuverlässig. Fehlerträchtige "Magic Strings" in DAOs oder Services werden eliminiert.
- **Frage 3:** *Wann ist die Definition von Named Queries in `orm.xml` gegenüber Code-Annotationen vorzuziehen?*
  - **Antwort:** Wenn Abfragen für unterschiedliche Deployments, Performance-Tunings oder DB-Dialekte angepasst werden müssen, ohne die kompilierten Entity-JAR-Dateien neu zu bauen, oder wenn das Domain-Modell strikt frei von Query-Annotationen gehalten werden soll.

#### Modul `2_15_CriteriaAPI_Simple`
- **Frage 1:** *Welche Vor- und Nachteile hat die Criteria API gegenüber JPQL bezüglich Typsicherheit, dynamischem Zusammenbau von Suchfiltern und Lesbarkeit?*
  - **Antwort:**
    - Vorteile: Vollständig programmatisch und typsicher; keine fehleranfälligen String-Konkatenationen; ideal für komplexe, dynamische Suchmasken mit optionalen Filterkriterien (`List<Predicate>`).
    - Nachteile: Deutlich verbose und schwerer lesbare Syntax gegenüber kompaktem JPQL; steilere Lernkurve für Entwickler.
- **Frage 2:** *Warum eignet sich das `List<Predicate>`-Muster ideal für variable Benutzeroberflächen-Suchmasken im Vergleich zu dynamischen JPQL-Stringverkettungen?*
  - **Antwort:** Mit `List<Predicate>` können beliebige Kombinationen von Filterfeldern (z. B. nur Marke, nur Preis, oder beides) modular über `if (input != null) predicates.add(...)` hinzugefügt werden. Es müssen keine komplexen `WHERE 1=1 AND ...`-Stringbasteleien gepflegt werden, und die `CriteriaBuilder.and()`-Verknüpfung bleibt immer syntaktisch korrekt.
- **Frage 3:** *Wie hilft das JPA Static Metamodel (`User_.person`, etc.) dabei, auch Attributnamen zur Compilezeit vollständig abzusichern?*
  - **Antwort:** Der Annotation Processor generiert zu jeder Entity eine statische Metamodel-Klasse (z. B. `User_`), deren statische Attribute (`SingularAttribute<User, Person> person`) den Typ und Namen repräsentieren. Anstelle von String-Pfaden (`root.get("person")`) schreibt man `root.get(User_.person)` – Refactorings und Typfehler werden direkt vom Java-Compiler validiert.

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
