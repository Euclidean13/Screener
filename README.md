# Screener

Automated analyst where the user could upload the companies they were currently considering to invest in (a.k.a, their
deal flow or pipeline). This API provides statistical data of these companies cross-referenced with the investment
criteria stipulated by the user.

## Swagger

API to document endpoints. In order to see the endpoints and test them please run the application and open the following
link on the browser of your choice: [swagger](http://localhost:8080/swagger-ui/#/).

<span style="color:green">**It is recommended to review and test the performance of the endpoints of this application
using this tool.**</span>

## DataBase

<span style="color:orange">Firebase</span> has been used as the database in this project for convenience and speed,
using Firestore as the store of NoSql database.

[firebase](https://firebase.google.com/)

[firebase console](https://console.firebase.google.com/u/0/)

## Endpoints

The basic endpoint paths are as follows. <span style="color:red">Important!</span> Parameters and bodies are not
included. For more information, see the swagger API.

### Crud controller

```console
http://localhost:8080/createUser
```

```console
http://localhost:8080/deleteUser
```

```console
http://localhost:8080/getUser
```

```console
http://localhost:8080/updateUser
```

### Criteria controller

```console
http://localhost:8080/addCriteria
```

```console
http://localhost:8080/deleteCriteria
```

```console
http://localhost:8080/userCriteria
```

### Investment controller

```console
http://localhost:8080/addUserCompany
```

```console
http://localhost:8080/decision
```

```console
http://localhost:8080/getUserCompaniesFunnel
```

```console
http://localhost:8080/updateCompanyHaves
```

```console
http://localhost:8080/userCompanies
```

```console
http://localhost:8080/userCompanyDetails
```

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

## Kubernetes

- screenerService.yaml: load balancer service
- screenerDeploymen.yaml: deployment with one replicaset and one pod