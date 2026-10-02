# Ownify
Ownify is a modern web application that enables users to register, log in, add and list products, manage wishlists, and communicate via messaging. The backend is built with Spring Boot, while the frontend uses static HTML, CSS, and JavaScript files for a responsive and user-friendly experience.

---

## Features

- **User Authentication:** Register, log in, and log out securely.
- **Product Management:** Add, view, and list products with category filtering.
- **Wishlist:** Add products to a personal wishlist for easy access later.
- **Messaging:** Communicate with other users via a built-in messaging system.
- **Dashboard:** Personalized dashboard for managing your products and settings.
- **Static Pages:** About Us, FAQs, Customer Support, and more.
- **Responsive Design:** Modern UI with static resources.

---

## Project Structure

```
Ownify/
├── README.md
├── .gitignore
└── ownify/                      # Spring Boot application (Maven project)
    ├── src/
    │   ├── main/
    │   │   ├── java/com/ownify/
    │   │   │   ├── Controller/      # Controllers for web/API endpoints
    │   │   │   ├── Entity/          # JPA Entities
    │   │   │   ├── Model/           # Data models (e.g., for messaging)
    │   │   │   ├── Repository/      # Spring Data JPA repositories
    │   │   │   ├── Service/         # Business logic services
    │   │   │   └── config/          # Configuration classes (security, CORS, etc.)
    │   │   └── resources/
    │   │       ├── static/          # Static files (index.html, CSS, JS, images)
    │   │       └── application.properties # App configuration
    │   └── test/                    # Test files
    ├── pom.xml                      # Maven dependencies
    └── mvnw, mvnw.cmd               # Maven wrapper
```

---

## Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- (Optional) An IDE such as IntelliJ IDEA, Eclipse, or VS Code

### Installation

1. **Clone the Repository**
```
   git clone https://github.com/melikeisil/Ownify.git
   cd Ownify/ownify
```
2. **Set environment variables** (see [Environment Variables](#environment-variables))
   ```
   export DB_PASSWORD='<choose-a-database-password>'
   export ADMIN_PASSWORD='<choose-an-admin-password>'
   ```
   On Windows (PowerShell): `$env:DB_PASSWORD='...'`. You can also keep them in a local
   `.env` file for your IDE/run configuration; `.env` is ignored by git and must never be committed.

3. **Build the Project**
   ```
   mvn clean install
   ```
   (or `./mvnw clean install` without a local Maven installation)

4. **Run the Application**
   ```
   mvn spring-boot:run
   ```
   Or run `Application.java` from your IDE with the same environment variables.

5. **Access the Application**
   - Homepage: [http://localhost:8080/](http://localhost:8080/)
   - Login/Register: `/signin`, `/signup`
   - Dashboard: `/dashboard`
   - Wishlist: `/wishlist`
   - Shop: `/shop`
   - About: `/about`
   - FAQs: `/faqs`
   - Customer Support: `/customer-support`

---

## Usage

- **Register** a new user or **log in** with existing credentials.
- **Add products** from the dashboard and view them in the shop.
- **Add products to your wishlist** for quick access.
- **Send and receive messages** with other users.
- **Navigate** through static pages for more information.

---

## Technologies Used

- **Backend:** Spring Boot, Spring MVC, Spring Security, H2 Database
- **Frontend:** HTML, CSS, JavaScript (static files)
- **Build Tool:** Maven

---

## Configuration

- **Database:**
  - The application uses a **file-based** H2 database (`jdbc:h2:file:./ownifydb`), not an in-memory one.
    Data persists between restarts in `ownifydb.mv.db` in the working directory you start the app from
    (for example `ownify/` when using `mvn spring-boot:run`).
  - Database files (`*.mv.db`, `*.trace.db`) are local only and ignored by git.
  - If you already have a local `ownifydb.mv.db` created with a different password, either set
    `DB_PASSWORD` to that password or delete the file so a fresh database is created.
  - The H2 web console is **disabled by default**. For local debugging only, start with
    `H2_CONSOLE_ENABLED=true` (it is served at `/h2-console`).
  - You can change database settings in `ownify/src/main/resources/application.properties`.
- **Static Files:**
  - All static resources (HTML, CSS, JS) are located in `src/main/resources/static/`.
- **Port:**
  - The default port is `8080`. You can change it in `application.properties`.

---

## Environment Variables

No passwords are stored in `application.properties`; they are read from the environment.

| Variable             | Required | Default  | Description                                       |
|----------------------|----------|----------|---------------------------------------------------|
| `DB_USERNAME`        | No       | `sa`     | H2 datasource username                            |
| `DB_PASSWORD`        | Yes\*    | *(empty)* | H2 datasource password                            |
| `ADMIN_USERNAME`     | No       | `admin`  | Spring Security built-in user name                |
| `ADMIN_PASSWORD`     | Yes\*    | *(empty)* | Spring Security built-in user password; if unset, Spring Boot generates a random one at startup |
| `H2_CONSOLE_ENABLED` | No       | `false`  | Enables the H2 console at `/h2-console` (local debugging only) |

\* The application starts without them, but you should always set them outside of throwaway local runs.

---

## API Endpoints (Examples)

- `GET /api/products` — List all products
- `GET /api/categories` — List all categories
- `POST /api/products` — Add a new product
- `GET /api/products/{id}` — Get product details
- `POST /api/users` — User registration (JSON: `firstName`, `lastName`, `email`, `password`)
- `POST /api/login` — User login (JSON: `email`, `password`; starts a session)
- `GET /api/users/{id}` — Get your own user record (requires login; only your own `id`)
- `PUT /api/users/{id}` — Update your own user record (requires login; only your own `id`)
- `GET /api/users/check-email?email=...` — Check whether an email is already registered

Password hashes are never included in API responses.

> For more, see the controller classes in `ownify/src/main/java/com/ownify/Controller/`.

---

## Security notes

> **This is an educational project and must not be used in production as is.**

- **All paths are permitted.** `SecurityConfig` ends with `anyRequest().permitAll()`; Spring Security does
  not authenticate requests. Access control is done manually in controllers by checking the `user`
  attribute in the HTTP session, and only where that check has been added.
- **CSRF protection is disabled on most paths**, including `/api/**`, `/dashboard/**`, `/wishlist/**`,
  `/messaging/**`, `/login` and the WebSocket endpoints.
- **Session cookies are not marked secure** (`server.servlet.session.cookie.secure=false`) so the app
  works over plain HTTP locally.
- Things that are in place: passwords are hashed with BCrypt and never returned in API responses;
  `GET/PUT /api/users/{id}` only allow access to your own record; the session id is rotated on login;
  public product responses only include the seller's id and name; chat messages take the sender from
  the logged-in session and are rejected without one; secrets are read from environment variables;
  the H2 console is off by default.

Before any real deployment you would at least need proper Spring Security authentication and
authorization rules, CSRF protection, HTTPS with secure cookies, and a production database.

---

## Contribution

Contributions are welcome! To contribute:
1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a pull request

---

## License

No license has been chosen for this project yet, so all rights are reserved by the author.
If you want to allow reuse, add a `LICENSE` file (for example MIT or Apache-2.0) and update this section.

---

## Contact

For questions, suggestions, or support, please open an issue or contact the maintainer.
