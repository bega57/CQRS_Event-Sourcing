# lab_cqrs template


This template serves as a starter code skeleton for the CQRS lab in the *System Architectures* course.
The project is structured into the following components:
- `eventbus`: A skeleton for a standalone Quarkus-based implementation of an event bus. It is responsible for decoupling the read side and write side by handling the distribution of events.
- `writeside`: A skeleton for a standalone Quarkus-based application that contains the command logic of the CQRS architecture used in this lab. It processes incoming commands, applies business rules, and emits corresponding events.
- `readside`: A skeleton for a standalone Quarkus-based application that contains the query logic of the CQRS architecture. It listens to events from the event bus and maintains read models optimized for querying.


## Exercise

### Domain
- Implement a complete role-based permission management system.
- A role is represented as a recursive tree-like data structure, where each node in the role tree can contain `n` child nodes. A child node can either be:
    - a permission, represented as a string
    - another role
- If a child node is a role, the parent role inherits all permissions from that child role.
- The permission system follows a simple whitelist approach: if a role contains the requested permission, the action is allowed; otherwise, it is denied.
- The role-based permission management system must support the following operations:
    - Add and remove roles
    - Add and remove permissions to and from a role
    - Check whether a role has a specific permission


Design how to implement and persist the role-tree data structure on both the write side and the read side:
- Hint: The write side implementation can be based on a relational table with 2–3 string-based columns.
- Hint: The read side model does not need to mirror the tree structure; instead, design it for efficient permission checks with respect to runtime performance.
- Hint: Think if there are ways to get trapped in an infinite loading loop.

### Example Use Case
Imagine you are implementing this role-based permission system for a large, locally operating goods-producing company, where it is used for physical access control:

- The company operates multiple locations (plants).
- Each location consists of different departments (e.g., shopfloor, office, warehouse, and others you may define).
- Each department consists of multiple subsections (e.g. the office is parted in rooms)

![Plant-Layout Company](./Plant-Layout%20Company.png)

Permissions should be structured as URN-like strings. For example:  
`my_company.plant_3.warehouse.storage_2` grants access to storage 2 within the warehouse of plant 3.

Different types of employees require different access levels. For example:
- A finance worker only needs access to the office building and their assigned office room within their plant.
- A truck driver transporting goods between plants requires access to all warehouse sectors across all plants for loading and unloading.
- A production worker only needs access to all shopfloor sectors within their own plant.
- A logistics worker operates at a single plant and therefore requires access to the entire warehouse and shopfloor of that plant.
- Cleaning personnel should have access to all office rooms and warehouse storage areas.
- A service worker needs access to their own office at their home plant, but also requires access to the shopfloor of every plant to service machines.
- The head of a plant should have unrestricted access to everything within their plant.
- The company’s CEO should have full access to all locations and departments.

To simplify management, permissions should be organized hierarchically using roles. For example:
- To grant access to the entire shopfloor of plant `n`, define a role `my_company.plant_n.shopfloor` that aggregates permissions for all sectors (e.g., `my_company.plant_n.shopfloor.sector_1`, ..., `my_company.plant_n.shopfloor.sector_m`).
- Each user can also be modeled as a role. For example, an employee with ID `123456` working as a production worker in plant 3 could be represented as the role `my_company.employee.id_123456`, which includes the child role `my_company.plant_3.shopfloor`.

Create a data generator to populate the system:
- Create at least 20 roles to manage employee permissions, making use of nested roles to reflect realistic hierarchies.
- Create roles for at least 1000 users, assigning them appropriate role compositions based on the example use case.

### Technical Requirements
- The role-based permission system must be implemented using a CQRS architecture.
- Each service must follow either a layered or hexagonal architecture. Ensure proper decoupling of modules through well-defined interfaces.
- The `readside` and `writeside` services must each persist their data in separate PostgreSQL databases.
- The `eventbus` must persist all events in the provided KurrentDB event store.
- Inter-service communication must be implemented exclusively via REST and events.
- The `readside` and `writeside` services must be fully decoupled and communicate only through the `eventbus`.
- The `readside` must subscribe to the `eventbus` during startup. After subscription, the `eventbus` actively notifies the `readside` whenever a new event occurs.
- A frontend application is required to send commands to the `writeside` and perform queries on the `readside`. Extend the `docker-compose.yml` so that the frontend starts together with all other defined containers.
- New `readside` and `writeside` service instances must be able to register at any time. Upon initialization, a service must request and process all relevant past events to reconstruct the current system state.
- The architecture must support running multiple `readside` instances concurrently.
- A `readside` instance must be configurable (using Quarkus configuration) to support whitelist-based event filtering. An incoming event should only be processed if it matches at least one configured prefix.  
  Example: If a `readside` instance is configured to process only role events with IDs starting with `my_company.employee` or `my_company.plant_3`, then that instance effectively handles access control for plant 3 only.

## Architecture Overview

All three services persist their data in a database:
- `writeside` and `readside` each use their own separate PostgreSQL databases.
- `eventbus` stores all events in KurrentDB (formerly EventStoreDB).

Application ports and credentials are preconfigured in the `docker-compose.yml` file. When starting the compose setup, all three services and their respective databases are automatically initialized.

The Quarkus services are configured to run in development mode. This means:
- The Dev UI is available at `http://localhost:PORT/q/dev-ui/`.
- The Dev UI provides access to Swagger UIs for interacting with all available REST endpoints.
- Hot reloading is enabled, so changes are applied automatically without needing to manually restart services in most cases.

Application ports:
- `eventbus`: 9000
- `writeside`: 9100
- `readside`: 9200

KurrentDB comes with a own Web-UI, accessible via `http://localhost:9010/web`. This may be used for debug or management purposes. \
For database connection details and JVM debug ports, refer to the `docker-compose.yml` file. 


## Technical Guides

- Quarkus Dependency Injection: https://quarkus.io/guides/cdi-reference
- Quarkus Config Usage: https://quarkus.io/guides/config-reference
- Quarkus Hibernate Panache: https://quarkus.io/guides/hibernate-orm-panache
- Quarkus REST Endpoints: https://quarkus.io/guides/rest
- Quarkus REST Client: https://quarkus.io/guides/rest-client
- KurrentDB Java Client: https://docs.kurrent.io/clients/java/v1.1/getting-started.html