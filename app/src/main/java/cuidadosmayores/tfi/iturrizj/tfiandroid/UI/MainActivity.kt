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
import com.google.android.gms.maps.GoogleMap
import cuidadosmayores.tfi.iturrizj.tfiandroid.MapsActivity
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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
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
            R.id.nav_logout -> cerrarSesion()
            R.id.nav_maps -> startActivity(Intent(this@MainActivity, MapsActivity::class.java))
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