# Infrastructure Back-end

## 1. Install IntelliJ IDE
   * Link download: [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows)

## Open project
   * Open IntelliJ. At the top menu, choose File > Open
   * Navigate the project folder.
   * Wait util IDE auto build completely
    
## Config database and run project
   * Open application.properties. And config to your database.
      `spring.jpa.hibernate.ddl-auto=update
      spring.datasource.url=jdbc:mysql://localhost:3306/infrastructure
      spring.datasource.username=root
      spring.datasource.password=root123456789
      spring.jpa.show-sql=true`
      
   * Then click Run in menu
   * Navigate to `http://localhost:8080/swagger-ui.html/`