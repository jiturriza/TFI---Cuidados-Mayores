package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
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


class RegisterActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.setApplicationId("572767276391517")
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
        usuario.Idioma = (spinner_idiomas.selectedItem as Idioma)
        usuario.Password = clave.text.toString()

        ServiceGenerator.createService(CuidadosMayoresServices::class.java)
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
                })

    }

/*
    private fun registarFacebook() {
        var mCallbackManager: CallbackManager = CallbackManager.Factory.create()
        facebook_singin_button.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult?) {
                if (loginResult != null) handleFacebookAccessToken(loginResult.getAccessToken());
            }

            override fun onError(error: FacebookException?) {
            }

            override fun onCancel() {
            }
        }
        )
    }
*/

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this@RegisterActivity, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            val user = mAuth.getCurrentUser()
                        } else {
                        }

                    }
                })
    }

}
