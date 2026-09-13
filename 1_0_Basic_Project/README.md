# Basic Project

## Java and Maven

Use Java 17 or newer. The project targets Java 17 through the shared Maven parent;
newer JDKs are supported as long as the build remains compatible.

Use the checked-in Maven Wrapper instead of a globally installed Maven:

```bash
./mvnw -version
```

## Test prerequisite

The JPA exercises use a Derby Network Server by default. Start Derby before running integration tests and make sure it listens on `localhost:1527` with the database name `rentacar`.

The standard JDBC settings are:

- URL: `jdbc:derby://localhost:1527/rentacar;create=true`
- User: `APP`
- Password: `APP`

Then run a module test, for example:

```bash
./mvnw -pl 1_1_JPA_DB_CONFIG_Loesung -am test
```

The tests intentionally use an external server so that participants can inspect the created schema and data while the exercises are running. The schema-generation setting is `drop-and-create` or `create-drop`, depending on the provider configuration, so use a dedicated course database.
