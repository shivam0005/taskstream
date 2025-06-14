# TaskStream

**TaskStream** is a modular, Kafka-based system designed to handle asynchronous task distribution using microservices. This repository is structured to support scalable message-driven components like task producers, consumers, and processing engines.

---

## 🏗️ Project Structure

taskstream/
  - task-producer/ # Accepts task requests and publishes to Kafka
  - task-consumer/ # Consumes tasks from Kafka and executes processing (email, SMS)
  - notification-service/ # Sends email/SMS via third-party APIs (e.g., SMTP, Msg91)
  - pom.xml # Parent Maven project file


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

▶️ Run the Task Producer
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

{
  "taskType": "sms",
  "payload": {
    "name": "xxxxx",
    "mobile": "9188XXXXXXXX"
  }
}




▶️ Run the Task Consumer

cd ../task-consumer
mvn spring-boot:run

Listens to Kafka

Deserializes the message

Dynamically routes to the right processor (email, SMS)

On failure, message is retried and sent to DLQ if retries fail



▶️ Run the Notification Service

cd ../notification-service
mvn spring-boot:run

REST API to handle email and SMS delivery

Integrates with:

  SMTP (for email)
  Msg91 (for SMS)



🧠 Architecture Overview

                         ┌───────────────────────┐
                         │   Task Producer API   │
                         │ (Receives HTTP Tasks) │
                         └──────────┬────────────┘
                                    │
                                    ▼
                           [Kafka Topic: task-topic]
                                    │
                                    ▼
                         ┌────────────────────────┐
                         │    Task Consumer        │
                         │ (Consumes & Routes)     │
                         └──────────┬──────────────┘
                                    │
                                    ▼
                    ┌────────────────────────────┐
                    │  TaskProcessor Registry     │
                    └──────────┬──────────────────┘
                               │
           ┌──────────────────┴──────────────────┐
           ▼                                     ▼
  ┌──────────────────────┐           ┌──────────────────────┐
  │ Email Task Processor │           │ SMS Task Processor   │
  └─────────┬────────────┘           └──────────┬────────────┘
            │Rest Api                           │Rest Api
            ▼                                   ▼
     ┌────────────────────────┐        ┌────────────────────────┐
     │ Notification Service   │        │ Notification Service   │
     │                        │        │ (REST API for SMS)     │
     └─────────┬──────────────┘        └──────────┬─────────────┘
               │                                       │
         Uses SMTP                             Uses MSG91 API
               │                                       │
               ▼                                       ▼
       [Real Email Delivery]                 [Real SMS Delivery]

                 ↘                            ↙
               [DLQ Topic: task-dlq-topic] (If errors/failures)


- Task Producer: Accepts REST task requests and publishes them to a Kafka topic.

- Task Consumer:

  - Subscribes to Kafka topic(s).

  - Deserializes each task message.

  - Dynamically routes to a processor (e.g., email, sms) based on taskType.

  - Delegates actual message delivery (email/SMS) by calling the Notification Service via REST API.

- Notification Service:

  - Exposes REST endpoints for email and SMS delivery.

  - Integrates with:

    - SMTP (for email)

    - Msg91 (for SMS)

  - Returns success/failure responses, which the consumer handles accordingly.


✅ Supported Task Types

  - email: Sends actual emails via SMTP

  - sms: Sends real SMS via Msg91



🔄 Fault Tolerance

  - Integrated DLQ (Dead Letter Queue) via Kafka for unprocessable messages

  - Custom retry logic using DefaultErrorHandler



📌 Roadmap

✅ Task Producer module

✅ Task Consumer module with task-type routing

✅ Email processor (SMTP)

✅ SMS processor (Msg91 integration)

✅ DLQ support with retries

⏳ Observability (metrics, tracing)

⏳ Docker & CI/CD setup
