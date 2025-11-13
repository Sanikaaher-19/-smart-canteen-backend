# Smart Canteen Database Documentation

## Database Overview

### Database Management System
- **Type**: MySQL
- **Version**: Compatible with MySQL 8.x
- **Schema Name**: `canteen_bite`

### ORM Framework
- **Technology**: JPA (Java Persistence API) with Hibernate
- **DDL Auto**: `update` (automatically creates/updates tables based on entity annotations)
- **SQL Logging**: Enabled (`spring.jpa.show-sql=true`)

### Connection Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/canteen_bite
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Schema Design

### Entity Relationship Diagram (Text Representation)

```
┌─────────────┐       ┌─────────────┐
│    User     │       │    Order    │
├─────────────┤       ├─────────────┤
│ id (PK)     │◄──────┤ id (PK)     │
│ name        │       │ table_number│
│ email (UQ)  │       │ status      │
│ password    │       │ order_time  │
│ role        │       │ total_amount│
└─────────────┘       │ user_id (FK)│
                      └─────────────┘
                             │
                             │ 1:N
                             ▼
┌─────────────┐       ┌─────────────┐
│  OrderItem  │       │  MenuItem   │
├─────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)     │
│ quantity    │       │ name        │
│ price       │       │ description │
│ order_id(FK)│       │ price       │
│ menu_item_id│──────►│ category    │
│ (FK)        │       │ available   │
└─────────────┘       └─────────────┘
```

### Relationships Summary
- **User → Order**: One-to-Many (Optional - orders can be placed without user account)
- **Order → OrderItem**: One-to-Many (Cascade ALL, orphan removal)
- **OrderItem → MenuItem**: Many-to-One (Snapshot relationship)
- **OrderItem → Order**: Many-to-One (Required relationship)

## Table Structures

### users Table

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| name | VARCHAR(255) | NOT NULL | User's full name |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User's email address |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| role | VARCHAR(255) | NOT NULL | User role (ADMIN, STAFF, STUDENT, CUSTOMER, KITCHEN_STAFF) |

**Indexes:**
- PRIMARY KEY on `id`
- UNIQUE KEY on `email`

**Notes:**
- Password is stored as BCrypt hash
- Role is stored as string representation of enum

### menu_items Table

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| name | VARCHAR(255) | NOT NULL | Item name |
| description | VARCHAR(255) | NULL | Item description |
| price | DOUBLE | NOT NULL | Item price |
| category | VARCHAR(255) | NULL | Item category |
| available | BIT(1) | NULL | Availability status |

**Indexes:**
- PRIMARY KEY on `id`

**Notes:**
- `available` defaults to NULL (treated as true in application logic)
- Price stored as DOUBLE for decimal values

### orders Table

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| table_number | VARCHAR(255) | NOT NULL | Table/dining area identifier |
| status | VARCHAR(255) | NOT NULL | Order status (PENDING, PREPARING, READY, COMPLETED, CANCELLED) |
| order_time | DATETIME(6) | NOT NULL | Timestamp when order was placed |
| total_amount | DOUBLE | NOT NULL | Total order amount |
| user_id | BIGINT | NULL, FOREIGN KEY | Reference to user (nullable for guest orders) |

**Indexes:**
- PRIMARY KEY on `id`
- FOREIGN KEY on `user_id` → `users.id`

**Notes:**
- `user_id` is nullable to allow guest orders
- `order_time` uses microsecond precision
- Status transitions: PENDING → PREPARING → READY → COMPLETED

### order_items Table

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| quantity | INT | NOT NULL | Quantity ordered |
| price | DOUBLE | NOT NULL | Price per item at time of order |
| order_id | BIGINT | NOT NULL, FOREIGN KEY | Reference to order |
| menu_item_id | BIGINT | NOT NULL, FOREIGN KEY | Reference to menu item |

**Indexes:**
- PRIMARY KEY on `id`
- FOREIGN KEY on `order_id` → `orders.id`
- FOREIGN KEY on `menu_item_id` → `menu_items.id`

**Notes:**
- `price` is a snapshot of the menu item price at order time
- `order_id` is required (not null)
- `menu_item_id` is required (not null)

## Relationships and Constraints

### Foreign Key Constraints

1. **orders.user_id → users.id**
   - ON DELETE: RESTRICT (prevents deleting users with existing orders)
   - ON UPDATE: CASCADE

2. **order_items.order_id → orders.id**
   - ON DELETE: CASCADE (deleting order removes all items)
   - ON UPDATE: CASCADE

3. **order_items.menu_item_id → menu_items.id**
   - ON DELETE: RESTRICT (prevents deleting menu items referenced in orders)
   - ON UPDATE: CASCADE

### Business Logic Constraints

