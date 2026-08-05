# Library Management System

A full-stack desktop application built with Java and JavaFX, featuring a MySQL database backend and dual interface support (GUI + Console).

## Technologies

- Java
- JavaFX
- MySQL
- JDBC
- BCrypt

## Features

- Customer and Administrator roles with secure login
- Borrow, return, and reserve books
- Reservation queue system with priority ordering
- Overdue fines calculation
- Invoice generation
- BCrypt password hashing for secure authentication
- Dual interface — JavaFX GUI and Console UI

## Setup

1. Clone the repository.

2. Import the database schema: open `library_schema.sql` (in the `dumps` folder) in MySQL Workbench and run it to create the `library` database.

3. Set the following environment variables so the application can connect to your database:

```
DB_URL=jdbc:mysql://localhost:3306/library
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password
ADMIN_MASTER_PASSWORD=choose_your_own_value
```

For a remote database, use that host in `DB_URL` — e.g. `jdbc:mysql://your-instance.region.rds.amazonaws.com:3306/library`

You can set these in your IDE's run configuration (IntelliJ: **Run → Edit Configurations → Environment variables**) or as system-wide environment variables.

4. Run `Main.java`.

Database credentials are read from the environment — no credentials are stored in the source code.

## Administrator Registration

Registering an Administrator account requires the master password set in the
`ADMIN_MASTER_PASSWORD` environment variable. Choose your own value — there is
no default, and admin registration is disabled if the variable is not set.

## Project Structure

- `src/` — application source code
- `DatabaseSchemas/` — ERD and relational schema design
- `dumps/` — SQL schema dump (`library_schema.sql`)
- `UML Diagram/` — system UML diagram
