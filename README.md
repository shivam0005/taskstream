# TaskStream

**TaskStream** is a modular, Kafka-based system designed to handle asynchronous task distribution using microservices. This repository is structured to support scalable message-driven components like task producers, consumers, and processing engines.

---

## 🏗️ Project Structure

taskstream/
├── task-producer/ # Module to accept task requests and publish them to Kafka
├── task-consumer/ # (Planned) Module to consume and process tasks from Kafka
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


📌 Roadmap
 ✅ Task Producer with dynamic Kafka topic creation

 Task Consumer to process messages

 Monitoring & retry logic

 Common module for DTOs and utils

 Authentication & authorization
