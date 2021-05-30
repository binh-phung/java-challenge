### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 3 years experience in Java and I started to use Spring Boot from last year
- I'm a beginner and just recently learned Spring Boot
- I know Spring Boot very well and have been using it for many years

# NEW IMPLEMENTATION AND REFACTOR

## What changed

1. Protect API Endpoint by using Spring Security
   - the API Endpoint is protected by Spring Security, any Request that have header Authorization: Bearer TOKEN_VALUE can make request otherwise 403
   - User data is in-memory data.
   - to login, Client have to make a request to /login and send {userName,password} (user1,user1Pass),(user2,user2Pass),(admin,adminPass)
2. Reimplement Employee Controller. 
   - remove System.out.println... , it make request content for console and then become very slow.
   - check for conditions and then return coresponding response.
   - Remove property based dependency injection. (if need be, better to use constructor-based)
3. Rename methods in EmployeeService for more intent and add isExisting method
   - Rename method such as save -> add for more intents
   - Remove property based dependency injection. (if need be, better to use constructor-based)
   - add isExisting method to check if employee is existing or not, better than retrive a whole object and check for null.
4. Employee Entities 
   - change Interger to normal int so that can avoid boxing,unboxing.
5. Add mvc mock test for EmployeeController.
6. Comments
   - honestly i prefer intent method name and clean code rather than comments. Comment may be  obsolete because of change of implementation, put more pressure on   mantenance.
   - But i am willing to follow team convention.
   
## If i have more time

1. Learn more about Spring Security with Oauth2 and JWT to better protect the endpoint, Intergrate with user data in database not in memory data
2. Create more intergration test with a true database.
3. Create unit test for service layer if need be

## My Experiences.

-  I have more than 5 years working with Java and Spring framework
-  I have not designed API before but i have experienced developing web application End-to-end.
-  I have a solid programming skill, Knowledge and very keen on Architecture.
-  I just started using Spring boot recently
-  Swagger is new to me.



