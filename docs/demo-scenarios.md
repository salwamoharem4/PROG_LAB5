# Demo Scenarios Guide

# Demo Scenarios Guide

## 1. Introduction

This document describes the five demonstration scenarios used to test the MARS-OPS system. Each scenario demonstrates a specific system feature, including mission execution, agent validation, resource management, emergency handling, and environmental restrictions.

## 2. Scenario 1: Successful Exploration Mission

**Objective:** Demonstrate a mission that starts and completes successfully.

**Setup:**
- Habitat oxygen: 100
- Battery power: 50
- Agents: Scientist Dr. Jane and Rover Curiosity-2
- Mission ID: 701
- Mission duration: 3 simulation cycles

**Execution:**
1. Create the exploration mission.
2. Assign the scientist and rover.
3. Check that both agents meet the mission requirements.
4. Start the mission.
5. Advance the simulation by three cycles.

**Expected Results:**
- The mission starts successfully.
- Both agents become unavailable while assigned.
- The mission reaches the `COMPLETED` state.
- The scientist's fatigue increases.
- The rover's battery level is updated.
- Both agents become available again.

## 3. Scenario 2: Unsuitable Agent

**Objective:** Verify that an agent cannot perform a mission without the required capabilities.

**Setup:**
- Create an exploration mission.
- Assign an engineer who does not have the `EXPLORE` capability.

**Execution:**
1. Attempt to assign the engineer to the mission.
2. Validate the agent's capabilities.
3. Check the mission status.

**Expected Results:**
- The mission is rejected.
- The system reports that the agent does not meet the mission requirements.
- The mission does not start.
- The engineer remains available.

## 4. Scenario 3: Resource Shortage

**Objective:** Ensure that missions cannot start when the colony has insufficient resources.

**Setup:**
- Available spare parts: 2
- Required spare parts: 10
- Required battery power: 20

**Execution:**
1. Create a mission with the specified resource requirements.
2. Attempt to start the mission.
3. Check the available resources.

**Expected Results:**
- The mission is rejected because of insufficient resources.
- The mission does not become active.
- Available resources remain unchanged.
- Resource quantities never become negative.

## 5. Scenario 4: Oxygen Leak Emergency

**Objective:** Demonstrate how the colony handles an oxygen leak emergency.

**Setup:**
- Facility: Biodome
- Initial oxygen: 150
- Initial spare parts: 5
- Oxygen loss: 30
- Required spare parts for repair: 1
- Responders: One qualified human agent and one qualified robot.

**Execution:**
1. Create the oxygen leak emergency.
2. Apply the initial oxygen loss.
3. Check the emergency status.
4. Dispatch the required human and robot responders.
5. Check spare parts availability.
6. Consume the required spare part and resolve the emergency.

**Expected Results:**
- Oxygen decreases from 150 to 120.
- The emergency initially has the `PENDING` status.
- Both responders are dispatched successfully.
- One spare part is consumed.
- The emergency becomes `RESOLVED`.
- Both responders become available again.

## 6. Scenario 5: Dust Storm Restrictions

**Objective:** Verify that environmental conditions can prevent unsafe surface missions.

**Setup:**
- Set the environment condition to `DUST` (dust storm).
- Prepare a surface exploration mission.
- Assign suitable agents.

**Execution:**
1. Change the environmental condition to a dust storm.
2. Attempt to start the surface exploration mission.
3. Check the mission status and agent availability.

**Expected Results:**
- The mission is rejected because of the environmental restriction.
- The system provides a reason for rejecting the mission.
- The mission does not become active.
- The assigned agents remain available.

## 7. Conclusion

The five scenarios demonstrate the main functionalities of MARS-OPS:

- Successful mission execution and lifecycle management.
- Agent capability validation.
- Resource availability checks.
- Emergency response and resource consumption.
- Environmental restrictions on mission execution.

Together, these scenarios help verify that the colony system handles normal operations and unexpected situations correctly.
