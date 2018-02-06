package cuidadosmayores.tfi.iturrizj.tfiandroid

class LoginRequest {

    constructor(u : String, c:String){
        usuario = u
        clave = c
    }

    var usuario: String? = null
        get() = field

    var clave: String? = null
        get() = field

}
