# BrewLog

BrewLog is a Kotlin + SQLite Android app for managing wholesale coffee bean batches and cafe orders.

## Run

```bash
./gradlew :app:assembleDebug
```

## Login

Default credentials:

- Username: `admin`
- Password: `brewlog`

## What is implemented

- SQLite schema with `products`, `customers`, and `orders`
- ViewBinding-based XML screens for login, inventory, product edit, order creation, and order history
- Freshness badges for coffee batches
- Live order total calculation and stock deduction on submit
- Order history with color-coded status badges
