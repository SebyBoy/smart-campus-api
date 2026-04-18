# Author
Name : Sebastian Young
uniID: w2082018

# Smart Campus API

## Overview

This is a RESTful Smart Campus API project developed in java using JAX-RS(Jersey) and Maven. The Objective of this project is to manage rooms, sensors and historical readings for a university campus using in-memory storage.

## Technologies Used

- Java 17
- Maven
- JAX-RS (Jersey)
- Grizzly HTTP Server
- VS Code

## How to Run

1. Open the project in VS Code
2. Ensure Java 17 and Maven are installed
3. Run `Main.java`
4. The server starts at:
http://localhost:8088/api/v1/

## API Endpoints

### Discovery
GET /api/v1/

### Rooms
GET /api/v1/rooms  
POST /api/v1/rooms  
GET /api/v1/rooms/{roomId}  
DELETE /api/v1/rooms/{roomId}

### Sensors
GET /api/v1/sensors  
GET /api/v1/sensors?type=CO2  
POST /api/v1/sensors

### Readings
GET /api/v1/sensors/{sensorId}/readings  
POST /api/v1/sensors/{sensorId}/readings

## Business Rules Implemented

- Rooms cannot be deleted if sensors are assigned to them.
- Sensors cannot be created for rooms that do not yet exist.
- Sensors in MAINTENANCE mode cannot accept any new readings.
- Posting a reading updates the sensor currentValue.
- Custom JSON error responses have been implemented.




## Coursework Report Answers

# Part 1: Service Architecture & Setup

## 1. Project & Application Configuration

### Question:
In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

### Answer:
By default, JAX_RS classes work by creating a new resource instance for each incoming request as opposed to one singleton instance being shared across various requests. Its useful because each user gets their own temporary data, so one persons information does not accidentally affect another persons. However, in my implementation, the application data is not stored inside the resource instance. Instead I store them in a shared memory collection called DataStore, such as maps and lists. Because these maps and lists are shared, various requests could modify them simultaneously. It means that request-scoped resources don't automatically create shared data that is thread safe. In larger more complex systems, they would need synchronisation to avoid race conditions.

## 2. The Discovery Endpoint

### Question:
Why is the provision of “Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

### Answer:
Hypermedia is important because responses can include links to related resources, which helps clients navigate the API without relying on static documentation. An example of this would be the discovery endpoint, which provides links to /rooms and /sensors. This helps developers because the API explains itself through its own responses. Clients can therefore follow links rather than hard-coding every possible endpoint. This reduces coupling and makes the API easier to update.

# Part 2: Room Management

## 1. Room Resource Implementation

### Question:
When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

### Answer:
When returning only room ID's, it reduces the response size which cuts down on bandwidth. However, the client would need more requests to get other bits of data therefore increasing traffic. Returning a full rooms data, it increases bandwidth but nullifies the clients need to send more requests. I chose to return the entire rooms data because more data is usually required,

## 2. Room Deletion & Safety Logic

### Question:
Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

### Answer:
Regardless of how many times a DELETE request is sent, it doesn't keep changing the room after the first successful deletion. Once the room is removed, more requests does not delete anything else. The response may change but the state of the server remains the same. Therefore DELETE is idempotent.

# Part 3: Sensor Operations & Linking

## 1. Sensor Resource & Integrity

### Question:
We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

### Answer:
When using @Consumes(MediaType.APPLICATION_JSON), it means the POST method specifically accepts JSON. In the case that a client uses something else, it wont match the request to the method correctly. This will result in a 415 Unsupported Media Type error. It ensures the client follow the expected JSON format so the server is able to convert the request correctly.

## 2. Filtered Retrieval & Search

### Question:
You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

### Answer:
Using @QueryParam like /api/v1/sensors?type=CO2, is far better for filtering because the client still accesses the sensor collection. A path like /api/v1/sensors/type/CO2 can look separate. When combining multiple filters, like status, query parameters are much better.

# Part 4: Deep Nesting with Sub-Resources

## 1. The Sub-Resource Locator Pattern

### Question:
Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

### Answer:
The Sub-Resource locater pattern improves the structure by dealing nested logic separately. In mt design, SensorResource handles sensors, while SensorReadingResource handles readings for any specifically requested sensor. If everything was together, the code would be hard to maintain and modify if needed. Having them separate keeps the design cleaner.

## 2. Historical Data Management

# Part 5: Advanced Error Handling, Exception Mapping & Logging

## 1. Resource Conflict (409)

## 2. Dependency Validation (422 Unprocessable Entity)

### Question:
Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

### Answer:
404 Not Found is less accurate than 422 Unprocessable Entity for one main reason. In 422, the endpoint already exists and the JSON is valid, but the data inside cannot be processed the correct way. With 404, we don't know anything other than it doesn't work.

## 3. State Constraint (403 Forbidden)

## 4. The Global Safety Net (500)

### Question:
From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

### Answer:
Revealing class, package, file names as well as line numbers and framework details are the risks that come with showing Java stack traces. It can give potential attackers information about how the internal system works. They can use this information to target weak points or discover vulnerabilities. Its why, in my API design, I use a global exception mapper to return a generic JSON error.

## 5. API Request & Response Logging Filters

### Question:
Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

### Answer:
Using JAX-RS filters for logging has many advantages. It lets the logic be written only once and applied everywhere, especially considering the fact that logging is needed across many endpoints. If logging was added manually with every method, it could be forgotten in some places especially for larger files. Filters improves consistency and keeps resources focused on logic.