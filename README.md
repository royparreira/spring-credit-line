# spring-credit-line

Java Spring Boot Micro-Service

---

### Dependencies

- This project is built on top of Java 11 JDK, so you will need to have a JDK11 installed in order
  to build, run and debug it.


- The dependencies and build are managed by gradle. You don't need to install nothing,

  just run the gradle commands form the root dir using `./gradlew` instead of `gradle` to use gradle
  wrapper


- The service connects to a Postgres DB and a Redis DB. You can point to you own instances by
  editing the [application.yml](./src/main/resources/application.yml) file for Postgres
  and [redisson-jcache.json]() file for redis.
    - You can run instances locally using
      the [local-dependencies/docker-compose.yml](./local-dependencies/docker-compose.yml) file.
      __See [Local Dependencies](#local-dependencies) session__

---

### Local Dependencies

- In order to run the mandatory databases locally, you can use docker
  with [docker-compose.yml](./local-dependencies/docker-compose.yml) file
    - You must have both `docker` and `docker-compose` installed
    - From the [local-dependencies](./local-dependencies) directory run
    ```shell
      $ docker-compose up -d
    ```

---

### How to Run Locally

- The application is configured to run in the base path `credit-line/` using the tcp port `5001`
    - You can change it in the [application.yml](./src/main/resources/application.yml) if you want
    - Or
      leverage [Spring Profiles](https://docs.spring.io/spring-boot/docs/1.2.0.M1/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties)
      to override it


- Get the application up and running by hitting, from the project root directory
  ```shell
    $ ./gradlew bootRun 
  ```

- Once running the application can be reached in **http://localhost:5001/credit-line**


- `The api is documented by SWAGGER, It's very basic and need customization, but you can check it
  on` **http://localhost:5001/credit-line/swagger**

---

### Api POST '/v1/request-credit-line'

Is the endpoint to request a credit line.

#### REQUEST

- You must inform the Cash Balance, Monthly Revenue and the Requested CreditLine in the Request Body
    - Those are numbers with two decimal places.

```json
{
  "cashBalance": 1000000.99,
  "monthlyRevenue": 150000.99,
  "requestedCreditLine": 10000.99
}
```

- You must inform the Funding Type as a Request Header
    - It's a String that must be either `"SME"` or `"STARTUP"`

      ``` 
      foundingType = SME 
      ```

- You must inform the Customer Id Type as a Request Header
    - It's a UUID String that in a real situation should be filled in by an API Gateway after the
      authentication/authorization
    - It's also used to rate limit the api by user

      ``` 
      customerId = 18eee9c2-f577-11ec-b939-0242ac120002 
      ```

#### RESPONSE

The response body will always have the `utcTimestamp` and the `path` attributes independently if
it's a success or an error response

<br/>
<u>SUCCESS RESPONSES:</u>

Response body pattern:

```json
{
  "response": {
    "...": "..."
  },
  "utcTimestamp": "2022-07-02T00:01:56.121211Z",
  "path": "/v1/request-credit-line"
}
```

HTTP 202 STATUS - If credit line approved

```json
{
  "response": {
    "creditLineStatus": "ACCEPTED",
    "acceptedCreditLine": 10000.99
  }
}
```

HTTP 200 STATUS - If credit line rejected

```json
{
  "response": {
    "creditLineStatus": "REJECTED"
  }
}
```

or

```json
{
  "response": {
    "creditLineStatus": "REJECTED",
    "message": "A sales agent will contact you"
  }
}
```

<br/>
<u>ERROR RESPONSES:</u>

POSSIBLE ERRORS: 400, 429 and 500

Response body pattern:
HTTP 4XX and 5XX STATUS

```json
{
  "error": {
    "errorType": "string",
    "errorCode": "string",
    "errorMessage": "string"
  },
  "utcTimestamp": "2022-06-26T02:14:21.12Z",
  "path": "/v1/request-credit-line"
}
```

---

### Continuous Integration

<u>SONARQUBE:</u>

- Sonarqube scan is also configured for the project

  From the project root directory run
    ```shell
      $ ./gradlew sonarqube
    ```
  Note that the sonarqube command already runs the unit tests and the coverage is integrated

<u>UNIT TEST AND CODE COVERAGE:</u>

- If you want just to run the Unit Tests, the project is configured to code coverage Jacoco
  Verification, you can check the code coverage by
  running the tests

  From the project root directory run
    ```shell
      $ ./gradlew test
    ```
  Or run tests with coverage using the preferred IDE

---
