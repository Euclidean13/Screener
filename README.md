# Screener

## Swagger

API to document endpoints. In order to see the endpoints and test them please run the application and open the following
link on the browser of your choice: [swagger](http://localhost:8080/swagger-ui/#/).

<span style="color:green">**It is recommended to review and test the performance of the endpoints of this application
using this tool.**</span>

## Docker

The Dockerfile provided has two stages:

- Build Stage.
- Package Stage.

The purpose of these stages is to avoid having to build and copy the build into the docker image. And thus, run it only
with docker commands.

```console
docker build -t screener .
```

For building a container for local testing purposes.

```console
docker run -p 8080:8080 --rm -it screener:latest
```