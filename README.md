# json-validator-service #

## Build & Run with sbt & Docker ##
```sh
$ cd json-validator-service
$ sbt dockerComposeUp
```
API will now be running at http://localhost/

#### Stop Docker Containers ####
```sh
$ sbt dockerComposeStop
```

## Build & Run in sbt interactive mode ##
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