# Booking API
This is a Booking API:
* The hotel has only one room available.
* To give a chance to everyone to book the room, the stay can’t be longer than 3 days and
  can’t be reserved more than 30 days in advance.
* All reservations start at least the next day of booking, to simplify the use case, a “DAY’ in the hotel room starts from 00:00 to 23:59:59.

In this project it is possible to:
* Create a booking.
* Get a booking by its id.
* Get all bookings.
* Delete a booking.
* Update a booking.

The technologies/frameworks used were:
* Java 8
* Spring Boot
* JUnit
* MockMvc
* Mockito
* Swagger
* Docker

### To run the application:
To run the application you will need at least to have installed [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/).

Using your terminal, go to the project folder and run:
* *docker build -t hotelapi .*
* *docker run -p 8080:8080 hotelapi*

With this commands, your application will be running and you will be able to access the [Application Swagger](http://localhost:8080/swagger-ui.html).

To access H2DB inside of the Docker container:
* *http://localhost:8080/h2-console/*

##Comments

In order to create a thread-safe api, you need to change the way a reservation is placed.

* Spring boot controller default scope is singleton, but it isn't thread-safe
* You'd probably need to develop a synchronized method, or a read/write lock for placing/reading the reservation
* There are other microservice patterns which should be used for replacing the locks mentioned above.
* This project uses docker and you can add a docker-compose routine to start a new node whenever it's needed