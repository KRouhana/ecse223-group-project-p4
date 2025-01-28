# ClimbSafe Application Overview

The ClimbSafe application is structured around clear **MVC principles**, uses **Umple** to generate a robust domain model, persists data via **serialization**, enforces business logic through **controller classes**, and provides a user-friendly **JavaFX interface**. Testing follows **BDD practices**, ensuring each feature is validated against real-world scenarios. All of this is supported by thorough validation, role-based permissions, and careful management of equipment, bundles, and assignments.


![Class Diagram](https://github.com/user-attachments/assets/7de61bde-27b4-4c33-922a-282ecd164f8e)

---

## 1. General Architecture and Design
- **MVC Structure**: The application is divided into **Model**, **View**, and **Controller** layers:
  - **Model** (`model` package): Contains data entities like `ClimbSafe`, `Member`, etc.
  - **Controller** (`controller` package): Implements business logic, orchestrating interactions between the UI and model.
  - **View** (`javafx/fxml`): JavaFX-based user interface; FXML files define layouts, and JavaFX controller classes capture UI events.
- **Entry Point**: `ClimbSafeApplication.java` launches the JavaFX application and sets up the main UI.
- **Flow**: Users interact with the JavaFX UI → The UI controllers pass requests to the business controllers → The controllers manipulate the domain model and update the view.

---

## 2. Domain Model and Umple Integration
- **Umple-Generated Model**: Classes like `ClimbSafe`, `Member`, and `Guide` are defined in `.ump` files (e.g., `ClimbSafe.ump`) and automatically generate Java code.
- **Inheritance Hierarchy**:
  - `User` is a base abstract class; `NamedUser` extends it for entities needing a `name` and `emergencyContact`.
  - `BookableItem` is another abstract class extended by `Equipment` and `EquipmentBundle`.
- **Associations**:
  - `BundleItem` links `Equipment` to `EquipmentBundle` with specified quantities.
  - `Assignment`, `Hotel`, and `Review` create relationships among `Member`, `Guide`, and other entities.
- **Constraints**: Umple constraints (like unique emails and mandatory fields) ensure integrity and consistency at the model level.

---

## 3. Persistence Layer
- **Classes**: `ClimbSafePersistence.java` and `PersistenceObjectStream.java` handle saving/loading of the application’s state.
- **Serialization**:
  - Objects (e.g., `ClimbSafe`, `Member`) are serialized to a file (commonly `data.climbsafe`).
  - When the application starts, it attempts to deserialize (`load`) the saved state; on shutdown or significant changes, the current state is serialized (`save`).
- **File-Based Storage**: This plain-object serialization approach keeps the application simple yet effective for the required data persistence.

---

## 4. Business Logic / Controller Layer
- **Controller Classes**:
  - `ClimbSafeFeatureSet1Controller` through `ClimbSafeFeatureSet7Controller` and `ExtraFeaturesController` each manage specific features or modules (e.g., members, guides, equipment).
- **Responsibilities**:
  - Validate inputs, apply business rules (e.g., preventing double-booking of guides), and update the model.
  - Throw exceptions (such as `InvalidInputException`) if constraints or rules are violated.
- **Error Handling**: Errors are sent back to the UI layer, which informs the user with appropriate messages.

---

## 5. GUI (JavaFX) Layer
- **FXML Files**: Found in `javafx/fxml/pages`, each file (e.g., `AssignmentOperations.fxml`, `EquipmentOperations.fxml`) defines the layout for different parts of the application.
- **JavaFX Controllers**:
  - `AssignmentOperationsController.java`, `EquipmentOperationsController.java`, etc., capture UI events and invoke the business logic controllers.
- **Interaction Flow**: User clicks UI components → JavaFX controller receives the event → Invokes feature-set controller methods → Results (success or error) are reflected back in the UI.

---

## 6. Gradle Build and Project Configuration
- **Gradle Plugins**:
  - `application` plugin for running the JavaFX app.
  - `org.openjfx.javafxplugin` for JavaFX support.
- **Dependencies**: Managed in `build.gradle` (e.g., JavaFX modules, JUnit for testing, Cucumber for BDD).
- **Tasks**:
  - Standard tasks like `build`, `run`, and `test`.
  - Custom tasks, such as `copyResources`, may be included to handle resources before runtime.

---

## 7. Handling User Roles and Permissions
- **Inheritance**:
  - `User` → `NamedUser` → `Guide` or `Member`
  - A separate `Administrator` class extends `User`.
- **Role-Based Restrictions**:
  - **Administrator**: Full system access (e.g., add equipment).
  - **Guide**: Manages guiding assignments, but no system-wide control.
  - **Member**: Limited to booking, viewing personal info, writing reviews.
- **Controller Checks**: Before certain operations (e.g., adding equipment), the system ensures the current user is an Administrator.

---

## 8. Assignment and Scheduling Logic
- **Classes**: `Assignment.java` and `AssignmentController.java` coordinate assigning guides to members.
- **State Machine**:
  - Assignments transition through states like `Assigned`, `Paid`, `Started`, `Finished`.
- **Availability Checks**:
  - Guides track weeks allocated (`weeksTaken`); the system enforces no overbooking.
- **Conflict Handling**: If a guide is unavailable or assignment data is invalid, the system throws an exception, stopping the process.

---

## 9. Review and Ratings Feature
- **Model**: `Review.java` links to a `Member` and an `Assignment`.
- **Creation Flow**:
  - A member chooses a rating, writes a comment, and the system creates a `Review` object.
  - Stored in the `ClimbSafe` system’s review list.
- **UI**: `ReviewOperationsController` manages the FXML page where members submit and view reviews; administrators or guides may also view them, depending on permissions.

---

## 10. Testing Strategy (BDD/Cucumber)
- **Gherkin Feature Files**:
  - Scenarios in `.feature` files (e.g., `AddEquipment.feature`) describe behaviors in **Given-When-Then** format.
- **Step Definitions**:
  - Java classes (e.g., `AssignmentFeatureStepDefinitions.java`) map each Gherkin step to actual test code that invokes the relevant controllers.
- **Purpose**:
  - Tests reflect real user stories, ensuring the application meets specified requirements and continues to do so as it evolves.

---

## 11. Equipment and Bundles Management
- **Key Classes**:
  - `Equipment.java` (individual items)
  - `EquipmentBundle.java` (discounted groups of items)
  - `BundleItem.java` (links specific equipment to a bundle with a quantity)
- **Stock Tracking**:
  - `Equipment` tracks total stock; `BundleItem` references how much of an item is in each bundle.
  - When renting, the system checks if enough stock is available; if not, it rejects the request.

---

## 12. Data Validation and Constraints
- **Umple Constraints**:
  - Enforce unique fields (like `email` for `User`) and non-negative numeric values (like `weight` in `Equipment`).
- **Controller-Level Checks**:
  - Validate data beyond model-level constraints (e.g., ensuring a date range is valid or a field isn’t empty).
- **Error Reporting**:
  - The system throws `InvalidInputException` for invalid inputs; the UI catches and displays these errors to users.

---

## 13. Logging and Demo Data
- **Demo File**: `ClimbSafeDemo.data` (or similarly named) can be loaded with “starter” data.
- **Purpose**:
  - Provides a ready-to-use dataset for demonstrations or initial testing.
  - Often pre-populates the system with an Administrator, some Equipment items, Bundles, Members, etc.
- **Workflow**:
  - A class like `DemoFileCreator` (in a `test/demo` package) seeds the system with default objects → The file is saved → The application can load this data at startup for quick demos or test runs.

