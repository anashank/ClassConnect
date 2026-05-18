# ClassConnect

A Java web app that helps students find compatible study partners and manage group coursework.

ClassConnect scores compatibility between users based on schedule overlap and academic profile (grade, school), then surfaces the best matches. From there, students can form study groups, track assignments, coordinate schedules, and message each other — all in one place.

## Features

- **Compatibility matching** — scores students by schedule overlap and shared academic profile
- **Study groups** — create and join groups by subject
- **Assignments** — shared assignment tracker per group
- **Scheduling** — coordinate availability across group members
- **Friends & messaging** — connect and communicate with matches

## Tech stack

| Layer | Tech |
|---|---|
| Backend | Java, Spring Boot |
| Frontend | Vaadin |
| Database | SQLite |
| Build | Maven |
| Containerization | Docker |

## Setup

**With Docker:**
```bash
docker build -t classconnect .
docker run -p 8080:8080 classconnect
```

**Without Docker:**
```bash
./mvnw spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).
