# json-validator-service #

## Prerequisites ##
- Docker <https://docs.docker.com/install/>
- OpenJDK <https://adoptopenjdk.net/> or <https://openjdk.java.net/>
- sbt <https://www.scala-sbt.org/>

## Test runner instructions ##
Should you wish to run the tests within this application, you can do so with the sbt task "test".
```sh
$ cd json-validator-service
$ sbt
> test
```

## Build Instructions ##
Below are a number of options for running the application. The recommended method is to use Docker (and docker-compose) via sbt, you'll find the instructions for this directly below. Additional options are running the application are also listed below. 

### Option 1. Build & Run with sbt & Docker ###
```sh
$ cd json-validator-service
$ sbt dockerComposeUp
```
API will now be running at http://localhost/

#### Stop Docker Containers ####
```sh
$ sbt dockerComposeStop
```

### Option 2. Build & Run in sbt interactive mode ###
```sh
$ cd json-validator-service
$ sbt
> dockerComposeUp
```
API will now be running at http://localhost/

#### Stop Docker Containers ####
```sh
$ sbt
> dockerComposeStop
```

### Option 3: Build & Run in development watch mode ###
```sh
$ cd json-validator-service
$ docker-compose -f docker/docker-compose.yml up -d redis
$ sbt
> ~;jetty:stop;jetty:start
```

#### Stop Docker Containers ####
```sh
$ docker container stop docker_redis_1
```