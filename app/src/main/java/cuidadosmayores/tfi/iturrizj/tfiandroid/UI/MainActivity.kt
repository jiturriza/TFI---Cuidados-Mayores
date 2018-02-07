package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import cuidadosmayores.tfi.iturrizj.tfiandroid.ApplicationCM
import cuidadosmayores.tfi.iturrizj.tfiandroid.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Desea alertar una emergencia?", Snackbar.LENGTH_LONG)
                    .setActionTextColor(resources.getColor(R.color.colorAccent))
                    .setAction("ALERTAR",
                            { _ -> Toast.makeText(applicationContext, "Avisando a emergencias y a familiares agendados..", Toast.LENGTH_SHORT).show() }
                    ).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.getHeaderView(0).findViewById<TextView>(R.id.textView_username).text = FirebaseAuth.getInstance().currentUser?.displayName
        banana.setOnClickListener({ _ ->
            startActivity(Intent(this@MainActivity,
                    FormularioComidaActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, banana, "bananashared").toBundle())

        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> (application as ApplicationCM).logOut()
            R.id.nav_maps -> startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            R.id.nav_settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun cerrarSesion() {
        AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("Esta seguro que desea terminar la sesion?")
                .setPositiveButton("Aceptar", { _, _ -> startActivity(Intent(this@MainActivity, LoginActivity::class.java)) })
                .setNegativeButton("Cancelar", null)
                .create()
                .show()
    }
}
