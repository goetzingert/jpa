# JPA 3.x / Hibernate 6.x Spickzettel (Cheat Sheet)

Kompaktes Nachschlagewerk für den Seminaralltag und die tägliche Entwicklung mit Jakarta Persistence (JPA) und Hibernate.

---

## 1. Entity-Grundlagen & Feld-Mappings

### Entity-Deklaration
```java
@Entity
@Table(name = "T_VEHICLE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"LICENSE_PLATE"})
})
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LICENSE_PLATE", nullable = false, length = 20)
    private String licensePlate;

    @Column(name = "PRICE_PER_DAY", precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING) // Immer STRING statt ORDINAL!
    @Column(name = "FUEL_TYPE", length = 20)
    private FuelType fuelType;

    @Temporal(TemporalType.DATE) // Nur für java.util.Date / Calendar nötig; nicht für LocalDate!
    private LocalDate registrationDate;

    @Transient // Wird nicht in der Datenbank persistiert
    private boolean temporarySelected;

    // Pflicht: No-Arg Konstruktor (mindestens package-private / protected)
    protected Vehicle() {}

    public Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
```

### Primärschlüssel-Strategien (`@GeneratedValue`)
| Strategie | Annotation | Verhalten & Verwendung |
| :--- | :--- | :--- |
| **IDENTITY** | `strategy = GenerationType.IDENTITY` | Auto-Increment Spalte der DB (MySQL, PostgreSQL IDENTITY, Derby). Deaktiviert JDBC Batch-Inserts beim `persist()`. |
| **SEQUENCE** | `@SequenceGenerator(...)`<br>`strategy = GenerationType.SEQUENCE` | DB-Sequenz (Oracle, PostgreSQL). Erlaubt optimiertes Pre-Allocation Batching (`allocationSize = 50`). |
| **TABLE** | `@TableGenerator(...)`<br>`strategy = GenerationType.TABLE` | Eigene DB-Generatortabelle. Portabel, aber langsamer durch separate Locking-Transaktionen. |
| **AUTO** | `strategy = GenerationType.AUTO` | Provider wählt automatisch passende Strategie (Hibernate wählt bei modernen DBs SEQUENCE/TABLE). |

#### Sequence-Generator Beispiel:
```java
@Id
@SequenceGenerator(name = "veh_seq_gen", sequenceName = "SEQ_VEHICLE", allocationSize = 50)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "veh_seq_gen")
private Long id;
```

### Zusammengesetzte Schlüssel (`@IdClass` vs. `@EmbeddedId`)
```java
// Variante 1: @IdClass
@Entity
@IdClass(KundeId.class)
public class Kunde {
    @Id private String firmenCode;
    @Id private Long kundenNummer;
}

public class KundeId implements Serializable {
    private String firmenCode;
    private Long kundenNummer;
    // Pflicht: equals() & hashCode(), no-arg Konstruktor
}

// Variante 2: @EmbeddedId
@Entity
public class Kunde {
    @EmbeddedId
    private KundeId id;
}
```

---

## 2. Value Objects (`@Embeddable` & `@Embedded`)

```java
@Embeddable
public class Address {
    @Column(nullable = false)
    private String street;
    private String zipCode;
    private String city;
    // Konstruktoren, Getter/Setter
}

@Entity
public class Customer {
    @Id @GeneratedValue private Long id;

    @Embedded
    private Address homeAddress;

    // Mehrfache Verwendung desselben Embeddables über Spalten-Overrides:
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "BILLING_ZIP")),
        @AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY"))
    })
    private Address billingAddress;
}
```

---

## 3. Beziehungs-Mappings (Assoziationen)

### Übersicht: Kardinalitäten & Standard-Fetch-Typen
| Beziehung | Standard-FetchType | Empfehlung | Owning Side (Fremdschlüsselträger) |
| :--- | :--- | :--- | :--- |
| `@ManyToOne` | **EAGER** | Immer explizit auf **LAZY** ändern! | Die Seite mit der Annotation (`@JoinColumn`) |
| `@OneToOne` | **EAGER** | Auf **LAZY** ändern (optional = false bei bidirektional) | Die Seite mit `@JoinColumn` |
| `@OneToMany` | **LAZY** | **LAZY** belassen | Die Gegenseite (`@ManyToOne` trägt `mappedBy`) |
| `@ManyToMany`| **LAZY** | **LAZY** belassen | Beliebig wählbar (die Seite mit `@JoinTable`) |

---

### 1:1 Beziehung (One-to-One)
```java
// Owning Side (besitzt FK 'USER_ID' in Tabelle T_PASS)
@Entity
public class Passport {
    @Id @GeneratedValue private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", unique = true, nullable = false)
    private User user;
}

// Inverse Side (liest nur über 'user' Property in Passport)
@Entity
public class User {
    @Id @GeneratedValue private Long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Passport passport;
}
```

---

