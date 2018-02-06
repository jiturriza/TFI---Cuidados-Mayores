package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class ActivityWithBackButton : AppCompatActivity() {

    //Manejo del boton Back
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}