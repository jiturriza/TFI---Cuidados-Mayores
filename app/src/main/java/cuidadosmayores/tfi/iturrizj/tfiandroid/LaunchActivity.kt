package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.LoginActivity
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.MainActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@LaunchActivity, LoginActivity::class.java))
        }
    }
}