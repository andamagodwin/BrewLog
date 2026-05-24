package app.andama.brewlog.ui

import app.andama.brewlog.data.local.BrewLogContract
import org.junit.Assert.assertEquals
import org.junit.Test

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