# MARS-OPS: Autonomous Mars Colony Management & Emergency Simulator

## Project Overview

MARS-OPS is a Java-based simulation system designed to manage a Mars research colony. It models human personnel, robotic units, facilities, resources, missions, environmental conditions, and emergency situations.

The system demonstrates fundamental Object-Oriented Programming concepts, including encapsulation, abstraction, inheritance, polymorphism, and object relationships.

The project includes five demonstration scenarios covering successful mission execution, mission rejection, resource shortages, emergency handling, and environmental restrictions.

---

## Table of Contents

1. [System Description and Main Components](#1-system-description--main-components)
2. [Important Design Decisions and Assumptions](#2-important-design-decisions--assumptions)
3. [Resource Allocation Rules](#3-resource-allocation-rules)
4. [Emergency Rules](#4-emergency-rules)
5. [Environmental Effects](#5-environmental-effects)
6. [Mission States and Lifecycle](#6-mission-states--lifecycle)
7. [Additional Implementation Decisions](#7-additional-implementation-decisions)
8. [Compilation and Execution Instructions](#8-compilation--execution-instructions)
9. [Required Demonstration Scenarios](#9-required-demonstration-scenarios)

---

## Project Directory and File Structure

The repository is organized according to the project submission specifications.

```text
PROG_LAB5/
│
├── .gitignore
├── README.md
│
├── docs/
│   ├── design-assumptions.md
│   └── demo-scenarios.md
│
├── uml/
│   ├── class-diagram.drawio
│   ├── object-diagram.drawio
│   ├── state-machine-diagram.drawio
│   ├── sequence-diagrams.drawio
│   ├── use-case-diagram.drawio
│   └── exports/
│       └── exported-images-or-pdf
│
├── src/
│   ├── Main.java
│   ├── agent/
│   │   ├── Agent.java
│   │   ├── BaseAgent.java
│   │   ├── human/
│   │   │   ├── Human.java
│   │   │   ├── HealthStatus.java
│   │   │   ├── Scientist.java
│   │   │   ├── Engineer.java
│   │   │   └── MedicalOfficer.java
│   │   └── robot/
│   │       ├── Robot.java
│   │       ├── Rover.java
│   │       ├── RepairRobot.java
│   │       └── Drone.java
│   │
│   ├── colony/
│   │   ├── Colony.java
│   │   └── Simulation.java
│   │
│   ├── core/
│   │   ├── Capability.java
│   │   ├── Skill.java
│   │   ├── Location.java
│   │   ├── Surface.java
│   │   └── EmergencyType.java
│   │
│   ├── environment/
│   │   ├── Environment.java
│   │   └── EnvironmentCondition.java
│   │
│   ├── emergencies/
│   │   ├── Emergency.java
│   │   ├── EmergencyStatus.java
│   │   ├── OxygenLeak.java
│   │   └── Fire.java
│   │
│   ├── facilities/
│   │   ├── Facility.java
│   │   ├── Habitat.java
│   │   ├── Laboratory.java
│   │   ├── StorageFacility.java
│   │   ├── PowerStation.java
│   │   ├── CommunicationStation.java
│   │   └── Equipment.java
│   │
│   ├── mission/
│   │   ├── Mission.java
│   │   ├── BaseMission.java
│   │   ├── MissionStatus.java
│   │   ├── MissionExecutor.java
│   │   └── missiontype/
│   │       ├── MissionType.java
│   │       ├── ExplorationMission.java
│   │       ├── RepairMission.java
│   │       ├── ResearchMission.java
│   │       └── RescueMission.java
│   │
│   └── resource/
│       └── ResourceType.java
│
└── bin/
    └── compiled-classes/
```

### Directory Details

| Directory / File | Description |
|---|---|
| `src/` | Contains all Java source files and application components. |
| `uml/` | Contains the five editable UML diagrams and their exported versions. |
| `docs/` | Contains design assumptions and demonstration scenario documentation. |
| `bin/` | Stores compiled Java `.class` files and is excluded from version control. |
| `README.md` | Main project documentation. |
| `.gitignore` | Specifies files and directories excluded from Git. |

### UML Diagrams

The project includes five UML diagrams:

1. **Class Diagram:** Represents the system structure, classes, and relationships.
2. **Object Diagram:** Shows a runtime snapshot of system objects.
3. **State Machine Diagram:** Describes mission states and transitions.
4. **Sequence Diagrams:** Illustrate interactions during mission execution, emergency handling, and environmental restrictions.
5. **Use Case Diagram:** Represents system actors and their interactions with the system.

---

## 1. System Description & Main Components

The system is divided into several main components.

### 1.1 Agents

Agents represent the human personnel and robotic units working in the colony.

- **Agent:** Interface defining the common operations required from colony agents.
- **BaseAgent:** Abstract class implementing shared agent behavior, such as identity, location, availability, and mission assignment.
- **Human:** Represents human personnel.
- **Robot:** Represents robotic units.

Concrete agent types include:

- `Scientist`: Performs scientific and exploration-related activities.
- `Engineer`: Handles repair-related operations.
- `MedicalOfficer`: Handles medical responsibilities.
- `Rover`: Supports surface exploration.
- `RepairRobot`: Supports repair operations.

Agents have capabilities and availability conditions that determine whether they can participate in missions or emergencies.

### 1.2 Colony

The `Colony` class coordinates the main system components. It manages:

- Facilities and agents.
- Colony-wide resource totals.
- Resource validation and consumption.
- Mission submission and execution.
- Environmental conditions.
- Emergency-related operations.
- Colony status reports.

### 1.3 Facilities and Equipment

Facilities represent locations where colony resources and equipment are stored.

Examples include habitats, laboratories, and other facility types.

Facilities maintain their operational status, resources, and equipment. Equipment can be checked before a mission starts to ensure that the required equipment is available and operational.

### 1.4 Resources

The system uses a `ResourceType` enumeration to represent resources such as:

- Oxygen
- Water
- Food
- Fuel
- Battery power
- Medical supplies
- Spare parts

Resource quantities are tracked by facilities and calculated at the colony level.

### 1.5 Missions

Missions represent activities performed by colony agents.

The system includes a mission abstraction and concrete mission types, including:

- Exploration missions.
- Repair missions.
- Research missions.
- Rescue missions.

Missions contain requirements that must be satisfied before execution, such as agent capabilities, skills, equipment, battery power, and resources.

### 1.6 Emergencies

The emergency system handles situations that threaten colony operations.

The implemented oxygen leak scenario demonstrates how an emergency affects a facility, consumes oxygen, identifies suitable responders, consumes spare parts, and updates the emergency status.

### 1.7 Environment and Simulation

The environment component manages environmental conditions that can affect colony operations.

The simulation component advances time and allows active missions to progress until completion or failure.

---

## 2. Important Design Decisions & Assumptions

The following decisions describe the project's implementation and selected design policies.

### 2.1 Object-Oriented Design

The project uses interfaces and abstract classes to avoid repeating common behavior.

- `Agent` defines common agent operations.
- `BaseAgent` provides shared implementation.
- `Human` and `Robot` extend `BaseAgent`.
- Concrete agent classes inherit from their corresponding parent classes.
- Mission types share common mission behavior through the mission abstraction.

This structure supports inheritance, abstraction, encapsulation, and polymorphism.

### 2.2 Agent Availability

An agent cannot participate in another operation while already assigned to an active mission.

The `assign()` and `release()` methods control assignment state.

Availability also depends on whether the agent is operational and usable.

### 2.3 Colony Ownership and Management

The `Colony` class maintains collections of facilities and agents.

It coordinates missions and resources rather than allowing each facility to independently manage mission execution.

### 2.4 Resource Allocation Policy

Resources are stored in individual facilities but managed at the colony level.

When consuming resources from multiple facilities, the implementation iterates through the facility collection and consumes available quantities until the requirement is satisfied.

Therefore, the allocation order follows the order in which facilities are stored in the colony's collection.

### 2.5 Mission Execution

A mission is validated before it starts.

The system checks the selected agents, mission requirements, equipment, environmental conditions, and available resources.

Resources are consumed only after the relevant validation and agent assignment steps succeed.

### 2.6 Emergency Response Team

The current emergency dispatch implementation requires both:

- One available human responder.
- One available robotic responder.

Both must have the capability required by the emergency.

This is a design policy selected for the current implementation.

---

## 3. Resource Allocation Rules

Resources are stored in facilities but managed through the colony.

### 3.1 Colony-Wide Resource Totals

The colony calculates its total resources by adding the quantities stored in all operational facilities.

Resources held in non-operational facilities are excluded from the total.

### 3.2 Resource Validation

Before a mission consumes resources, the system checks whether the colony has enough of every required resource.

If any requirement cannot be satisfied:

- The operation is rejected.
- No resources should be consumed.
- The failure reason is reported.
- Resource quantities must never become negative.

### 3.3 Resource Consumption

When the colony has enough resources, the system can collect the required quantities from multiple operational facilities.

For example, if a mission requires 10 units of spare parts, the colony may obtain them from more than one facility.

### 3.4 Emergency Resource Consumption

Emergency resource consumption follows the emergency's own requirements.

For example, the oxygen leak scenario requires spare parts to repair the leak.

The affected facility must have enough spare parts before the repair can succeed.

### 3.5 Local Oxygen Loss

An oxygen leak affects the oxygen stored in the affected facility only.

The oxygen loss reduces that facility's resource quantity and consequently reduces the colony-wide oxygen total.

---

## 4. Emergency Rules

The emergency system represents unexpected situations that require a response from colony agents.

### 4.1 Emergency Handling Process

The oxygen leak implementation follows these main steps:

1. Create and detect the emergency.
2. Identify the affected facility.
3. Apply the initial oxygen loss.
4. Search for suitable available responders.
5. Dispatch the required human and robotic responders.
6. Check whether the affected facility has enough spare parts.
7. Consume the required spare parts if available.
8. Mark the emergency as resolved or unresolved.
9. Release dispatched responders when the response ends.

### 4.2 Oxygen Leak Behavior

The `OxygenLeak` class represents an oxygen leak affecting a specific facility.

The constructor applies the initial oxygen loss immediately.

If the emergency cannot be resolved because responders are unavailable or spare parts are insufficient, an additional oxygen loss is applied to represent the continuing leak.

If the response succeeds:

- The required spare parts are consumed.
- The emergency status changes to `RESOLVED`.
- Responders are released.
- No additional oxygen loss is applied.

### 4.3 Emergency Status

The emergency implementation uses the following statuses:

| Status | Description |
|---|---|
| `PENDING` | The emergency has been detected but not resolved. |
| `RESOLVED` | The emergency has been successfully handled. |
| `UNRESOLVED` | The emergency response could not be completed. |

### 4.4 Responder Availability

Responders are moved to the emergency location and assigned during the response.

When the response ends, they are released so they can become available for future operations.

---

## 5. Environmental Effects

Environmental conditions can prevent certain operations from starting or affect missions already in progress.

The project includes conditions such as:

- Normal conditions.
- Dust storms.
- Communication disruption.

### 5.1 Dust Storm

A dust storm prevents surface exploration missions from starting.

When an exploration mission is submitted during a dust storm:

1. The colony checks the current environmental condition.
2. The environment identifies that surface operations are restricted.
3. The mission is rejected.
4. A failure reason is stored.
5. The selected agents remain available because the mission was not started.

The demonstration output uses the failure reason:

```text
Surface operations are not allowed during a dust storm.
```

### 5.2 Environmental Changes During Execution

When the colony's environmental condition changes, the `setEnvironmentCondition()` method updates the environment and checks registered missions through the environment component.

This allows environmental changes to affect missions that are already active.

---

## 6. Mission States & Lifecycle

Each mission follows a lifecycle controlled by its status.

The project uses the `MissionStatus` enumeration.

### 6.1 Mission States

| State | Description |
|---|---|
| `PENDING` | Mission has been created but not assigned. |
| `ASSIGNED` | Suitable agents have been assigned and requirements are satisfied. |
| `IN_PROGRESS` | Mission execution has started. |
| `COMPLETED` | Mission has successfully finished. |
| `FAILED` | Mission could not be completed. |
| `CANCELLED` | Mission has been cancelled, if supported by the implementation. |

### 6.2 Mission Lifecycle

The normal mission execution process is:

1. Create a mission with its requirements.
2. Submit it to the colony.
3. Validate selected agents and their capabilities.
4. Check required equipment and resources.
5. Assign suitable agents.
6. Consume the required resources.
7. Start the mission and change its status to `IN_PROGRESS`.
8. Advance simulation time.
9. Complete or fail the mission.
10. Release assigned agents when the mission ends.

### 6.3 Mission Duration

Each mission has a duration measured in time units.

The simulation advances mission progress one unit at a time.

When the elapsed time reaches the required duration, the mission becomes `COMPLETED`.

### 6.4 Mission Failure

A mission can fail or be rejected for reasons such as:

- An unsuitable agent.
- Missing required capabilities or skills.
- Insufficient resources.
- Missing or non-operational equipment.
- Insufficient battery power.
- Environmental restrictions.
- Unavailable personnel.

The system stores a failure reason to explain why the operation could not proceed.

### 6.5 Active Mission Management

The colony tracks its active normal mission and updates that reference when the mission completes or fails.

The mission executor manages registered missions and advances their progress.

---

## 7. Additional Implementation Decisions

This section documents implementation policies and assumptions selected where the project specification leaves design decisions open.

### 7.1 Use of Enumerations

Enumerations are used for fixed sets of values, including:

- `ResourceType`
- `MissionStatus`
- `EmergencyType`
- `EnvironmentCondition`
- Agent-related states and capabilities where applicable.

This avoids relying on arbitrary strings for important system states.

### 7.2 Validation

Constructors and public methods validate important input values.

Examples include checking for null references, invalid resource quantities, invalid mission requirements, and invalid agent data.

Invalid operations are rejected rather than silently accepted.

### 7.3 Encapsulation

Important fields are kept private, and public methods are used to access or modify object state.

Collections returned to other classes are exposed as unmodifiable views where appropriate.

### 7.4 Separation of Responsibilities

The project separates responsibilities between its main components:

- `Colony` coordinates colony-wide operations.
- `BaseAgent` handles shared agent behavior.
- Facility classes manage their own resources and equipment.
- Mission classes represent mission requirements and lifecycle.
- `MissionExecutor` manages mission progression.
- Emergency classes manage emergency-specific response behavior.
- Environment classes manage environmental restrictions.
- `Simulation` coordinates the passage of time.

### 7.5 Status Reporting

The colony can generate a status report containing:

- Current environmental condition.
- Facilities and their operational states.
- Colony-wide resource totals.
- Agent locations and availability.
- Active mission information.

---

## 8. Compilation & Execution Instructions

### 8.1 Requirements

- Java Development Kit (JDK) 17 or later.
- A terminal or command prompt.
- A Java-compatible development environment, such as Visual Studio Code.

The project uses Java features such as collection factories (`List.of()`), which require a modern JDK.

### 8.2 Compile the Project

Open a terminal in the project root directory.

Create the output directory:

```bash
mkdir -p bin
```

Compile all Java source files:

```bash
javac -d bin $(find src -name "*.java")
```

The `-d bin` option directs compiled classes into the `bin/` directory while preserving their package structure.

### 8.3 Run the Project

Execute the main class:

```bash
java -cp bin Main
```

The program runs the five demonstration scenarios sequentially and prints their results in the terminal.

### 8.4 Recompile After Changes

Whenever source files are modified, compile the project again before running it:

```bash
javac -d bin $(find src -name "*.java")
java -cp bin Main
```

---

## 9. Required Demonstration Scenarios

The `Main` class executes the five required demonstration scenarios from Section 12 of the project specification.

<details>
<summary><strong>Scenario 1: Successful Exploration</strong></summary>

**Objective:** Demonstrate successful execution of an exploration mission.

The scenario creates:

- A habitat with oxygen and battery power.
- A scientist with the required geology skill.
- An exploration rover.
- A terrain scanner.
- An exploration mission with a duration of three cycles.

**Expected Behavior:**

- The mission is accepted.
- The scientist and rover become unavailable while the mission is active.
- Simulation time advances by three units.
- The mission becomes `COMPLETED`.
- The agents become available again.
- The scientist's fatigue and rover's battery level are updated.

</details>

<details>
<summary><strong>Scenario 2: Mission Rejection</strong></summary>

**Objective:** Demonstrate that an unsuitable agent cannot be assigned to a mission.

The scenario attempts to assign an exploration mission to an engineer who does not possess the required exploration capability.

**Expected Behavior:**

- The mission is rejected.
- The mission becomes `FAILED`.
- A meaningful failure reason is displayed.
- The mission never becomes active.
- The engineer remains available.

</details>

<details>
<summary><strong>Scenario 3: Resource Shortage</strong></summary>

**Objective:** Demonstrate safe resource handling when the colony does not have enough resources.

The scenario attempts to execute a mission requiring 10 spare parts and 20 battery power when the colony has only 2 spare parts.

**Expected Behavior:**

- The mission is rejected.
- The failure reason identifies insufficient colony resources.
- No unavailable resources are consumed.
- The spare parts balance remains unchanged.
- Resource quantities never become negative.

</details>

<details>
<summary><strong>Scenario 4: Emergency Handling</strong></summary>

**Objective:** Demonstrate the colony's response to an oxygen leak.

The scenario creates a biodome containing oxygen and spare parts, an engineer, and a repair robot.

An oxygen leak is then triggered.

**Expected Behavior:**

- The initial oxygen loss is applied.
- The emergency is detected as `PENDING`.
- Suitable responders are identified and dispatched.
- One spare part is consumed to repair the leak.
- The emergency becomes `RESOLVED`.
- The responders become available again.
- The final oxygen quantity reflects the initial loss.

</details>

<details>
<summary><strong>Scenario 5: Environmental Effect</strong></summary>

**Objective:** Demonstrate how environmental conditions affect mission execution.

The scenario changes the colony's environmental condition to `DUST` and attempts to start a surface exploration mission.

**Expected Behavior:**

- The environment changes to a dust storm condition.
- The exploration mission is rejected.
- The failure reason explains that surface operations are not allowed during a dust storm.
- The mission does not become active.
- The agents remain available.

</details>

---

## Documentation

Additional project documentation is available in the repository:

- [Design Assumptions](docs/design-assumptions.md)
- [Demo Scenarios Guide](docs/demo-scenarios.md)
- [UML Diagrams](uml/)

---

## Conclusion

MARS-OPS demonstrates how Object-Oriented Programming can be used to model and coordinate a complex system.

Through its agents, facilities, resources, missions, emergencies, and environmental components, the project demonstrates object interaction, state management, validation, resource allocation, and simulation time progression.

The five demonstration scenarios provide practical examples of the system's main functionality and show how the colony responds to both normal operations and unexpected situations.

---

**Project:** MARS-OPS – Autonomous Mars Colony Management & Emergency Simulator  
**Language:** Java  
**Academic Context:** Programming II – Fall 2026