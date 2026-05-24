package app.andama.brewlog.ui

import app.andama.brewlog.data.local.BrewLogContract
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.data.local.Customer
import app.andama.brewlog.data.local.Employee
import app.andama.brewlog.data.local.Product
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object BrewLogSeedData {

    fun seedIfNeeded(dao: BrewLogDao) {
        if (dao.getEmployeeByUsername(DEFAULT_USERNAME) == null) {
            dao.insertEmployee(DEFAULT_EMPLOYEE)
        }

        if (dao.getAllCustomers().isEmpty()) {
            sampleCustomers.forEach { dao.insertCustomer(it) }
        }

        if (dao.getAllProducts().isEmpty()) {
            sampleProducts.forEach { dao.insertProduct(it) }
        }
    }

    private const val DEFAULT_USERNAME = "admin"
    private const val DEFAULT_PASSWORD = "brewlog"

    private val DEFAULT_EMPLOYEE = Employee(
        username = DEFAULT_USERNAME,
        password = DEFAULT_PASSWORD,
        fullName = "Roastery Manager",
    )

    private val sampleCustomers = listOf(
        Customer(cafeName = "Cafe Luna", contactName = "Maya Chen", email = "maya@cafeluna.example"),
        Customer(cafeName = "Harbor Roast", contactName = "Ethan Cole", email = "ethan@harborroast.example"),
        Customer(cafeName = "Northline Espresso", contactName = "Sofia Patel", email = "sofia@northline.example"),
    )

    private val sampleProducts = listOf(
        Product(
            name = "Ethiopia Yirgacheffe",
            origin = "Ethiopia",
            roastLevel = BrewLogContract.RoastLevel.LIGHT,
            roastDate = daysAgo(4),
            pricePerKg = 14.00,
            stockKg = 48.0,
        ),
        Product(
            name = "Guatemala Antigua",
            origin = "Guatemala",
            roastLevel = BrewLogContract.RoastLevel.MEDIUM,
            roastDate = daysAgo(15),
            pricePerKg = 13.50,
            stockKg = 35.0,
        ),
        Product(
            name = "Sumatra Mandheling",
            origin = "Indonesia",
            roastLevel = BrewLogContract.RoastLevel.DARK,
            roastDate = daysAgo(30),
            pricePerKg = 12.75,
            stockKg = 22.0,
        ),
    )

    private fun daysAgo(days: Int): String {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -days)
        }
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
    }
}