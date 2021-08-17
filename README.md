# Task Manager

This repo hosts a Task Manager application and an API to try it out. The task manager enables you to
run processes (dummy ones for now) and manage them by:

- listing currently running processes (the API allows you to get them sorted as well).
- killing a specific process by its id.
- killing a group of processes at once.
- killing all processes.

There are multiple ways of how the task manager manages its capacity which is possible to
configure (see [configurations section](#configuration)).

## Supported Task Managers

* **Prefixed Capacity**

  A task manager that has a prefixed capacity, when it is reached, the task manager will reject
  adding new processes until older process is manually killed.

* **Fifo Strategy**

  First-in-first-out task manager, when it reaches its capacity, the task manager will evict the
  first process that was created (oldest) in favour of the new one.

* **Priority Strategy**

  This task manager upon reaching its capacity, it will evict the least prior process in favour of
  the new one.

## Getting Started

### Prerequisites

* About 15 minutes
* A favorite text editor or IDE
* JDK 11 or later
* Maven ([see here](https://maven.apache.org/install.html))
* You can also import the code straight into your IDE:
    * [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Running the application

You can run the application suing Intellij by clicking on the play button at the top bar. Or by
using maven in the CLI in the following way:

```shell
mvn spring-boot:run
```

it will run a Spring Boot application on the default port 8080, you can access it by going to your
localhost and use swagger to interact with it at:

```shell
http://localhost:8080/swagger-ui/
```

### Configuration

As mentioned above, there are different strategies that can be used within the Task Manager. The
strategy and the capacity of the task manager can be configured in the
properties [application.yml](/src/main/resources/application.yml)
Please note that same configuration can be done for the integration tests in the file[tests/../application.yml](src/test/resources/application.yml). 
file of the service. Simply set the required capacity and pick one of the possible strategies:

- `priority` for priority based eviction strategy.
- `prefixed` for prefixed capacity.
- `fifo` for first-in-first-out eviction strategy.

### Tests

In this repo we have:

- **Unit Tests** using junit 5, testing the logic of the application making sure all units are
  working as expected. it also tests negative scenarios.
- **Integration Tests** using SpringMvc, testing the endpoints and making sure they do what they
  promise to do. These tests ensure safer rollout.

To run the tests simply run:

```shell
mvn verify
```

To change the configurations of the tests, checkout the configurations file: [application.yml](src/test/resources/application.yml).

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.3/maven-plugin/reference/html/#build-image)

