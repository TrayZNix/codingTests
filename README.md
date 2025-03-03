# 🍃 Java Backend - gRPC & REST Microservices 📡

Category   ➡️   Software

Subcategory   ➡️   Java Backend

Difficulty   ➡️  Medium

Average solution time ➡️ 4 hours. The timer will begin when you click the start button and will stop upon your submission. However, this is only a reference metric and does not impact your final score. Focus on delivering a high-quality solution.

---

## 🌐 Background

In this challenge you will showcase your ability to build and debug Java-based microservices using gRPC, REST, and MySQL. The system comprises four containerized services—UserService, MessageService, NotificationService, and FrontendService—designed for user management, messaging, notifications, and frontend communication.

Your tasks include: fixing inter-service communication bugs, implementing notification delivery, and ensuring smooth integration across services using shared Proto Definitions. The challenge evaluates your debugging skills, code review, feature implementation, and code quality while adhering to the existing architecture and protocols.

### 📂 Repository Structure

A repository tree is provided below and should not be modified. Everything you need to develop the challenge is already included.

```bash
caixabank-backend-java-grpc
├── frontend-grpc-service
│   ├── LICENSE
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│      └── main
│          ├── java
│          │   └── io
│          │       └── nuwe
│          │           └── technical
│          │               └── api
│          │                   ├── APIConfig.java
│          │                   ├── controllers
│          │                   │   └── NotificationController.java
│          │                   ├── entities
│          │                   │   └── Notification.java
│          │                   ├── grpc
│          │                   │   └── FrontendProtoService.java
│          │                   ├── JacksonConfiguration.java
│          │                   ├── repositories
│          │                   │   └── NotificationRepository.java
│          │                   ├── services
│          │                   │   └── NotificationService.java
│          │                   └── ServletInitializer.java
│          └── resources
│              └── application.properties
├── message-service
│   ├── LICENSE
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│      └── main
│          ├── java
│          │   └── io
│          │       └── nuwe
│          │           └── technical
│          │               └── api
│          │                   ├── APIConfig.java
│          │                   ├── controllers
│          │                   │   └── MessageController.java
│          │                   ├── entities
│          │                   │   └── Message.java
│          │                   ├── grpc
│          │                   │   ├── GrpcClientService.java
│          │                   │   └── MessageProtoService.java
│          │                   ├── JacksonConfiguration.java
│          │                   ├── repositories
│          │                   │   └── MessageRepository.java
│          │                   ├── services
│          │                   │   └── MessageService.java
│          │                   └── ServletInitializer.java
│          └── resources
│              └── application.properties
├── notification-service
│   ├── LICENSE
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│      └── main
│          ├── java
│          │   └── io
│          │       └── nuwe
│          │           └── technical
│          │               └── api
│          │                   ├── APIConfig.java
│          │                   ├── controllers
│          │                   │   └── NotificationController.java
│          │                   ├── entities
│          │                   │   └── Notification.java
│          │                   ├── grpc
│          │                   │   ├── GrpcClientService.java
│          │                   │   └── NotificationProtoService.java
│          │                   ├── JacksonConfiguration.java
│          │                   ├── repositories
│          │                   │   └── NotificationRepository.java
│          │                   ├── services
│          │                   │   └── NotificationService.java
│          │                   └── ServletInitializer.java
│          └── resources
│              └── application.properties
├── proto-definition
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └──src
│      └── main
│          └── proto
│              ├── message.proto
│              ├── notification.proto
│              └── user.proto
├── README.md
├── build.sh
└── user-service
    ├── LICENSE
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
       └── main
           ├── java
           │   └── io
           │       └── nuwe
           │           └── technical
           │               └── api
           │                   ├── APIConfig.java
           │                   ├── controllers
           │                   │   └── UserController.java
           │                   ├── entities
           │                   │   └── User.java
           │                   ├── grpc
           │                   │   ├── GrpcClientService.java
           │                   │   └── UserProtoService.java
           │                   ├── JacksonConfiguration.java
           │                   ├── repositories
           │                   │   └── UserRepository.java
           │                   ├── services
           │                   │   └── UserService.java
           │                   └── ServletInitializer.java
           └── resources
               └── application.properties

```

