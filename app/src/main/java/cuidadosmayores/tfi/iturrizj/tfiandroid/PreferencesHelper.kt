package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.content.Context

const val PREFERENCES_NAME = "Storage"

class PreferencesHelper {

    enum class BooleanElements {
        SHOW_TUTORIAL
    }

    companion object {
        fun isEnabled(context: Context, element: BooleanElements): Boolean {
            return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(element.toString(), true)
        }

        fun setEnabled(context: Context, element: BooleanElements, enabled:Boolean) {
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putBoolean(element.toString(), enabled).apply()
        }

    }

}