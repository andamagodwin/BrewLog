package app.andama.brewlog.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BrewLogDao(context: Context) {

    private val databaseHelper = BrewLogDatabaseHelper(context.applicationContext)

    fun insertProduct(product: Product): Long = databaseHelper.writableDatabase.use { db ->
        db.insert(BrewLogContract.Products.TABLE_NAME, null, product.toContentValues())
    }

    fun updateProduct(product: Product): Int = databaseHelper.writableDatabase.use { db ->
        db.update(
            BrewLogContract.Products.TABLE_NAME,
            product.toContentValues(),
            "${BaseColumns._ID} = ?",
            arrayOf(product.id.toString()),
        )
    }

    fun deleteProduct(productId: Long): Int = databaseHelper.writableDatabase.use { db ->
        db.delete(
            BrewLogContract.Products.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(productId.toString()),
        )
    }

    fun getProductById(productId: Long): Product? = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Products.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(productId.toString()),
            null,
            null,
            null,
        ).use { cursor -> cursor.firstProductOrNull() }
    }

    fun getAllProducts(): List<Product> = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Products.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${BrewLogContract.Products.COLUMN_ROAST_DATE} DESC, ${BaseColumns._ID} DESC",
        ).use { cursor -> cursor.toProductList() }
    }

    fun insertCustomer(customer: Customer): Long = databaseHelper.writableDatabase.use { db ->
        db.insert(BrewLogContract.Customers.TABLE_NAME, null, customer.toContentValues())
    }

    fun insertEmployee(employee: Employee): Long = databaseHelper.writableDatabase.use { db ->
        db.insert(BrewLogContract.Employees.TABLE_NAME, null, employee.toContentValues())
    }

    fun authenticateEmployee(username: String, password: String): Employee? = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Employees.TABLE_NAME,
            null,
            "${BrewLogContract.Employees.COLUMN_USERNAME} = ? AND ${BrewLogContract.Employees.COLUMN_PASSWORD} = ?",
            arrayOf(username, password),
            null,
            null,
            null,
        ).use { cursor -> cursor.firstEmployeeOrNull() }
    }

    fun getEmployeeByUsername(username: String): Employee? = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Employees.TABLE_NAME,
            null,
            "${BrewLogContract.Employees.COLUMN_USERNAME} = ?",
            arrayOf(username),
            null,
            null,
            null,
        ).use { cursor -> cursor.firstEmployeeOrNull() }
    }

    fun updateCustomer(customer: Customer): Int = databaseHelper.writableDatabase.use { db ->
        db.update(
            BrewLogContract.Customers.TABLE_NAME,
            customer.toContentValues(),
            "${BaseColumns._ID} = ?",
            arrayOf(customer.id.toString()),
        )
    }

    fun deleteCustomer(customerId: Long): Int = databaseHelper.writableDatabase.use { db ->
        db.delete(
            BrewLogContract.Customers.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(customerId.toString()),
        )
    }

    fun getCustomerById(customerId: Long): Customer? = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Customers.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(customerId.toString()),
            null,
            null,
            null,
        ).use { cursor -> cursor.firstCustomerOrNull() }
    }

    fun getAllCustomers(): List<Customer> = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Customers.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${BrewLogContract.Customers.COLUMN_CAFE_NAME} COLLATE NOCASE ASC",
        ).use { cursor -> cursor.toCustomerList() }
    }

    fun insertOrder(order: OrderRecord): Long = databaseHelper.writableDatabase.use { db ->
        db.insert(BrewLogContract.Orders.TABLE_NAME, null, order.toContentValues())
    }

    fun updateOrderStatus(orderId: Long, status: Int): Int = databaseHelper.writableDatabase.use { db ->
        val values = ContentValues().apply {
            put(BrewLogContract.Orders.COLUMN_ORDER_STATUS, status)
        }

        db.update(
            BrewLogContract.Orders.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(orderId.toString()),
        )
    }

    fun deleteOrder(orderId: Long): Int = databaseHelper.writableDatabase.use { db ->
        db.beginTransaction()
        return try {
            val order = getOrderById(orderId)
            if (order == null) {
                0
            } else {
                val restoredStock = getProductStock(db, order.productId)
                if (restoredStock == null) {
                    0
                } else {
                    val stockValues = ContentValues().apply {
                        put(BrewLogContract.Products.COLUMN_STOCK_KG, restoredStock + order.quantityKg)
                    }

                    val stockUpdated = db.update(
                        BrewLogContract.Products.TABLE_NAME,
                        stockValues,
                        "${BaseColumns._ID} = ?",
                        arrayOf(order.productId.toString()),
                    )

                    if (stockUpdated == 1) {
                        val deleted = db.delete(
                            BrewLogContract.Orders.TABLE_NAME,
                            "${BaseColumns._ID} = ?",
                            arrayOf(orderId.toString()),
                        )
                        if (deleted == 1) {
                            db.setTransactionSuccessful()
                        }
                        deleted
                    } else {
                        0
                    }
                }
            }
        } finally {
            db.endTransaction()
        }
    }

    fun getOrderById(orderId: Long): OrderRecord? = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Orders.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(orderId.toString()),
            null,
            null,
            null,
        ).use { cursor -> cursor.firstOrderOrNull() }
    }

    fun getAllOrders(): List<OrderRecord> = databaseHelper.readableDatabase.use { db ->
        db.query(
            BrewLogContract.Orders.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${BrewLogContract.Orders.COLUMN_ORDER_DATE} DESC, ${BaseColumns._ID} DESC",
        ).use { cursor -> cursor.toOrderList() }
    }

    fun getOrderHistory(): List<OrderHistoryItem> = databaseHelper.readableDatabase.use { db ->
        val sql = """
            SELECT
                o.${BaseColumns._ID} AS order_id,
                c.${BrewLogContract.Customers.COLUMN_CAFE_NAME} AS cafe_name,
                p.${BrewLogContract.Products.COLUMN_NAME} AS product_name,
                o.${BrewLogContract.Orders.COLUMN_QUANTITY_KG} AS quantity_kg,
                o.${BrewLogContract.Orders.COLUMN_TOTAL_PRICE} AS total_price,
                o.${BrewLogContract.Orders.COLUMN_ORDER_DATE} AS order_date,
                o.${BrewLogContract.Orders.COLUMN_ORDER_STATUS} AS order_status
            FROM ${BrewLogContract.Orders.TABLE_NAME} o
            INNER JOIN ${BrewLogContract.Customers.TABLE_NAME} c
                ON c.${BaseColumns._ID} = o.${BrewLogContract.Orders.COLUMN_CUSTOMER_ID}
            INNER JOIN ${BrewLogContract.Products.TABLE_NAME} p
                ON p.${BaseColumns._ID} = o.${BrewLogContract.Orders.COLUMN_PRODUCT_ID}
            ORDER BY o.${BrewLogContract.Orders.COLUMN_ORDER_DATE} DESC, o.${BaseColumns._ID} DESC
        """.trimIndent()

        db.rawQuery(sql, null).use { cursor -> cursor.toOrderHistoryList() }
    }

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

    fun getProductStock(productId: Long): Double? = databaseHelper.readableDatabase.use { db ->
        getProductStock(db, productId)
    }

    fun clearAllTables() {
        databaseHelper.writableDatabase.use { db ->
            db.delete(BrewLogContract.Orders.TABLE_NAME, null, null)
            db.delete(BrewLogContract.Customers.TABLE_NAME, null, null)
            db.delete(BrewLogContract.Products.TABLE_NAME, null, null)
        }
    }

    private fun getProductStock(db: SQLiteDatabase, productId: Long): Double? {
        db.query(
            BrewLogContract.Products.TABLE_NAME,
            arrayOf(BrewLogContract.Products.COLUMN_STOCK_KG),
            "${BaseColumns._ID} = ?",
            arrayOf(productId.toString()),
            null,
            null,
            null,
        ).use { cursor ->
            if (!cursor.moveToFirst()) {
                return null
            }

            val stockIndex = cursor.getColumnIndexOrThrow(BrewLogContract.Products.COLUMN_STOCK_KG)
            return cursor.getDouble(stockIndex)
        }
    }

    private fun Product.toContentValues(): ContentValues = ContentValues().apply {
        put(BrewLogContract.Products.COLUMN_NAME, name)
        put(BrewLogContract.Products.COLUMN_ORIGIN, origin)
        put(BrewLogContract.Products.COLUMN_ROAST_LEVEL, roastLevel)
        put(BrewLogContract.Products.COLUMN_ROAST_DATE, roastDate)
        put(BrewLogContract.Products.COLUMN_PRICE_PER_KG, pricePerKg)
        put(BrewLogContract.Products.COLUMN_STOCK_KG, stockKg)
    }

    private fun Customer.toContentValues(): ContentValues = ContentValues().apply {
        put(BrewLogContract.Customers.COLUMN_CAFE_NAME, cafeName)
        put(BrewLogContract.Customers.COLUMN_CONTACT_NAME, contactName)
        put(BrewLogContract.Customers.COLUMN_EMAIL, email)
    }

    private fun Employee.toContentValues(): ContentValues = ContentValues().apply {
        put(BrewLogContract.Employees.COLUMN_USERNAME, username)
        put(BrewLogContract.Employees.COLUMN_PASSWORD, password)
        put(BrewLogContract.Employees.COLUMN_FULL_NAME, fullName)
    }

    private fun OrderRecord.toContentValues(): ContentValues = ContentValues().apply {
        put(BrewLogContract.Orders.COLUMN_CUSTOMER_ID, customerId)
        put(BrewLogContract.Orders.COLUMN_PRODUCT_ID, productId)
        put(BrewLogContract.Orders.COLUMN_QUANTITY_KG, quantityKg)
        put(BrewLogContract.Orders.COLUMN_TOTAL_PRICE, totalPrice)
        put(BrewLogContract.Orders.COLUMN_ORDER_DATE, orderDate)
        put(BrewLogContract.Orders.COLUMN_ORDER_STATUS, orderStatus)
    }

    private fun Cursor.toProductList(): List<Product> {
        val products = mutableListOf<Product>()
        while (moveToNext()) {
            toProductOrNull()?.let(products::add)
        }
        return products
    }

    private fun Cursor.toCustomerList(): List<Customer> {
        val customers = mutableListOf<Customer>()
        while (moveToNext()) {
            toCustomerOrNull()?.let(customers::add)
        }
        return customers
    }

    private fun Cursor.toOrderList(): List<OrderRecord> {
        val orders = mutableListOf<OrderRecord>()
        while (moveToNext()) {
            toOrderOrNull()?.let(orders::add)
        }
        return orders
    }

    private fun Cursor.toOrderHistoryList(): List<OrderHistoryItem> {
        val items = mutableListOf<OrderHistoryItem>()
        while (moveToNext()) {
            items.add(
                OrderHistoryItem(
                    id = getLong(getColumnIndexOrThrow("order_id")),
                    cafeName = getString(getColumnIndexOrThrow("cafe_name")),
                    productName = getString(getColumnIndexOrThrow("product_name")),
                    quantityKg = getDouble(getColumnIndexOrThrow("quantity_kg")),
                    totalPrice = getDouble(getColumnIndexOrThrow("total_price")),
                    orderDate = getString(getColumnIndexOrThrow("order_date")),
                    orderStatus = getInt(getColumnIndexOrThrow("order_status")),
                )
            )
        }
        return items
    }

    private fun Cursor.firstProductOrNull(): Product? = if (moveToFirst()) toProductOrNull() else null

    private fun Cursor.firstCustomerOrNull(): Customer? = if (moveToFirst()) toCustomerOrNull() else null

    private fun Cursor.firstEmployeeOrNull(): Employee? = if (moveToFirst()) toEmployeeOrNull() else null

    private fun Cursor.firstOrderOrNull(): OrderRecord? = if (moveToFirst()) toOrderOrNull() else null

    private fun Cursor.toProductOrNull(): Product? {
        val idIndex = getColumnIndex(BaseColumns._ID)
        val nameIndex = getColumnIndex(BrewLogContract.Products.COLUMN_NAME)
        val originIndex = getColumnIndex(BrewLogContract.Products.COLUMN_ORIGIN)
        val roastLevelIndex = getColumnIndex(BrewLogContract.Products.COLUMN_ROAST_LEVEL)
        val roastDateIndex = getColumnIndex(BrewLogContract.Products.COLUMN_ROAST_DATE)
        val priceIndex = getColumnIndex(BrewLogContract.Products.COLUMN_PRICE_PER_KG)
        val stockIndex = getColumnIndex(BrewLogContract.Products.COLUMN_STOCK_KG)

        if (idIndex == -1 || nameIndex == -1 || originIndex == -1 || roastLevelIndex == -1 || roastDateIndex == -1 || priceIndex == -1 || stockIndex == -1) {
            return null
        }

        return Product(
            id = getLong(idIndex),
            name = getString(nameIndex),
            origin = getString(originIndex),
            roastLevel = getString(roastLevelIndex),
            roastDate = getString(roastDateIndex),
            pricePerKg = getDouble(priceIndex),
            stockKg = getDouble(stockIndex),
        )
    }

    private fun Cursor.toCustomerOrNull(): Customer? {
        val idIndex = getColumnIndex(BaseColumns._ID)
        val cafeNameIndex = getColumnIndex(BrewLogContract.Customers.COLUMN_CAFE_NAME)
        val contactNameIndex = getColumnIndex(BrewLogContract.Customers.COLUMN_CONTACT_NAME)
        val emailIndex = getColumnIndex(BrewLogContract.Customers.COLUMN_EMAIL)

        if (idIndex == -1 || cafeNameIndex == -1 || contactNameIndex == -1 || emailIndex == -1) {
            return null
        }

        return Customer(
            id = getLong(idIndex),
            cafeName = getString(cafeNameIndex),
            contactName = getString(contactNameIndex),
            email = getString(emailIndex),
        )
    }

    private fun Cursor.toEmployeeOrNull(): Employee? {
        val idIndex = getColumnIndex(BaseColumns._ID)
        val usernameIndex = getColumnIndex(BrewLogContract.Employees.COLUMN_USERNAME)
        val passwordIndex = getColumnIndex(BrewLogContract.Employees.COLUMN_PASSWORD)
        val fullNameIndex = getColumnIndex(BrewLogContract.Employees.COLUMN_FULL_NAME)

        if (idIndex == -1 || usernameIndex == -1 || passwordIndex == -1 || fullNameIndex == -1) {
            return null
        }

        return Employee(
            id = getLong(idIndex),
            username = getString(usernameIndex),
            password = getString(passwordIndex),
            fullName = getString(fullNameIndex),
        )
    }

    private fun Cursor.toOrderOrNull(): OrderRecord? {
        val idIndex = getColumnIndex(BaseColumns._ID)
        val customerIdIndex = getColumnIndex(BrewLogContract.Orders.COLUMN_CUSTOMER_ID)
        val productIdIndex = getColumnIndex(BrewLogContract.Orders.COLUMN_PRODUCT_ID)
        val quantityIndex = getColumnIndex(BrewLogContract.Orders.COLUMN_QUANTITY_KG)
        val totalPriceIndex = getColumnIndex(BrewLogContract.Orders.COLUMN_TOTAL_PRICE)
        val orderDateIndex = getColumnIndex(BrewLogContract.Orders.COLUMN_ORDER_DATE)
        val statusIndex = getColumnIndex(BrewLogContract.Orders.COLUMN_ORDER_STATUS)

        if (idIndex == -1 || customerIdIndex == -1 || productIdIndex == -1 || quantityIndex == -1 || totalPriceIndex == -1 || orderDateIndex == -1 || statusIndex == -1) {
            return null
        }

        return OrderRecord(
            id = getLong(idIndex),
            customerId = getLong(customerIdIndex),
            productId = getLong(productIdIndex),
            quantityKg = getDouble(quantityIndex),
            totalPrice = getDouble(totalPriceIndex),
            orderDate = getString(orderDateIndex),
            orderStatus = getInt(statusIndex),
        )
    }

    private fun currentOrderTimestamp(): String = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault(),
    ).format(Date())
}