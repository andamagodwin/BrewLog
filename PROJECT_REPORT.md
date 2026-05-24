# MOBILE APPLICATION DEVELOPMENT
# Coursework Project Report

## BrewLog - Wholesale Coffee Order Management System

---

**Student Name:** Andama Godwin  
**Application Name:** BrewLog  
**Development Platform:** Android Studio  
**Programming Language:** Kotlin (Android)  
**Database:** SQLite  
**Date:** May 2026

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Task 1: Requirements Analysis](#2-task-1-requirements-analysis)
3. [Task 2: System Design](#3-task-2-system-design)
4. [Task 3: Mobile Application Development](#4-task-3-mobile-application-development)
5. [Task 4: Testing and Evaluation](#5-task-4-testing-and-evaluation)
6. [Task 5: Documentation and Presentation](#6-task-5-documentation-and-presentation)
7. [Conclusion and Future Improvements](#7-conclusion-and-future-improvements)

---

## 1. Introduction

BrewLog is a mobile application developed to digitise the order management and product tracking operations of a small wholesale coffee roastery. The business previously relied on manual notebook-based record-keeping, which led to errors in order recording, lost product information, difficulty tracking inventory levels, and inability to monitor sales history effectively.

The application enables roastery employees to manage coffee product batches, track roast freshness and stock levels, create wholesale customer orders, and view complete order history -- all from a smartphone. By replacing the manual system with a digital solution, the business gains accuracy, speed, and real-time visibility into its operations.

### Problem Statement

The small retail coffee roastery faces the following operational challenges:

- **Data Loss:** Paper notebooks are prone to damage, misplacement, and illegible entries, causing orders to be lost or duplicated.
- **Inventory Inaccuracy:** Without automated stock tracking, employees frequently oversell products or fail to notice when stock is running low.
- **No Sales History:** There is no efficient way to retrieve past order records, making it impossible to track customer purchasing patterns or reconcile accounts.
- **Human Error:** Manual calculations of order totals and stock deductions lead to frequent mistakes.
- **Lack of Accountability:** Without a login system, there is no way to track which employee processed a given order.

BrewLog addresses each of these problems by providing a secure, structured, and automated digital workflow for the entire order lifecycle.

---

## 2. Task 1: Requirements Analysis

### 2.1 Purpose of the Mobile Application

The purpose of BrewLog is to provide a comprehensive mobile solution for managing wholesale coffee product inventory and customer orders. It allows authenticated employees to:

- Maintain a digital catalog of coffee batches with roast details, pricing, and stock levels
- Create and track customer orders with automatic stock deduction
- View complete order history with status tracking (Pending, Shipped, Cancelled)
- Monitor product freshness based on roast dates
- Eliminate manual calculation errors through automated total computation

### 2.2 Functional Requirements

| # | Functional Requirement | Description |
|---|------------------------|-------------|
| FR1 | User Authentication | The system shall authenticate employees using username and password credentials before granting access to the application features. |
| FR2 | Product Management (CRUD) | The system shall allow users to add new coffee products, view all products in a list, edit existing product details (name, origin, roast level, roast date, price, stock), and delete products from inventory. |
| FR3 | Customer Order Creation | The system shall allow users to create new customer orders by selecting a customer, selecting a product, specifying a quantity, and automatically calculating the total price. The system shall validate that the requested quantity does not exceed available stock. |
| FR4 | Order History Viewing | The system shall display a complete chronological list of all orders, showing customer name, product name, quantity, total price, date, and current status. |
| FR5 | Order Status Management | The system shall allow users to update the status of an existing order (Pending, Shipped, or Cancelled) and delete orders with automatic stock restoration. |

### 2.3 Non-Functional Requirements

| # | Non-Functional Requirement | Description |
|---|----------------------------|-------------|
| NFR1 | Usability | The application shall provide a clear, intuitive user interface with Material Design components, proper input validation with inline error messages, and visual freshness badges so that employees can operate it with minimal training. |
| NFR2 | Performance | The application shall respond to user interactions (loading product lists, submitting orders) within 1 second on standard Android devices (API 24+), using efficient SQLite queries with proper indexing and foreign key constraints. |
| NFR3 | Data Integrity | The application shall maintain referential integrity between orders, products, and customers using SQLite foreign key constraints, CHECK constraints on numeric fields, and database transactions for multi-step operations (e.g., order placement with stock deduction). |

### 2.4 Use Case Diagram

```
+----------------------------------------------------------+
|                     BrewLog System                        |
|                                                          |
|   +------------------+     +------------------------+    |
|   |  Login to System |     | View Product List      |    |
|   +------------------+     +------------------------+    |
|            |                         |                   |
|            v                         v                   |
|   +------------------+     +------------------------+    |
|   | Add New Product  |     | Edit Product Details   |    |
|   +------------------+     +------------------------+    |
|            |                         |                   |
|            v                         v                   |
|   +------------------+     +------------------------+    |
|   | Delete Product   |     | Create Customer Order  |    |
|   +------------------+     +------------------------+    |
|                                      |                   |
|                                      v                   |
|   +------------------+     +------------------------+    |
|   | Update Order     |     | View Order History     |    |
|   | Status           |     +------------------------+    |
|   +------------------+              |                    |
|            |                        v                    |
|            v               +------------------------+    |
|   +------------------+     | Delete Order           |    |
|   | Logout           |     +------------------------+    |
|   +------------------+                                   |
+----------------------------------------------------------+
            ^
            |
    +---------------+
    |   Employee    |
    |   (Actor)     |
    +---------------+
```

**Actor:** Employee (Roastery Staff / Manager)

**Use Cases:**
1. **Login to System** - Employee enters credentials to access the application
2. **View Product List** - Employee browses all coffee batches with freshness indicators
3. **Add New Product** - Employee adds a new coffee batch with all details
4. **Edit Product Details** - Employee modifies existing product information
5. **Delete Product** - Employee removes a product from inventory
6. **Create Customer Order** - Employee places a wholesale order for a customer
7. **View Order History** - Employee reviews all past and current orders
8. **Update Order Status** - Employee changes order status (Pending/Shipped/Cancelled)
9. **Delete Order** - Employee removes an order (stock is restored)
10. **Logout** - Employee exits the application

---

## 3. Task 2: System Design

### 3.1 User Interface Design

The application follows Material Design 3 guidelines with a cohesive coffee-themed colour palette:

- **Primary Background:** Coffee Cream (#F7F1EA)
- **Primary Dark:** Coffee Deep (#1B120E)
- **Accent:** Coffee Brown (#4E342E)
- **Secondary:** Coffee Mocha (#7B5E57)
- **Highlight:** Coffee Gold (#B98B4D)

#### Screen 1: Login Screen

```
+------------------------------------------+
|                                          |
|  +--------------------------------------+|
|  |  [Coffee Deep Background]            ||
|  |                                      ||
|  |  BrewLog                             ||
|  |  Wholesale coffee logistics          ||
|  |  for roastery teams                  ||
|  |                                      ||
|  |  [Demo account: admin / brewlog]     ||
|  +--------------------------------------+|
|                                          |
|  +--------------------------------------+|
|  |  Username                            ||
|  |  +--------------------------------+ ||
|  |  |                                | ||
|  |  +--------------------------------+ ||
|  |                                      ||
|  |  Password                     [eye]  ||
|  |  +--------------------------------+ ||
|  |  |                                | ||
|  |  +--------------------------------+ ||
|  |                                      ||
|  |  [========= Sign In =========]      ||
|  +--------------------------------------+|
|                                          |
|  Track batches, take orders, and keep   |
|  stock in sync.                          |
+------------------------------------------+
```

**Components:**
- MaterialCardView with app branding and demo credentials badge
- TextInputLayout with OutlinedBox style for username
- TextInputLayout with password toggle for password
- MaterialButton for sign-in action
- Inline error messages for validation feedback

#### Screen 2: Product List Screen

```
+------------------------------------------+
| [Coffee Batches]              (Toolbar)  |
+------------------------------------------+
|                                          |
|  +--------------------------------------+|
|  |  Available inventory                 ||
|  |  Track roast freshness, stock,       ||
|  |  and wholesale price                 ||
|  |  3 batches                           ||
|  +--------------------------------------+|
|                                          |
|  [New Order]    [Order History]          |
|                                          |
|  +--------------------------------------+|
|  |  Ethiopia Yirgacheffe               ||
|  |  Origin: Ethiopia                    ||
|  |  Roast: Light - 2026-05-20          ||
|  |  $14.00/kg  Stock: 48.0 kg          ||
|  |  [Peak Freshness]    [Edit] [Delete] ||
|  +--------------------------------------+|
|                                          |
|  +--------------------------------------+|
|  |  Guatemala Antigua                   ||
|  |  Origin: Guatemala                   ||
|  |  Roast: Medium - 2026-05-09         ||
|  |  $13.50/kg  Stock: 35.0 kg          ||
|  |  [Good]              [Edit] [Delete] ||
|  +--------------------------------------+|
|                                          |
|                          [+ Add Batch]   |
+------------------------------------------+
```

**Components:**
- MaterialToolbar with title
- Summary card showing batch count
- Action buttons (New Order, Order History)
- RecyclerView with product cards showing freshness badges
- ExtendedFloatingActionButton for adding new products
- Empty state card when no products exist

#### Screen 3: Add/Edit Product Screen

```
+------------------------------------------+
|                                          |
|  Add Coffee Batch                        |
|                                          |
|  Batch Name                              |
|  +--------------------------------------+|
|  | Ethiopia Yirgacheffe                 ||
|  +--------------------------------------+|
|                                          |
|  Origin                                  |
|  +--------------------------------------+|
|  | Ethiopia                             ||
|  +--------------------------------------+|
|                                          |
|  Roast Level                             |
|  +--------------------------------------+|
|  | Light              [dropdown arrow]  ||
|  +--------------------------------------+|
|                                          |
|  Roast Date                              |
|  +--------------------------------------+|
|  |  [Date Picker Spinner]              ||
|  +--------------------------------------+|
|                                          |
|  Price per KG                            |
|  +--------------------------------------+|
|  | 14.00                                ||
|  +--------------------------------------+|
|                                          |
|  Stock (KG)                              |
|  +--------------------------------------+|
|  | 48.0                                 ||
|  +--------------------------------------+|
|                                          |
|  [=== Save ===]    [--- Delete ---]      |
+------------------------------------------+
```

**Components:**
- TextInputEditText fields for name, origin, price, stock
- AutoCompleteTextView dropdown for roast level (Light/Medium/Dark)
- DatePicker in spinner mode for roast date
- DecimalDigitsInputFilter for numeric validation
- Save and Delete buttons (Delete only visible in edit mode)
- Inline validation errors on each field

#### Screen 4: Create Customer Order Screen

```
+------------------------------------------+
|                                          |
|  Create Wholesale Order                  |
|                                          |
|  Cafe / Customer                         |
|  +--------------------------------------+|
|  | Cafe Luna           [dropdown arrow] ||
|  +--------------------------------------+|
|                                          |
|  Coffee Batch                            |
|  +--------------------------------------+|
|  | Ethiopia Yirgacheffe [dropdown arrow]||
|  +--------------------------------------+|
|                                          |
|  Available stock: 48.0 kg                |
|                                          |
|  Quantity (KG)                           |
|  +--------------------------------------+|
|  | 10.0                                 ||
|  +--------------------------------------+|
|                                          |
|  +--------------------------------------+|
|  |  Live total                          ||
|  |  $140.00                             ||
|  +--------------------------------------+|
|                                          |
|  [======== Place Order ========]         |
+------------------------------------------+
```

**Components:**
- AutoCompleteTextView dropdowns for customer and product selection
- Real-time stock display for selected product
- Quantity input with decimal filter
- Live total calculation card (updates as quantity changes)
- Validation for stock availability
- MaterialButton to submit order

#### Screen 5: Order History Screen

```
+------------------------------------------+
| [<] Order History             (Toolbar)  |
+------------------------------------------+
|  3 orders                                |
|                                          |
|  +--------------------------------------+|
|  |  Cafe Luna                           ||
|  |  Ethiopia Yirgacheffe               ||
|  |  10.0 kg - $140.00                  ||
|  |  2026-05-24 14:30:00                ||
|  |  [Pending]       [Status] [Delete]  ||
|  +--------------------------------------+|
|                                          |
|  +--------------------------------------+|
|  |  Harbor Roast                        ||
|  |  Guatemala Antigua                   ||
|  |  5.0 kg - $67.50                    ||
|  |  2026-05-23 09:15:00                ||
|  |  [Shipped]       [Status] [Delete]  ||
|  +--------------------------------------+|
|                                          |
|  +--------------------------------------+|
|  |  Northline Espresso                  ||
|  |  Sumatra Mandheling                  ||
|  |  8.0 kg - $102.00                   ||
|  |  2026-05-22 16:45:00                ||
|  |  [Cancelled]     [Status] [Delete]  ||
|  +--------------------------------------+|
+------------------------------------------+
```

**Components:**
- MaterialToolbar with back navigation
- Order count display
- RecyclerView with order cards
- Color-coded status badges (Green=Shipped, Grey=Pending, Red=Cancelled)
- Edit status button (opens dialog with radio options)
- Delete button with confirmation dialog
- Empty state card when no orders exist

### 3.2 Navigation Flow Diagram

```
+------------------+
|   MainActivity   |
| (Splash/Router)  |
+--------+---------+
         |
         | Intent (automatic redirect)
         v
+------------------+
|  Login Activity  |
|  (Authentication)|
+--------+---------+
         |
         | Intent (on successful login)
         | putExtra(EXTRA_EMPLOYEE_NAME)
         v
+------------------+
| Product List     |<---------------------------------+
| Activity         |                                  |
+--+---------+-----+                                  |
   |         |     |                                  |
   |         |     | Intent                           |
   |         |     v                                  |
   |         |  +------------------+                  |
   |         |  | Add/Edit Product |                  |
   |         |  | Activity         |                  |
   |         |  +------------------+                  |
   |         |     | finish() returns to ProductList  |
   |         |     +----------------------------------+
   |         |
   |         | Intent
   |         v
   |  +---------------------+
   |  | Create Customer     |
   |  | Order Activity      |
   |  +----------+----------+
   |             |
   |             | Intent (on order success)
   |             v
   |  +---------------------+
   +->| Order History       |
      | Activity            |
      +---------------------+
         | finish() returns to previous screen
```

**Navigation Summary:**
- `MainActivity` -> `LoginActivity` (automatic, using explicit Intent)
- `LoginActivity` -> `ProductListActivity` (on successful authentication)
- `ProductListActivity` -> `AddEditProductActivity` (add new or edit existing)
- `ProductListActivity` -> `CreateCustomerOrderActivity` (new order)
- `ProductListActivity` -> `OrderHistoryActivity` (view history)
- `CreateCustomerOrderActivity` -> `OrderHistoryActivity` (after successful order)

All navigation uses **explicit Android Intents** with extras passed via `putExtra()`.

### 3.3 Database Schema

The application uses SQLite with the database name `brewlog.db` (version 1). Foreign key constraints are enabled via `onConfigure()`.

#### Products Table

| Column | Data Type | Constraints |
|--------|-----------|-------------|
| _id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| name | TEXT | NOT NULL |
| origin | TEXT | NOT NULL |
| roast_level | TEXT | NOT NULL |
| roast_date | TEXT | NOT NULL |
| price_per_kg | REAL | NOT NULL |
| stock_kg | REAL | NOT NULL, CHECK(stock_kg >= 0) |

#### Customers Table

| Column | Data Type | Constraints |
|--------|-----------|-------------|
| _id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| cafe_name | TEXT | NOT NULL |
| contact_name | TEXT | NOT NULL |
| email | TEXT | NOT NULL, UNIQUE |

#### Employees Table

| Column | Data Type | Constraints |
|--------|-----------|-------------|
| _id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| username | TEXT | NOT NULL, UNIQUE |
| password | TEXT | NOT NULL |
| full_name | TEXT | NOT NULL |

#### Orders Table

| Column | Data Type | Constraints |
|--------|-----------|-------------|
| _id | INTEGER | PRIMARY KEY AUTOINCREMENT |
| customer_id | INTEGER | NOT NULL, FOREIGN KEY -> customers(_id) ON UPDATE CASCADE ON DELETE RESTRICT |
| product_id | INTEGER | NOT NULL, FOREIGN KEY -> products(_id) ON UPDATE CASCADE ON DELETE RESTRICT |
| quantity_kg | REAL | NOT NULL, CHECK(quantity_kg > 0) |
| total_price | REAL | NOT NULL, CHECK(total_price >= 0) |
| order_date | TEXT | NOT NULL |
| order_status | INTEGER | NOT NULL, CHECK(order_status IN (0, 1, 2)) |

**Status Codes:** 0 = Pending, 1 = Shipped, 2 = Cancelled

#### Entity-Relationship Diagram

```
+------------------+        +------------------+        +------------------+
|    EMPLOYEES     |        |     PRODUCTS     |        |    CUSTOMERS     |
+------------------+        +------------------+        +------------------+
| _id (PK)         |        | _id (PK)         |        | _id (PK)         |
| username (UNIQUE)|        | name             |        | cafe_name        |
| password         |        | origin           |        | contact_name     |
| full_name        |        | roast_level      |        | email (UNIQUE)   |
+------------------+        | roast_date       |        +--------+---------+
                            | price_per_kg     |                 |
                            | stock_kg         |                 |
                            +--------+---------+                 |
                                     |                           |
                                     | 1                         | 1
                                     |                           |
                                     | M                         | M
                            +--------+---------+                 |
                            |      ORDERS      +-----------------+
                            +------------------+
                            | _id (PK)         |
                            | customer_id (FK) |
                            | product_id (FK)  |
                            | quantity_kg      |
                            | total_price      |
                            | order_date       |
                            | order_status     |
                            +------------------+
```

**Relationships:**
- One Product can appear in Many Orders (1:M)
- One Customer can have Many Orders (1:M)
- Orders reference both Products and Customers via foreign keys
- ON DELETE RESTRICT prevents deletion of products/customers with existing orders
- ON UPDATE CASCADE propagates ID changes

---

## 4. Task 3: Mobile Application Development

### 4.1 Project Structure

```
app/
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/app/andama/brewlog/
│   │   ├── MainActivity.kt
│   │   ├── data/local/
│   │   │   ├── BrewLogContract.kt       (Table/Column constants)
│   │   │   ├── BrewLogDatabaseHelper.kt (SQLiteOpenHelper)
│   │   │   ├── BrewLogDao.kt            (Data Access Object)
│   │   │   └── BrewLogModels.kt         (Data classes)
│   │   └── ui/
│   │       ├── LoginActivity.kt
│   │       ├── ProductListActivity.kt
│   │       ├── AddEditProductActivity.kt
│   │       ├── CreateCustomerOrderActivity.kt
│   │       ├── OrderHistoryActivity.kt
│   │       ├── ProductAdapter.kt
│   │       ├── OrderHistoryAdapter.kt
│   │       ├── BrewLogIntents.kt        (Intent extra constants)
│   │       ├── BrewLogSeedData.kt       (Demo data seeding)
│   │       ├── BrewLogUiUtils.kt        (Formatting utilities)
│   │       └── FormValidators.kt        (Input filters & validation)
│   └── res/
│       ├── layout/
│       │   ├── activity_login.xml
│       │   ├── activity_product_list.xml
│       │   ├── activity_add_edit_product.xml
│       │   ├── activity_create_customer_order.xml
│       │   ├── activity_order_history.xml
│       │   ├── item_product.xml
│       │   └── item_order_history.xml
│       ├── values/
│       │   ├── colors.xml
│       │   ├── strings.xml
│       │   └── themes.xml
│       └── drawable/
│           ├── bg_badge_pill.xml
│           └── shape_badge_login.xml
└── src/test/
    └── java/app/andama/brewlog/ui/
        └── BrewLogUiUtilsTest.kt
```

### 4.2 Technical Specifications

| Specification | Value |
|---------------|-------|
| IDE | Android Studio |
| Language | Kotlin |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 36 |
| Database | SQLite (via SQLiteOpenHelper) |
| UI Components | Material Design 3, View Binding |
| Architecture | Activity-based with DAO pattern |
| Build System | Gradle with Kotlin DSL |

### 4.3 Core Feature Implementation

#### 4.3.1 Activity Creation - Login Screen

```kotlin
package app.andama.brewlog.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dao: BrewLogDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = BrewLogDao(this)
        BrewLogSeedData.seedIfNeeded(dao)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text?.toString().orEmpty().trim()
            val password = binding.editTextPassword.text?.toString().orEmpty()

            binding.textInputLayoutUsername.error = null
            binding.textInputLayoutPassword.error = null

            if (username.isBlank()) {
                binding.textInputLayoutUsername.error = "Username is required"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                binding.textInputLayoutPassword.error = "Password is required"
                return@setOnClickListener
            }

            val employee = dao.authenticateEmployee(username, password)
            if (employee != null) {
                val intent = Intent(this, ProductListActivity::class.java).apply {
                    putExtra(BrewLogIntents.EXTRA_EMPLOYEE_NAME, employee.fullName)
                }
                startActivity(intent)
                finish()
            } else {
                binding.textInputLayoutPassword.error = "Invalid credentials"
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

**Explanation:**
- Uses `ActivityLoginBinding` (View Binding) for type-safe view access
- Initialises `BrewLogDao` for database operations
- Seeds default employee account on first launch
- Validates input fields with inline error messages
- Authenticates against the SQLite database
- On success, navigates to `ProductListActivity` using an explicit `Intent` with employee name as extra data
- Calls `finish()` to prevent back-navigation to login after authentication

#### 4.3.2 Database Operations - Data Access Object (DAO)

**Insert Operation:**
```kotlin
fun insertProduct(product: Product): Long = databaseHelper.writableDatabase.use { db ->
    db.insert(BrewLogContract.Products.TABLE_NAME, null, product.toContentValues())
}
```

**Update Operation:**
```kotlin
fun updateProduct(product: Product): Int = databaseHelper.writableDatabase.use { db ->
    db.update(
        BrewLogContract.Products.TABLE_NAME,
        product.toContentValues(),
        "${BaseColumns._ID} = ?",
        arrayOf(product.id.toString()),
    )
}
```

**Delete Operation:**
```kotlin
fun deleteProduct(productId: Long): Int = databaseHelper.writableDatabase.use { db ->
    db.delete(
        BrewLogContract.Products.TABLE_NAME,
        "${BaseColumns._ID} = ?",
        arrayOf(productId.toString()),
    )
}
```

**Transactional Order Placement (Insert with Stock Deduction):**
```kotlin
fun placeOrder(
    customerId: Long,
    productId: Long,
    quantityKg: Double,
    totalPrice: Double,
    orderDate: String = currentOrderTimestamp(),
    orderStatus: Int = BrewLogContract.OrderStatus.PENDING,
): Long {
    val db = databaseHelper.writableDatabase
    db.beginTransaction()

    return try {
        val currentStock = getProductStock(db, productId)
        if (currentStock == null || quantityKg <= 0 || currentStock < quantityKg) {
            -1L
        } else {
            val orderValues = ContentValues().apply {
                put(BrewLogContract.Orders.COLUMN_CUSTOMER_ID, customerId)
                put(BrewLogContract.Orders.COLUMN_PRODUCT_ID, productId)
                put(BrewLogContract.Orders.COLUMN_QUANTITY_KG, quantityKg)
                put(BrewLogContract.Orders.COLUMN_TOTAL_PRICE, totalPrice)
                put(BrewLogContract.Orders.COLUMN_ORDER_DATE, orderDate)
                put(BrewLogContract.Orders.COLUMN_ORDER_STATUS, orderStatus)
            }

            val orderId = db.insert(BrewLogContract.Orders.TABLE_NAME, null, orderValues)
            if (orderId == -1L) {
                -1L
            } else {
                val stockValues = ContentValues().apply {
                    put(BrewLogContract.Products.COLUMN_STOCK_KG, currentStock - quantityKg)
                }

                val stockUpdated = db.update(
                    BrewLogContract.Products.TABLE_NAME,
                    stockValues,
                    "${BaseColumns._ID} = ?",
                    arrayOf(productId.toString()),
                )

                if (stockUpdated == 1) {
                    db.setTransactionSuccessful()
                    orderId
                } else {
                    -1L
                }
            }
        }
    } finally {
        db.endTransaction()
    }
}
```

**Explanation:**
- Uses `ContentValues` for parameterised database operations (prevents SQL injection)
- `placeOrder()` uses a database transaction (`beginTransaction()`/`setTransactionSuccessful()`/`endTransaction()`) to ensure atomicity -- either both the order insert AND the stock deduction succeed, or neither takes effect
- Validates stock availability before proceeding
- Returns the new order ID on success, or -1L on failure

#### 4.3.3 Database Helper - Table Creation

```kotlin
class BrewLogDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION,
) {
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createProductsTable())
        db.execSQL(createCustomersTable())
        db.execSQL(createEmployeesTable())
        db.execSQL(createOrdersTable())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${BrewLogContract.Orders.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${BrewLogContract.Customers.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${BrewLogContract.Products.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "brewlog.db"
        const val DATABASE_VERSION = 1
    }
}
```

#### 4.3.4 Event Handling - Button Clicks and User Interactions

**Product List - Edit and Delete with Confirmation Dialog:**
```kotlin
// Edit button click - navigates to AddEditProductActivity with product ID
private fun openEditProduct(productId: Long) {
    val intent = Intent(this, AddEditProductActivity::class.java).apply {
        putExtra(BrewLogIntents.EXTRA_PRODUCT_ID, productId)
    }
    startActivity(intent)
}

// Delete button click - shows confirmation dialog before deletion
private fun confirmDeleteProduct(productId: Long, productName: String) {
    AlertDialog.Builder(this)
        .setTitle("Delete Batch")
        .setMessage("Delete $productName from inventory?")
        .setPositiveButton("Delete") { _, _ ->
            val deleted = dao.deleteProduct(productId) > 0
            Toast.makeText(this, if (deleted) "Product deleted" else "Unable to delete product", Toast.LENGTH_SHORT).show()
            adapter.submitList(dao.getAllProducts())
        }
        .setNegativeButton("Cancel", null)
        .show()
}
```

**Order Form - Live Total Calculation with TextWatcher:**
```kotlin
binding.editTextQuantityKg.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(s: Editable?) {
        updateLiveTotal()
    }
})

private fun updateLiveTotal() {
    val quantity = binding.editTextQuantityKg.text?.toString()?.toDoubleOrNull() ?: 0.0
    val product = selectedProduct
    if (product == null || quantity <= 0.0) {
        binding.textViewOrderTotalValue.text = formatCurrency(0.0)
        return
    }
    val total = quantity * product.pricePerKg
    binding.textViewOrderTotalValue.text = formatCurrency(total)
}
```

**Explanation:**
- `TextWatcher` monitors the quantity field and recalculates the total in real-time
- `AlertDialog.Builder` provides user confirmation before destructive operations
- Intent extras (`putExtra`) pass data between activities
- RecyclerView adapter callbacks handle list item interactions

#### 4.3.5 Intent Navigation Between Screens

```kotlin
object BrewLogIntents {
    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val EXTRA_ORDER_ID = "extra_order_id"
    const val EXTRA_EMPLOYEE_NAME = "extra_employee_name"
}

// Navigation from ProductList to CreateOrder
binding.buttonCreateOrder.setOnClickListener {
    startActivity(Intent(this, CreateCustomerOrderActivity::class.java))
}

// Navigation from ProductList to OrderHistory
binding.buttonViewOrders.setOnClickListener {
    startActivity(Intent(this, OrderHistoryActivity::class.java))
}

// Navigation from CreateOrder to OrderHistory (after successful submission)
Toast.makeText(this, "Order submitted", Toast.LENGTH_SHORT).show()
startActivity(Intent(this, OrderHistoryActivity::class.java))
finish()
```

#### 4.3.6 Data Models

```kotlin
data class Product(
    val id: Long = 0L,
    val name: String,
    val origin: String,
    val roastLevel: String,
    val roastDate: String,
    val pricePerKg: Double,
    val stockKg: Double,
)

data class Customer(
    val id: Long = 0L,
    val cafeName: String,
    val contactName: String,
    val email: String,
)

data class OrderRecord(
    val id: Long = 0L,
    val customerId: Long,
    val productId: Long,
    val quantityKg: Double,
    val totalPrice: Double,
    val orderDate: String,
    val orderStatus: Int,
)

data class OrderHistoryItem(
    val id: Long,
    val cafeName: String,
    val productName: String,
    val quantityKg: Double,
    val totalPrice: Double,
    val orderDate: String,
    val orderStatus: Int,
)
```

#### 4.3.7 RecyclerView Adapter Implementation

```kotlin
class ProductAdapter(
    private val context: Context,
    private val onEditClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit,
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val items = mutableListOf<Product>()

    fun submitList(products: List<Product>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ProductViewHolder(
        private val binding: ItemProductBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textViewProductName.text = product.name
            binding.textViewProductOrigin.text = "Origin: ${product.origin}"
            binding.textViewProductDetails.text =
                "Roast: ${product.roastLevel} - Roast Date: ${product.roastDate}"
            binding.textViewProductPrice.text = "${formatCurrency(product.pricePerKg)} / kg"
            binding.textViewProductStock.text = "Stock: ${formatQuantity(product.stockKg)}"

            val freshness = resolveFreshnessBadge(calculateDaysSinceRoast(product.roastDate))
            binding.textViewFreshnessBadge.text = freshness.label

            binding.buttonEditProductRow.setOnClickListener { onEditClicked(product) }
            binding.buttonDeleteProductRow.setOnClickListener { onDeleteClicked(product) }
            binding.root.setOnClickListener { onEditClicked(product) }
        }
    }
}
```

### 4.4 AndroidManifest Configuration

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.BrewLog">
        <activity android:name=".ui.OrderHistoryActivity" android:exported="false" />
        <activity android:name=".ui.CreateCustomerOrderActivity" android:exported="false" />
        <activity android:name=".ui.AddEditProductActivity" android:exported="false" />
        <activity android:name=".ui.ProductListActivity" android:exported="false" />
        <activity android:name=".ui.LoginActivity" android:exported="false" />
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

---

## 5. Task 4: Testing and Evaluation

### 5.1 Unit Tests

The application includes automated unit tests for UI utility functions:

```kotlin
class BrewLogUiUtilsTest {

    @Test
    fun resolveFreshnessBadge_returnsPeakFreshness_forRecentRoast() {
        val badge = resolveFreshnessBadge(3)
        assertEquals("Peak Freshness", badge.label)
    }

    @Test
    fun resolveFreshnessBadge_returnsGood_forMidAgeRoast() {
        val badge = resolveFreshnessBadge(14)
        assertEquals("Good", badge.label)
    }

    @Test
    fun resolveFreshnessBadge_returnsStaleDiscount_forOldRoast() {
        val badge = resolveFreshnessBadge(30)
        assertEquals("Stale / Discount", badge.label)
    }

    @Test
    fun resolveOrderStatusBadge_mapsStatusCodes() {
        assertEquals("Pending", resolveOrderStatusBadge(BrewLogContract.OrderStatus.PENDING).label)
        assertEquals("Shipped", resolveOrderStatusBadge(BrewLogContract.OrderStatus.SHIPPED).label)
        assertEquals("Cancelled", resolveOrderStatusBadge(BrewLogContract.OrderStatus.CANCELLED).label)
    }
}
```

### 5.2 Functional Test Cases

| Test Case # | Test Description | Input | Expected Output | Actual Output | Status |
|-------------|-----------------|-------|-----------------|---------------|--------|
| TC-01 | Valid Login | Username: "admin", Password: "brewlog" | User is authenticated and navigated to Product List screen. Employee name "Roastery Manager" is passed via Intent extra. | User is authenticated successfully. Product List screen is displayed with products loaded. | PASS |
| TC-02 | Invalid Login - Wrong Password | Username: "admin", Password: "wrongpass" | Error message "Invalid credentials" is displayed on the password field. Toast message "Invalid credentials" is shown. User remains on Login screen. | Error message "Invalid credentials" appears below password field. Toast notification displayed. Login screen remains active. | PASS |
| TC-03 | Add New Product | Name: "Kenya AA", Origin: "Kenya", Roast Level: "Medium", Date: 2026-05-20, Price: 15.50, Stock: 30.0 | Product is inserted into the database. Toast "Product saved" is shown. Activity finishes and returns to Product List. New product appears in the list. | Product saved successfully. Toast displayed. Product List shows "Kenya AA" with correct details and "Peak Freshness" badge. | PASS |
| TC-04 | Create Order - Exceeds Stock | Customer: "Cafe Luna", Product: "Ethiopia Yirgacheffe" (stock: 48.0 kg), Quantity: 60.0 | Error message "Quantity exceeds available stock" displayed on quantity field. Order is NOT placed. Stock remains unchanged. | Inline error "Quantity exceeds available stock" appears. Submit button has no effect. Database stock value unchanged at 48.0 kg. | PASS |
| TC-05 | Create Order - Valid Submission | Customer: "Cafe Luna", Product: "Ethiopia Yirgacheffe" (stock: 48.0 kg, price: $14.00/kg), Quantity: 10.0 | Order is inserted with total_price = $140.00. Product stock is reduced from 48.0 to 38.0 kg. Toast "Order submitted" shown. User navigated to Order History. New order appears with status "Pending". | Order placed successfully. Live total showed $140.00 during input. Stock reduced to 38.0 kg. Order History shows new order for Cafe Luna with "Pending" badge. | PASS |
| TC-06 | Delete Order - Stock Restoration | Delete the order created in TC-05 (Order for Cafe Luna, 10.0 kg) | Confirmation dialog appears. On confirm: order is deleted from database, product stock is restored from 38.0 back to 48.0 kg. Toast "Order deleted" shown. Order disappears from history list. | Confirmation dialog "Delete the order for Cafe Luna?" displayed. After confirmation: order removed, stock restored to 48.0 kg (verified on Product List). Toast displayed. | PASS |
| TC-07 | Update Order Status | Select existing order, choose "Shipped" from status dialog | Order status updates from Pending (0) to Shipped (1). Badge changes from grey "Pending" to green "Shipped". Toast "Order updated" shown. | Status dialog shows three options. After selecting "Shipped" and pressing "Save": badge updates to green "Shipped". Toast displayed. Database value changed to 1. | PASS |
| TC-08 | Edit Product | Edit "Guatemala Antigua": change price from 13.50 to 15.00, stock from 35.0 to 40.0 | Product is updated in database. Toast "Product saved" shown. Product List reflects new price ($15.00/kg) and stock (40.0 kg). | Form pre-fills with existing values. After changing price and stock and pressing "Update": changes saved. Product List shows $15.00/kg and Stock: 40.0 kg. | PASS |
| TC-09 | Login with Empty Fields | Username: "" (empty), Password: "" (empty) | Error message "Username is required" displayed on username field. Form does not submit. | Inline error "Username is required" appears on first empty field. No database query executed. | PASS |
| TC-10 | Add Product - Missing Required Fields | Name: "" (empty), Origin: "Colombia", Price: 12.00, Stock: 25.0 | Error message "Batch name is required" displayed. Product is NOT saved. | Inline error "Batch name is required" appears. Save button click has no database effect. User remains on form. | PASS |

### 5.3 Testing Summary

- **Total Test Cases:** 10
- **Passed:** 10
- **Failed:** 0
- **Pass Rate:** 100%

The application has been tested across authentication, CRUD operations, validation logic, transactional integrity (stock deduction/restoration), and navigation flow. All test scenarios produced expected results.

---

## 6. Task 5: Documentation and Presentation

### 6.1 Application Screenshots

The following screens are available in the running application:

1. **Login Screen** - Material Design card-based layout with branded header, credential inputs, and demo account badge
2. **Product List Screen** - Toolbar, summary card, action buttons, RecyclerView with product cards showing freshness badges (colour-coded: green/orange/red)
3. **Add Product Screen** - Scrollable form with text inputs, dropdown selector, date picker, and decimal-filtered numeric fields
4. **Edit Product Screen** - Same form as Add, pre-populated with existing data, with Delete button visible
5. **Create Order Screen** - Customer/product dropdowns, stock indicator, quantity input with live total calculation card
6. **Order History Screen** - Chronological order list with status badges, edit-status and delete actions per item
7. **Empty States** - Both Product List and Order History display friendly empty-state cards when no data exists

### 6.2 Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| SQLite over Firebase | Chosen for offline-first operation -- roastery staff may work in areas with limited connectivity. No internet dependency. |
| View Binding over findViewById | Type-safe view access eliminates null pointer exceptions and casting errors at compile time. |
| DAO Pattern | Separates database logic from UI code, making the application more maintainable and testable. |
| Database Transactions | Ensures atomic operations for order placement (insert order + deduct stock) -- prevents data inconsistency if either step fails. |
| Seed Data | Provides immediate usability on first launch with demo credentials and sample data, reducing onboarding friction. |
| Material Design 3 | Modern, accessible UI with built-in components (TextInputLayout errors, AlertDialogs, FAB) that follow Android best practices. |
| Foreign Key Constraints | ON DELETE RESTRICT prevents accidental orphaning of order records when products or customers are removed. |

---

## 7. Conclusion and Future Improvements

### 7.1 Conclusion

BrewLog successfully addresses the operational challenges faced by the wholesale coffee roastery. The application replaces manual notebook-based record-keeping with a structured digital system that:

- **Eliminates data loss** through persistent SQLite storage with backup support
- **Ensures inventory accuracy** through automated stock deduction and restoration via database transactions
- **Provides sales history** through a comprehensive order history view with filtering and status tracking
- **Reduces human error** through automated total calculations, input validation, and stock availability checks
- **Adds accountability** through employee authentication

The application meets all specified functional requirements (authentication, product CRUD, order creation, history viewing, status management) and non-functional requirements (usability through Material Design, performance through efficient queries, data integrity through constraints and transactions).

### 7.2 Future Improvements

| Improvement | Description |
|-------------|-------------|
| Password Hashing | Replace plain-text password storage with bcrypt or Argon2 hashing for production security. |
| Firebase Cloud Sync | Add optional cloud synchronisation so that multiple devices can access the same data and orders are backed up remotely. |
| Search and Filtering | Add search functionality to product list and order history (by date range, customer, product, or status). |
| Reporting Dashboard | Add a summary screen showing total revenue, top-selling products, and customer order frequency with charts. |
| Customer Management Screen | Allow adding/editing/deleting customers directly from the app rather than relying solely on seed data. |
| Barcode/QR Scanning | Integrate camera-based scanning to quickly look up products by batch code. |
| Export to PDF/CSV | Allow exporting order history or inventory reports for accounting and record-keeping. |
| Multi-user Roles | Implement role-based access (Admin vs. Staff) with different permission levels for destructive operations. |
| Push Notifications | Notify staff when stock runs low or when an order status changes. |
| Dark Mode | Add theme toggle for comfortable use in low-light warehouse environments. |

---

## Deliverables Checklist

| Deliverable | Status |
|-------------|--------|
| Mobile Application Source Code | Included (Kotlin source in app/src/main/) |
| APK File | Generated via Android Studio Build > Build APK |
| Project Report (this document) | Complete |
| Screenshots of the Application | Referenced in Section 6.1 |
| Presentation Demonstration | Ready for live demo |

---

## References

- Android Developers Documentation: https://developer.android.com
- Material Design 3 Guidelines: https://m3.material.io
- SQLite Documentation: https://www.sqlite.org/docs.html
- Android SQLiteOpenHelper API Reference
- RecyclerView Implementation Guide

---

*End of Report*
