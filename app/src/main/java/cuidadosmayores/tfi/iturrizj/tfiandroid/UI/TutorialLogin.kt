package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import cuidadosmayores.tfi.iturrizj.tfiandroid.R

class TutorialLogin : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSkipButton(true)
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_login_title1),
                getString(R.string.tutorial_login_desc1),
                R.drawable.ic_pulse,
                resources.getColor(R.color.login_tutorial1)))

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_login_title2),
                getString(R.string.tutorial_login_desc2),
                android.R.drawable.ic_dialog_map,
                resources.getColor(R.color.login_tutorial2)))

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_login_title3),
                getString(R.string.tutorial_login_desc3),
                R.drawable.ic_ambulance,
                resources.getColor(R.color.login_tutorial3)))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        AlertDialog.Builder(this@TutorialLogin).
                setTitle(R.string.tutorial_skip).setTitle("Omitir Tutorial")
                .setMessage(R.string.tutorial_skip)
                .setPositiveButton("Aceptar", { _, _ -> finish() })
                .setNegativeButton("Cancelar", { _, _ -> })
                .show()
    }
}