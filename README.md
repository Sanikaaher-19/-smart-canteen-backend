# 🍽️ Smart Canteen Ordering System
 A full-stack digital canteen management and food ordering platform with online payments and AI-powered assistance.

## 📌 About The Project

**Smart Canteen Ordering System** is a full-stack web application developed to digitize and simplify the traditional canteen ordering process.

The system allows users to browse the food menu, add items to their cart, place orders, make online payments through **Razorpay**, view their order information, and interact with an **AI-powered chatbot** for canteen-related assistance.

An administrative module is also provided to manage food items and monitor customer orders.

The project follows a **frontend–backend architecture**, where the React frontend communicates with a Spring Boot REST API, which handles business logic, database operations, authentication, payment processing, and AI integration.


## 🎯 Problem Statement

Traditional canteen systems often involve:

- Long queues during peak hours
- Manual order management
- Difficulty tracking customer orders
- Manual payment handling
- Limited information about menu availability
- Increased chances of order errors

The Smart Canteen system addresses these challenges by providing a centralized digital platform for ordering and canteen management.

## 💡 Solution

The application provides a digital workflow:


┌────────────────────┐
│    Browse Menu     │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│    Select Food     │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│     Add to Cart    │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│      Checkout      │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│   Online Payment   │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│ Order Confirmation │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│  Kitchen Processing│
└─────────┬──────────┘
          ↓
┌────────────────────┐
│   Status Updates   │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│  Order Tracking    │
│      by User       │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│  Order Completed   │
└────────────────────┘

✨ Features
# 👤 User Module

User Registration
User Login
Authentication & Authorization
Browse Food Menu
Search Food Items
Filter Food by Category
View Food Details
Add Items to Cart
Update Cart Quantity
Remove Items from Cart
Place Food Orders
View Order History
View Order Details
Online Payment
Payment Verification
AI-powered Chatbot

# 👨‍💼 Admin Module

Secure Admin Login
Admin Dashboard
Add Food Items
Update Food Items
Delete Food Items
Manage Food Availability
View Customer Orders
Monitor Order Status
Manage Menu Items

# 🤖 AI-Powered Chatbot

The Smart Canteen Ordering System includes a **hybrid chatbot architecture** that combines rule-based responses with an AI-powered conversational layer.

Instead of depending completely on an external AI API, the chatbot first handles common and predefined canteen-related queries through backend-defined response rules.

If a query does not match any predefined rule, the request is forwarded to the **Groq API** for an AI-generated response.

This approach ensures that the chatbot can continue providing basic assistance even when the external AI service is temporarily unavailable.

 🔄 Chatbot Processing Flow


                         User Query
                              │
                              ▼
                    ┌────────────────────┐
                    │  Backend Chatbot   │
                    │   Query Handler    │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │ Predefined Rules / │
                    │ Basic QA Matching  │
                    └─────────┬──────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
                  Match              No Match
                    │                   │
                    ▼                   ▼
            ┌──────────────┐    ┌────────────────┐
            │ Predefined   │    │    Groq API    │
            │   Response   │    │ AI Processing   │
            └──────┬───────┘    └───────┬────────┘
                   │                    │
                   │             ┌──────┴──────┐
                   │             │             │
                   │          Success       API Failure
                   │             │             │
                   │             ▼             ▼
                   │       AI Response    Fallback /
                   │                     Basic Response
                   │             │             │
                   └─────────────┴─────────────┘
                                 │
                                 ▼
                         Response to User
The chatbot uses two response mechanisms:

1. Rule-Based Responses

Common and predictable questions are handled directly by the backend using predefined response rules.

Examples include:

Menu-related basic questions
Food availability
Food categories
Basic canteen information
Frequently asked questions

This avoids unnecessary API calls for simple queries and provides faster responses.

2. AI-Based Responses

When the user's query does not match the predefined rules, the backend forwards the query to the Groq API.

The AI layer can handle more flexible and natural-language questions that are not explicitly covered by the predefined rules.

3. Fallback Handling

The chatbot is designed with fallback handling so that basic functionality is not completely dependent on the availability of the external AI service.

If the Groq API is unavailable or encounters an error, the application can fall back to the predefined response mechanism for supported queries.

# ✅ Benefits of This Architecture
Reduces unnecessary AI API requests
Provides faster responses for common questions
Reduces dependency on external AI services
Provides basic functionality even when the AI service is unavailable
Combines deterministic responses with generative AI


💳 Online Payment Integration

The system integrates Razorpay Test Mode for online payment processing.

Payment Architecture
            
┌───────────────┐
│     User      │
└───────┬───────┘
        │
        │ Selects items & Checkout
        ▼
┌───────────────────┐
│  React Frontend   │
└─────────┬─────────┘
          │
          │ POST /api/payment/create-order
          ▼
