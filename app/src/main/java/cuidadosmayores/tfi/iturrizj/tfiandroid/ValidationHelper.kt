package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.view.View
import android.widget.EditText
import android.widget.Spinner

class ValidationHelper {

    companion object {
        fun <V : View> validarViews(views: List<V>): Boolean {
            for (v in views){
                when (v){
                    is EditText -> if (v.error != null) return false
                }
            }
            return true
        }
    }


}