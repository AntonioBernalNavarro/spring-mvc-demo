<!-- Badges -->
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-enabled-6DB33F?logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-template%20engine-005F0F?logo=thymeleaf&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-See%20LICENSE-blue)

# Spring MVC Demo – Quiz Application

> A pedagogical Spring Boot application illustrating the **Model‑View‑Controller (MVC)** pattern with **Spring Security** and **Thymeleaf**, featuring a role‑based quiz system with in‑memory data storage.

---

## 📑 Table of Contents

- [Abstract](#-abstract)
- [Overview](#-overview)
- [Technologies and Dependencies](#-technologies-and-dependencies)
- [Project Structure](#-project-structure)
- [Architectural Pattern: MVC](#-architectural-pattern-model-view-controller)
- [Security Configuration](#-security-configuration)
- [Data Model](#-data-model)
- [Service Layer](#-service-layer)
- [Controller Endpoints](#-controller-endpoints)
- [Building and Running](#-building-and-running-the-application)
- [Usage](#-usage)
- [User Interface](#-user-interface)
- [Testing](#-testing)
- [Limitations and Future Work](#-limitations-and-future-work)
- [License](#-license)
- [Conclusion](#-conclusion)

---

## 📖 Abstract

This repository contains a demonstration web application built with the **Spring MVC** framework, illustrating the implementation of the **Model‑View‑Controller (MVC)** architectural pattern in a Java‑based enterprise environment.

The application implements a **role‑based quiz system** with two distinct user profiles — *administrator* and *standard user* — secured through **Spring Security**. The project serves as a pedagogical reference for the integration of **Spring Boot**, **Spring Security**, **Thymeleaf**, and the **MVC** design pattern, while deliberately employing in‑memory data storage to maintain simplicity and focus on the framework’s core capabilities.

---

## 🔎 Overview

The `spring-mvc-demo` project is a self‑contained Spring Boot application that provides a minimal but complete quiz management platform. It enables administrators to create, modify, and delete quiz questions, while standard users can take quizzes and view their results.

> ⚠️ **Disclaimer:** This application is **not intended for production deployment**. Its primary purpose is to demonstrate the effective use of Spring MVC components and their interactions.

---

## 🛠 Technologies and Dependencies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Programming language |
| **Spring Boot** | 4.1.1 | Application framework and dependency management |
| **Spring Web MVC** | *(managed by Spring Boot)* | Implementation of the MVC pattern |
| **Spring Security** | *(managed by Spring Boot)* | Authentication and authorization |
| **Thymeleaf** | *(managed by Spring Boot)* | Server‑side template engine |
| **Thymeleaf Extras Spring Security 6** | *(managed by Spring Boot)* | Integration between Thymeleaf and Spring Security |
| **Maven** | via wrapper | Build and dependency management |
| **Custom CSS** | — | Hand‑written stylesheet served at `/css/styles.css` |

### Key Dependencies

The `pom.xml` declares the following starters:

- `spring-boot-starter-security`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-webmvc`
- `thymeleaf-extras-springsecurity6`
- Corresponding test starters for **Security**, **Thymeleaf**, and **Web MVC**.

---

## 📂 Project Structure

The source tree follows the conventional **layered architecture** of a Spring MVC application:

```text
src/
├── main/
│   ├── java/org/example/springmvcdemo/
│   │   ├── config/          # Security configuration
│   │   ├── controller/      # MVC controllers
│   │   ├── model/           # Domain entities (Question, User, QuizResult)
│   │   ├── service/         # Business logic and user management
│   │   └── SpringMvcDemoApplication.java
│   └── resources/
│       ├── static/
│       │   └── css/
│       │       └── styles.css   # Global stylesheet
│       ├── templates/       # Thymeleaf view templates
│       └── application.properties
└── test/
    └── java/org/example/springmvcdemo/
```

| Package / Folder | Responsibility |
|------------------|----------------|
| `config` | Security setup (`WebSecurityConfig`) |
| `controller` | HTTP request handling (`QuizController`) |
| `model` | `Question`, `User` and `QuizResult` entities |
| `service` | Business logic and user-detail loading |
| `resources/static/css` | Global stylesheet served at `/css/styles.css` |
| `resources/templates` | Thymeleaf view templates |

---

## 🏛 Architectural Pattern: Model‑View‑Controller

The application adheres strictly to the **MVC** pattern:

- **Model** — Represented by the `Question`, `User`, and `QuizResult` classes, which encapsulate the data and domain logic.
- **View** — Implemented via Thymeleaf templates: `login.html`, `register.html`, `quiz.html`, `quizList.html`, `addQuiz.html`, `editQuiz.html`, `result.html`.
- **Controller** — The `QuizController` class manages all incoming requests, coordinates the model and view, and delegates business operations to the service layer.

The controller defines endpoints for authentication, registration, quiz administration (admin only), and quiz participation (user only).

---

## 🔐 Security Configuration

Security is configured in `WebSecurityConfig.java` using Spring Security’s `SecurityFilterChain`. The principal aspects are:

- **CSRF protection** is disabled to allow `PUT` and `DELETE` requests from JavaScript `fetch()` calls.
- **Access rules** are role‑based:

| Scope | Path | Role |
|-------|------|------|
| Public | `/register`, `/login`, `/css/**`, `/js/**` | — |
| Admin | `/quizList`, `/addQuiz`, `/editQuiz/**`, `/deleteQuiz/**` | `ROLE_ADMIN` |
| User | `/quiz`, `/submitAnswers`, `/result` | `ROLE_USER` |
| Other | * | Authenticated |

- **Form login** uses a custom success handler that redirects administrators to `/quizList` and standard users to `/quiz`.
- **Logout** is configured at `/logout` with a redirect to the login page.
- **Passwords** are encoded using `BCryptPasswordEncoder`.

User details are loaded by `QuizUserDetailsService`, which implements `UserDetailsService` and stores users in an in‑memory `HashMap`. A default administrative account is pre‑loaded for demonstration:

```text
Username: admin
Password: admin123
```

---

## 🧩 Data Model

Three classes constitute the data model:

| Class | Description |
|-------|-------------|
| **`Question`** | Represents a quiz question with an identifier, question text, a list of possible answers, and the correct answer. |
| **`User`** | Represents an application user with username, email, password, and role. |
| **`QuizResult`** | DTO that encapsulates the outcome of a single question: the question text, the user’s answer, the correct answer, and a boolean flag indicating whether the answer was correct. |

All classes provide standard constructors, getters, setters, and `toString()` methods. The `User.toString()` method explicitly omits the password field for security reasons.

---

## ⚙️ Service Layer

The service layer comprises two classes:

- **`QuestionsService`** — manages the collection of quiz questions in an in‑memory `HashMap`. It provides methods to:
  - load all questions
  - add a question
  - edit an existing question
  - delete a question by identifier

  Two sample questions are pre‑loaded in the constructor.

- **`QuizUserDetailsService`** — implements `UserDetailsService` to authenticate users. It stores user records in an in‑memory map, encodes passwords with **BCrypt**, and offers a `registerUser` method to add new users.

> ⚠️ **Note:** Both services use **volatile in‑memory storage**; consequently, all data is lost upon application restart. This design decision is intentional to keep the demonstration focused on the framework’s mechanics rather than on persistence concerns.

---

## 🌐 Controller Endpoints

The `QuizController` exposes the following endpoints:

| HTTP Method | Path | Role | Description |
|:-----------:|------|------|-------------|
| `GET` | `/login` | Public | Displays the login page. |
| `GET` | `/register` | Public | Displays the registration form. |
| `POST` | `/register` | Public | Processes user registration. |
| `GET` | `/quizList` | ADMIN | Lists all quiz questions. |
| `GET` | `/addQuiz` | ADMIN | Shows the form to add a new question. |
| `POST` | `/addQuiz` | ADMIN | Persists a new question. |
| `GET` | `/editQuiz/{id}` | ADMIN | Shows the edit form for a specific question. |
| `PUT` | `/editQuiz` | ADMIN | Updates an existing question (JSON body). |
| `DELETE` | `/deleteQuiz/{id}` | ADMIN | Deletes a question by identifier. |
| `GET` | `/quiz` | USER | Displays the quiz for the authenticated user. |
| `POST` | `/submitAnswers` | USER | Records the user’s answers. |
| `GET` | `/result` | USER | Shows the quiz results and score. |

The controller leverages `@ModelAttribute`, `@RequestParam`, `@PathVariable`, and `@RequestBody` annotations to bind request data to method parameters, illustrating typical Spring MVC request‑handling techniques.

---

## 🚀 Building and Running the Application

The project includes a **Maven wrapper** (`mvnw`), so Maven need not be installed globally.

### Build

```bash
./mvnw clean package
```

### Run with the Maven plugin

```bash
./mvnw spring-boot:run
```

### Run the generated JAR

```bash
java -jar target/spring-mvc-demo-0.0.1-SNAPSHOT.jar
```

Once the application is running, it is accessible at:

```text
http://localhost:8080
```

---

## 💻 Usage

The application defines **two roles**:

### 👑 Administrator

Log in with the pre‑configured credentials (`admin` / `admin123`). After authentication, the administrator is redirected to `/quizList`, where questions can be **added**, **edited**, or **deleted**.

### 👤 Standard User

A new user can self‑register via `/register` and is assigned the `USER` role by default. After logging in, the user is redirected to `/quiz`, where the available questions are presented. Upon submission, the user is shown a **result page** with the score and a breakdown of correct and incorrect answers.

---

## 🎨 User Interface

The application uses a single, hand‑written stylesheet located at:

```text
src/main/resources/static/css/styles.css
```

It is referenced from every Thymeleaf template via:

```html
<link rel="stylesheet" th:href="@{/css/styles.css}">
```

Key features of the UI:

- Centralised CSS custom properties (`:root`) for colors, radii, and transitions.
- Responsive design with a `@media (max-width: 600px)` breakpoint that converts tables into stacked cards.
- Reusable component classes: `.container`, `.page-header`, `.form-group`, `.btn`, `.alert`, `.question`, `.row--correct`, `.row--incorrect`.

---

## 🧪 Testing

The `src/test` directory contains the default Spring Boot test class generated by Spring Initializr. Additional unit and integration tests are **not included** in this demonstration.

Developers extending the project are encouraged to add tests for the **controller**, **service**, and **security** layers using:

- **JUnit 5**
- **Mockito**
- **Spring’s `MockMvc`**

---

## 🧭 Limitations and Future Work

The current implementation has several limitations inherent to its demonstrative nature:

- [ ] **Data persistence** is entirely in‑memory; a production system would require a relational database and a persistence framework such as **Spring Data JPA**.
- [ ] **CSRF protection** is disabled; a production application should enable CSRF protection and configure it appropriately for stateless or stateful interactions.
- [ ] **User registration** does not include validation or duplicate‑username checks.
- [ ] **Error handling** is minimal; a production application should implement comprehensive exception handling and user‑friendly error pages.

---

## 📄 License

This project is distributed under the terms of the license included in the [`LICENSE`](LICENSE) file at the root of the repository.

---

## ✅ Conclusion

The `spring-mvc-demo` repository provides a concise yet instructive example of a Spring MVC application integrating **Spring Security** and **Thymeleaf**. By separating concerns into *model*, *view*, *controller*, and *service* layers, it exemplifies the **MVC** pattern and offers a solid foundation for further exploration and extension.

---

<div align="center">

**⭐ If you found this project useful, consider giving it a star! ⭐**

Made with ❤️ using Spring Boot

</div>
