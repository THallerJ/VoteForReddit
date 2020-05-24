package com.hallert.voteforreddit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    private val LOGIN_REQUEST_CODE = 0

    private lateinit var auth: Authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation_bar)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_subreddits -> {
                Toast.makeText(this@BottomNavActivity,
                    "TODO: Launch subreddits Fragment",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            R.id.nav_search -> {
                Toast.makeText(this@BottomNavActivity,
                    "TODO: Launch search Fragment",
                    Toast.LENGTH_SHORT)
                    .show()

            }
            R.id.nav_submit -> {
                // TODO: Replace check with Authentication.isUserless()
                if(!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                    Toast.makeText(this@BottomNavActivity,
                        "TODO: Launch submit BottomSheet",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loginNewUser()
                }
            }
            R.id.nav_messages -> {
                // TODO: Replace check with Authentication.isUserless()
                if(!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                    Toast.makeText(this@BottomNavActivity,
                        "TODO: Launch messages Fragment",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loginNewUser()
                }
            }
            R.id.nav_profile -> {
                // TODO: Replace check with Authentication.isUserless()
                if(!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                    Toast.makeText(this@BottomNavActivity,
                        "TODO: Launch profile Fragment",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loginNewUser()
                }
            }
        }
        true
    }

    private fun loginNewUser() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }
}