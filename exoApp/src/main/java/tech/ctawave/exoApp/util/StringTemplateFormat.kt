package tech.ctawave.exoApp.util

import android.content.Context
import java.text.MessageFormat

fun templateFormat(stringId: Int, context: Context, vararg args: Any?): String {
    val fmt = context.getString(stringId)
    return MessageFormat(fmt).format(args)
}
