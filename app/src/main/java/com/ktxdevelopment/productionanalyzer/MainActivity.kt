package com.ktxdevelopment.productionanalyzer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ktxdevelopment.productionanalyzer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentMain: FragmentMain
    private lateinit var fragmentResult: FragmentResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentMain = FragmentMain()
        supportFragmentManager.beginTransaction().add(R.id.navHost, fragmentMain).commit()
    }

    fun navigateToResults(results: ArrayList<MainModel>) {
        fragmentResult = FragmentResult(results)
        supportFragmentManager.beginTransaction().add(R.id.navHost, fragmentResult, "res_tag").commit()
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag("res_tag").let {
            if (it != null && it.isVisible) supportFragmentManager.beginTransaction().remove(fragmentResult).commit()
            else finish()
        }
    }
}