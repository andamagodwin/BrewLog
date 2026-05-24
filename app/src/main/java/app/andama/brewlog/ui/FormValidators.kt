package app.andama.brewlog.ui

import android.text.InputFilter
import android.text.Spanned
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class DecimalDigitsInputFilter(
    private val maxIntegerDigits: Int,
    private val maxFractionDigits: Int,
) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        val candidate = buildString {
            append(dest.subSequence(0, dstart))
            append(source.subSequence(start, end))
            append(dest.subSequence(dend, dest.length))
        }

        if (candidate.isBlank()) return null

        val parts = candidate.split('.')
        if (parts.size > 2) return ""

        val integerPart = parts.first().replace("-", "")
        if (integerPart.length > maxIntegerDigits) return ""

        if (parts.size == 2 && parts[1].length > maxFractionDigits) return ""

        return null
    }
}

fun TextInputLayout.setErrorMessage(message: String?) {
    error = message
    isErrorEnabled = !message.isNullOrBlank()
}

fun parsePositiveDoubleOrNull(value: CharSequence?): Double? {
    val parsed = value?.toString()?.trim()?.toDoubleOrNull() ?: return null
    return parsed.takeIf { it > 0.0 }
}

fun formatProductDate(year: Int, monthZeroBased: Int, dayOfMonth: Int): String = String.format(
    Locale.US,
    "%04d-%02d-%02d",
    year,
    monthZeroBased + 1,
    dayOfMonth,
)