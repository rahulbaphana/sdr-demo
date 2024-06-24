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
#### 2. [Gradle 8.2](https://gradle.org/releases/). Recommended to use [SDKMAN](https://sdkman.io/) to install `Gradle 8.7` or above.
    [**NOTE:** Gradle installation is optional since the workspace uses gradle wrapper.] 
#### 3. [Docker](https://docs.docker.com) 
#### 5. [Docker compose.](https://docs.docker.com/compose)
#### 6. [Test containers.](https://java.testcontainers.org)
#### 7. [Dependabot.](https://docs.github.com/en/code-security/getting-started/dependabot-quickstart-guide)

---
## How to run the service:
#### 1. Navigate to the home folder `sdr-demo`
```sh
$ pwd
<PATH_TO_THE_REPO>/sdr-demo

$ ls -all
   rwxr-xr-x   14   rahulbaphana   staff    448 B     Tue Jun 11 18:36:53 2024    .git/
   rwxr-xr-x    3   rahulbaphana   staff     96 B     Tue Jun  4 22:11:24 2024    .github/
   rw-r--r--    1   rahulbaphana   staff    488 B     Tue Jun 11 16:17:02 2024    .gitignore
   rwxr-xr-x    6   rahulbaphana   staff    192 B     Mon May 27 14:34:18 2024    .gradle/
   rwxr-xr-x   12   rahulbaphana   staff    384 B     Tue Jun 11 18:40:15 2024    .idea/
   rw-r--r--    1   rahulbaphana   staff      3 KiB   Mon May 27 13:46:28 2024    HELP.md
   rw-r--r--    1   rahulbaphana   staff      4 KiB   Tue Jun 11 18:46:08 2024    README.md
   rwxr-xr-x    5   rahulbaphana   staff    160 B     Tue Jun 11 18:41:15 2024    app/
   rwxr-xr-x    5   rahulbaphana   staff    160 B     Tue Jun 11 18:41:14 2024    avro/
   rwxr-xr-x    5   rahulbaphana   staff    160 B     Tue Jun 11 18:41:14 2024    build/
   rw-r--r--    1   rahulbaphana   staff      2 KiB   Tue Jun 11 18:40:52 2024    build.gradle
   rw-r--r--    1   rahulbaphana   staff      1 KiB   Tue Jun 11 13:33:23 2024    docker-compose.yml
   rwxr-xr-x    3   rahulbaphana   staff     96 B     Mon May 27 13:46:28 2024    gradle/
   rwxr-xr-x    1   rahulbaphana   staff      8 KiB   Mon May 27 13:46:28 2024    gradlew
   rw-r--r--    1   rahulbaphana   staff      2 KiB   Mon May 27 13:46:28 2024    gradlew.bat
   rw-r--r--    1   rahulbaphana   staff     52 B     Tue Jun 11 12:03:17 2024    settings.gradle
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

#### 5. Build the service (**⚠️Note:** please stop the `docker containers` if they are running.)
```sh
$ ./gradlew clean build
```

#### 6. Run redis server locally (one of the below)
```sh
# Run this in new window. Please wait till the log generation stops.
$ docker compose up 

# NOTE: if you want to destroy and recreate the redis server use the below command:
# docker compose up --force-recreate
```

#### 7. Run the spring boot service 
```sh
$ ./gradlew app:bootRun
```

---
## Using the api's:

#### 1.Create a product:
##### a. Without kafka:
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

##### b. With kafka listener: 
(Flow will be - `POST products/sendAvroMessage` -> `Kafka producer` sends message to topic -> `Kafka consumer` reads from topic -> upserts the record in `Redis`)
```sh
curl --location 'localhost:9292/products/sendAvroMessage' \
--header 'Content-Type: application/json' \
--data '{
    "id": 7,
    "name": "iPhone 15 pro max",
    "quantity": 1,
    "price": 1250
}'
```
> output:
> ```sh
> Message sent to Kafka topic product_updates_topic
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
