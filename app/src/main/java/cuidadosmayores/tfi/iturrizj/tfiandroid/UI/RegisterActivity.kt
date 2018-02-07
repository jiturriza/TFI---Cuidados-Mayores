package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Idioma
import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Usuario
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Callback
import cuidadosmayores.tfi.iturrizj.tfiandroid.CuidadosMayoresServices
import cuidadosmayores.tfi.iturrizj.tfiandroid.R
import cuidadosmayores.tfi.iturrizj.tfiandroid.ServiceGenerator
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Response
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        obtenerIdiomas()
//        registarFacebook()
        register_button.setOnClickListener({ _ -> registrar() })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtenerIdiomas() {
        ServiceGenerator.createService(CuidadosMayoresServices::class.java)
                ?.getIdiomas()
                ?.enqueue(object : Callback<List<Idioma>>() {
                    override fun onResponse(call: Call<List<Idioma>>?, response: Response<List<Idioma>>?) {
                        super.onResponse(call, response)
                        var idiomas: List<Idioma>? = response?.body()
                        if (idiomas != null) {
                            this@RegisterActivity.spinner_idiomas.adapter = SpinnerIdiomasAdapter(idiomas, this@RegisterActivity)
                        }
                    }
                })
    }

    private fun registrar() {
        var usuario = Usuario()
        usuario.Email = email.text.toString()
//        usuario.Idioma = (spinner_idiomas.selectedItem as Idioma)
        usuario.Password = clave.text.toString()

        /*ServiceGenerator.createService(CuidadosMayoresServices::class.java)
                ?.register(usuario)
                ?.enqueue(object : Callback<Any>() {
                    override fun onSuccess() {
                        super.onSuccess()
                        Toast.makeText(this@RegisterActivity, "Usuario Creado", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onFailure(call: Call<Any>?, t: Throwable?) {
                        super.onFailure(call, t)
                        Toast.makeText(this@RegisterActivity, "Registracion falló", Toast.LENGTH_SHORT).show()
                    }
                })*/

        mAuth.createUserWithEmailAndPassword(email.text.toString(), clave.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        setResult(Activity.RESULT_OK)
                    } else {
                        setResult(Activity.RESULT_CANCELED)
                    }
                }

        mAuth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                showUsernameDialog(user)
            }
        }
    }

    fun showUsernameDialog(user: FirebaseUser) {
        var textview_user = EditText(this@RegisterActivity)
        AlertDialog.Builder(this@RegisterActivity)
                .setTitle("Establecer Nombre de usuario")
                .setView(textview_user)
                .setPositiveButton("Aceptar", { _, _ -> updateProfile(user, textview_user.text.toString()) })
                .setNegativeButton("Cancelar", { _, _ -> updateProfile(user, email.text.toString()) })
                .create()
                .show()
    }

    fun updateProfile(user: FirebaseUser, name: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name).build()
        user.updateProfile(profileUpdates).addOnCompleteListener { _ -> finish() }
    }

}
