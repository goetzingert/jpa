# JPA & Hibernate: Troubleshooting- & Best-Practice-Guide

Dieser Leitfaden fasst die wichtigsten Fallstricke, Fehlermeldungen und Best Practices aus der Praxis zusammen. Er dient Seminarteilnehmern als kompaktes Nachschlagewerk während der Übungen und für reale Projekte.

---

## Inhaltsverzeichnis

1. [Typische Fehlerbilder & Troubleshooting](#1-typische-fehlerbilder--troubleshooting)
   - [LazyInitializationException](#11-lazyinitializationexception)
   - [Das N+1-Select-Problem](#12-das-n1-select-problem)
   - [JPA Entity Graphs (Dynamisches Fetching)](#13-jpa-entity-graphs-dynamisches-fetching)
   - [OptimisticLockException & StaleObjectStateException](#14-optimisticlockexception--staleobjectstateexception)
   - [Pessimistisches Locking & Concurrency-Deadlocks](#15-pessimistisches-locking--concurrency-deadlocks)
   - [TransientPropertyValueException & PersistentObjectException](#16-transientpropertyvalueexception--persistentobjectexception)
   - [ConstraintViolationException (Bean Validation)](#17-constraintviolationexception-bean-validation)
   - [Unzulässige Operationen auf @MappedSuperclass](#18-unzulässige-operationen-auf-mappedsuperclass)
   - [Derby-spezifische Fallstricke & Locks](#19-derby-spezifische-fallstricke--locks)
2. [JPA Architecture & Best Practices](#2-jpa-architecture--best-practices)
   - [Assoziationen: FetchType & Ownership](#21-assoziationen-fetchtype--ownership)
   - [Defensive Synchronisation bei bidirektionalen Beziehungen](#22-defensive-synchronisation-bei-bidirektionalen-beziehungen)
   - [CascadeType & orphanRemoval in der Praxis](#23-cascadetype--orphanremoval-in-der-praxis)
   - [equals() und hashCode() bei Entities](#24-equals-und-hashcode-bei-entities)
   - [Transaktionsgrenzen & Persistence Context Management](#25-transaktionsgrenzen--persistence-context-management)
   - [Bulk Operations (executeUpdate) & 1st-Level Cache Invalidation](#26-bulk-operations-executeupdate--1st-level-cache-invalidation)
   - [Abfrage-Optimierung: DTO-Projektionen vs. Entity-Queries](#27-abfrage-optimierung-dto-projektionen-vs-entity-queries)
   - [Primärschlüssel-Strategien & @EmbeddedId](#28-primärschlüssel-strategien--embeddedid)
   - [Custom Type Mapping mit AttributeConverter (@Converter)](#29-custom-type-mapping-mit-attributeconverter-converter)
3. [Entscheidungs- & Referenztabellen](#3-entscheidungs--referenztabellen)
   - [CascadeType-Matrix](#31-cascadetype-matrix)
   - [Entity-Lifecycle-Übersicht](#32-entity-lifecycle-übersicht)

---

## 1. Typische Fehlerbilder & Troubleshooting

### 1.1 LazyInitializationException

#### Symptom
```text
org.hibernate.LazyInitializationException: could not initialize proxy [net.rentacar.model.Shop#1] - no Session
```

#### Ursache
Es wurde auf eine `LAZY`-Beziehung oder Collection einer Entity zugegriffen, nachdem der `EntityManager` (bzw. die Hibernate-`Session`) bereits geschlossen (`close()`) oder die Transaktion beendet (`commit()`) wurde. Die Entity befindet sich im Zustand **Detached**.

#### Falsch (Antipattern)
- Globales Umstellen aller Beziehungen auf `FetchType.EAGER` (führt zu massiven Performance-Einbußen und N+1-Kaskaden).
- *Open Session in View* (OSIV) im Web-Tier (verschleiert unsaubere Abfragedesigns und erzeugt unkontrollierte DB-Queries beim Rendern).

#### Richtig
1. **Fetch Join in JPQL / Criteria API:**
   ```java
   TypedQuery<Shop> q = manager.createQuery(
       "SELECT s FROM Shop s JOIN FETCH s.vehicles WHERE s.id = :id", Shop.class);
   q.setParameter("id", shopId);
   Shop shop = q.getSingleResult();
   ```
2. **Entity Graphs (JPA 2.1+):**
   ```java
   EntityGraph<Shop> graph = manager.createEntityGraph(Shop.class);
   graph.addAttributeNodes("vehicles");
   Map<String, Object> hints = Map.of("jakarta.persistence.fetchgraph", graph);
   Shop shop = manager.find(Shop.class, shopId, hints);
   ```
3. **DTO-Projektion:** Wenn nur Teilinformationen für die GUI/API benötigt werden (siehe Abschnitt 2.5).

---

### 1.2 Das N+1-Select-Problem

#### Symptom
In den SQL-Logs taucht für 1 Abfrage auf eine Liste (z. B. 50 Shops) anschließend für jeden Eintrag eine separate Zusatzabfrage auf (50 zusätzliche `SELECT`-Statements für Fahrzeuge).

#### Ursache
- `FetchType.EAGER` auf einer `@OneToMany`- oder `@ManyToOne`-Beziehung bei Listenabfragen (`SELECT s FROM Shop s`).
- Oder Iteration über eine `LAZY`-Collection in einer Schleife außerhalb eines Fetch Joins.

#### Diagnose
SQL-Logging aktivieren (`<property name="hibernate.show_sql" value="true"/>`) oder Query-Counter im Test prüfen.

#### Lösung
- Verwende immer `FetchType.LAZY` als Standard.
- Lade benötigte Relationen gezielt per `JOIN FETCH`:
  ```sql
  SELECT DISTINCT s FROM Shop s LEFT JOIN FETCH s.vehicles
  ```
- Alternativ: Batch Fetching per Hibernate-Annotation (`@BatchSize(size = 25)`) auf Collection-Ebene.

---

### 1.3 JPA Entity Graphs (Dynamisches Fetching)

#### Warum Entity Graphs statt JOIN FETCH?
- **Keine Query-Modifikation nötig:** JPQL bleibt einfach (`SELECT s FROM Shop s`), der dynamische Ladeplan wird als Hint übergeben.
- **Kein kartesisches Produkt / Duplikate:** Mehrere `JOIN FETCH` auf Collections erzeugen `MultipleBagFetchException`s oder riesige Resultsets. Entity Graphs lösen dies elegant.
- **Sauberes Entity-Mapping:** Das Basis-Mapping bleibt durchgängig auf `FetchType.LAZY`.

#### API & Query-Hints
```java
// 1. Programmatischer EntityGraph
EntityGraph<Shop> graph = manager.createEntityGraph(Shop.class);
graph.addAttributeNodes("carpool");
Subgraph<Vehicle> vehGraph = graph.addSubgraph("carpool");
vehGraph.addAttributeNodes("type");

// 2. Abfrage mit Query Hint (fetchgraph oder loadgraph)
List<Shop> shops = manager.createQuery("SELECT s FROM Shop s", Shop.class)
    .setHint("jakarta.persistence.fetchgraph", graph)
    .getResultList();
```

| Hint-Name | Verhalten |
| :--- | :--- |
| `jakarta.persistence.fetchgraph` | Alle gelisteten Attribute = `EAGER`, alle ungelisteten = `LAZY` (überschreibt statisches EAGER strikt). |
| `jakarta.persistence.loadgraph` | Alle gelisteten Attribute = `EAGER`, alle ungelisteten behalten ihr definiertes Mapping. |

---

### 1.4 OptimisticLockException & StaleObjectStateException

#### Symptom
```text
jakarta.persistence.OptimisticLockException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)
```

#### Ursache
Zwei parallele Transaktionen haben dieselbe Entity mit `@Version` geladen. Transaktion A speichert zuerst und erhöht die Versionsnummer (z. B. von 1 auf 2). Wenn Transaktion B speichert, schlägt das `UPDATE ... WHERE id = ? AND version = 1` fehl, da 0 Zeilen verändert wurden.

#### Lösungsstrategie
1. **Fachliche Behandlung:** OptimisticLockException fangen, Transaktion zurückrollen (`rollback()`), den aktuellen Stand aus der Datenbank neu laden (`clear()` / `find()`) und den Benutzer informieren oder den Vorgang wiederholen (Retry-Pattern).
2. **Sauberes Concurrency-Handling im Test:**
   ```java
   firstManager.getTransaction().commit(); // Erhöht Version in der DB
   
   assertThrows(OptimisticLockException.class, () -> {
       secondManager.flush(); // Bemerkt veraltete Version
   });
   secondManager.getTransaction().rollback(); // Verbindung sauber aufräumen
   ```

---

### 1.5 Pessimistisches Locking & Concurrency-Deadlocks

#### Wann Optimistisches Locking nicht ausreicht
Bei hoher Schreibkonkurrenz auf knappen Ressourcen (z. B. Ticketreservierung, Fahrzeugbuchung, Kontostände) scheitern optimistische Sperren ständig an Kollisionen.

#### Lösung: Pessimistic Locking (`SELECT ... FOR UPDATE`)
```java
// 1. Beim Laden sperren
PessimisticVehicle v = manager.find(PessimisticVehicle.class, 1L, LockModeType.PESSIMISTIC_WRITE);

// 2. Nachträglich in bestehender Entity sperren
manager.lock(v, LockModeType.PESSIMISTIC_WRITE);

// 3. Mit Lock-Timeout in JPQL Query
List<PessimisticVehicle> list = manager.createQuery(
    "SELECT v FROM PessimisticVehicle v WHERE v.reserved = false", PessimisticVehicle.class)
    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
    .setHint("jakarta.persistence.lock.timeout", 3000)
    .getResultList();
```

---

### 1.6 TransientPropertyValueException & PersistentObjectException

#### Symptome
- `org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing`
- `jakarta.persistence.EntityExistsException: PersistentObjectException: detached entity passed to persist`

#### Ursachen & Lösungen
| Ausnahme | Ursache | Lösung |
| :--- | :--- | :--- |
| `TransientPropertyValueException` | Eine gemanagte Entity verweist auf eine neue (*transiente*) Entity, die der DB noch nicht bekannt ist. | Entweder die Kind-Entity vor dem Commit explizit mit `manager.persist(child)` speichern ODER `cascade = {CascadeType.PERSIST, CascadeType.MERGE}` an der Beziehung deklarieren. |
| `EntityExistsException` / `PersistentObjectException` | `manager.persist(entity)` wird auf einer Entity aufgerufen, die bereits eine ID besitzt oder detached ist. | Verwende `manager.merge(entity)`, um Änderungen an einer detached Entity in den aktuellen Persistence Context zu übernehmen. |

---

### 1.7 ConstraintViolationException (Bean Validation)

#### Symptom
```text
jakarta.validation.ConstraintViolationException: Validation failed for classes [net.rentacar.model.ValidatedVehicle] during persist time
```

#### Wichtige Details
- Hibernate führt Bean Validation standardmäßig bei `pre-persist` und `pre-update` (also beim `flush()` oder `commit()`) automatisch aus.
- Die geworfene Ausnahme ist eine `jakarta.validation.ConstraintViolationException` (direkt vom Validator) bzw. wird bei Transaktions-Commits in eine `RollbackException` verpackt.
- Über `hibernate.validator.apply_to_ddl=true` werden Annotationen wie `@NotNull`, `@Size(max=...)` direkt in relationale DDL (`NOT NULL`, `VARCHAR(x)`) übersetzt.

---

### 1.8 Unzulässige Operationen auf @MappedSuperclass

#### Symptom
```text
java.lang.IllegalArgumentException: Unknown entity: net.rentacar.model.AbstractBusinessObject
```

#### Ursache
`@MappedSuperclass` ist **keine** Entity, besitzt keine eigene Datenbanktabelle und kann weder polymorph per JPQL abgefragt noch direkt über `manager.find(AbstractBusinessObject.class, id)` geladen werden.

#### Lösung
- Für `find(...)` immer die konkrete abgeleitete Entity-Klasse (z. B. `manager.find(Truck.class, id)`) übergeben.
- Sollen polymorphe Abfragen über alle Unterklassen möglich sein, muss eine echte Vererbungsstrategie (`@Inheritance(strategy = InheritanceType.JOINED)` oder `SINGLE_TABLE`) gewählt werden.

---

### 1.9 Derby-spezifische Fallstricke & Locks

#### 1. LockTimeoutException bei parallelen EntityManager-Instanzen
- **Problem:** Derby sperrt Zeilen und Tabellensegmente restriktiv. Wenn `firstManager` Daten per `persist()` oder `flush()` ändert, ohne zu committen, blockiert ein lesender Zugriff von `secondManager` bis zum Lock-Timeout.
- **Lösung:** Transaktionen immer zügig mit `commit()` oder `rollback()` abschließen.

#### 2. DDL-Warnungen beim Start (`create-drop`)
- **Beobachtung:** Beim Start werden Warnungen geloggt wie `DROP TABLE ... cannot be performed because it does not exist`.
- **Bedeutung:** Dies ist bei `jakarta.persistence.schema-generation.database.action=drop-and-create` (Hibernate-Äquivalent: `hibernate.hbm2ddl.auto=create-drop`) völlig normal. Der Provider versucht vor der Erstellung existierende Relationen zu löschen.

---

## 2. JPA Architecture & Best Practices

### 2.1 Assoziationen: FetchType & Ownership

1. **Faustregel für Fetch-Typen:**
   - `@OneToMany` und `@ManyToMany` sind standardmäßig `LAZY` – **so belassen!**
   - `@ManyToOne` und `@OneToOne` sind standardmäßig `EAGER` – **in realen Projekten explizit auf `FetchType.LAZY` umstellen:**
     ```java
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "SHOP_ID")
     private Shop shop;
     ```

2. **Ownership & Fremdschlüssel:**
   - Die Seite mit dem Fremdschlüssel (`@JoinColumn`) ist immer die **Owning Side**.
   - Die Gegenseite definiert `mappedBy = "<feldnameInOwningEntity>"` und darf **niemals** `@JoinColumn` enthalten.

---

### 2.2 Defensive Synchronisation bei bidirektionalen Beziehungen

JPA liest für relationale Fremdschlüssel nur die Owning Side aus. Im Java-Hauptspeicher sind Beziehungen jedoch unabhängig. Werden beide Seiten nicht synchron gehalten, entstehen inkonsistente Cache-Zustände.

#### Best Practice: Helper-Methoden in der Parent-Entity
```java
@Entity
public class Shop {
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new HashSet<>();

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.setShop(this); // Owning-Side synchronisieren
    }

    public void removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.setShop(null);
    }
}
```

---

### 2.3 CascadeType & orphanRemoval in der Praxis

#### Unterschied: `CascadeType.REMOVE` vs. `orphanRemoval = true`
- **`CascadeType.REMOVE`:** Löscht abhängige Kind-Datensätze nur dann, wenn das gesamte Elternobjekt (`RentalContract`) mit `manager.remove(contract)` gelöscht wird.
- **`orphanRemoval = true`:** Löscht ein Kindobjekt zusätzlich per SQL `DELETE`, wenn es aus der Java-Collection des Elternobjekts entfernt wird (`contract.removeDamage(d)`).

```java
@Entity
public class RentalContract {
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DamageRecord> damages = new ArrayList<>();

    public void removeDamage(DamageRecord damage) {
        this.damages.remove(damage);
        damage.setContract(null); // Triggert SQL DELETE beim Flush!
    }
}
```

---

### 2.4 equals() und hashCode() bei Entities

#### Die 3 goldenen Regeln:
1. **Niemals die Datenbank-ID (`@Id`) allein für `hashCode()` nutzen**, wenn diese per `@GeneratedValue` generiert wird!
   - *Grund:* Vor dem `persist()` ist `id == null` (Hashcode z. B. 0). Nach dem Speichern hat die Entity eine ID (neuer Hashcode). Wird die Entity in einem `HashSet` oder `HashMap` abgelegt, wird sie nach dem `persist()` nicht mehr wiedergefunden!
2. **Business Key (Natürlicher Schlüssel):**
   - Verwende fachlich eindeutige, unveränderliche Attribute (z. B. `fin`/Fahrgestellnummer, `iban`, `email`, `uuid`).
3. **Konstanter Hashcode als Fallback:**
   - Falls kein natürlicher Schlüssel existiert, kann `hashCode()` einen festen Wert (z. B. `getClass().hashCode()`) zurückgeben und `equals()` prüft `this == other || (id != null && id.equals(other.id))`.

---

### 2.5 Transaktionsgrenzen & Persistence Context Management

#### 1. Entity-Zustände im Überblick
```text
           persist()
 [New] -----------------> [Managed] <----------------- find() / query
                             |   ^
                    detach() |   | merge()
                    clear()  |   |
                    close()  v   |
                          [Detached]
                             |
                    remove() v
                          [Removed]
```

#### 2. Batch Processing mit Flush & Clear
Beim Verarbeiten großer Datenmengen wächst der Persistence Context (1st Level Cache) unbegrenzt und führt zu `OutOfMemoryError`.
```java
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

for (int i = 0; i < items.size(); i++) {
    em.persist(items.get(i));
    if (i > 0 && i % 50 == 0) {
        em.flush(); // Schreibt SQL-Inserts in die DB
        em.clear(); // Leert den Persistence Context
    }
}

em.getTransaction().commit();
em.close();
```

---

### 2.6 Bulk Operations (executeUpdate) & 1st-Level Cache Invalidation

#### Das Stale-Data-Problem
JPQL `UPDATE`- und `DELETE`-Befehle (`query.executeUpdate()`) werden direkt als SQL an die Datenbank gesendet und **umgehen den 1st-Level-Cache vollständig**. Bereits geladene Entities im Persistence Context behalten ihren alten Stand.

#### Das 3-Schritt-Muster
```java
// 1. Ungespeicherte Änderungen in DB flushen
manager.flush();

// 2. Massenoperation direkt auf DB ausführen
int updatedCount = manager.createQuery(
    "UPDATE BulkVehicle v SET v.dailyRate = v.dailyRate * 1.1 WHERE v.active = true")
    .executeUpdate();

// 3. Cache leeren, damit Entities frisch geladen werden!
manager.clear(); // Alternativ gezielt: manager.refresh(entity);
```

---

### 2.7 Abfrage-Optimierung: DTO-Projektionen vs. Entity-Queries

Wenn Daten nur zur Anzeige oder Weiterverarbeitung gelesen werden, ist das Laden vollständiger Entities mit allen Feldern und Proxies ineffizient.

#### DTO Constructor Expression
```java
public record VehicleSummaryDto(String brand, String modelName, String shopCity) {}

// JPQL Query:
List<VehicleSummaryDto> dtos = manager.createQuery(
    "SELECT new net.rentacar.dto.VehicleSummaryDto(v.brand, v.model.name, s.city) " +
    "FROM Vehicle v JOIN v.shop s", VehicleSummaryDto.class
).getResultList();
```
**Vorteile:**
- Extrem schnell (nur die benötigten DB-Spalten werden transferiert).
- Keine `LazyInitializationException` im UI- oder Service-Layer.
- Keine Belastung des Persistence Contexts mit Managed Entities.

---

### 2.8 Primärschlüssel-Strategien & @EmbeddedId

| Strategie | Funktionsweise | Vorteile | Nachteile |
| :--- | :--- | :--- | :--- |
| `IDENTITY` | Auto-Increment Spalte der DB | Einfach, von fast allen DBs unterstützt | **Verhindert JDBC Batching**, da JPA die ID sofort beim `persist()` per DB-Insert ermitteln muss. |
| `SEQUENCE` | DB-Sequenz (`CREATE SEQUENCE`) | **Batching-fähig**, performant mit Sequenz-Preallocation (`allocationSize = 50`) | Nicht von allen DBs (z. B. älteres MySQL) unterstützt. |
| `TABLE` | Eigene Tabelle verwaltet Zähler | Datenbankunabhängig | Langsam durch separate Tabellen-Locks und Updates. |
| `@EmbeddedId` (Empfohlen) | Zusammengesetztes `@Embeddable` ID-Objekt | Kapselt Schlüssel sauber, typsicher bei `find()` | Erfordert `Serializable`, `equals` & `hashCode`. |
| `@IdClass` (Vermeiden) | Flache Attribute in Entity + separate ID-Klasse | Historische Alternative | Redundanz in der Entity, unübersichtlich. |

---

### 2.9 Custom Type Mapping mit AttributeConverter (@Converter)

Klassische Enum-Mappings mit `@Enumerated(ORDINAL)` führen bei Code-Refactorings zu unbemerkter Datenkorruption, während `@Enumerated(STRING)` unnötig viel Speicherplatz verbraucht.

#### Lösung mit `AttributeConverter<X, Y>`:
```java
@Converter(autoApply = true)
public class FuelTypeConverter implements AttributeConverter<FuelType, String> {
    @Override
    public String convertToDatabaseColumn(FuelType type) {
        return type != null ? type.getCode() : null; // z.B. "E", "D", "P"
    }

    @Override
    public FuelType convertToEntityAttribute(String code) {
        return FuelType.fromCode(code);
    }
}
```
**Vorteile:**
- DB speichert extrem kompakte Einzelzeichen (`CHAR(1)`).
- Java-Code nutzt typsicheres, erweiterbares Enum.
- Durch `autoApply = true` global für alle Entities aktiv.

---

## 3. Entscheidungs- & Referenztabellen

### 3.1 CascadeType-Matrix

| Kaskadierungs-Typ | Wirkung auf Kind-Objekt | Typischer Einsatz |
| :--- | :--- | :--- |
| `PERSIST` | Wird beim `persist(parent)` automatisch mitgespeichert. | Aggregate Roots, Kompositionen (z. B. Rechnung -> Rechnungspositionen). |
| `MERGE` | Änderungen am Kind werden beim `merge(parent)` übernommen. | Web-Anwendungen mit Detached Objekten. |
| `REMOVE` | Wird beim `remove(parent)` mitgelöscht. | Strikte Eltern-Kind-Beziehungen (Vorsicht bei `ManyToMany`!). |
| `REFRESH` | Wird beim Neuladen des Parents ebenfalls neu aus DB geladen. | Selten, bei extern modifizierten DB-Ständen. |
| `DETACH` | Wird beim `detach(parent)` aus dem Cache entfernt. | Manuelle Cache-Verwaltung. |
| `ALL` | Beinhaltet alle oben genannten Typen. | Nur bei exklusiver Eigentümerschaft (*Composition*). |

> **Wichtig:** Verwende niemals `CascadeType.REMOVE` oder `CascadeType.ALL` auf `@ManyToMany`- oder vielen `@ManyToOne`-Beziehungen, da sonst verknüpfte Stammdaten unabsichtlich gelöscht werden!

---

### 3.2 Entity-Lifecycle-Übersicht

| Annotation | Ausführungszeitpunkt | Anwendungsfall |
| :--- | :--- | :--- |
| `@PrePersist` | Vor dem ersten `INSERT` | Setzen von Erstelldatum (`createdAt = Instant.now()`), Initialwerten. |
| `@PostPersist` | Nach dem erfolgreichen `INSERT` | Benachrichtigungen, Audit-Logs mit generierter ID. |
| `@PreUpdate` | Vor dem `UPDATE` | Aktualisieren von Änderungsdatum (`updatedAt = Instant.now()`). |
| `@PostUpdate` | Nach dem `UPDATE` | Event-Publishing, Cache-Invalidierung. |
| `@PreRemove` | Vor dem `DELETE` | Konsistenzprüfungen, Abhängigkeiten bereinigen. |
| `@PostRemove` | Nach dem `DELETE` | Aufräumarbeiten, Audit-Logging. |
| `@PostLoad` | Nach dem Laden aus der DB | Initialisieren flüchtiger (`@Transient`) Felder. |

---
