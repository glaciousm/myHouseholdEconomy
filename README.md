# myHouseholdEconomy

A personal finance application built with JavaFX.

## Prerequisites

- **Java** 8 or newer
- **Maven** 3.x

## Build

From the project root run:

```bash
mvn clean package
```

The compiled application will be placed under `target/` as `myHouseholdEconomy-0.8.2.jar`.

## Start the application

After building you can launch the GUI with:

```bash
java -jar target/myHouseholdEconomy-0.8.2.jar
```

Alternatively run the `MainApp` class via Maven:

```bash
mvn exec:java -Dexec.mainClass="gr.mmam.myHouseholdEconomy.MainApp"
```

## Database

The application stores its data in an SQLite database file named `database.db` located in the project root directory. The file is created automatically if it does not already exist.
