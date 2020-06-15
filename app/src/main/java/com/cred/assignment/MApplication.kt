package com.cred.assignment

import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Realm
        Realm.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }
}