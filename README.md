# MARS-OPS: Autonomous Mars Colony Management & Emergency Simulator

## Project Directory & File Structure

This repository is structured in accordance with the project submission specifications:

```text
PROG_LAB5/
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
│       └── [exported-images-or-pdf]
│
├── src/
│   └── Main.java
│
└── bin/
    └── [compiled-classes]
```

### Directory Details:

* **`src/` (Java Source Code):**
  * Contains all project `.java` source files, including the central driver [`Main.java`](src/Main.java).
  * Houses all domain models, interfaces, enums, and simulation coordinators.
  * When compiled, bytecodes are directed to the `bin/` folder.

* **`uml/` (UML Design Source Files):**
  * Stores the editable source diagram files created with diagramming tools (e.g., Draw.io `.drawio` files).
  * Contains all 5 mandatory UML models:
    1. **Class Diagram** (`class-diagram.drawio`): Static system architecture and relationships.
    2. **Object Diagram** (`object-diagram.drawio`): Runtime snapshot of entities, facilities, and missions.
    3. **State Machine Diagram** (`state-machine-diagram.drawio`): Lifecycle and transitions of missions.
    4. **Sequence Diagrams** (`sequence-diagrams.drawio`): Collaboration flows for mission execution, emergency handling, and environmental blocks.
    5. **Use Case Diagram** (`use-case-diagram.drawio`): System boundary, external actors, and use case extensions/inclusions.

* **`uml/exports/` (Rendered Diagrams):**
  * Stores high-resolution exported images (`.png`) or `.pdf` versions of each diagram for final review and grading.

* **`docs/` (Design Notes & Scenarios):**
  * `design-assumptions.md`: Working notes detailing object relationship justifications, custom rules, and edge-case assumptions.
  * `demo-scenarios.md`: Detailed test plans and expected console traces for Scenarios 1 through 5.

* **`bin/` (Compiled Bytecode):**
  * Output directory where the Java compiler (`javac`) places generated `.class` files.
  * Kept out of version control via `.gitignore`.

* **`README.md`:**
  * Master documentation covering architecture, policies, rules, compilation commands, and scenario run instructions.

* **Submission Packaging:**
  * The entire root folder `StudentID-Prog-II-Lab5/` is compressed into `StudentID-Prog-II-Lab5.zip` for final lab delivery.

---

## 1. System Description & Main Components
<!-- TODO: Add description of the system and its main components -->

## 2. Important Design Decisions & Assumptions
<!-- TODO: Add explanation of important design decisions and assumptions -->

## 3. Resource Allocation Rules
<!-- TODO: Document resource allocation rules and chosen policies -->

## 4. Emergency Rules
<!-- TODO: Document emergency rules and response conditions -->

## 5. Environmental Effects
<!-- TODO: Document environmental effects and how in-progress operations are impacted -->

## 6. Mission States & Lifecycle
<!-- TODO: Document mission states and transitions -->

## 7. Additional Implementation Decisions
<!-- TODO: Document other implementation decisions left open by the specification -->

## 8. Compilation & Execution Instructions
<!-- TODO: Add instructions for compiling and running the application -->

## 9. Required Demonstration Scenarios
<!-- TODO: Add instructions for executing each of the 5 required demonstration scenarios -->
