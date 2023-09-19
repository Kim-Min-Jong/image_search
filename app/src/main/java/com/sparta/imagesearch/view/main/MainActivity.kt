package com.sparta.imagesearch.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sparta.imagesearch.R
import com.sparta.imagesearch.databinding.ActivityMainBinding
import com.sparta.imagesearch.view.search.SearchFragment
import com.sparta.imagesearch.view.save.SaveFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        initViews()
    }
    private val mainViewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }

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