┌───────────────────────┐
│   Spring Boot Backend │
│                       │
│  RazorpayClient       │
│  creates Order        │
└──────────┬────────────┘
           │
           │ Razorpay Order ID
           ▼
┌───────────────────────┐
│       Razorpay        │
│   Checkout (Test)     │
└──────────┬────────────┘
           │
           │ User completes payment
           ▼
┌───────────────────────┐
│   Payment Response    │
│                       │
│ • Payment ID          │
│ • Order ID            │
│ • Signature           │
└──────────┬────────────┘
           │
           │ POST /api/payment/verify
           ▼
┌───────────────────────┐
│   Spring Boot Backend │
│                       │
│ Verify Signature      │
│ using Key Secret      │
└──────────┬────────────┘
           │
      ┌────┴─────┐
      │           │
    Valid       Invalid
      │           │
      ▼           ▼
┌──────────┐  ┌──────────────┐
│ Payment  │  │ Payment      │
│ Verified │  │ Rejected     │
└────┬─────┘  └──────────────┘
     │
     ▼
┌───────────────────────┐
│   Order Confirmation  │
└───────────────────────┘

# Security

The Razorpay Key Secret is stored only on the backend using environment variables.

The secret is never exposed to the frontend.

⚠️ Razorpay is currently configured in Test Mode for development and testing purposes.

# 🏗️ System Architecture
                         ┌─────────────────────┐
                         │        USER         │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    React Frontend   │
                         │                     │
                         │  • Authentication   │
                         │  • Menu             │
                         │  • Cart             │
                         │  • Orders           │
                         │  • Payment          │
                         │  • Chatbot          │
                         └──────────┬──────────┘
                                    │
                               REST APIs
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   Spring Boot API   │
                         │                     │
                         │  • Controllers      │
                         │  • Services         │
                         │  • Repositories     │
                         │  • Security         │
                         │  • Payment Logic    │
                         │  • AI Integration   │
                         └───────┬─────┬───────┘
                                 │     │
                   ┌─────────────┘     └──────────────┐
                   ▼                                  ▼
        ┌─────────────────────┐             ┌──────────────────┐
        │    MySQL / TiDB     │             │     Groq API     │
        │                     │             │                  │
        │  • Users            │             │  AI Chatbot      │
        │  • Menu Items       │             │                  │
        │  • Orders           │             └──────────────────┘
        │  • Payment Data     │
        └─────────────────────┘
                   │
                   │
                   ▼
        ┌─────────────────────┐
        │      Razorpay       │
        │                     │
        │  Payment Gateway    │
        └─────────────────────┘
# 🛠️ Tech Stack

Layer	Technologies
Frontend	React.js, JavaScript, HTML5, CSS3
Backend	Java, Spring Boot
API	REST API
ORM	Spring Data JPA, Hibernate
Security	Spring Security
Database	MySQL / TiDB Cloud
Payment	Razorpay
AI	Groq API
API Testing	Postman
Build Tool	Maven
Version Control	Git & GitHub

 🧩 Backend Architecture

The backend follows a layered architecture:

Controller
     ↓
Service
     ↓
Repository
     ↓
Database
Controller Layer

Handles incoming HTTP requests and returns responses to the frontend.

Service Layer

Contains business logic and application processing.

Repository Layer

Handles database communication using Spring Data JPA.

Database Layer

Stores users, menu items, orders, and related application data.

# 🔐 Authentication & Security

The application implements authentication and role-based access control.

Users and administrators have different access levels.

Sensitive configuration is managed through environment variables.
# 🔐 Role-Based Access

The application separates functionality based on the user's role.

                         ┌──────────────┐
                         │     Login    │
                         └──────┬───────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │ Role Validation │
                       └────────┬────────┘
                                │
              ┌─────────────────┼─────────────────┐
              │                 │                 │
              ▼                 ▼                 ▼
          👤 USER          👨‍🍳 KITCHEN       👨‍💼 ADMIN
              │                 │                 │
              ▼                 ▼                 ▼
        User Dashboard    Kitchen Dashboard   Admin Dashboard
              │                 │                 │
              ▼                 ▼                 ▼
         Order Food        Manage Orders      Manage System
         Make Payment      Update Status      Menu Management
         Track Order                          Revenue
         Chatbot                               Users

# 🗄️ Database Design

The application uses a relational database to manage persistent data.

Users

users
├── id
├── name
├── email
├── password
└── role

---

Menu Items

menu_items
├── id
├── name
├── description
├── category
├── price
└── available

---

Orders

orders
├── id
├── user_id
├── order_time
├── total_amount
├── status
├── payment_status
├── razorpay_order_id
└── razorpay_payment_id


The exact database schema may vary according to the current implementation.

---

# 👥 User Roles & System Modules

The Smart Canteen Ordering System follows a **role-based architecture** with three primary roles:

- 👤 User
- 👨‍🍳 Kitchen Staff
- 👨‍💼 Admin

