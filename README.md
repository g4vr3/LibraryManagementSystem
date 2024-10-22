# Library Management Application

This project is a **Library Management Application** that allows for the administration of books, authors, users, and loans.

> [!IMPORTANT]
> This is a base project. Before going into production, it is essential to perform extensive testing and data entry validations, as well as implement improvements to ensure its functionality and security.

## Database Integration

The application uses **JDBC** for secure database connection and management. Secure connections are implemented, and SQL injection is avoided by using prepared statements in queries. This ensures that data is handled securely and efficiently.

## Project Structure

The project architecture is organized into several layers, each with a specific function:

- **User Interface (UI)**:
    - The **UI** provides an interactive and minimalistic experience. It allows users to perform all necessary operations to manage the library.

- **Model**:
    - **DTO (Data Transfer Object)**: Each entity in the system has a corresponding DTO that is used to transfer data between the UI and the DAO. DTOs encapsulate object properties in program memory, simplifying data manipulation.

    - **Data Access Object (DAO)**: Each entity also has a DAO that manages database access operations. DAOs encapsulate the logic required to interact with the database and are responsible for queries and updates to the data. This ensures that database access is separated from business logic.

    - **Service**: The service layer acts as an intermediary between the UI and the DAOs. This class is responsible for coordinating operations among the different components, managing the synchronization of in-memory data with the database, and applying the necessary business logic.

- **Database Connection**:
    - **DDL (Data Definition Language)**: This class is responsible for managing the database connection and executing the SQL script to create it and configure its schema. When starting the application, `DDL` checks if the database exists and, if not, creates it. It also executes an SQL file that defines the tables and relationships needed for the application. This ensures that the database is always available and correctly configured for use. It uses secure connections via JDBC and avoids SQL injections through prepared statements.

- **Exceptions**:
    - **ServiceException**: This class handles exceptions and errors that may arise during the operation of the application. `ServiceException` provides custom messages that facilitate the identification and resolution of data entry problems, missing objects, and other general application errors. This class extends `Exception`, allowing service-specific errors to be thrown and handled, thus improving the user experience when interacting with the interface.

- **Required Resources**:
    - **resources/**: This folder contains resource files required for the application.
        - **MariaDB-Driver/**: This subfolder includes the MariaDB driver .jar file, which is essential for establishing the connection to the database. Make sure to include this file in the classpath of your project for the database connection to work properly.
        - **sql/**: This folder contains the SQL script that defines the database structure, including the creation of tables and some initial data inserts. This script is used by the `DDL` class to set up the database automatically if it does not exist.

- **Project Documentation**:
    - **JavaDoc/**: This folder contains the documentation for the project. It includes detailed descriptions of the classes, methods, and their functionalities, thus facilitating the understanding of the code and its maintenance.

## User Interface

The User Interface is developed with **Swing** and uses `CardLayout` to manage different panels of the interface. The application is structured as follows:

- **Main Panel**: Contains buttons to access the management of each entity (Books, Authors, Users, Loans, Book_Author).
- **Management Panel**: Activated when an entity is selected, displaying tools to perform management operations.
- **Dynamic Input Fields**: Depending on the selected operation, the required input fields are generated dynamically.

## Application Functionalities

The application offers several functionalities for library management:

1. **Book Management**:
    - **Create, Edit, and Delete Books**: Allows users to create books, indicating their properties, edit these properties, and delete books.
    - **Search Books**: Allows users to search for books based on their ID.

2. **Author Management**:
    - **Create, Edit, and Delete Authors**: Allows users to manage authors.
    - **Search Authors**: Allows users to search for authors based on their ID.

3. **User Management**:
    - **Create, Edit, and Delete Users**: Allows users to add new users to the system and manage their information, as well as delete them.
    - **Search Users**: Allows users to search for users based on their ID.

4. **Loan Management**:
    - **Create and Delete Loans**: Allows management of book loans to users, with automatic assignment of start and end dates.
    - **Search Loans**: Allows users to view all loans made, filtering them by book or by user.

5. **Management of Relationships between Books and Authors**:
    - **Create, Search, and Delete Relationships**: Allows managing relationships between books and authors.
    - **Search**: Allows users to search all the books of an author or all the authors of a book.

> [!NOTE]
> In future implementations, the application will allow a more intuitive search, showing all the relevant information for each result, as well as the visualization of all the records for each entity.  
> The management of relationships between books and authors will be integrated into the management of the entities involved, allowing, for example, to assign one or several authors when creating a book, or to visualize the information of the authors of a book, or the books of an author from the management of the specific entity.

## Requirements

- **Java**: The application is compiled with Java 21. Make sure that you have the corresponding JDK version installed on your system.
- **Database**: A MariaDB instance is required for data management. Ensure that the server is up and running and accessible from the application.
- **JDBC Driver**: Include the MariaDB driver .jar file in the project classpath.
- **IDE**: It is recommended to use an IDE such as IntelliJ IDEA, Eclipse, or NetBeans to facilitate the development and execution of the project.

## Authors

- [g4vr3](https://github.com/g4vr3)
- [jagudo27](https://github.com/jagudo27)
