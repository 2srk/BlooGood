# BloGood

## Overview

The BlooGood is a Java-based application that provides a graphical user interface (GUI) for managing blogs. The application allows users to add new blogs, configure settings, upload images via FTP, and view the list of blogs fetched from a remote PHP-MySQL backend.

## Features

- **Add New Blog**: Allows users to add a new blog with title, content, and up to two images.
- **Configuration Page**: A settings page to configure FTP and backend server details.
- **View Blogs**: Displays a list of existing blogs fetched from a PHP-MySQL backend.

## Technologies Used

- **Java Swing**: For creating the GUI.
- **Apache Commons Net**: For FTP functionality.
- **JSON**: For handling JSON data.
- **PHP**: Backend scripting for data handling.
- **MySQL**: Database to store blog data.

## Setup and Installation

### Prerequisites

- **Java JDK**: Ensure Java is installed on your system.
- **Apache Maven**: For managing project dependencies.
- **XAMPP**: To run Apache server and MySQL.
- **PHP**: For backend scripts.

### Clone the Repository

```sh
git clone https://github.com/yourusername/blog-management-system.git
cd blog-management-system
```

### Install Dependencies

Ensure your `pom.xml` includes the necessary dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>commons-net</groupId>
        <artifactId>commons-net</artifactId>
        <version>3.9.0</version>
    </dependency>
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20231013</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
</dependencies>
```
Run Maven to install the dependencies:
```sh
mvn clean install
```

### Configuration

1. **Set Up MySQL Database**:
    - Create a database named `blogs`.
    - Create a table named `blog_data` with columns `blog_id`, `blog_name`, `blog_data`, `date`, `image1`, `image2`.

2. **Update PHP Configurations**:
    - Place the `blogadd.php` file in your server directory (e.g., `htdocs` for XAMPP).
    - Update database credentials in `blogadd.php`.

3. **config.properties File**:
    - Create a `config.properties` file in the root directory.
    - Add the following configurations:

    ```
    FTP Host=your-ftp-host
    FTP Username=your-ftp-username
    FTP Password=your-ftp-password
    Blog PHP URL=http://your-server-address/blogadd.php
    PHP Password=yourSecretPassword
    ```
### Running the Application

To run the application, execute the following commands:

1. **Blog Management GUI**:
    ```sh
    mvn exec:java -Dexec.mainClass="BlogManagementGUI"
    ```

2. **Configuration Page**:
    ```sh
    mvn exec:java -Dexec.mainClass="ConfigurationPage"
    ```

3. **Add Blog GUI**:
    ```sh
    mvn exec:java -Dexec.mainClass="BlogAddGUI"
    ```

## Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.
