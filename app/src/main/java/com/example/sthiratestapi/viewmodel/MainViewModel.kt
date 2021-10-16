package com.example.sthiratestapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sthiratestapi.model.AttendancesItem
import com.example.sthiratestapi.network.ApiClient
import com.example.sthiratestapi.utils.Constants.BASE_URL
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.ArrayList

class MainViewModel: ViewModel() {


    private val loading = MutableLiveData<Boolean>()
    private val status = MutableLiveData<Int>()
    private val message = MutableLiveData<String>()

    fun getJadwal(): LiveData<ArrayList<AttendancesItem?>> {
        val jadwal = MutableLiveData<ArrayList<AttendancesItem?>>()
        loading.value = true
        viewModelScope.launch {
            try {
                val data = ApiClient.getClient().getJadwal(BASE_URL)
                if (data.isSuccessful) {
                    jadwal.value = data.body()?.data?.attendances
                } else {
                    status.value = data.code()
                }
                loading.value = false
            } catch (t: Throwable) {
                when (t) {
                    is Exception -> message.value = t.message.toString()
                    is HttpException -> message.value = t.message().toString()
                    else -> message.value = "Unknow Error"
                }
                loading.value = false
            }
        }
        return jadwal
    }

    fun getLoading(): LiveData<Boolean> = loading
    fun getStatus(): LiveData<Int> = status
    fun getMessage(): LiveData<String> = message

}