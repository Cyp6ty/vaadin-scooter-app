# Vaadin Scooter App

Course project for the Java Web Development course.

The application is built using Vaadin and Spring Boot.
The project includes Maven Wrapper, so Maven does not need to be installed separately on the machine.


When the application is launched, the following default users are created automatically:

| Username      | Password |
|----------------|----------|
| User           | user123 |
| Super          | super123 |
| Administrator  | admin123 |

---

## Sample Data

The project includes an optional SQL file for inserting example data into the H2 database:

```text
src/main/resources/sample-data.sql
```

The data was originally created through the application's user interface and exported from the H2 console.

The SQL file can be executed manually in the H2 Console if example data is needed for testing the application.

The script uses `MERGE INTO` statements so it can be executed multiple times without causing duplicate primary key errors.

---

## Technologies
- Java
- Spring Boot
- Vaadin
- Maven
- PostgreSQL
- Docker

----

## Cloning the Project Clone the repository:

```bash
git clone https://github.com/Cyp6ty/vaadin-scooter-app.git
````

Navigate to the project directory:

```bash
cd vaadin-scooter-app
```

---

### Requirements

To run the project, you need:

* JDK 21 or newer
* JAVA_HOME configured as an environment variable

---

### Running the Application

### Windows PowerShell

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

---

### Docker

The application can also be started using Docker:

```bash
docker compose up --build
```

---



