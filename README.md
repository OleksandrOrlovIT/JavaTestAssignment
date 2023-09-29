# JavaTestAssignment Orlov Oleksandr
This application uses :
Java Version: Java 17
Spring Boot Version: 3.0.7
Lombok,H2 Database, Hibernate Validator, Jackson Datatype JSR 310, Maven.

Requirements:

Using the resources listed below learn what is RESTful API and what are the best practices to implement it 
According to the requirements implement the RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users. 

1. It has the following fields:
1.1. Email (required). Add validation against email pattern
1.2. First name (required)
1.3. Last name (required)
1.4. Birth date (required). Value must be earlier than current date
1.5. Address (optional)
1.6. Phone number (optional)
   
![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/61a944ef-149f-48e6-ac06-890f32c68e5e)

It has the following functionality:
   
2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
   In UserServiceImpl checks for 18 years old.
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/c82a1455-7f49-4912-8b66-058b6f619569)

   In PropertySourceResolver app takes minAge from application.properties.
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/6e12612b-f03b-42b1-aad0-9060cae62301)

   application.properties file.
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/b1d3dbb0-b02a-4933-9dfe-7b891d41a4f6)

   Method to create a user in UserController.class .
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/e31c9d8e-0f69-4f8d-b5fb-6691aecbbe93)

2.2. Update one/some user fields
   Method to update one or some fields.
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/4b805f51-9d07-458e-bd18-2f9b855bcd40)

   Method updateUserPartially() in UserServiceImpl sets all nonnull fields in newUser to foundUser.
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/ab675c42-f9af-464e-8c98-d3c506d2d30b)

2.3. Update all user fields
    Method updateUser() use PutMapping and already mentioned save() from UserServiceImpl to save updated user.
    ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/3db63fab-07b6-47d0-b212-fa0fde36a075)

2.4. Delete user

  Method deleteUser() deletes user using id.  
  ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/6725cd39-51f3-4490-9cd7-2329d998f07a)

  Method to delete user in UserServiceImpl.
  
  ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/17e733b5-41a2-460c-a959-e3db161bdd26)


2.5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”.Should return a list of objects

Method takes 2 LocalDates from http request.

![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/66f6d024-8ec6-4cd1-a61e-7c907a02827b)

Method to find all users in range in UserServiceImpl.
![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/e018b41c-1aea-4d71-9533-6970b8324c54)

3. Code is covered by unit tests using Spring
   I wrote @DataJpaTest for User.class, @SpringBootTest for UserServiceImpl
   and @ExtendWith(MockitoExtension.class) UserControllerTest for Usercontroller.
   
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/852ec481-a754-4f3c-af4a-99789349b336)
   
    Window after tests run.
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/79f6d2f6-c405-4646-b8b7-d6030bd61868)

4. Code has error handling for REST
   ExceptionController.class is RestControllerAdvice which looks for exceptions while application is running.
   
   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/82125f22-6bc7-49f0-9b9f-e6ec330b3a71)
5. API responses are in JSON format
   All api responses are in JSON format and all possible use cases were tested in unitTesting and Postman.

   ![image](https://github.com/OleksandrOrlovIT/JavaTestAssignment/assets/86959421/02a501e6-ed52-4217-9813-065f2580fe81)

