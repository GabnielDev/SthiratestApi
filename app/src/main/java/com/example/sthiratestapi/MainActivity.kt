package com.example.sthiratestapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sthiratestapi.adapter.JadwalAdapter
import com.example.sthiratestapi.databinding.ActivityMainBinding
import com.example.sthiratestapi.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var jadwalAdapter: JadwalAdapter

    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()
        setUpViewModel()
        getData()

        searchView()



    }

    private fun getData() {
        getLoading()
        showJadwal()
    }

    private fun setAdapter() {
        jadwalAdapter = JadwalAdapter()
        binding.rvJadwal.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = jadwalAdapter
        }
    }

    private fun setUpViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun showJadwal() {
        mainViewModel.getJadwal().observe(this, {
            jadwalAdapter.setData(it)
            Log.e("jadwalData", "$it")
        })
        mainViewModel.getJadwal()
    }

    private fun getLoading() {
        mainViewModel.getLoading().observe(this, {
            loading = it
            if (loading) binding.progressCircular.visibility = View.VISIBLE
            else binding.progressCircular.visibility = View.GONE
        })

        mainViewModel.getStatus().observe(this, {
            if (it >= 400) binding.lineNodata.visibility = View.GONE
        })

        mainViewModel.getMessage().observe(this, {
            if (!it.isNullOrEmpty()) binding.lineNodata.visibility = View.VISIBLE
            binding.rvJadwal.visibility = View.GONE

            if (it.isNullOrEmpty()) binding.lineNodata.visibility = View.GONE

        })

    }



    private fun searchView(): Boolean {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    jadwalAdapter.filter.filter("")
                } else {
                    try {
                        jadwalAdapter.filter.filter(newText)
                    } catch (e: Exception) {
                        e.message
                    }
                }
                return false
            }

        })

        return true
    }

}