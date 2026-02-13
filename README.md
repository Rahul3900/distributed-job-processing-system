
# 🚀 Distributed Job Processing & Monitoring System

A **production-grade asynchronous job orchestration system** built using **Java and Spring Boot**, designed to reliably process high-volume jobs with **controlled concurrency, back-pressure, retries, and transactional safety**.

This project demonstrates how real backend platforms execute tasks under heavy load **without thread exhaustion, database deadlocks, or stuck jobs**.

---

## 🧠 Problem Statement

In real-world backend systems:

- Incoming requests can be much faster than job execution
- Uncontrolled async execution leads to thread exhaustion
- Incorrect transaction boundaries cause deadlocks
- Jobs often get stuck in `RUNNING` state forever

This project solves these problems by **decoupling job creation from execution** and introducing **scheduler-based orchestration with strict concurrency limits**.

---

## ✨ Key Features

- Asynchronous job execution using `@Async`
- Scheduler-driven job orchestration
- Dynamic concurrency control (back-pressure)
- FIFO job execution
- Retry mechanism with retry limits
- Transaction-safe job lifecycle management
- No stuck `RUNNING` jobs
- Successfully tested with **1000+ jobs**

---

## 🧱 High-Level Architecture

Client
|
v
REST Controller
|
v
Job Service (CREATE ONLY)
|
v
Database (CREATED)
|
v
Job Execution Scheduler
|
v
Async Job Executor (@Async)
|
v
Database (RUNNING → COMPLETED / FAILED)

---


### 🔑 Core Design Principle
 **Job creation is completely decoupled from job execution**

This ensures system stability and predictable throughput under load.

---

## 🧩 Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Scheduler
- Spring Async
- PostgreSQL
- Maven

---

## 📂 Package Structure

com.global.platform.job_processing_system
│
├── controller // REST APIs
├── service // Business logic
├── scheduler // Job orchestration & retries
├── repository // Database access
├── entity // JPA entities
├── enums // Job lifecycle states
└── config // Async executor configuration

---

## 📘 Job Lifecycle

CREATED
↓
RUNNING
↓
COMPLETED
↓
FAILED → RETRYING → RUNNING

Each transition is **explicit, controlled, and transactional**.

---

## 🧠 Core Components Explained

### 1️⃣ Job Entity
Represents a unit of work and tracks:
- Job status
- Retry count
- Execution time
- Creation, start, and completion timestamps

---

### 2️⃣ JobRepository
Handles safe and ordered job selection.

Key methods:
- `countByStatus(...)` – Enforces concurrency limits
- `findByStatusOrderByCreatedAtAsc(...)` – FIFO job selection using pagination

---

### 3️⃣ JobController
- Accepts job creation requests
- Does **not** execute jobs
- Keeps API layer thin and safe

---

### 4️⃣ JobService
- Creates jobs
- Initializes lifecycle
- Persists jobs in `CREATED` state

---

### 5️⃣ JobExecutionScheduler
The **heart of the system**.

Responsibilities:
- Calculates available execution capacity
- Picks jobs dynamically based on capacity
- Marks jobs as `RUNNING`
- Dispatches jobs asynchronously

 The scheduler is intentionally **not transactional** to avoid async deadlocks.

---

### 6️⃣ JobExecutionService
Executes jobs asynchronously.

Key characteristics:
- Uses `@Async` for parallel execution
- Uses `@Transactional` to guarantee DB updates
- Fetches jobs by ID to avoid stale entity issues
- Responsible for final job state (`COMPLETED` / `FAILED`)

---

### 7️⃣ RetryScheduler
Handles failed jobs safely:
- Periodically picks failed jobs
- Applies retry limits
- Prevents infinite retries
- Avoids recursive execution

---

## ⚙️ Async Executor Configuration

corePoolSize = 10
maxPoolSize = 10
queueCapacity = 50

This configuration ensures:
- Predictable throughput
- No thread explosion
- No database starvation

---

## 🐞 Production Issues Solved

| Issue | Root Cause | Solution |
|-----|-----------|---------|
Jobs stuck in RUNNING | Scheduler held DB transaction | Removed `@Transactional` from scheduler |
Unlimited RUNNING jobs | No back-pressure | Dynamic capacity control |
Retry count not updating | Stale entity updates | Transactional retry logic |
Async not parallel | Default executor | Custom ThreadPoolTaskExecutor |
CREATED jobs not draining | Hard concurrency limit | Capacity-based job picking |

---

## 🧪 How to Run & Test

### Start the application

```bash```
mvn spring-boot:run

### Create Job API

```html```
POST /jobs?jobName=example-job

### Monitor Jobs

```sql```
SELECT status, COUNT(*) FROM jobs GROUP BY status;

---

## ✨ Summary

This project demonstrates a reliable and scalable approach to asynchronous job processing using Spring Boot with proper concurrency control and transactional safety.