## 🎯 Tasks

1. **Task 1**: `user-service` implementation. CRD API.
2. **Task 2**: `message-service` implementation and communication between `message-service` and `user-service`.
3. **Task 3**: `notification-service` implementation and communication between `notification-service` and `frontend-service`.


### 📑 Detailed information about tasks

⚠️ If you have any problem with the repo you can go to this section of the readme [Repo size](#repository-size)⚠️

#### Task 1

You must implement a set of RESTful endpoints to manage user operations. These include creating users, retrieving all users, retrieving a user by ID, and deleting users (both individually and in bulk). It is essential to implement proper HTTP status codes for each operation and adhere to the following specifications.

**Base url: http://localhost:8080**

1. **Create a New User**
- **Method**: `POST`
- **Endpoint**: `/user`
- **Request Body** (JSON):
  ```json
  {
    "name": "User Name",
    "email": "email@example.com",
    "age": 30
  }
  ```
**Status Codes**:
- 201 Created: User successfully created.
- 400 Bad Request: The request contains invalid data.

2. **Retrieve All Users**
- **Method**: `GET`
- **Endpoint**: `/user/all`
- **Status Codes**:
    - `200 OK`: Users retrieved successfully.
    - `204 No Content`: No users found.
- **Expected Response** (JSON when users exist):
    ```json
    [
    {
        "id": 1,
        "name": "User Name",
        "email": "email@example.com",
        "age": 30
    },
    {
        "id": 2,
        "name": "Another User",
        "email": "another@example.com",
        "age": 25
    }
    ]
    ```

3. **Retrieve a User by ID**
- **Method**: `GET`
- **Endpoint**: `/user/{id}`
- **Path Parameter**:
    - `id`: The ID of the user to retrieve.
- **Status Codes**:
    - `200 OK`: User retrieved successfully.
    - `404 Not Found`: No user found with the given ID.
- **Expected Response** (JSON):
    ```json
    {
        "id": 1,
        "name": "User Name",
        "email": "email@example.com",
        "age": 30
    }
    ```

4. **Delete a User by ID**
- **Method**: `DELETE`
- **Endpoint**: `/user/{id}`
- **Path Parameter**:
    - `id`: The ID of the user to delete.
- **Status Codes**:
    - `200 OK`: User deleted successfully.
    - `404 Not Found`: No user found with the given ID.

5. **Delete All Users**
- **Method**: `DELETE`
- **Endpoint**: `/user/all`
- **Status Codes**:
    - `200 OK`: All users deleted successfully.

The project to be modified for this task will be [user-service](/user-service/src/main/java/io/nuwe/technical/api/) where you can find everything you need to just add or modify the necessary code.

**In future tasks, you will need to come back here and note that a user must be subscribed to notifications by default once it is created.**

**To complete this task it will be necessary to code the .proto file for `user-service`.**

#### Task 2

**Base url for messages: http://localhost:8181**

Task 2 is focused on fixing the internal communication between microservices. Specifically, the communication between the `Messages - Service` and the `User - Service`. Something is not working as expected and a bug has stopped the software developing of the project. 

To complete this task it is necessary that the `user-service` and `message-service` services are functioning correctly and that there is a connection between them.

To verify the correct operation of this task, the following will be verified:

- It is possible to verify that users exist or not using the HTTP endpoint of users: http://localhost:8080/user/all (previously done in task 1)
- It is possible to create users in the DB using the users HTTP endpoint: http://localhost:8080/user (previously done in task 1)
- It is possible to verify that messages exist or not using the HTTP endpoint of messages: http://localhost:8181/message/all
- You can create new messages using the HTTP message endpoint: http://localhost:8181/message


**To complete this task it will be necessary to code the .proto file for `message-service` and finish the method for creating new messages in `message-service`.**

**In future tasks, you will need to come back here and note that a user must be subscribed to notifications by default once it is created. The app must verify it.**

**Detailed information on how to create a message**:

- **Method**: `POST`
- **URL**: `/message`
- **Request body**(JSON):
    ```json
    {
        "userSenderId": 1,
        "userReceiverId": 2,
        "body": "Hello! This is a test message."
    }
    ```
- **Responses**:
    - **Status Code**: `201 Created`
        - **Response body**:
        ```json
        {
            "id": 1,
            "userSenderId": 1,
            "userReceiverId": 2,
            "body": "Hello! This is a test message.",
            "sentAt": "2025-01-21T15:23:30"
        }
        ```
    - **Status Code**: `404 Not Found`
        - **Scenario**: `userSenderId` or `userReceiverId` does not exist.


#### Task 3

Task 3 aims to implement the communication between the `Notification - Service` and the `Frontend - Service`.

Every time a new message is created, the `Message - Service` pushes a new notification to the `Notification - Service` only if the user that receives the message is subscribed (by default). The `Notification - Service` has to then send the notification message to the `Frontend - Service` (the device).

The objective is to implement the method that receives the `Notification - Service` from the `Message - Service` and pass it over the `Frontend - Service`.

This method should:
- Create a new notification.
- Push the notification to the frontend service via gRPC.
- Set the `notificationArrived` attribute to the response of the pushed notification.
- Save the new notification into database.

**To complete this task it will be necessary to code the .proto file for `notification-service` and code the files [NotificationProtoService.java](/notification-service/src/main/java/io/nuwe/technical/api/grpc/NotificationProtoService.java) and [GrpcClientService.java](/notification-service/src/main/java/io/nuwe/technical/api/grpc/GrpcClientService.java)**

In addition, to be considered fully done, both notification-service and frontend-service endpoints will be tested. In this way it is checked if gRPC has been properly configured.

## ❓ Guides

Make sure to read the codebase. It provides with a lot of function that will help you develop the API. 

This project contains 4 + 1 different repositories, each of one has an specific function. These repositories follow the Microservices design. The repositories are:

- **User**: The user repository found as `user-service/`, whose only function is to manage users. It defines a basic CRD for Users. 
	- **HTTP**: It exposes an HTTP server (backend) to manage the users.
	- **gRPC**: It exposes an gRPC server (backend) to retireve user information.

- **Message**: The message repository found as `message-service/`, whose function is to send and retrieve messages between users. 
	- **HTTP**: It exposes an HTTP server (backend) to manage the users.
	- **gRPC**: It exposes an gRPC server (backend) to send and retreive messages.

- **Notification**: The notification repository found as `notification-service/`, whose only function is send notification to user device.
	- **HTTP**: It exposes an HTTP server (backend) to list all the notifications send and its state.
	- **gRPC**: It exposes an gRPC server (backend) to push notifications.
    
- **Frontend**: The frontend, a server simulating a frontend device, found as `frontend-grpc-service/` it serves as a testing device to check the notification system.
    - **HTTP**: It exposes an HTTP server to list all the notifications using the frontend service to check the correct functioning and communication between notification and frontend
	- **gRPC**: It exposes an gRPC server (backend) that accepts notifications.

Finally, there is another repository called `proto-definition/`. This repository contains the definition of the Protobuffers used in the gRPC server/clients among all the projects. It is a MUST dependency of all projects listed above.


Here is a the table of ports used in each service:

| **Service**             | **HTTP** | **gRPC** |
|-------------------------|----------|----------|
| `user-service`          | 8080     | 9797     |
| `message-service`       | 8181     | 9696     |
| `notification-service`  | 8282     | 9898     |
| `frontend-grpc-service` | 8031     | 3030     |
|-------------------------|----------|----------|

**ATTENTION**: You are not allowed to install 3rd party modules nor create new files. Modify only existing files. Everything else is already provided.

### How to run, compile & deliver

It is very important to read this part well as it contains crucial information for a correct correction of the challenge.

The file [build.sh](./build.sh) contains the commands that you will have to execute whenever you want to compile your code.

The following commands will be executed in the correction:

```bash
#Run User service 
java -jar user-service/target/technical.api-0.0.1-SNAPSHOT.jar &

# Run the Message service 
java -jar message-service/target/technical.api-0.0.1-SNAPSHOT.jar &

# Run the Notification service 
java -jar notification-service/target/technical.api-0.0.1-SNAPSHOT.jar &

# Run the Frontend service 
java -jar frontend-grpc-service/target/technical.api-0.0.1-SNAPSHOT.jar &
```

**Therefore, you should always deliver an updated version of the binaries. To do this, just run `build.sh` before pushing the code and verify that all the `.jar files` have been created in their respective projects inside `/target`.**

**It is also very important that you check if the .jar has been created for the `/proto-definition` project, as this is the one that creates the library needed for the rest of the projects.**

**IMPORTANT NOTE: In case of receiving a 0 in the score even though tasks have been completed, it may be due to a misconfiguration of gRPC or that one of the dependent projects has not compiled correctly.**

### Database

In this case, we will use a dockerised version of MySQL for the correction.

You can use MySQL in any way you want as long as you respect the configuration provided.

In case you want to use a container like ours, run the following command:

```bash
docker network create services_network

docker run -d \
  --name mysql \
  --network services_network \
  -e MYSQL_ROOT_PASSWORD=getthemall \
  -e MYSQL_DATABASE=technical_api \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=getthemall \
  -p 3306:3306 \
  --restart always \
  mysql:8.0
```

**It is very important not to modify any path specified in the project.**

### Repository size

It is possible that due to continuous pushing of large .jar files, the size limit of the repository could be reached.

That is why we add a series of commands to be executed to avoid reaching this limit. 

You can use two versions, one with `BFG Repo Cleaner` and one with `git-filter-repo`.

#### BFG Repo Cleaner

The first thing to do is to make a backup copy of the code. You can do this by simply copying the folders with the different projects into a separate folder. In case something goes wrong, you will never lose your progress.

You can download de .jar file here: [BFG Repo Cleaner](https://rtyley.github.io/bfg-repo-cleaner/)

Move this file to the root folder of the challenge.

Execute the following commands:

```bash
java -jar bfg.jar --delete-files "*.jar" # Change bfg.jar to the name of your downloaded file
git reflog expire --expire=now --all
git gc --prune=now --aggressive
git push origin --force --all
```

#### git-filter-repo

The first thing to do is to make a backup copy of the code. You can do this by simply copying the folders with the different projects into a separate folder. In case something goes wrong, you will never lose your progress.

```bash
pip install git-filter-repo # Install with pip
```
Execute the following commands:

```bash
git filter-repo --path-glob "*.jar" --invert-paths
git reflog expire --expire=now --all
git gc --prune=now --aggressive
git push origin --force --all
```

#### What to do if the repository is full?

If the repository is still full and does not allow any more pushes, please contact NUWE support and we will solve your problem as soon as possible.

## 📤 Submission

1. Solve the proposed tasks.
2. Continuously push the changes you have made.
3. Wait for the results.
4. Click submit challenge when you have reached your maximum score.

## 📊 Evaluation

The final score will be given according to whether or not the objectives have been met.

In this case, the challenge will be evaluated on 1500 (1100 for tasks and 400 for code quality) points which are distributed as follows:

- **Task 1**: 300 points
- **Task 2**: 400 points
- **Task 3**: 400 points
- **Code quality**: 400 points

## ❓ Additional information

Only the files proposed in the objectives should be modified. You are not allowed to modify anything other than the proposed files.

**Q1: What happens if I modify files that are not in scope?**

A1: The correction will fail because those changes will not be taken into account.

**Q2: Can I add resources that are not in pom.xml?**

A2: No, everything needed to complete the challenge is included.