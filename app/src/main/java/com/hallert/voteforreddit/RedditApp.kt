package com.hallert.voteforreddit

import android.app.Application
import android.content.Context
import com.hallert.voteforreddit.database.RedditDatabase
import com.hallert.voteforreddit.user.Authentication
import com.hallert.voteforreddit.user.UserPreferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class RedditApp : Application() {

    @Inject
    lateinit var auth: Authentication

    @Inject
    lateinit var userPref: UserPreferences

    @Inject
    lateinit var db: RedditDatabase

    override fun onCreate() {
        super.onCreate()

        appContext = this

        userPref.setTheme()

        auth.authenticateOnStart(applicationContext)
        runBlocking { CoroutineScope(IO).launch { db.submissionDao.clearDatabase() } }
    }

    companion object {
        lateinit var appContext: Context
    }
}