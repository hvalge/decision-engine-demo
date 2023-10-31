# Loan Decision Engine

## Deployment

### Deploying with Docker Compose

- Ensure you have both [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your system before proceeding.

- Run the following command on the root directory to deploy both the frontend (accessible locally on port 8080) and backend (accessible locally on port 8081).

    ```bash
    docker-compose up -d
    ```

### Local Deployment

- Ensure you have JDK 17 and Node.js 20 installed beforehand.

- For locally deploying vue, navigate to the "loan-decision-app" directory and run:

     ```bash
     npm run serve
     ```

- For locally deploying Spring Boot, navigate to the "DecisionEngine" directory and run:

     ```bash
     ./gradlew bootRun
     ```

- Alternatively, just use any IDE of your choice to run the applications.

### Accessing the Application

- Open `http://localhost:8080` in a web browser to access the input form, which completely reflects the backend API contract for this specific implementation.
  - Alternatively, you can also access the backend API directly via POST (with Postman, curl, etc) at `http://localhost:8081/api/loan/decision`, with a JSON body that looks something like the next example:

    ```json
    {
    "personalCode": "49002010976",
    "loanAmount": 300000,
    "loanPeriodInMonths": 13
    }
    ```

## Choices made for this implementation

The current solution mostly implements the functional requirements one-to-one as described in the task description, including adjusting the loan and loan period higher or lower where needed. The solution primarily stores currency as integers (1 cent = 1 integer value), but storing as decimal is also perfectly suitable. Issues related to floating point calculations are addressed. A loan amount increment of 10000 (or 100€) is used when adjusting loan amounts for re-calculation, for balancing between performance and accuracy.\
Due to time constraints, authentication and authorization is not implemented. This is also why the frontend has personal code as an input field, which in a real world scenario is not ideal due to security concerns.\
The backend has a few tests for the loan decision calculator service, and none else in the backend, for there is little else important to test from a business logic and integration perspective.\
The frontend is fairly minimalistic. The only extra features included are additional constraints to the input fields before anything is sent to the backend, and a few error messages for a better user experience.
