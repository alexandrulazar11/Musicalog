# Musicalog
Music Store Catalog Web App


## Description


## Functionality

unit tests - Junit \
integration test \
check how MediaType is printed and how it's saved
what will each endpoint return
add swagger \
controller returns Flux or Mono as a Reactive value to maintain the non-blocking nature of the application. However, it can be changed to return the actual values if it needs to.

### Running MongoDB in a Docker Container:

Within the root folder of this application, run this:

docker-compose up -d 

This will start the docker container locally with the MongoDB application. You can check by running:

docker ps

To stop it, you can run this:

docker-compose down

### Endpoints:

Healthcheck \
curl http://localhost:8080/actuator/health

Fetching all albums from the catalogue: \
curl http://localhost:8080/api/album/all

Fetching all albums from the catalogue by title: \
curl -X GET "http://localhost:8080/api/album/by-title?title=YourTitleHere"

Fetching all albums from the catalogur by Artist name: \
curl -X GET "http://localhost:8080/api/album/by-artist?artistName=YourArtistNameHere"

Fetching album from catalogue by Id: \
curl -X GET "http://localhost:8080/api/album/YourAlbumIdHere"

Saving a new Album to the catalogue: \
curl -X POST "http://localhost:8080/api/album/save" -H "Content-Type: application/json" -d "{\"id\": \"1\", \"title\": \"Album Title\", \"artistName\": \"Artist Name\", \"type\": \"VINYL\", \"stock\": 100, \"cover\": null}" \
(Base64 encoded image would be very big here and that's why it has the cover as null)


Note: The cover field should be a base64 encoded image correctly

Updating an existing Album from the catalogue: \
curl -X PUT "http://localhost:8080/api/album/update" -H "Content-Type: application/json" -d "{\"id\": \"YourAlbumIdHere\", \"title\": \"UpdatedTitle\", \"artistName\": \"UpdatedArtist\", \"type\": \"VINYL\", \"stock\": 100, \"cover\": \"base64_encoded_updated_cover_data\"}"

Deleting an Album from the catalogue: \
curl -X DELETE "http://localhost:8080/api/album/YourAlbumIdHere"


## Deployment

github actions file \
dockerisation \
helm chart for K8s

## Local Set-up

Make sure you use Java 21
Run the application

## Future Work

security \
Oauth with roles and permissions for users
Can have an API Gateway between UI and this service to do the authorization and authentication \
Album, MediaType and other domains should be present in a client repository \
End to End testing / Automated tests -> can be done in a different application for tests so it doesn't affect scalability