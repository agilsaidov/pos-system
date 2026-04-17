# 🛒 POS System — Multi-Branch Market Chain

A production-ready **Point of Sale backend system** built with Java Spring Boot, designed for multi-branch retail chains. Features JWT-based authentication via Keycloak, role-based access control, Redis caching, and a comprehensive REST API.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Security | Keycloak + OAuth2 + JWT |
| Database | PostgreSQL |
| Migrations | Flyway |
| Cache | Redis |
| ORM | Spring Data JPA + Hibernate |
| Mapping | MapStruct |
| Validation | Jakarta Validation |
| Build Tool | Maven |

---

## 📐 Architecture Overview

```
┌─────────────────────────────────────────────┐
│                  Frontend                   │
│         (POS Terminal / Backoffice)         │
└────────────────────┬────────────────────────┘
                     │ JWT Token
┌────────────────────▼─────────────────────────┐
│              Spring Boot API                 │
│  ┌──────────┐ ┌──────────┐ ┌─────────────┐   │
│  │   POS    │ │   MGMT   │ │    ADMIN    │   │
│  │ Endpoints│ │ Endpoints│ │  Endpoints  │   │
│  └──────────┘ └──────────┘ └─────────────┘   │
│  ┌─────────────────────────────────────────┐ │
│  │          JWT Auth Filter                │ │
│  └─────────────────────────────────────────┘ │
└──────┬──────────────┬───────────────┬────────┘
       │              │               │
┌──────▼──────┐ ┌─────▼─────┐  ┌──────▼──────┐
│  PostgreSQL │ │   Redis   │  │   Keycloak  │
│  (Main DB)  │ │  (Cache)  │  │   (Auth)    │
└─────────────┘ └───────────┘  └─────────────┘
```

---

## 🔐 Authentication & Authorization

Authentication is handled by **Keycloak**. The frontend obtains a JWT token directly from Keycloak using ROPC flow and sends it with every request.

### Roles

| Role | Access |
|---|---|
| `ADMIN` | Full system access — users, stores, products, promotions |
| `MANAGER` | Store management — inventory, stock, reports, cashiers |
| `CASHIER` | POS operations — barcode scan, checkout, sales history |

### Login Flow
```
POST http://localhost:8180/realms/pos-realm/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

client_id=pos-frontend
grant_type=password
username=cashier1
password=password123
```

---

## 📡 API Reference

### 🔑 Auth (`/api/auth`) — Public

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/forget-password/initiate` | Send OTP to email |
| `POST` | `/api/auth/forget-password/validate` | Verify OTP + reset password |

---

### 🛍️ POS — Cashier Endpoints (`/api/pos`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/pos/products/lookup?barcode=` | Lookup product by barcode |
| `GET` | `/api/pos/sales` | Cashier's own sales history |
| `POST` | `/api/pos/sales/checkout` | Finalize sale — create sale, items, payment, update inventory |

#### Checkout Request Example
```json
{
  "storeId": 1,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ],
  "paymentMethod": "CASH",
  "amountTendered": 20.00
}
```

#### Checkout Response Example
```json
{
  "saleId": 101,
  "receiptNo": "RCP-1-00001",
  "subTotal": 5.25,
  "discountTotal": 0.30,
  "taxTotal": 0.40,
  "total": 5.35,
  "amountTendered": 20.00,
  "change": 14.65,
  "paymentMethod": "CASH",
  "items": [...]
}
```

---

### 🏪 Management — Manager Endpoints (`/api/mgmt`)

#### Inventory
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/mgmt/inventory?storeId=` | View store inventory |

#### Stock Movements
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/mgmt/stock-movement` | List movements with filters |
| `POST` | `/api/mgmt/stock-movement` | Create stock IN/OUT/ADJUST |

#### Sales
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/mgmt/sales` | View all sales with filters |

#### Reports
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/mgmt/reports/cashiers?storeId=` | Cashier performance summary |
| `GET` | `/api/mgmt/reports/cashiers/{cashierId}?storeId=` | Detailed cashier report |
| `GET` | `/api/mgmt/reports/daily?storeId=&date=` | Daily revenue report |

#### Cashier Management
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/mgmt/users/cashiers/stores/{storeId}` | List cashiers in store |
| `GET` | `/api/mgmt/users/cashiers/{cashierId}/stores/{storeId}` | Single cashier details |
| `POST` | `/api/mgmt/users/cashiers/{cashierId}/stores/{storeId}` | Assign cashier to store |
| `DELETE` | `/api/mgmt/users/cashiers/{cashierId}/stores/{storeId}` | Remove cashier from store |

---

### ⚙️ Admin Endpoints

#### Users (`/api/admin/users`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/admin/users` | List users with filters |
| `POST` | `/api/admin/users` | Create user (syncs with Keycloak) |
| `PUT` | `/api/admin/users/{id}` | Update user |
| `PATCH` | `/api/admin/users/{id}/enable?enabled=` | Enable/disable user |
| `PATCH` | `/api/admin/users/{id}/change-password` | Reset user password |

#### Stores (`/api/stores`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/stores` | List stores with filters |
| `GET` | `/api/stores/{id}` | Store basic info |
| `GET` | `/api/stores/{id}/details` | Store detail with staff count |
| `PUT` | `/api/stores` | Create store |
| `POST` | `/api/stores/{id}` | Update store |
| `PATCH` | `/api/stores/{id}/activate?active=` | Toggle store active |

