***Spring Boot application for processing Supermarket purchases***

<u>***Technologies:***</u> 

- JDK version 18 
- Maven 3.8.1 
- Spring Boot 2.6.3
- MySQL 8.0.28



***<u>Initialization:</u>***
Firstly you need to clone the git repository: 
- ```
  git clone https://github.com/ivanivanovv13/EndavaFinalExam.git
```

After completion of this guide you need to open your Java editor and load clonned resources

- File -> Open -> {path to the directory where you clone this repository}

- Go to `resources/application.properties` and add properties e.g. 

- ```
  spring.jpa.hibernate.ddl-auto=create
  spring.datasource.url=jdbc:mysql://localhost:3306/user_db?createDatabaseIfNotExist=true
  spring.datasource.username= YOUR_USER_NAME_HERE
  spring.datasource.password= YOUR_PASSWORD_HERE
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect
  ```
  
- Go to `Application.java`

- Click right button and select `Run 'Application'`

==========Congratulations, you are running Supermarket API now==========

