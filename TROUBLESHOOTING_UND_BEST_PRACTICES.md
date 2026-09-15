# JPA & Hibernate: Troubleshooting- & Best-Practice-Guide

Dieser Leitfaden fasst die wichtigsten Fallstricke, Fehlermeldungen und Best Practices aus der Praxis zusammen. Er dient Seminarteilnehmern als kompaktes Nachschlagewerk während der Übungen und für reale Projekte.

---

## Inhaltsverzeichnis

1. [Typische Fehlerbilder & Troubleshooting](#1-typische-fehlerbilder--troubleshooting)
   - [LazyInitializationException](#11-lazyinitializationexception)
   - [Das N+1-Select-Problem](#12-das-n1-select-problem)
   - [OptimisticLockException & StaleObjectStateException](#13-optimisticlockexception--staleobjectstateexception)
   - [TransientPropertyValueException & PersistentObjectException](#14-transientpropertyvalueexception--persistentobjectexception)
   - [ConstraintViolationException (Bean Validation)](#15-constraintviolationexception-bean-validation)
   - [Unzulässige Operationen auf @MappedSuperclass](#16-unzulässige-operationen-auf-mappedsuperclass)
   - [Derby-spezifische Fallstricke & Locks](#17-derby-spezifische-fallstricke--locks)
2. [JPA Architecture & Best Practices](#2-jpa-architecture--best-practices)
   - [Assoziationen: FetchType & Ownership](#21-assoziationen-fetchtype--ownership)
   - [Defensive Synchronisation bei bidirektionalen Beziehungen](#22-defensive-synchronisation-bei-bidirektionalen-beziehungen)
   - [equals() und hashCode() bei Entities](#23-equals-und-hashcode-bei-entities)
   - [Transaktionsgrenzen & Persistence Context Management](#24-transaktionsgrenzen--persistence-context-management)
   - [Abfrage-Optimierung: DTO-Projektionen vs. Entity-Queries](#25-abfrage-optimierung-dto-projektionen-vs-entity-queries)
   - [Primärschlüssel-Strategien im Vergleich](#26-primärschlüssel-strategien-im-vergleich)
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

### 1.3 OptimisticLockException & StaleObjectStateException

#### Symptom
```text
jakarta.persistence.OptimisticLockException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)
```

#### Ursache
Zwei parallele Transaktionen haben dieselbe Entity mit `@Version` geladen. Transaktion A speichert zuerst und erhöht die Versionsnummer (z. B. von 1 auf 2). Wenn Transaktion B speichert, schlägt das `UPDATE ... WHERE id = ? AND version = 1` fehl, da 0 Zeilen verändert wurden.

#### Lösungsstrategie
1. **Fachliche Behandlung:** OptimisticLockException fangen, Transaktion zurückrollen (`rollback()`), den aktuellen Stand aus der Datenbank neu laden (`clear()` / `find()`) und den Beuser informieren oder den Vorgang wiederholen (Retry-Pattern).
2. **Sauberes Concurrency-Handling im Test:**
   ```java
   firstManager.getTransaction().commit(); // Erhöht Version in der DB
   
   assertThrows(OptimisticLockException.class, () -> {
       secondManager.flush(); // Bemerkt veraltete Version
   });
   secondManager.getTransaction().rollback(); // Verbindung sauber aufräumen
   ```

---

### 1.4 TransientPropertyValueException & PersistentObjectException

#### Symptome
- `org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing`
- `jakarta.persistence.EntityExistsException: PersistentObjectException: detached entity passed to persist`

#### Ursachen & Lösungen
| Ausnahme | Ursache | Lösung |
| :--- | :--- | :--- |
| `TransientPropertyValueException` | Eine gemanagte Entity verweist auf eine neue (*transiente*) Entity, die der DB noch nicht bekannt ist. | Entweder die Kind-Entity vor dem Commit explizit mit `manager.persist(child)` speichern ODER `cascade = {CascadeType.PERSIST, CascadeType.MERGE}` an der Beziehung deklarieren. |
| `EntityExistsException` / `PersistentObjectException` | `manager.persist(entity)` wird auf einer Entity aufgerufen, die bereits eine ID besitzt oder detached ist. | Verwende `manager.merge(entity)`, um Änderungen an einer detached Entity in den aktuellen Persistence Context zu übernehmen. |

---

### 1.5 ConstraintViolationException (Bean Validation)

#### Symptom
```text
jakarta.validation.ConstraintViolationException: Validation failed for classes [net.rentacar.model.ValidatedVehicle] during persist time
```

#### Wichtige Details
- Hibernate führt Bean Validation standardmäßig bei `pre-persist` und `pre-update` (also beim `flush()` oder `commit()`) automatisch aus.
- Die geworfene Ausnahme ist eine `jakarta.validation.ConstraintViolationException` (direkt vom Validator) bzw. wird bei Transaktions-Commits in eine `RollbackException` verpackt.
- Über `hibernate.validator.apply_to_ddl=true` werden Annotationen wie `@NotNull`, `@Size(max=...)` direkt in relationale DDL (`NOT NULL`, `VARCHAR(x)`) übersetzt.

---

### 1.6 Unzulässige Operationen auf @MappedSuperclass

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

### 1.7 Derby-spezifische Fallstricke & Locks

#### 1. LockTimeoutException bei parallelen EntityManager-Instanzen
- **Problem:** Derby sperrt Zeilen und Tabellensegmente restriktiv. Wenn `firstManager` Daten per `persist()` oder `flush()` ändert, ohne zu committen, blockiert ein lesender Zugriff von `secondManager` bis zum Lock-Timeout.
- **Lösung:** Transaktionen immer zügig mit `commit()` oder `rollback()` abschließen.

#### 2. DDL-Warnungen beim Start (`create-drop`)
- **Beobachtung:** Beim Start werden Warnungen geloggt wie `DROP TABLE ... cannot be performed because it does not exist`.
- **Bedeutung:** Dies ist bei `hibernate.hbm2ddl.auto=create-drop` völlig normal. Hibernate versucht vor der Erstellung existierende Relationen zu löschen.

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

### 2.3 equals() und hashCode() bei Entities

#### Die 3 goldenen Regeln:
1. **Niemals die Datenbank-ID (`@Id`) allein für `hashCode()` nutzen**, wenn diese per `@GeneratedValue` generiert wird!
   - *Grund:* Vor dem `persist()` ist `id == null` (Hashcode z. B. 0). Nach dem Speichern hat die Entity eine ID (neuer Hashcode). Wird die Entity in einem `HashSet` oder `HashMap` abgelegt, wird sie nach dem `persist()` nicht mehr wiedergefunden!
2. **Business Key (Natürlicher Schlüssel):**
   - Verwende fachlich eindeutige, unveränderliche Attribute (z. B. `fin`/Fahrgestellnummer, `iban`, `email`, `uuid`).
3. **Konstanter Hashcode als Fallback:**
   - Falls kein natürlicher Schlüssel existiert, kann `hashCode()` einen festen Wert (z. B. `getClass().hashCode()`) zurückgeben und `equals()` prüft `this == other || (id != null && id.equals(other.id))`.

---

### 2.4 Transaktionsgrenzen & Persistence Context Management

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

### 2.5 Abfrage-Optimierung: DTO-Projektionen vs. Entity-Queries

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

### 2.6 Primärschlüssel-Strategien im Vergleich

| Strategie | Funktionsweise | Vorteile | Nachteile |
| :--- | :--- | :--- | :--- |
| `IDENTITY` | Auto-Increment Spalte der DB | Einfach, von fast allen DBs unterstützt | **Verhindert JDBC Batching**, da JPA die ID sofort beim `persist()` per DB-Insert ermitteln muss. |
| `SEQUENCE` | DB-Sequenz (`CREATE SEQUENCE`) | **Batching-fähig**, performant mit Sequenz-Preallocation (`allocationSize = 50`) | Nicht von allen DBs (z. B. älteres MySQL) unterstützt. |
| `TABLE` | Eigene Tabelle verwaltet Zähler | Datenbankunabhängig | Langsam durch separate Tabellen-Locks und Updates. |
| `@IdClass` / `@EmbeddedId` | Zusammengesetzter Schlüssel | Für bestehende Alttabellen ohne künstlichen PK | Aufwendiger im Handling; erfordert `Serializable`, `equals` & `hashCode`. |

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
