package com.khb.mvvmmygithub.ui.main

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.khb.mvvmlibrary.base.view.activity.BaseActivity
import com.khb.mvvmmygithub.R

class MainActivity : BaseActivity() {
    override val layoutId = R.layout.activity_main

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    companion object {
        fun launch(appCompatActivity: FragmentActivity) {

            appCompatActivity.apply {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }


        }
    }

}
