# json-validator-service #

## Build & Run with sbt & Docker ##
```sh
$ cd json-validator-service
$ sbt dockerComposeUp
```
Navigate to http://localhost:8080/ in your browser.

### Stop Docker Containers
```sh
$ sbt dockerComposeStop
```

## Build & Run in sbt interactive mode ##
```sh
$ cd json-validator-service
$ sbt
> dockerComposeUp
> browse
> dockerComposeStop
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
