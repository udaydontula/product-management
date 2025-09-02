# Product Management

## Description
This is a Quarkus-based REST API for managing products.  
It provides endpoints to create, read, update, delete, check stock availability, and retrieve products sorted by price.

---

## Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+
- IDE (IntelliJ / VS Code / Eclipse)

---

## Installation / Setup

1. **Clone the repository:**
```bash
git clone https://github.com/your-username/product-management.git
cd product-management
```

2. **Configure the database:**  
   Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=root
quarkus.datasource.password=your_password
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/product_db
quarkus.hibernate-orm.database.generation=update
```

3. **Build the application:**
```bash
mvn clean install
```

---

## Running the Application

Run in **development mode**:
```bash
mvn quarkus:dev
```
The API will be available at: `http://localhost:8080/product`

---

## Running Tests

Execute all unit and integration tests:
```bash
mvn test
```

---

## API Endpoints

| HTTP Method | Path | Description | Request Body | Response |
|-------------|------|-------------|--------------|----------|
| POST | /product/saveProduct | Create a new product | `ProductRequestResponseDto` JSON | 201 Created + Product JSON |
| GET | /product/allProducts | Get all products | None | 200 OK + List of Products / 404 if empty |
| GET | /product/getDetails/{id} | Get product by ID | None | 200 OK + Product / 404 Not Found |
| PUT | /product/update/{id} | Update product by ID | `ProductRequestResponseDto` JSON | 200 OK + Updated Product / 404 Not Found |
| DELETE | /product/delete/{id} | Delete product by ID | None | 204 No Content |
| GET | /product/check-availability?id={id}&count={count} | Check stock availability | Query params: id, count | 200 OK + AvailabilityResponse / 404 if not found |
| GET | /product/sorted-by-price | Get all products sorted by price (ascending) | None | 200 OK + List of Products |

---

## Request / Response Examples

### 1. Save Product
**POST /product/saveProduct**
```json
{
  "name": "Laptop",
  "price": 75000,
  "quantity": 10
}
```
**Response: 201 Created**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 75000,
  "quantity": 10
}
```

### 2. Check Availability
**GET /product/check-availability?id=1&count=5**  
**Response 200 OK**
```json
{
  "productId": 1,
  "requestedCount": 5,
  "available": true
}
```

### 3. Get Sorted Products
**GET /product/sorted-by-price**  
**Response 200 OK**
```json
[
  { "id": 2, "name": "Mouse", "price": 500 },
  { "id": 1, "name": "Keyboard", "price": 1500 },
  { "id": 3, "name": "Laptop", "price": 75000 }
]
```

---

## Technologies Used
- Java 17
- Quarkus
- Hibernate ORM + Panache
- MySQL
- Maven
- JUnit 5

---

## Author
Your Name - [your.email@example.com](mailto:your.email@example.com)

