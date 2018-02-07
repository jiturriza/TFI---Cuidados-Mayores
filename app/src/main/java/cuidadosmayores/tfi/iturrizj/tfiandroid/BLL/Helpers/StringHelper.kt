package cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Helpers

import android.widget.EditText

abstract class StringHelper() {

    companion object {
        fun validateLenght(text:EditText, min: Int?, max: Int?): Unit {
            if (min != null) {
                if (text.text.length <= min){
                    text.setError("El texto ingresado debe tener más de " + min + " caracteres.")
                }
            }

            if (max != null) {
                if (text.text.length >= max){
                    text.setError( "El texto ingresado debe tener menos de " + min + " caracteres.")
                }
            }
        }
    }

}