### 1:N / N:1 Beziehung (Bidirektional)
```java
// Inverse / Eltern-Seite (1)
@Entity
public class Shop {
    @Id @GeneratedValue private Long id;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    // Defensive Helper-Methode für bidirektionale Konsistenz
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setShop(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setShop(null);
    }
}

// Owning / Kind-Seite (N) - trägt den Fremdschlüssel
@Entity
public class Vehicle {
    @Id @GeneratedValue private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SHOP_ID", nullable = false)
    private Shop shop;
}
```

---

### N:M Beziehung (Many-to-Many)
```java
// Owning Side
@Entity
public class Vehicle {
    @Id @GeneratedValue private Long id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "T_VEHICLE_FEATURE",
        joinColumns = @JoinColumn(name = "VEHICLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "FEATURE_ID")
    )
    private Set<Feature> features = new HashSet<>();
}

// Inverse Side
@Entity
public class Feature {
    @Id @GeneratedValue private Long id;

    @ManyToMany(mappedBy = "features")
    private Set<Vehicle> vehicles = new HashSet<>();
}
```

---

## 4. Vererbungsstrategien

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // oder JOINED, TABLE_PER_CLASS
@DiscriminatorColumn(name = "VEHICLE_CATEGORY", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BASE")
public abstract class VehicleType {
    @Id @GeneratedValue private Long id;
    private String name;
}

@Entity
@DiscriminatorValue("TRUCK")
public class Truck extends VehicleType {
    private Double maxPayload;
}
```

### Vergleich der Vererbungsstrategien
| Strategie | DB-Tabellen | Performance Abfragen | Schemakonsistenz (NOT NULL) | Einsatzgebiet |
| :--- | :--- | :--- | :--- | :--- |
| `SINGLE_TABLE` *(Default)* | 1 gemeinsame Tabelle | Schnell (keine Joins/Unions) | Subklassen-Felder müssen `NULL` erlauben | Flache Hierarchien, wenige Subklassen-Felder |
| `JOINED` | 1 Basistabelle + 1 Tabelle je Subklasse | Normal (Joins über PK/FK) | Exzellent (echte NOT NULL & FK Constraints) | Tiefe Hierarchien, viele spezifische Attribute |
| `TABLE_PER_CLASS` | 1 Tabelle je konkrete Klasse | Langsam bei polymorphen Abfragen (`UNION`) | Gut je Tabelle | Selten polymorphe Abfragen |
| `@MappedSuperclass` | Keine eigene Tabelle | Schnell (Attribute in Kindtabellen) | Gut je Entity | Gemeinsame technische Basisfelder (`id`, `createdAt`) |

---

## 5. EntityManager Lifecycle & Operationen

```
          [ New / Transient ]
             |          ^
     persist()          | (nach DB delete)
             v          |
          [ Managed ] -------- remove() -------> [ Removed ]
             |     ^                                 |
      detach()     |                                 |
       clear()     | merge()                         | commit()
       close()     |                                 v
             v     |                            [ Gelöscht ]
          [ Detached ]
```

### Kernmethoden des `EntityManager`
```java
// 1. Objekt im Persistence Context registrieren (Insert bei Flush/Commit)
em.persist(entity);

// 2. Objekt anhand des Primärschlüssels suchen (Cache -> DB)
Vehicle v = em.find(Vehicle.class, 1L);

// 3. Detached Entity wieder in den Context überführen (liefert NEUE gemanagte Instanz!)
Vehicle managedV = em.merge(detachedV);

// 4. Objekt zum Löschen vormerken (Delete bei Flush/Commit)
em.remove(managedV);

// 5. Änderungen sofort per SQL in die DB spülen (ohne Commit)
em.flush();

// 6. Persistence Context komplett leeren (alle Entities werden DETACHED)
em.clear();

// 7. Einzelne Entity aus dem Context entfernen
em.detach(managedV);

// 8. Entity mit aktuellem DB-Stand überschreiben
em.refresh(managedV);

// 9. Prüfen, ob Entity aktuell gemanagt wird
boolean isManaged = em.contains(entity);
```

### Transaktionssteuerung (Resource-Local)
```java
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();
try {
    tx.begin();
    
    // ... DB-Operationen ...
    
    tx.commit();
} catch (Exception e) {
    if (tx.isActive()) {
        tx.rollback();
    }
    throw e;
} finally {
    em.close();
}
```

---

## 6. JPQL Syntax-Referenz

### Grundlegende Abfragen & Parameter
```sql
-- Benannte Parameter (:name)
SELECT v FROM Vehicle v WHERE v.licensePlate = :plate AND v.pricePerDay <= :maxPrice

-- Indexierte Parameter (?1)
SELECT v FROM Vehicle v WHERE v.licensePlate = ?1

-- Sortierung und Filterung
SELECT v FROM Vehicle v WHERE v.fuelType IN ('PETROL', 'DIESEL') ORDER BY v.pricePerDay DESC
```

### Joins & N+1 Vermeidung
```sql
-- Inner Join (lädt nur passende Beziehungen)
SELECT v FROM Vehicle v JOIN v.shop s WHERE s.city = 'München'

-- Left Outer Join
SELECT s, v FROM Shop s LEFT JOIN s.vehicles v

-- JOIN FETCH (Lädt Assoziationen sofort mit in 1 SQL-Query -> Löst N+1 Problem!)
SELECT DISTINCT s FROM Shop s JOIN FETCH s.vehicles WHERE s.city = :city
```

### Projektionen & Constructor Expressions (DTOs)
```sql
-- Skalare Einzelwerte
SELECT v.licensePlate, v.pricePerDay FROM Vehicle v

-- Typsichere DTO Constructor Expression (SELECT new ...)
SELECT new net.rentacar.dto.VehicleSummaryDto(v.id, v.licensePlate, v.shop.name) 
FROM Vehicle v WHERE v.pricePerDay < 100.0
```

### Aggregation & Gruppierung
```sql
SELECT s.city, COUNT(v), AVG(v.pricePerDay), MAX(v.pricePerDay)
FROM Shop s JOIN s.vehicles v
GROUP BY s.city
HAVING COUNT(v) >= 5
```

### JPQL Ausführung im Java-Code
```java
TypedQuery<Vehicle> query = em.createQuery(
    "SELECT v FROM Vehicle v WHERE v.pricePerDay <= :maxPrice", Vehicle.class);
query.setParameter("maxPrice", new BigDecimal("75.00"));

// Pagination: Seite 2 bei 20 Einträgen pro Seite
query.setFirstResult(20);  // Offset
query.setMaxResults(20);   // Limit

List<Vehicle> results = query.getResultList();

// Einzelergebnis (wirft NoResultException oder NonUniqueResultException)
Vehicle single = query.getSingleResult();
```

---

## 7. Criteria API (Typsichere dynamische Abfragen)

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);
Root<Vehicle> vehicle = cq.from(Vehicle.class);

// Dynamische Prädikate / Where-Bedingungen
List<Predicate> predicates = new ArrayList<>();

if (searchPlate != null) {
    predicates.add(cb.like(vehicle.get("licensePlate"), "%" + searchPlate + "%"));
}
if (maxPrice != null) {
    predicates.add(cb.lessThanOrEqualTo(vehicle.get("pricePerDay"), maxPrice));
}

// Join Fetch in Criteria API
Join<Vehicle, Shop> shop = (Join<Vehicle, Shop>) vehicle.fetch("shop", JoinType.INNER);

cq.select(vehicle)
  .where(cb.and(predicates.toArray(new Predicate[0])))
  .orderBy(cb.asc(vehicle.get("pricePerDay")));

List<Vehicle> result = em.createQuery(cq).getResultList();
```

---

## 8. Lifecycle Callbacks & Entity Listeners

```java
@Entity
@EntityListeners(AuditListener.class)
public class Vehicle {
    @Id @GeneratedValue private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

// Separater Listener
public class AuditListener {
    @PostPersist
    public void onPostPersist(Object entity) {
        System.out.println("Entity persistiert: " + entity);
    }
}
```

### Verfügbare Callback-Annotationen
- `@PrePersist` / `@PostPersist`
- `@PreUpdate` / `@PostUpdate`
- `@PreRemove` / `@PostRemove`
- `@PostLoad` (nach dem Laden aus der DB)

---

## 9. Concurrency & Locking

### Optimistic Locking (`@Version`)
```java
@Entity
public class Vehicle {
    @Id @GeneratedValue private Long id;

    @Version
    @Column(name = "OPTI_VERSION")
    private int version; // Typ: int, Integer, short, long oder java.sql.Timestamp
}
```

### Explizites Pessimistic Locking
```java
// Beim Suchen sperren (SELECT ... FOR UPDATE)
Vehicle v = em.find(Vehicle.class, 1L, LockModeType.PESSIMISTIC_WRITE);

// Nachträglich sperren
em.lock(v, LockModeType.PESSIMISTIC_READ);
```

---

## 10. `persistence.xml` Standard-Konfiguration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="rentacarPU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <!-- Explizit gelistete Entities -->
        <class>net.rentacar.model.Vehicle</class>
        <class>net.rentacar.model.Shop</class>

        <properties>
            <!-- JDBC Verbindung -->
            <property name="jakarta.persistence.jdbc.driver" value="org.apache.derby.jdbc.ClientDriver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:derby://localhost:1527/rentacar;create=true"/>
            <property name="jakarta.persistence.jdbc.user" value="APP"/>
            <property name="jakarta.persistence.jdbc.password" value="APP"/>

            <!-- Schema-Generierung (Standard JPA) -->
            <!-- Optionen: none | create | drop-and-create | drop -->
            <property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create"/>

            <!-- Hibernate spezifische Einstellungen -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.highlight_sql" value="true"/>
            <property name="hibernate.jdbc.batch_size" value="25"/>
            <property name="hibernate.order_inserts" value="true"/>
            <property name="hibernate.order_updates" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```
