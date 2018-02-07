package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
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
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Helpers.PreferencesHelper
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Helpers.PreferencesHelper.BooleanElements
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Helpers.StringHelper
import cuidadosmayores.tfi.iturrizj.tfiandroid.BLL.Helpers.ValidationHelper
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

val facebookCallback = CallbackManager.Factory.create()
const val REQUEST_REGISTER: Int = 333

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (PreferencesHelper.isEnabled(applicationContext, BooleanElements.SHOW_LOGIN_TUTORIAL)) showTutorial()

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
            startActivityForResult(Intent(this, RegisterActivity::class.java), REQUEST_REGISTER)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        login_button.setOnClickListener { _ ->
            if (hacerValidaciones()) {
                //Autenticacion en Firebase
                /*                mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
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
                                        } */
                startActivity(Intent(applicationContext, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            } else {
                val shake = TranslateAnimation(0f, 10f, 0f, 0f)
                shake.duration = 300
                shake.interpolator = CycleInterpolator(2f)
            }
        }
    }

    fun hacerValidaciones(): Boolean {
        StringHelper.validateLenght(email, 2, null)
        StringHelper.validateLenght(password, 2, 10)
        val views = listOf(email, password)
        return ValidationHelper.validarViews(views)
    }

    private fun showTutorial() {
        PreferencesHelper.setEnabled(applicationContext, BooleanElements.SHOW_LOGIN_TUTORIAL, false)
        startActivity(Intent(applicationContext, TutorialLogin::class.java))
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_REGISTER && resultCode == Activity.RESULT_OK) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(applicationContext, "Registracion exitosa!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                Activity.RESULT_CANCELED -> Toast.makeText(applicationContext, "Registracion falló!", Toast.LENGTH_SHORT).show()
            }
        }
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
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            finish()
                        }
                    }
                })
    }

}
