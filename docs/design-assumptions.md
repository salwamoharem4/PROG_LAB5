# Design Assumptions & Notes

# Design Assumptions & Notes

## 1. General Design Approach

The MARS-OPS project follows an object-oriented design approach. The system is divided into separate components, with each component responsible for a specific part of the simulation.

The main design goals are:

- Keep responsibilities separated between classes.
- Reuse common behavior through inheritance and abstraction.
- Protect object data through encapsulation.
- Use polymorphism to support different agent, mission, facility, and emergency types.
- Make the system easier to maintain and extend.

## 2. Agent Design

### 2.1 Agent Interface

The `Agent` interface defines the common operations that every colony agent must support, such as:

- Getting agent information.
- Moving between locations.
- Checking availability and usability.
- Checking capabilities.
- Assigning and releasing agents.

This allows the colony to work with different agent types through a common interface.

### 2.2 BaseAgent

`BaseAgent` is an abstract class that implements the shared behavior of agents.

Human and robotic agents extend this class, allowing common properties and methods to be implemented once instead of repeated in every concrete agent class.

### 2.3 Agent Availability

An agent is considered available only when it is not assigned to an active mission and is still usable.

The `assign()` and `release()` methods are used to control this state.

An unavailable or damaged agent cannot participate in new operations.

### 2.4 Human and Robot Differences

Human agents and robotic agents have different operational properties.

- Human agents have fatigue and health-related conditions.
- Robotic agents have battery levels and damage conditions.

These differences are handled in their respective classes rather than placing all properties inside the common agent class.

## 3. Mission Design

### 3.1 Mission Abstraction

Missions share common properties and behaviors, so a common mission abstraction is used.

Concrete mission classes define their own specific requirements and behavior while reusing the shared mission lifecycle.

### 3.2 Mission Requirements

A mission may require:

- Specific agent capabilities.
- Specific skills.
- Resources.
- Equipment.
- Minimum battery levels.
- A target location.
- A defined execution duration.

The mission is validated before execution to ensure that its requirements are satisfied.

### 3.3 Mission Assignment

Agents must be available and suitable for the mission before they can be assigned.

An agent that lacks a required capability or skill cannot be assigned.

### 3.4 Mission Lifecycle

Missions follow a controlled lifecycle using `MissionStatus`.

The main states are:

- `PENDING`
- `ASSIGNED`
- `IN_PROGRESS`
- `COMPLETED`
- `FAILED`
- `CANCELLED` (if supported by the implementation)

A mission cannot be considered completed until its required execution time has elapsed successfully.

### 3.5 Agent Release

When a mission finishes or fails, its assigned agents must be released so they can participate in future operations.

## 4. Resource Management

### 4.1 Resource Storage

Resources are stored inside individual facilities rather than in one global resource object.

The colony calculates total available resources by combining the resources of its operational facilities.

### 4.2 Operational Facilities

Only operational facilities contribute to colony-wide resource totals.

Resources inside non-operational facilities are excluded from the available colony resources.

### 4.3 Resource Allocation

When a mission requires resources, the colony checks whether the total available quantity is sufficient.

If resources are available, the required quantities can be collected from multiple facilities.

The allocation follows the order in which facilities are stored in the colony's collection.

### 4.4 Resource Safety

The system validates resource availability before consumption.

A mission must not consume resources if the required quantity is unavailable.

Resource quantities must never become negative.

### 4.5 Emergency Resources

Emergency operations can consume resources independently of normal mission execution.

For example, an oxygen leak requires spare parts to repair the affected facility.

## 5. Emergency Management

### 5.1 Emergency Abstraction

Emergencies share common properties, including:

- Emergency type.
- Affected location.
- Current status.
- Unresolved reason.
- Dispatched responders.

The abstract `Emergency` class provides shared emergency behavior, while concrete emergency classes implement their own resolution logic.

### 5.2 Emergency Response Team

The current implementation requires an emergency response team containing:

- One available human agent.
- One available robotic agent.

Both responders must possess the capability required by the emergency.

This policy ensures that emergency operations involve both human and robotic support.

### 5.3 Responder Dispatch

Before dispatching a responder, the system checks availability and the required emergency capability.

Selected responders are moved to the emergency location and marked as assigned.

### 5.4 Responder Release

After an emergency is resolved or marked unresolved, dispatched responders are released.

This allows them to become available for future operations.

### 5.5 Oxygen Leak Assumptions

The oxygen leak applies an initial oxygen loss when the emergency is created.

If the emergency cannot be resolved because suitable responders are unavailable or spare parts are insufficient, another oxygen loss is applied to represent the continuing leak.

If the repair succeeds, the required spare parts are consumed and the emergency becomes `RESOLVED`.

## 6. Environment Management

Environmental conditions are managed separately from agents and missions.

The environment component is responsible for representing conditions that may affect colony operations.

### 6.1 Dust Storm

A dust storm prevents surface exploration missions from starting.

The system checks the environmental condition before allowing the mission to execute.

If surface operations are restricted, the mission is rejected and a failure reason is recorded.

### 6.2 Environmental Changes

Environmental conditions can change during simulation execution.

The environment component checks registered missions when the condition changes, allowing environmental effects to be applied according to the implemented rules.

## 7. Simulation Time

The simulation component controls the progression of time.

Time advances in discrete units called cycles.

During each cycle, active missions can advance their elapsed execution time.

When a mission reaches its required duration, it can be completed and its assigned agents released.

This approach makes mission execution predictable and easier to demonstrate.

## 8. Validation and Error Handling

The project uses validation to prevent invalid system states.

Examples include:

- Rejecting null or invalid object data.
- Preventing negative resource quantities.
- Rejecting missions with unsuitable agents.
- Checking equipment and resource availability.
- Preventing unavailable agents from being assigned.
- Rejecting operations that violate environmental restrictions.

When an operation cannot proceed, the system provides a failure reason to explain the rejection.

## 9. Collections and Encapsulation

Collections are used to manage groups of agents, facilities, missions, equipment, and responders.

Where appropriate, collections are exposed through unmodifiable views to prevent external classes from directly modifying internal data.

Important fields are kept private, and public methods are used to control access to object state.

## 10. Additional Notes

- `Capability`, `EmergencyType`, `ResourceType`, and mission statuses are represented using enumerations where appropriate.
- Shared behavior is implemented in abstract classes to reduce code duplication.
- Interfaces are used to define common behavior without requiring every implementation to share the same class hierarchy.
- The colony acts as the main coordinator of system operations.
- Each concrete mission and emergency is responsible for its own specific requirements and behavior.
- The five demonstration scenarios are used to verify the main system behaviors.

---

