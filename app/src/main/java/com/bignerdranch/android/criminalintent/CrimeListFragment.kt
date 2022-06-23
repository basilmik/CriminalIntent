package com.bignerdranch.android.criminalintent

import android.icu.text.DateFormat.getDateInstance
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat.SHORT
import java.text.DateFormat.getDateInstance


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

    private inner class CrimeHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener
    {
        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init
        {
        itemView.setOnClickListener(this)
        }


        fun bind(crime: Crime)
        {
            this.crime = crime
            titleTextView.text = this.crime.title
            //dateTextView.text = this.crime.date.toString()
            dateTextView.text = DateFormat.format("EEEE, MMMM dd, yyyy.",this.crime.date)
            solvedImageView.visibility = if (crime.isSolved)
            {
                View.VISIBLE
            }
            else
            {
                View.GONE
            }
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
            val view = layoutInflater.inflate(R.layout.list_item_crime,parent,false)
            return CrimeHolder(view)
        }

        // количество элементов в наборе данных
        override fun getItemCount() = crimes.size


        //  заполняет данный холдер holder значениями из [преступления] данной позиции position
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

    }


    companion object {
        // will be called by activities to get a fragment
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }

    }

}