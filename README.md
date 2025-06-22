# springboot-projem1 Ownify
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
ownify.zip_expanded/
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
│   │       ├── templates/       # (Unused, static structure)
│   │       └── application.properties # App configuration
│   └── test/                    # Test files
├── pom.xml                      # Maven dependencies
└── ...                          # Other project files
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
   git clone <repository-url>
   cd ownify.zip_expanded
   ```

2. **Build the Project**
   ```
   mvn clean install
   ```

3. **Run the Application**
   ```
   mvn spring-boot:run
   ```
   Or run `Application.java` from your IDE.

4. **Access the Application**
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
  - The application uses an in-memory H2 database by default.
  - You can change database settings in `src/main/resources/application.properties`.
- **Static Files:**
  - All static resources (HTML, CSS, JS) are located in `src/main/resources/static/`.
- **Port:**
  - The default port is `8080`. You can change it in `application.properties`.

---

## API Endpoints (Examples)

- `GET /api/products` — List all products
- `GET /api/categories` — List all categories
- `POST /api/products` — Add a new product
- `GET /api/products/{id}` — Get product details
- `POST /api/login` — User login
- `POST /api/register` — User registration

> For more, see the controller classes in `src/main/java/com/ownify/Controller/`.

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

This project is open source. Add your license information here if needed.

---

## Contact

For questions, suggestions, or support, please open an issue or contact the maintainer.