#### Products (`/api/products`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/products` | List/search products |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product |
| `PATCH` | `/api/products/{id}/activate?active=` | Toggle product active |

#### Promotions (`/api/promotions`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/promotions` | List promotions with filters |
| `GET` | `/api/promotions/{promotionId}` | Promotion with products |
| `GET` | `/api/promotions/by-product/{productId}` | Promotions for a product |
| `POST` | `/api/promotions` | Create promotion |
| `PUT` | `/api/promotions/{promotionId}` | Update promotion |
| `POST` | `/api/promotions/{promotionId}/products` | Add products to promotion |
| `PATCH` | `/api/promotions/{id}/activate?active=` | Toggle promotion active |
| `PATCH` | `/api/promotions/{promotionId}/products/{productId}/activate?active=` | Toggle product in promotion |

---

## 🗄️ Database Schema

### Core Tables

```
users                 → system users (synced with Keycloak)
roles                 → ADMIN, MANAGER, CASHIER
user_roles            → user ↔ role assignments
stores                → market branches
store_assignments     → user ↔ store assignments
products              → global product catalog
categories            → product categories
product_categories    → product ↔ category
inventory             → per-store stock levels
stock_movements       → full audit trail of stock changes
sales                 → sale headers
sale_items            → individual line items (price snapshots)
payments              → payment records
promotions            → discount campaigns
promotion_products    → promotion ↔ product assignments
```

### Key Design Decisions

- **Price snapshots** — `sale_items` stores `unit_price_snapshot` and `tax_rate_snapshot` so historical receipts are never affected by price changes
- **Soft deletes** — products and promotions use `active` flag, never hard deleted
- **Audit trail** — every stock change logged in `stock_movements` with before/after quantities
- **Flyway migrations** — all schema changes versioned and tracked

---

## ⚙️ Configuration

### `application.yaml`
```yaml
spring:
  application:
    name: pos-system
  datasource:
    url: jdbc:postgresql://your_db_url
    username: your_username
    password: your_password

  flyway:
    mixed: true

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true

  security:
    oauth2:
      resource server:
        jwt:
          issuer-uri: your_issuer-uri

  data:
    redis:
      database: 0
      host: localhost
      port: 6379
      password: your_password

  mail:
    host: smtp.gmail.com
    port: 587
    username: your_username
    password: your_password

    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true


keycloak:
  server-url: your_serever-uri
  realm: pos-realm
  client-id: pos-backend
  client-secret: your_clinet_secret
  username : your_username
  password : your_password
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- PostgreSQL
- Redis
- Keycloak 24+
- Maven

### Run Keycloak (Docker)
```bash
docker run -p 8180:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:24.0.0 start-dev
```

### Keycloak Setup
1. Create realm: `pos-realm`
2. Create client: `pos-backend` (confidential, service accounts enabled)
3. Create client: `pos-frontend` (public)
4. Create realm roles: `ADMIN`, `MANAGER`, `CASHIER`
5. Assign `manage-users` and `view-users` to `pos-backend` service account

### Run the Application
```bash
# Clone the repository
git clone https://github.com/your-username/pos-system.git
cd pos-system

# Build
./mvnw clean install -DskipTests

# Run
./mvnw spring-boot:run
```

Flyway will automatically run all migrations on startup.

---

## 💡 Key Features

- **Multi-branch support** — inventory and staff managed per store
- **Real-time barcode lookup** — with active promotion detection
- **Atomic checkout** — sale, inventory, stock movements in single transaction
- **Pessimistic locking** — prevents race conditions on inventory during concurrent checkouts
- **OTP password reset** — Redis-backed OTP with TTL expiry
- **Role-based access** — three-tier security (ADMIN/MANAGER/CASHIER)
- **Promotion engine** — percentage and fixed-amount discounts with date ranges
- **Audit trail** — complete stock movement history
- **Receipt generation** — immutable price/tax snapshots per sale

---

## 📁 Project Structure

```
src/main/java/com/app/pos/system/
├── auth/                    # Auth flows (OTP, forgot password)
│   ├── controller/
│   ├── service/
│   └── dto/
├── config/                  # Spring, Keycloak, Redis, Security configs
├── controller/
│   ├── admin/               # Admin endpoints
│   ├── mgmt/                # Manager endpoints
│   └── pos/                 # Cashier endpoints
├── dto/
│   ├── request/
│   └── response/
├── email/                   # Email service
├── exception/               # Custom exceptions + global handler
├── mapper/                  # MapStruct mappers
├── model/                   # JPA entities
│   └── enums/
├── projection/              # JPA projections
├── repo/                    # Spring Data repositories
├── security/                # JWT filter + utilities
├── service/                 # Business logic
├── specification/           # JPA Specifications for dynamic queries
└── utils/                   # AuthUtils, helpers
```

---

## 🔒 Security Notes

- Passwords are never stored in the application database — managed entirely by Keycloak
- JWT tokens validated on every request via `JwtAuthFilter`
- User enabled/disabled checked on every authenticated request
- Store access validated per manager and cashier — managers and cashiers can only access their assigned stores
- Admin operations require `ADMIN` role enforced via `@PreAuthorize`

---

## 📄 License

This project is for portfolio purpose.
