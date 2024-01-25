# Musicalog
Music Store Catalog Web App

---

## Description

Musicalog is a Java 21, Spring Boot 3 application, acting as a REST API for a Catalogue of Music Albums

Current Completion Level represents the MVP of the application:

**A REST API** that allows to **list, create, edit or delete** albums from the catalogue

It is built using **Spring Reactive**, Java's answer for Reactive Programming. This ensures fast response times, making it more efficient and improving performance

---


## Functionality

- Full Unit Tests Coverage using Junit
- Integration Test
- Exception Handling when necessary
- controller returns Flux or Mono as a Reactive value to maintain the non-blocking nature of the application. However, it can be changed to return the actual values if it needs to.
- Split into a few layers: controller, service and repository
- Endpoints for all the CRUD actions to allow easy integration with a UI
- Dockerised Application to make it run anywhere, fast context switching for other devs and to make it easier to integrate with Kubernetes, which is a very scalable deployment option for this purpose
- Healthcheck to verify the application is running
- Encompassed MongoDB running in a Docker Container that's starting before the application
- Using Testcontainers library to generate MongoDB containers on the fly for the integration tests
- Using lightweight java functionality such as Reactive Repository, records and making the most out of Spring Dependency Injection
- Endpoints use the designated HTTP Request Types
- Docker Image is pretty small in size so lightweight for deployment in any environment

### Running Musicalog:

Make sure to build the application beforehand:

`./gradlew clean build`

Within the root folder of this application, run this:

`docker-compose up -d `

This will start 2 docker containers locally with both the MongoDB and the application. You can check by running:

`docker ps`

To stop them, you can run this:

`docker-compose down`

### Endpoints:

Healthcheck \
`curl http://localhost:8080/actuator/health`

Fetching all albums from the catalogue: \
`curl http://localhost:8080/api/album/all`

Fetching all albums from the catalogue by title: \
`curl -X GET "http://localhost:8080/api/album/title?title=YourTitleHere"`

Fetching all albums from the catalogur by Artist name: \
`curl -X GET "http://localhost:8080/api/album/artist?artistName=YourArtistNameHere"`

Fetching album from catalogue by Id: \
`curl -X GET "http://localhost:8080/api/album/YourAlbumIdHere"`

Saving a new Album to the catalogue: \
`curl -X POST "http://localhost:8080/api/album/save" -H "Content-Type: application/json" -d "{\"id\": \"1\", \"title\": \"Album Title\", \"artistName\": \"Artist Name\", \"type\": \"VINYL\", \"stock\": 100, \"cover\": null}"` \
(Base64 encoded image would be very big here and that's why it has the cover as null)


**Note: The cover field should be a base64 encoded image correctly**

Updating an existing Album from the catalogue: \
`curl -X PUT "http://localhost:8080/api/album/update" -H "Content-Type: application/json" -d "{\"id\": \"YourAlbumIdHere\", \"title\": \"UpdatedTitle\", \"artistName\": \"UpdatedArtist\", \"type\": \"VINYL\", \"stock\": 100, \"cover\": \"base64_encoded_updated_cover_data\"}"`

Deleting an Album from the catalogue: \
`curl -X DELETE "http://localhost:8080/api/album/YourAlbumIdHere"`


## Deployment

- Added a basic github actions file
- Dockerisation of both the application and MongoDB local database

## Local Set-up

- Make sure you use Java 21
- Run the application
- Build using Gradle (instruction above)
- Start using docker-compose (instruction above)
- Run Healthcheck first to confirm the application is running fine

## Future Work

- security 
- Oauth with roles and permissions for users
- Can have an API Gateway between UI and this service to do the authorization and authentication between the UI and this API
- Album, MediaType and other new domains should be present in a client repository to facilitate type matching between the UI and this API
- End-to-End testing / Automated tests -> can be done in a different application for tests so it doesn't affect performance and scalability
- Deploying it to K8s will ensure scalability by being more flexible and generating new pods with demand
- MongoDB is a good choice since there is no need for relational implementations or SQL based querying. However, if the application will scale to a very high level, it might need not be the best choice.
- For the initial set-up and configuration, it was safe working on the main branch. However, as it grows and becomes active in production, this should not happen