package com.searchandroidlist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.searchandroidlist.R
import com.searchandroidlist.data.SearchData
import com.searchandroidlist.databinding.ItemSearchListBinding
import com.squareup.picasso.Picasso
import java.util.Locale

class SearchListAdapter(
    var searchData: List<SearchData>, val context: Context
) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>(), Filterable {

    var searchFilterList: List<SearchData> = arrayListOf()

    init {
        searchFilterList = searchData
    }

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListAdapter.ViewHolder {
        val binding =
            ItemSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchListAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(searchFilterList[position]) {
                binding.searchTextName.text = this.name.common
                binding.searchTextCapital.text = this.capital?.get(0)
                Picasso.get()
                    .load(this.flags.png)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher_round)
                    .into(binding.searchImg)

            }
        }
    }

    override fun getItemCount(): Int {
        return searchFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    searchFilterList = searchData
                } else {
                    val resultList = ArrayList<SearchData>()
                    for (row in searchData) {
                        if (row.name.common.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    searchFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = searchFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchFilterList = results?.values as ArrayList<SearchData>
                notifyDataSetChanged()
            }
        }
    }
}