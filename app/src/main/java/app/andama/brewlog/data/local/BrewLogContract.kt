package app.andama.brewlog.data.local

import android.provider.BaseColumns

object BrewLogContract {

    object Products : BaseColumns {
        const val TABLE_NAME = "products"
        const val COLUMN_NAME = "name"
        const val COLUMN_ORIGIN = "origin"
        const val COLUMN_ROAST_LEVEL = "roast_level"
        const val COLUMN_ROAST_DATE = "roast_date"
        const val COLUMN_PRICE_PER_KG = "price_per_kg"
        const val COLUMN_STOCK_KG = "stock_kg"
    }

    object Customers : BaseColumns {
        const val TABLE_NAME = "customers"
        const val COLUMN_CAFE_NAME = "cafe_name"
        const val COLUMN_CONTACT_NAME = "contact_name"
        const val COLUMN_EMAIL = "email"
    }

    object Employees : BaseColumns {
        const val TABLE_NAME = "employees"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_FULL_NAME = "full_name"
    }

    object Orders : BaseColumns {
        const val TABLE_NAME = "orders"
        const val COLUMN_CUSTOMER_ID = "customer_id"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_QUANTITY_KG = "quantity_kg"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_ORDER_DATE = "order_date"
        const val COLUMN_ORDER_STATUS = "order_status"
    }

    object RoastLevel {
        const val LIGHT = "Light"
        const val MEDIUM = "Medium"
        const val DARK = "Dark"

        val ALL = listOf(LIGHT, MEDIUM, DARK)
    }

    object OrderStatus {
        const val PENDING = 0
        const val SHIPPED = 1
        const val CANCELLED = 2

        val ALL = listOf(PENDING, SHIPPED, CANCELLED)
    }
}