# TaskStream

**TaskStream** is a modular, Kafka-based system designed to handle asynchronous task distribution using microservices. This repository is structured to support scalable message-driven components like task producers, consumers, and processing engines.

---

## 🏗️ Project Structure

taskstream/
├── task-producer/ # Module to accept task requests and publish them to Kafka
├── task-consumer/ # Module to consume and process tasks from Kafka and execute them (e.g., send email)
├── common/ # (Optional) Shared utilities and DTOs
└── pom.xml # Parent Maven project file


---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Apache Kafka running locally or remotely
- Git

### Clone the repo

```bash
git clone https://github.com/shivam0005/taskstream.git
cd taskstream

Run the Task Producer
cd task-producer
mvn spring-boot:run

POST /tasks
Content-Type: application/json

{
  "taskType": "email",
  "payload": {
    "to": "user@example.com",
    "subject": "Hello",
    "body": "Welcome!"
  }
}

Run the Task Consumer

cd ../task-consumer
mvn spring-boot:run

On receiving a task from Kafka, the consumer dynamically routes it to the correct processor. Currently supports:

✅ email task type: sends real email using SMTP

🧠 Architecture Overview
Task Producer: Accepts incoming task requests via REST and pushes to Kafka topic.

Task Consumer: Listens to Kafka, deserializes the message, and routes it to appropriate task processor (e.g., email sender).

Processor Registry: Maps taskType to corresponding TaskProcessor implementation.

Extensible Design: Easily pluggable for more task types (e.g., SMS, push notifications).


📌 Roadmap

✅ Task Producer with dynamic Kafka topic creation

✅ Task Consumer module with task processing framework

✅ Email task processor (real email sending using SMTP)

⏳ Common module for DTOs and shared logic

⏳ Retry logic and error handling

⏳ Observability: logging, tracing, metrics

⏳ Authentication & Authorization

⏳ Docker & CI/CD setup
