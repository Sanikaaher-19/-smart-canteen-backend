# Smart Canteen Ordering System - Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture and Technologies](#architecture-and-technologies)
3. [Backend Documentation](#backend-documentation)
   - [Database Schema and Entities](#database-schema-and-entities)
   - [Repositories](#repositories)
   - [Services](#services)
   - [Controllers and APIs](#controllers-and-apis)
   - [Security Configuration](#security-configuration)
   - [Configuration Details](#configuration-details)
   - [Dependencies](#dependencies)
4. [Frontend Documentation](#frontend-documentation)
   - [Technology Stack](#frontend-technology-stack)
   - [Components and Pages](#components-and-pages)
   - [Features](#features)
   - [API Integration](#api-integration)
5. [Setup and Installation](#setup-and-installation)
6. [Usage](#usage)
7. [API Endpoints](#api-endpoints)

## Project Overview

The Smart Canteen Ordering System is a full-stack web application designed for campus canteens to streamline food ordering and management. The system supports multiple user roles (Customers, Kitchen Staff, Admins) and provides features like menu browsing, order placement, order tracking, and kitchen management.

### Key Features
- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Menu Management**: Dynamic menu with categories and items
- **Order Management**: Place, track, and cancel orders
- **Kitchen Portal**: Staff can update order statuses
- **Admin Panel**: Create staff accounts and manage system
- **Real-time Order Tracking**: Visual timeline for order progress
- **Order Cancellation**: Within 5-minute window for customers

## Architecture and Technologies

### Backend
- **Framework**: Spring Boot 3.5.6
- **Language**: Java 17
- **Database**: MySQL with JPA/Hibernate
- **Security**: Spring Security with JWT
- **Build Tool**: Maven

### Frontend
- **Framework**: React 19.1.1
- **Routing**: React Router DOM 7.9.1
- **HTTP Client**: Axios 1.13.1
- **Build Tool**: Create React App (React Scripts 5.0.1)

### Database
- **Type**: MySQL
- **ORM**: JPA/Hibernate with ddl-auto=update

## Backend Documentation

### Database Schema and Entities

#### User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password; // stored as bcrypt hash
    @Enumerated(EnumType.STRING)
    private Role role;
}
```

#### Role Enum
```java
public enum Role {
    ADMIN,
    STAFF,
    STUDENT,
    CUSTOMER,
    KITCHEN_STAFF;
}
```

#### MenuItem Entity
```java
@Entity
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Boolean available;
}
```

#### Order Entity
```java
@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableNumber;
    private String status; // PENDING, PREPARING, READY, COMPLETED, CANCELLED
    private LocalDateTime orderTime;
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}
```

#### OrderItem Entity
```java
@Entity
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MenuItem menuItem;
    private Integer quantity;
    private Double price; // snapshot price

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
```

### Repositories

#### UserRepository
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

#### MenuItemRepository
```java
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByAvailableTrue();
}
```

#### OrderRepository
```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
}
```

#### OrderItemRepository
```java
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Basic CRUD operations
}
```

### Services

#### UserService
- Handles user registration and validation
- Password encoding with BCrypt
- Role validation and email uniqueness checks

#### MenuService
- CRUD operations for menu items
- Filtering by availability

#### OrderService
- Order placement with validation
- Order cancellation (within 5-minute window)
- Status updates
- Transactional operations for data consistency

### Controllers and APIs

#### AuthController
- `POST /api/auth/register` - Customer registration
- `POST /api/auth/login` - User login with role selection

#### MenuController
- `GET /api/menu` - Get all menu items
- `GET /api/menu/available` - Get available items
- `GET /api/menu/{id}` - Get specific item
- `POST /api/menu/create` - Create menu item (Admin)
- `PUT /api/menu/{id}` - Update menu item (Admin)
- `DELETE /api/menu/{id}` - Delete menu item (Admin)

#### OrderController
- `POST /api/orders` - Place new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get specific order
- `PUT /api/orders/{id}/status` - Update order status
- `GET /api/orders/status/{status}` - Get orders by status
- `PUT /api/orders/{id}/cancel` - Cancel order (Customer)

#### AdminController
- `POST /api/admin/create-user` - Create staff/admin users

### Security Configuration

#### JWT Authentication
- Stateless authentication using JWT tokens
- Token expiration: 24 hours (86400000 ms)
- Secret key for signing tokens

#### Role-Based Access Control
- **Permit All**: Auth endpoints, menu GET requests
- **Admin Only**: `/api/admin/**`
- **Kitchen Staff/Admin**: `/api/kitchen/**`
- **Authenticated Users**: All other endpoints

#### CORS Configuration
- Allowed origins: Configurable via `app.frontend.origin` (default: http://localhost:3000)

### Configuration Details

#### application.properties
```properties
spring.application.name=SmartCanteen
spring.datasource.url=jdbc:mysql://localhost:3306/canteen_bite
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=ADMIN

server.port=9090

jwt.secret=mySmartCanteenSecretKey
app.jwt.secret=0b3f9d2a8e6c4b7f1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4
app.jwt.expiration-ms=86400000
app.frontend.origin=http://localhost:3000

logging.level.org.springframework.security=DEBUG
```

### Dependencies

#### Spring Boot Starters
- `spring-boot-starter-data-jpa` - JPA/Hibernate integration
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-actuator` - Monitoring endpoints

#### Database
- `mysql:mysql-connector-j` - MySQL JDBC driver

#### Security
- `io.jsonwebtoken:jjwt-api` - JWT token handling
- `io.jsonwebtoken:jjwt-impl` - JWT implementation
- `io.jsonwebtoken:jjwt-jackson` - JWT JSON support

#### Utilities
- `org.projectlombok:lombok` - Code generation

## Frontend Documentation

### Frontend Technology Stack
- **React**: 19.1.1 - UI framework
- **React Router DOM**: 7.9.1 - Client-side routing
- **Axios**: 1.13.1 - HTTP client for API calls
- **React Scripts**: 5.0.1 - Build and development tools

### Components and Pages

#### App.js
Main application component with routing and state management:
- Manages global state: orderItems, orders, inventory, role
- Handles authentication state persistence
- Role-based route protection

#### Auth.js
Authentication page with login/signup tabs:
- Customer registration (public)
- Login with role selection (Customer, Kitchen Staff, Admin)
- JWT token storage in localStorage

#### Menu.js
Menu browsing and selection:
- Search functionality
- Category-based navigation
- Recommended items display
- Add to cart functionality

#### Order.js
Cart/Order review page:
- Display selected items
- Quantity management
- Order placement

#### Pay.js
Payment processing page:
- Order summary
- Payment integration (placeholder)
- Order submission to backend

#### OrderPlaced.js
Order confirmation page:
- Success message
- Redirect to tracking

#### OrderTracking.js
Order status tracking:
- Visual timeline (Order Placed → Accepted → Preparing → Ready)
- Cancel order button (within 5 minutes)
- Real-time status updates

#### Kitchen.js
Kitchen staff portal:
- View all orders
- Update order statuses (Preparing → Ready)
- Order management

#### AdminCreateUser.js
Admin panel for user creation:
- Create Kitchen Staff or Admin accounts
- Form validation and error handling

### Features

#### User Authentication
- JWT-based authentication
- Role-based access control
- Persistent login state

#### Menu Management
- Dynamic menu loading from backend
- Search and filter functionality
- Category-based organization

#### Order Management
- Add items to cart
- Place orders with table number
- Track order status in real-time
- Cancel orders within 5-minute window

#### Kitchen Operations
- View incoming orders
- Update preparation status
- Manage order workflow

#### Admin Functions
- Create staff accounts
- System management

### API Integration

#### API Configuration (api.js)
```javascript
const API = axios.create({
  baseURL: "http://localhost:9090",
});

// Request interceptor for JWT token
API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
```

#### Key API Calls
- Authentication: `/api/auth/login`, `/api/auth/register`
- Menu: `/api/menu`, `/api/menu/available`
- Orders: `/api/orders`, `/api/orders/{id}/cancel`
- Admin: `/api/admin/create-user`

## Setup and Installation

### Backend Setup
1. **Prerequisites**:
   - Java 17
   - MySQL Server
   - Maven

2. **Database Setup**:
   ```sql
   CREATE DATABASE canteen_bite;
   ```

3. **Configuration**:
   - Update `application.properties` with your MySQL credentials
   - Default port: 9090

4. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup
1. **Prerequisites**:
   - Node.js (v14+)
   - npm or yarn

2. **Installation**:
   ```bash
   cd f:/Smart-Canteen/smart-canteen
   npm install
   ```

3. **Configuration**:
   - Update API base URL in `src/api.js` if needed
   - Default backend URL: http://localhost:9090

4. **Run Application**:
   ```bash
   npm start
   ```

## Usage

### For Customers
1. Register/Login as Customer
2. Browse menu and add items to cart
3. Review order and place it
4. Track order status
5. Cancel order within 5 minutes if needed

### For Kitchen Staff
1. Login as Kitchen Staff
2. View incoming orders
3. Update order statuses as they progress

### For Admins
1. Login as Admin
2. Create new staff accounts
3. Manage system settings

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register customer
- `POST /api/auth/login` - Login user

### Menu
- `GET /api/menu` - Get all menu items
- `GET /api/menu/available` - Get available items
- `GET /api/menu/{id}` - Get menu item by ID
- `POST /api/menu/create` - Create menu item (Admin)
- `PUT /api/menu/{id}` - Update menu item (Admin)
- `DELETE /api/menu/{id}` - Delete menu item (Admin)

### Orders
- `POST /api/orders` - Place new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `PUT /api/orders/{id}/status` - Update order status
- `GET /api/orders/status/{status}` - Get orders by status
- `PUT /api/orders/{id}/cancel` - Cancel order (Customer)

### Admin
- `POST /api/admin/create-user` - Create staff/admin user

### Security
- All endpoints except auth and menu GET require authentication
- Role-based access control implemented
- JWT tokens required for protected endpoints
