package org.pursa.app

import android.app.Application
import org.pursa.app.app.PursaAppContainer

class PursaApp : Application() {
    lateinit var container: PursaAppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = PursaAppContainer(this)
    }
}
