package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.app.Application
import com.facebook.FacebookSdk

class ApplicationCM : Application() {

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setApplicationId("572767276391517")
        FacebookSdk.sdkInitialize(applicationContext)
    }
}