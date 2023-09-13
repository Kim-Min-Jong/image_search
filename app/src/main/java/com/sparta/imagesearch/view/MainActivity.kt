package com.sparta.imagesearch.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.sparta.imagesearch.R
import com.sparta.imagesearch.databinding.ActivityMainBinding
import com.sparta.imagesearch.view.search.SearchFragment
import com.sparta.imagesearch.view.save.SaveFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() = with(binding) {
        bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    showFragment(SearchFragment.newInstance(), SearchFragment.TAG)
                    true
                }
                R.id.storage -> {
                    showFragment(SaveFragment.newInstance(), SaveFragment.TAG)
                    true
                }
                else -> false
            }

        }
        bottomNavView.selectedItemId = R.id.home
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment, tag)
            .commit()
    }

}