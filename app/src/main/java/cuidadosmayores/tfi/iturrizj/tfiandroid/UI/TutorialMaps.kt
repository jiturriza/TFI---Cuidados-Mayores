package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import cuidadosmayores.tfi.iturrizj.tfiandroid.R

class TutorialMaps : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_maps_title1),
                getString(R.string.tutorial_maps_desc1),
                R.drawable.ic_location,
                resources.getColor(R.color.login_maps1)))

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.tutorial_maps_title2),
                getString(R.string.tutorial_maps_desc2),
                R.drawable.ic_history,
                resources.getColor(R.color.login_maps2)))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        AlertDialog.Builder(this@TutorialMaps).
                setTitle(R.string.tutorial_skip).setTitle("Omitir Tutorial")
                .setMessage(R.string.tutorial_skip)
                .setPositiveButton("Aceptar", { _, _ ->
                    finish()
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out)
                })
                .setNegativeButton("Cancelar", { _, _ -> })
                .show()
    }

}
