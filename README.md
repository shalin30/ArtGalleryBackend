# 🎨 Art Gallery Backend

## 📌 Description
This is the backend service for the **Art Gallery** application, developed using **Maven**, **Spring Boot**, and **Java 11**. The backend handles core functionalities such as managing users, art pieces, categories, orders, and authentication. It interacts with an **MSSQL** database via **Hibernate** for ORM and utilizes **Cloudinary** for image storage. The database runs in a **Docker** container.

## ✨ Key Features
- **Entities and CRUD Operations**: 7 entity classes (`Users`, `Categories`, `Art Pieces`, `Orders`, etc.), supporting Create, Read, Update, and Delete operations.
- **Authentication**: Uses **JWT (JSON Web Tokens)** for secure login and authorization.
- **Password Recovery**: Implements a **Forgot Password** feature that generates a temporary token, expiring after a set time.
- **Token Blacklist**: Ensures secure logout by **blacklisting JWT tokens** to prevent reuse.
- **Database**: **MSSQL** database managed with **Hibernate** ORM.
- **Cloud Storage**: Stores images using **Cloudinary**.
- **Code Coverage**: **80%+ test coverage** using **JUnit** and **JaCoCo**.
- **Field Validation**: Strict validation for all incoming requests using annotations and custom validators.
- **Logging**: Implemented **traceId-based logs** at every step for transaction tracking.
- **Queries**: Uses **JPQL** (Java Persistence Query Language) for optimized queries.
- **Future Plans**: **Kafka Integration** to send email notifications upon order confirmation.

---

## 🛠️ Tech Stack
| **Technology**  | **Description**  |
|----------------|----------------|
| **Backend Framework** | Spring Boot 2.7.18 |
| **Programming Language** | Java 11 |
| **Database** | MSSQL (Dockerized) |
| **ORM** | Hibernate |
| **Authentication** | JWT Tokens |
| **Testing & Coverage** | JUnit, JaCoCo |
| **Logging** | SLF4J, TraceId-based tracking |
| **Cloud Storage** | Cloudinary (for art images) |
| **Containerization** | Docker |
| **Message Broker** | Kafka (planned for order confirmation emails) |

---

## 🛠️ Setup Instructions

### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/yourusername/art-gallery-backend.git
```

#### **Navigate to Project Directory**
To start, navigate into your project directory using the following command:
```bash
cd art-gallery-backend
```
### **2️⃣ Build the Project**
Use Maven to install dependencies and build the project:
```bash
mvn clean install
```
### **3️⃣ Database Setup**
Ensure Docker is installed and running.
#### **Create a Docker container for MSSQL:**
```bash
docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=your_password' \
-p 1433:1433 --name art-gallery-db -d mcr.microsoft.com/mssql/server:latest
```
#### **Open Azure Data Studio and connect using:**
Server: localhost</br>
User: sa</br>
Password: your_password

### **4️⃣ Configure the Application**
Update src/main/resources/application.properties with your MSSQL credentials:
```bash
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ArtGalleryDB;encrypt=false
spring.datasource.username=sa
spring.datasource.password=your_password
```

### **5️⃣Cloudinary Setup**
Create a cloudinary account and get these details and update:
```bash
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

### **6️⃣ Run the Backend**
Start the Spring Boot application:
```bash
mvn spring-boot:run
```
The backend should now be running on:
```bash
http://localhost:8080
```
