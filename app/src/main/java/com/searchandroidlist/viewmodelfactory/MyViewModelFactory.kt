package com.searchandroidlist.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.searchandroidlist.repositories.MainRepository
import com.searchandroidlist.viewmodels.MainViewModel

class MyViewModelFactory constructor(private val repository: MainRepository = MainRepository.getInstance()): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}