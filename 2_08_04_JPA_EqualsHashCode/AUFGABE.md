# Übung: 2_08_04 Equals und HashCode für JPA-Entities

## Lernziel

Das korrekte Implementieren von `equals()` und `hashCode()` für JPA-Entities verstehen, die Risiken veränderlicher Business-Keys und generierter IDs in Hash-basierten Collections (`HashSet`, `HashMap`) beherrschen und die Identitätsregeln im Persistence Context einhalten.

## Ausgangszustand

In `src/net/rentacar/model/ComparableVehicle.java` fehlen saubere `equals()` und `hashCode()` Methoden oder sie basieren fälschlicherweise auf veränderlichen Attributen. In `test/net/rentacar/TestEqualsHashCode.java` schlagen die Tests für die Verwaltung in `HashSet` fehl.

## Aufgabe

Bearbeite `src/net/rentacar/model/ComparableVehicle.java`:

1. **`equals()` auf Basis der fachlichen Identität bzw. ID implementieren:**
   - Zwei Entities sind gleich, wenn sie dieselbe Klasse und dieselbe nicht-null `id` besitzen (bzw. bei transienten Instanzen die Java-Objektidentität).
   - Achte darauf, `instanceof` und Getter zu verwenden, um Proxies (z. B. von Hibernate) korrekt zu unterstützen.

2. **`hashCode()` konsistent halten:**
   - `hashCode()` muss über den gesamten Lebenszyklus eines Objekts konstant bleiben (z. B. konstanter Klassen-Hash oder unveränderlicher Business-Key).
   - Ein HashCode, der vor dem Persistieren `0` (null-ID) und danach den Wert der DB-ID liefert, zerstört die Auffindbarkeit in bestehenden `HashSet`s!

3. **Verhalten in Sets testen:**
   - In `test/net/rentacar/TestEqualsHashCode.java`:
     - Zwei Instanzen mit derselben ID ergeben in einem `HashSet` exakt ein Element.
     - Wird die Eigenschaft `brand` nachträglich geändert, bleibt das Objekt im `HashSet` auffindbar.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_08_04_JPA_EqualsHashCode -am test
```

## Erfolgskriterium

Der Test `test/net/rentacar/TestEqualsHashCode.java` läuft mit allen 2 Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

## Reflexion

1. Warum führt die automatische Generierung von `equals`/`hashCode` über alle Felder (z. B. durch Lombok `@Data` oder IDE-Generatoren) bei JPA-Entities mit Lazy-Relations und veränderlichen Attributen oft zu schwer auffindbaren Bugs?
2. Warum reicht im Persistence Context der `==`-Vergleich für managed Entities mit derselben ID, während für detached Entities `equals()` zwingend erforderlich ist?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_08_04_JPA_EqualsHashCode_Loesung`.
