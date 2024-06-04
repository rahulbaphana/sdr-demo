# Spring data redis demo app
A demo springboot service to integrate with redis. This app does not have api tests but only a repository test. 
The intent of creating this repo was to learn about redis and how can we test it with springboot + testcontainers + github actions.

[Note: There isn't much error handling since this is a redis playground.]

---
## Table of contents
* [Tech-stack](#tech-stack)
* [How to run the service](#how-to-run-the-service)
* [Using the api's](#using-the-apis)

---
## Tech-stack:
#### 1. [JDK 21](https://docs.oracle.com/en/java/javase/21/). Recommended to use [SDKMAN](https://sdkman.io/) to install `JDK 21`.
#### 2. (optional since the workspace uses gradle wrapper) [Gradle 8.2](https://gradle.org/releases/). Recommended to use [SDKMAN](https://sdkman.io/) to install `Gradle 8.7` or above.
#### 3. Docker and docker-compose.
#### 4. Testcontainers.
#### 5. Github workflows for running builds.

---
## How to run the service:
#### 1. Navigate to the home folder `sdr-demo`
```sh
$ pwd
<PATH_TO_THE_REPO>/sdr-demo

$ ls -all
rwxr-xr-x   15   <username_on_your_machine>   staff    480 B     Tue Jun  4 16:23:51 2024    ./
rwxr-xr-x   15   <username_on_your_machine>   staff    480 B     Mon May 27 13:46:42 2024    ../
rwxr-xr-x   12   <username_on_your_machine>   staff    384 B     Tue Jun  4 16:17:10 2024    .git/
rw-r--r--    1   <username_on_your_machine>   staff    444 B     Mon May 27 13:46:28 2024    .gitignore 
rwxr-xr-x    6   <username_on_your_machine>   staff    192 B     Mon May 27 14:34:18 2024    .gradle/
rw-r--r--    1   <username_on_your_machine>   staff      2 KiB   Tue Jun  4 16:23:51 2024    README.md 
rwxr-xr-x   12   <username_on_your_machine>   staff    384 B     Tue Jun  4 16:16:01 2024    build/
rw-r--r--    1   <username_on_your_machine>   staff   1002 B     Mon May 27 17:03:20 2024    build.gradle 
rwxr-xr-x    3   <username_on_your_machine>   staff     96 B     Mon May 27 13:46:28 2024    gradle/
rwxr-xr-x    1   <username_on_your_machine>   staff      8 KiB   Mon May 27 13:46:28 2024    gradlew 
rw-r--r--    1   <username_on_your_machine>   staff      2 KiB   Mon May 27 13:46:28 2024    gradlew.bat 
rw-r--r--    1   <username_on_your_machine>   staff     30 B     Mon May 27 13:46:28 2024    settings.gradle 
rwxr-xr-x    4   <username_on_your_machine>   staff    128 B     Mon May 27 13:46:28 2024    src/
```

#### 2. Check the `JDK` version to be `21`
```sh
$ java --version

openjdk 21
...
```

#### 3. Check the `Gradle` wrapper version to be `8.7` or above
```sh
$ ./gradlew --version

------------------------------------------------------------
Gradle 8.7
------------------------------------------------------------

Build time:   2024-03-22 15:52:46 UTC
Revision:     650af14d7653aa949fce5e886e685efc9cf97c10

Kotlin:       1.9.22
Groovy:       3.0.17
Ant:          Apache Ant(TM) version 1.10.13 compiled on January 4 2023
JVM:          21.0.1 (Azul Systems, Inc. 21.0.1+12-LTS)
OS:           Mac OS X 14.5 aarch64
```

#### 5. Build the service
```sh
$ ./gradlew clean build
```

#### 6. Run redis server locally (one of the below)
```sh
$ docker compose up -d 

# NOTE: if you want to destroy and recreate the redis server use the below command:
# docker compose up -d  --force-recreate
```

#### 7. Run the spring boot service 
```sh
$ ./gradlew bootRun
```

---
## Using the api's:

#### 1.Create a product:
```sh
curl --location 'http://localhost:9292/products' \
--header 'Content-Type: application/json' \
--data '{
    "id": 12,
    "name": "Maggi noodles",
    "quantity": 100,
    "price": 200
}'
```
> output:
> ```sh
> {
>  "id": 12,
>  "name": "Maggi noodles",
>  "quantity": 100,
>  "price": 200
> }
> ```

#### 2. Get a product:
```sh
curl --location 'http://localhost:9292/products/12'
```
> output:
> ```sh
> {
>  "id": 12,
>  "name": "Maggi noodles",
>  "quantity": 100,
>  "price": 200
> }
> ```

#### 3. Get all products:
```sh
curl --location 'http://localhost:9292/products'
```
> output:
> ```sh
> [
>  {
>    "id": 12,
>    "name": "Maggi noodles",
>    "quantity": 100,
>    "price": 200
>  }
> ]
> ```

#### 4. Delete a product:
```sh
curl --location --request DELETE 'http://localhost:9292/products/12'
```
> output:
> ```sh
> Product removed!
> ```
