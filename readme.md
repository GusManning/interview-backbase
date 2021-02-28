# interview-backbase

This project is a code test developed for Backbase.  This project conists of three parts tagged as Q1, Q2 and Q3. It is developed in Java 8, using Spring 2.2.2 and Maven.  

## Running

You can run this either through your IDE of choice, launching com.backbase.BackbaseApplication as a Spring Boot Application.  Or through using Maven 'mvn spring-boot:run' works for running the application and 'mvn test' works for testing. 

## Q1

Question 1 is a Course registration API 

### Create course (POST http://localhost:5000/courses)
with body
```json
{
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10
}
```

On success returns 201, defining a id in the Json or leaving title, starDate, endDate or capacity as nulls will result in an 400 being returned.  Http Code 400 will also be returned if capacity is negative, title is blank, or endDate is before startDate. 

successful execution will return something resembling the following: 
```json
{
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 10
}
```

### Search course by title (GET http://localhost:5000/courses?q=title)
Response should be 200 with body :
```json
[
  {
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 10
  },
  ...
]
```
### Get course details (GET http://localhost:5000/courses/1)
Response should be 200 with body :
```json
  {
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 5,
  "participants":[
    {"name":"Daniel", "registrationDate":"2021-05-01"},
    ...
  ]
  },
```


### Sign up user for course (POST http://localhost:5000/courses/1/add)
Body should be user details:
```json
{
  "courseId": 1,
  "registrationDate": "2021-04-01",
  "name": "Daniel"
}
```
Response should be: 
* 200 if registration was successful, and a response body similar to get course details request.
* 400 if `name` already enrolled to the course.
* 400 if `registrationDate` is 3 days before course `startDate` or after.
* 400 if course is full.
* 404 if course does not exist.

* 400 if the ID in the URL does not match the id in the json, I tend to err on the side of caution rather than ignore using input.
* 400 if name blank or null, same of registrationDate is null.
* 400 if id is provided in the Json, as users are not allowed to set system generated variables.

### Cancel user enrollment (POST http://localhost:5000/courses/1/remove)
Body should be user details:
```json
{
  "courseId": 1,
  "cancelDate": "2021-05-01",
  "name": "Daniel"
}
```
Response should be: 
* 200 if cancellation was successful, and a response body similar to get course details request.
* 404 if course does not exist or user is not enrolled to course.
* 400 if `cancelDate` is 3 days before course `startDate` or after.

* 400 if the ID in the URL does not match the id in the json, I tend to err on the side of caution rather than ignore using input.
* 400 if name blank or null, same of cancelationDate is null.
* 400 if id is provided in the Json, as users are not allowed to set system generated variables.

Note: since I used @JsonSetter to make cancelations and registrations the same object you could technically use cancelDate for a Registration or registrationDate for a cancelation and it should work.

### Thoughts

An Interesting start, I wrestled with the idea of using Data Transfer Objects made with Lombok to get the Json to match the requested outputs or using Jackson @JsonValue to define my own serialization but settled on what might a slightly more hackey solution of modifying the objects based on situation.

## Q2

Question 2 was to create a simple version of bit.ly which takes in links and returns a short code that can then be exchanged for the original link.

### Turn a long URL into a short Hex String (GET `http://localhost:5000/short?url=https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38` )

This will persist the URL in the database for 30 minutes until a scheduled job delete is.  

* Will return Http Code 400 if url is blank or not a valid URL.

### `http://localhost:5000/long?tiny=44C0173` **immediately** 

Will give back the original `https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json` if called within half an hour of the above command.

* Will return 404 if the tiny is no longer in the database, or was never in the database.
* Will return 400 if tiny is not a hash string.

### Thoughs

I originally setup profiles so that I could test independently while still having database persistance but later abandoned that when I found out how to annotate test cases to reset the database.  I felt the question strongly encouraged me to use a hash of the URL to make the code.  I later realized that this was a Hexadecimal code when my 62 bit maping was too short and that no non hex characters were used.  I later realized that my encoder could simply be replaced with Integer's fuctions to make a hex string and decode hex strings.  I kept the encoder service as a class as I feel the tests were useful as when I switched over to Integer from my original code heavy approch they found immediate problems.  I can't say I really like the idea of using a hash based code as there is more risk of collision, mabye I could have devised a collusion solution similar to how HashMap works but since I duplicated the desired output I figured this is what the client wanted.

## Q3

Question 3 is a simple forum API which allows for the posting of questions and replys to those questions.

### Post new question: `http://localhost:5000/questions`
with body:
```json
{
  "author": "Daniel",
  "message": "Message text"
}
```
Response should be 201:
```json
{
  "id": 1,
  "author": "Daniel",
  "message": "Message text",
  "replies": 0
}
```
* 500 if author or message is blank.
* 400 if the user tries to define id in the posted json.

### Post a reply to a message: `http://localhost:5000/questions/{questionId}/reply`
with body:
```json
{
  "author": "Reply author",
  "message": "Message reply text"
}
```
Response should be 201:
```json
{
  "questionId": 1,
  "id": 5,
  "author": "Reply author",
  "message": "Message reply text"
}
```
* 500 if Author or Message are null or blank.
* 400 if the user tries to define id in the posted json.


### Get a thread: `http://localhost:5000/questions/{questionId}`,
the response should look like:
```json
{
  "id": 1,
  "author": "Daniel",
  "message": "Message text",
  "replies": [
    {
       "id": 5,
       "author": "Reply author",
       "message": "Message reply text"
    },
    ...
  ]
}
```

### Get a list of questions: `http://localhost:5000/questions`
The response should look like:
```json
[
    {
      "id": 1,
      "author": "Daniel",
      "message": "Message text",     
      "replies": 0
    },
    ...
]
```

### Thoughs

I felt this one really came together.  I'd never had to do a self referencing ManyToOne relationship in JPA before.  This would have been a lot easier to conceptualize with a traditional SQL database rather than Hibernate.  Lombok with Data Transfer Objects to handle worked very well.  The Test Suite is way bigger than the hand written code.  I also realized I probably could have used @NotNull and @NotBlank to ensure Entity validation.  Although I do like having more control over the message I send back to the user.  Maybe next time I'll consider a using a Validator to make a list of issues with submitted objects.  The downside of Lombok is the extra methods careless use of @Data generates.  Extra equals and hashcode methods really hurt the code coverage of test suites for Q1 and Q2 before I clued in on why my code coverage was barely 75%.  My only regret is maybe a more interface based approch would have simplifed testing.  I should probably look into tools for automating test writing.  