Each role has a dedicated dashboard and specific responsibilities within the ordering workflow.

---

## 👤 User Module

Users can:

- Register and Login
- Browse the food menu
- Search and filter food items
- View food details and prices
- Add items to cart
- Place orders
- Make online payments
- View order history
- Track the status of active orders
- View order details
- Interact with the AI-powered chatbot

# 📦 Order Tracking

After placing an order, the user can track its progress based on the status updated by the Kitchen Staff.


Order Placed
     ↓
Confirmed
     ↓
Preparing
     ↓
Ready
     ↓
Completed

👨‍🍳 Kitchen Staff Module

The Kitchen Staff has a dedicated dashboard for handling incoming customer orders.

Kitchen Staff can:
View incoming customer orders
Fetch and display order details
View ordered food items
View customer order information
Update order status
Manage the order preparation workflow
🔄 Kitchen Order Workflow
                 New Order
                     │
                     ▼
               Kitchen Dashboard
                     │
                     ▼
                  Confirmed
                     │
                     ▼
                 Preparing
                     │
                     ▼
                   Ready
                     │
                     ▼
                 Completed

Whenever the Kitchen Staff updates an order status, the updated status becomes available to the user for order tracking.

This creates a complete connection between the customer ordering process and kitchen operations.

👨‍💼 Admin Module

The Admin has the highest level of access and can manage the overall canteen system.

Admin can manage:
🍔 Menu Management
Add new food items
Update food items
Delete food items
Manage food categories
Update food prices
Manage food availability
📦 Order Management
View customer orders
Monitor order information
Monitor order status
Manage overall order data
👥 User Management
View registered users
Manage user-related information
Control role-based access
📊 Revenue & Dashboard

The Admin dashboard provides an overview of the canteen's business activity, including:

Total orders
Revenue information
Menu information
User information
Order activity

# ⏱️ Order Cancellation Window

The system provides a **5-minute order cancellation window** for users.

After placing an order, the user can cancel the order within **5 minutes of order placement**.

Once the 5-minute cancellation window expires, the cancellation option is no longer available to the user.

# Cancellation Flow


                    Order Placed
                         │
                         ▼
                 Start 5-Minute Timer
                         │
              ┌──────────┴──────────┐
              │                     │
         Within 5 Minutes       After 5 Minutes
              │                     │
              ▼                     ▼
       Cancellation Allowed    Cancellation Disabled
              │
              ▼
        Cancel Order
              │
              ▼
      Update Order Status



# 📸 Application Screenshots
<img width="518" height="802" alt="finalmenu1" src="https://github.com/user-attachments/assets/419d457f-393a-4e12-893c-7defbe8c7d17" />

----

<img width="535" height="797" alt="finalmenu2" src="https://github.com/user-attachments/assets/eb190d1e-273e-4dfd-bf41-93094359d752" />

----

<img width="534" height="987" alt="menu2" src="https://github.com/user-attachments/assets/e8f0e962-e9e7-4f47-87e6-d2cfd22c1414" />

---

<img width="527" height="808" alt="chatbot1" src="https://github.com/user-attachments/assets/e9e0c8eb-d8a9-4e59-b713-e8094cd2fa1b" />

---

<img width="532" height="792" alt="chatbot2" src="https://github.com/user-attachments/assets/533b170f-6849-47e0-ba75-a0baf3b5ed7e" />

---

<img width="528" height="807" alt="chatbot3" src="https://github.com/user-attachments/assets/bac8292e-3bda-48aa-9178-610140f019fd" />

---

<img width="526" height="797" alt="cartfinal" src="https://github.com/user-attachments/assets/69234f19-a54d-40b1-b194-18317ab5ef21" />

---
<img width="534" height="1093" alt="confirmandpay final" src="https://github.com/user-attachments/assets/4ecbad86-c39f-4880-81f9-c7285aa37dac" />

---

<img width="487" height="795" alt="pay1" src="https://github.com/user-attachments/assets/af16ec9c-bfeb-4c9b-83a2-60f6e006ec11" />

---

<img width="526" height="807" alt="pay2" src="https://github.com/user-attachments/assets/17fbcc78-2a93-4e9a-855f-c82663cc2aef" />

---

<img width="503" height="806" alt="pay3" src="https://github.com/user-attachments/assets/fb8790a1-d12b-4e2b-b0e7-bb2044be8d06" />

---

<img width="534" height="1399" alt="userinfo" src="https://github.com/user-attachments/assets/94542183-974a-4f04-862c-c4e0cbdbd7f0" />

---

<img width="1877" height="905" alt="staffdashfinal" src="https://github.com/user-attachments/assets/1929ec56-d4fb-40a5-9a12-ee8cfda7cfa3" />

---

<img width="1910" height="2057" alt="admindash6" src="https://github.com/user-attachments/assets/ae9f980e-19a1-4493-aff2-940707771bc7" />










