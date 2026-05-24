package app.andama.brewlog.data.local

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

data class Employee(
    val id: Long = 0L,
    val username: String,
    val password: String,
    val fullName: String,
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