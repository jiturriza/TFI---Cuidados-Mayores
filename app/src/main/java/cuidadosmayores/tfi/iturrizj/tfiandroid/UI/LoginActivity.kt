package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import cuidadosmayores.tfi.iturrizj.tfiandroid.*
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Callback
import cuidadosmayores.tfi.iturrizj.tfiandroid.PreferencesHelper.BooleanElements
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response


val facebookCallback = CallbackManager.Factory.create()

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (PreferencesHelper.isEnabled(applicationContext, BooleanElements.SHOW_TUTORIAL)) showTutorial()

        mAuth = FirebaseAuth.getInstance()

        LoginManager.getInstance().registerCallback(facebookCallback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) handleFacebookAccessToken(result.accessToken)
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        var loginCallback = object : Callback<LoginResponse>() {

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                if (response?.code() == 200) {
                    if (response.body() == null) {
                        Toast.makeText(applicationContext, "Usuario o clave Incorrecta", Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }
                super.onResponse(call, response)
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                super.onFailure(call, t)
                Toast.makeText(applicationContext, t?.message, Toast.LENGTH_SHORT).show()
            }
        }

        register_button.setOnClickListener { _ ->
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        login_button.setOnClickListener { _ ->
            /*            if (hacerValidaciones()) {
                            //Autenticacion en Firebase
                            mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            //User de Firebase
                                            val user = mAuth.currentUser
                                            if (user != null) {
                                                //Autenticacion en servidor
                                                ServiceGenerator.createService(CuidadosMayoresServices::class.java)
                                                        ?.login(user?.uid)
                                                        ?.enqueue(loginCallback)
                                            }
                                        } else {
                                            Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                        }*/
            startActivity(Intent(applicationContext, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    fun hacerValidaciones(): Boolean {
        StringHelper.validateLenght(email, 2, null)
        StringHelper.validateLenght(password, 2, 10)
        val views = listOf(email, password)
        return ValidationHelper.validarViews(views)
    }

    private fun showTutorial() {
        PreferencesHelper.setEnabled(applicationContext, BooleanElements.SHOW_TUTORIAL, false)
        startActivity(Intent(applicationContext, TutorialLogin::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookCallback.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this@LoginActivity, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            val user = mAuth.currentUser
                        }
                    }
                })
    }

}
