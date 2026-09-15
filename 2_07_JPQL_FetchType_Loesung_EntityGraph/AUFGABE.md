# Lösungsmodul: 2_07_JPQL_FetchType_Loesung_EntityGraph

## Lösungsweg mit EntityGraph (JPA 2.1+)

Im Gegensatz zu `2_07_..._Loesung_FetchJoin` (JPQL `JOIN FETCH`) und `2_07_..._Loesung_Eager` (statisches `FetchType.EAGER` im Mapping) nutzt dieses Lösungsbeispiel einen **Entity Graph**:

```java
EntityGraph<Shop> graph = manager.createEntityGraph(Shop.class);
graph.addAttributeNodes("carpool");

List<Shop> shops = manager
    .createQuery("SELECT s FROM Shop s", Shop.class)
    .setHint("jakarta.persistence.fetchgraph", graph)
    .getResultList();
```

### Vorteile von Entity Graphs:
1. Das Mapping im Modell bleibt sauber auf `FetchType.LAZY`.
2. Keine Duplizierung von Ergebniszeilen im SQL wie bei Collection Fetch Joins.
3. Flexibel steuerbar als `fetchgraph` (alles nicht Erwähnte ist Lazy) oder `loadgraph` (Default-FetchType des Modells bleibt für nicht Erwähntes erhalten).
