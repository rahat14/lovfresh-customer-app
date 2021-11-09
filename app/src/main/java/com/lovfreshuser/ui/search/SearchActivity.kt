package com.lovfreshuser.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.SearchView
import com.lovfreshuser.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private  lateinit var  binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.autoSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query?.lowercase())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               // search(newText)
                return true
            }
        })


    }

    private fun search(query: String?) {

    }

}