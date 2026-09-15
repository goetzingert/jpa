# Übung: 2_14 Default Entity Listener in orm.xml

## Lernziel

Einen globalen Standard-Listener (*Default Entity Listener*) über das JPA-Deployment-Deskriptor-Mapping (`src/META-INF/orm.xml`) definieren, der automatisch auf alle Entitäten der Persistence Unit angewendet wird.

## Ausgangszustand

In `src/net/rentacar/model/listener/DefaultListener.java` ist ein Listener implementiert, der beim Persistieren von Objekten XML-Dateien im Ausgabeverzeichnis erzeugt. In `src/META-INF/orm.xml` fehlt jedoch die globale Registrierung unter `<persistence-unit-defaults>`.

## Aufgabe

Bearbeite `src/META-INF/orm.xml`:

1. **Default Entity Listener in XML registrieren:**
   - Konfiguriere den Listener in `src/META-INF/orm.xml`:
     ```xml
     <persistence-unit-metadata>
         <persistence-unit-defaults>
             <entity-listeners>
                 <entity-listener class="net.rentacar.model.listener.DefaultListener"/>
             </entity-listeners>
         </persistence-unit-defaults>
     </persistence-unit-metadata>
     ```

2. **Globale Ausführung testen:**
   - In `test/net/rentacar/TestCallback.java`: Persistiere verschiedene Entitäten (`VehicleType`, `Shop`, `Vehicle`) und stelle fest, dass der `DefaultListener` für jede dieser Entitäten automatisch aufgerufen wird.

## Test und Beobachtung

Führe den Test aus:

```bash
./mvnw -pl 2_14_JPA_DefaultEntityListener -am test
```

## Erfolgskriterium

Der Test `test/net/rentacar/TestCallback.java` läuft mit allen Testmethoden fehlerfrei durch:
```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```
Die vom DefaultListener erzeugte Datei für den persistierten Shop existiert im Ausgabeverzeichnis.

## Reflexion

1. Wie können einzelne Entity-Klassen von globalen Default Listeners ausgeschlossen werden (`@ExcludeDefaultListeners`)?
2. In welcher Reihenfolge werden Callbacks ausgeführt, wenn Default Listeners, Entity Listeners und Entity-eigene Callbacks kombiniert werden?

## Lösungshinweis

Vergleiche deine Lösung bei Bedarf mit dem Referenzstand in `2_14_JPA_DefaultEntityListener_Loesung`.
