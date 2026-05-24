package app.andama.brewlog.ui

import app.andama.brewlog.data.local.BrewLogContract
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class BadgeUi(
    val label: String,
    val colorRes: Int,
)

fun formatCurrency(value: Double): String = NumberFormat.getCurrencyInstance(Locale.US).format(value)

fun formatQuantity(value: Double): String = String.format(Locale.US, "%.1f kg", value)

fun calculateDaysSinceRoast(roastDate: String): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return try {
        val roast = formatter.parse(roastDate) ?: return 0L
        val diffMillis = Date().time - roast.time
        TimeUnit.MILLISECONDS.toDays(diffMillis).coerceAtLeast(0L)
    } catch (_: ParseException) {
        0L
    }
}

fun resolveFreshnessBadge(daysOld: Long): BadgeUi = when {
    daysOld < 7L -> BadgeUi("Peak Freshness", android.R.color.holo_green_dark)
    daysOld <= 21L -> BadgeUi("Good", android.R.color.holo_orange_dark)
    else -> BadgeUi("Stale / Discount", android.R.color.holo_red_dark)
}

fun resolveOrderStatusBadge(status: Int): BadgeUi = when (status) {
    BrewLogContract.OrderStatus.SHIPPED -> BadgeUi("Shipped", android.R.color.holo_green_dark)
    BrewLogContract.OrderStatus.CANCELLED -> BadgeUi("Cancelled", android.R.color.holo_red_dark)
    else -> BadgeUi("Pending", android.R.color.darker_gray)
}