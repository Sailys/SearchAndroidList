package com.searchandroidlist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchandroidlist.adapters.SearchListAdapter
import com.searchandroidlist.data.SearchData
import com.searchandroidlist.databinding.ActivitySearchListBinding
import com.searchandroidlist.helpers.ConnectionLiveData
import com.searchandroidlist.utils.LoadingScreen
import com.searchandroidlist.utils.UiHelper
import com.searchandroidlist.viewmodelfactory.MyViewModelFactory
import com.searchandroidlist.viewmodels.MainViewModel
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.lang.reflect.Type

class SearchListActivity : AppCompatActivity() , SearchView.OnQueryTextListener{

    lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivitySearchListBinding
    private lateinit var adapter: SearchListAdapter
    private val searchItemsList: MutableList<SearchData> = mutableListOf()
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // create  layoutManager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        // pass it to rvLists layoutManager
        binding.searchRecycler.layoutManager = layoutManager

        viewModel = ViewModelProvider(this, MyViewModelFactory()).get(MainViewModel::class.java)

        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let {
                UiHelper.showLogs("isNetworkAvailable", " isNetworkAvailable ------ $it")
                if (it) {
                    viewModel.getCountryList()
                }
            }
        }

        viewModel.countryDataList.observe(this) {
            val jsonArray: JsonArray = it.asJsonArray

            // initialize the adapter,
            // and pass the required argument
            val gson = Gson()
            val founderListType: Type = object : TypeToken<ArrayList<SearchData?>?>() {}.type

            val founderList: List<SearchData> = gson.fromJson(jsonArray, founderListType)

            searchItemsList.clear()
            searchItemsList.addAll(founderList)
            adapter = SearchListAdapter(searchItemsList, this)
            // attach adapter to the recycler view
            binding.searchRecycler.adapter = AlphaInAnimationAdapter(adapter).apply {
                // Change the durations.
                setDuration(1000)
                // Disable the first scroll mode.
                setFirstOnly(false)
            }
        }

        viewModel.loading.observe(this, Observer {
            if (it) {
                LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
            } else {
                LoadingScreen.hideLoading()
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            UiHelper.showSnackBar(
                view,
                it
            )
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return onQueryTextSubmit(query)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return onQueryTextChange(newText)
    }
}