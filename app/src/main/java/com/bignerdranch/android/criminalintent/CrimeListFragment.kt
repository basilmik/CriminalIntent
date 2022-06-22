package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel:CrimeListViewModel by lazy()
    {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI()
    {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener
    {
        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        init
        {
        itemView.setOnClickListener(this)
        }


        fun bind(crime: Crime)
        {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View)
        {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()

        }

    }


    private inner class CrimeAdapter(var crimes: List<Crime>): RecyclerView.Adapter<CrimeHolder>()
    {
        // отвечает за содвание рпедставления н адисплее, оборачивает его в холдер и возвращает результат
        // наполняет list_item_view.xml и передает полученное представление
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            // добавить логику, к возращает различные ViewHolder в зависимости от ViewType
            // возвращаемого функцией getItemViewType(Int)

            val isPoliceRequired = getItemViewType(viewType)
            return if (isPoliceRequired == 1) {
                val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                CrimeHolder(view)
            } else {
                val view = layoutInflater.inflate(R.layout.list_item_crime_police, parent, false)
                CrimeHolder(view)
            }
        }

        // количество элементов в наборе данных
        override fun getItemCount() = crimes.size


        //  заполняет данный холдер holder значениями из [преступления] данной позиции position
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemViewType(position: Int): Int {
            //return super.getItemViewType(position)
            super.getItemViewType(position)
            return crimes[position].requiresPolice

        }

    }


    companion object {
        // will be called by activities to get a fragment
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }

    }

}