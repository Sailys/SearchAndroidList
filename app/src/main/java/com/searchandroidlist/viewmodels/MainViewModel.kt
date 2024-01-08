package com.searchandroidlist.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.searchandroidlist.helpers.ResultWrapper
import com.searchandroidlist.repositories.MainRepository
import kotlinx.coroutines.*

class MainViewModel constructor(
    private val mainRepository: MainRepository = MainRepository.getInstance()
) : ViewModel() {

    val errorMessage = MutableLiveData<String>()

    //    val movieList = MutableLiveData<List<Movie>>()
    val countryDataList = MutableLiveData<JsonElement>()

    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

//    fun getAllMovies(page: Int, size: Int) {
//        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//            loading.postValue(true)
//            val response = mainRepository.getAllMovies(page, size)
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//                    movieList.postValue(response.body())
//                    loading.value = false
//                } else {
//                    onError("Error : ${response.message()} ")
//                }
//            }
//        }
//    }

    fun getCountryList() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response =
                mainRepository.getCountryList()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ResultWrapper.NetworkError -> onError("Please check your network connection")
                    is ResultWrapper.GenericError -> onError("Error : ${response.message} ")
                    is ResultWrapper.Success -> {
                        countryDataList.postValue(response.value!!)
                        loading.value = false
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}