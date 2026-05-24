package app.andama.brewlog.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class BrewLogDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION,
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

    private fun createProductsTable(): String = """
        CREATE TABLE IF NOT EXISTS ${BrewLogContract.Products.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${BrewLogContract.Products.COLUMN_NAME} TEXT NOT NULL,
            ${BrewLogContract.Products.COLUMN_ORIGIN} TEXT NOT NULL,
            ${BrewLogContract.Products.COLUMN_ROAST_LEVEL} TEXT NOT NULL,
            ${BrewLogContract.Products.COLUMN_ROAST_DATE} TEXT NOT NULL,
            ${BrewLogContract.Products.COLUMN_PRICE_PER_KG} REAL NOT NULL,
            ${BrewLogContract.Products.COLUMN_STOCK_KG} REAL NOT NULL CHECK(${BrewLogContract.Products.COLUMN_STOCK_KG} >= 0)
        )
    """.trimIndent()

    private fun createCustomersTable(): String = """
        CREATE TABLE IF NOT EXISTS ${BrewLogContract.Customers.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${BrewLogContract.Customers.COLUMN_CAFE_NAME} TEXT NOT NULL,
            ${BrewLogContract.Customers.COLUMN_CONTACT_NAME} TEXT NOT NULL,
            ${BrewLogContract.Customers.COLUMN_EMAIL} TEXT NOT NULL UNIQUE
        )
    """.trimIndent()

    private fun createEmployeesTable(): String = """
        CREATE TABLE IF NOT EXISTS ${BrewLogContract.Employees.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${BrewLogContract.Employees.COLUMN_USERNAME} TEXT NOT NULL UNIQUE,
            ${BrewLogContract.Employees.COLUMN_PASSWORD} TEXT NOT NULL,
            ${BrewLogContract.Employees.COLUMN_FULL_NAME} TEXT NOT NULL
        )
    """.trimIndent()

    private fun createOrdersTable(): String = """
        CREATE TABLE IF NOT EXISTS ${BrewLogContract.Orders.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${BrewLogContract.Orders.COLUMN_CUSTOMER_ID} INTEGER NOT NULL,
            ${BrewLogContract.Orders.COLUMN_PRODUCT_ID} INTEGER NOT NULL,
            ${BrewLogContract.Orders.COLUMN_QUANTITY_KG} REAL NOT NULL CHECK(${BrewLogContract.Orders.COLUMN_QUANTITY_KG} > 0),
            ${BrewLogContract.Orders.COLUMN_TOTAL_PRICE} REAL NOT NULL CHECK(${BrewLogContract.Orders.COLUMN_TOTAL_PRICE} >= 0),
            ${BrewLogContract.Orders.COLUMN_ORDER_DATE} TEXT NOT NULL,
            ${BrewLogContract.Orders.COLUMN_ORDER_STATUS} INTEGER NOT NULL CHECK(${BrewLogContract.Orders.COLUMN_ORDER_STATUS} IN (0, 1, 2)),
            FOREIGN KEY(${BrewLogContract.Orders.COLUMN_CUSTOMER_ID}) REFERENCES ${BrewLogContract.Customers.TABLE_NAME}(${BaseColumns._ID}) ON UPDATE CASCADE ON DELETE RESTRICT,
            FOREIGN KEY(${BrewLogContract.Orders.COLUMN_PRODUCT_ID}) REFERENCES ${BrewLogContract.Products.TABLE_NAME}(${BaseColumns._ID}) ON UPDATE CASCADE ON DELETE RESTRICT
        )
    """.trimIndent()

    companion object {
        const val DATABASE_NAME = "brewlog.db"
        const val DATABASE_VERSION = 1
    }
}