- **Email Uniqueness**: Enforced at application level in UserService
- **Role Validation**: Enum values validated in UserService
- **Order Cancellation Window**: 5-minute limit enforced in OrderService
- **Order Status Transitions**: Controlled in OrderService.updateStatus()

## DDL Auto Generation

### Hibernate DDL-Auto Settings
- **ddl-auto=update**: Creates missing tables and columns, updates existing schema
- **Pros**: Automatic schema evolution during development
- **Cons**: Not suitable for production (use migrations like Flyway/Liquibase)

### Generated DDL Examples

#### Users Table DDL
```sql
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_6dotkott2kjsp8vw4d0m25fb7 (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### Orders Table DDL
```sql
CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    table_number VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    order_time DATETIME(6) NOT NULL,
    total_amount DOUBLE NOT NULL,
    user_id BIGINT,
    PRIMARY KEY (id),
    KEY FK32ql8ubntj5uh44ph9659tiih (user_id),
    CONSTRAINT FK32ql8ubntj5uh44ph9659tiih
        FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### Order Items Table DDL
```sql
CREATE TABLE order_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY FKbioxgbv59vetrxe0ejfubep1w (order_id),
    KEY FKt4dc2r9nbtbhjij7mwcc1x5u8 (menu_item_id),
    CONSTRAINT FKbioxgbv59vetrxe0ejfubep1w
        FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT FKt4dc2r9nbtbhjij7mwcc1x5u8
        FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## Data Types and Storage

### Numeric Types
- **BIGINT**: Used for primary keys (64-bit integers)
- **INT**: Used for quantities
- **DOUBLE**: Used for monetary values (price, total_amount)

### String Types
- **VARCHAR(255)**: Default for most text fields
- **BIT(1)**: Boolean values (available flag)

### Date/Time Types
- **DATETIME(6)**: Timestamp with microsecond precision

### Enum Storage
- **Role**: Stored as VARCHAR string representation
- **Status**: Stored as VARCHAR string

## Indexing Strategy

### Primary Keys
- All tables use BIGINT AUTO_INCREMENT primary keys
- Clustered indexes on primary keys

### Foreign Keys
- Automatic indexes created on foreign key columns
- Composite indexes not needed for current query patterns

### Unique Constraints
- Email uniqueness enforced with unique index
- No other unique constraints defined

## Performance Considerations

### Query Patterns
1. **User lookup by email**: Indexed (unique constraint)
2. **Orders by user**: Foreign key index on user_id
3. **Order items by order**: Foreign key index on order_id
4. **Orders by status**: No index (consider adding if frequently queried)

### Potential Optimizations
- Add index on `orders.status` for frequent status-based queries
- Add composite index on `(order_time, status)` for time-based filtering
- Consider partitioning `orders` table by date if historical data grows large

## Backup and Recovery

### Recommended Backup Strategy
- **Daily Full Backups**: Complete database backup
- **Transaction Logs**: Enable binary logging for point-in-time recovery
- **Application-Level Backups**: Export critical data before schema changes

### Recovery Scenarios
- **Data Loss**: Restore from latest backup + replay binary logs
- **Schema Changes**: Use migration scripts in production (replace ddl-auto=update)
- **Partial Recovery**: Table-level recovery using mysqldump

## Migration to Production

### Schema Migration Tools
- **Flyway**: Version-controlled SQL migrations
- **Liquibase**: XML/YAML-based migrations

### Production Configuration
```properties
spring.jpa.hibernate.ddl-auto=validate  # Only validate, don't modify
spring.jpa.show-sql=false  # Disable in production
```

### Migration Steps
1. Generate initial schema from development
2. Create baseline migration script
3. Apply migrations in staging environment
4. Test thoroughly before production deployment

## Monitoring and Maintenance

### Key Metrics to Monitor
- **Table Sizes**: Monitor growth of orders and order_items
- **Query Performance**: Slow query logs
- **Connection Pool**: HikariCP metrics via Spring Boot Actuator
- **Index Usage**: Monitor unused indexes

### Maintenance Tasks
- **Regular Backups**: Automated daily backups
- **Index Rebuilding**: Periodic index maintenance
- **Statistics Updates**: Keep query optimizer statistics current
- **Archive Old Data**: Move completed orders to archive tables if needed

## Security Considerations

### Data Protection
- **Passwords**: BCrypt hashed (not reversible)
- **Sensitive Data**: No PII beyond email/name
- **Access Control**: Role-based permissions at application level

### Connection Security
- **SSL/TLS**: Enable for production connections
- **Firewall**: Restrict database access to application servers
- **Credentials**: Use environment variables, not hardcoded

### Audit Trail
- **Order History**: Complete audit trail via order_time and status changes
- **User Actions**: Login attempts logged via Spring Security
- **Data Changes**: JPA auditing can be enabled for create/update timestamps
