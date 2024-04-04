package com.guilherme.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.guilherme.myapplication.adapter.PillAdapter
import com.guilherme.myapplication.databinding.ActivityMainBinding
import com.guilherme.myapplication.model.Pill

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var pillAdapter: PillAdapter? = null
    private val pillList = ArrayList<Pill>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val recyclerViewPill = binding!!.RecyclerViewPill
        recyclerViewPill.layoutManager = LinearLayoutManager(this)
        recyclerViewPill.setHasFixedSize(true)
        pillAdapter = PillAdapter(pillList, this)
        recyclerViewPill.adapter = pillAdapter
        pill
    }

    private val pill: Unit
        private get() {
            val pill1 = Pill(
                R.drawable.remedio1,
                "remedio 1",
                "40 remedios",
                "10 dias"
            )
            pillList.add(pill1)
            val pill2 = Pill(
                R.drawable.remedio2,
                "remedio 2",
                "50 remedios",
                "78 dias"
            )
            pillList.add(pill2)
            val pill3 = Pill(
                R.drawable.remedio3,
                "remedio 3",
                "80 remedios",
                "24 dias"
            )
            pillList.add(pill3)
            val pill4 = Pill(
                R.drawable.remedio4,
                "remedio 4",
                "90 remedios",
                "80 dias"
            )
            pillList.add(pill4)
            val pill5 = Pill(
                R.drawable.remedio5,
                "remedio 5",
                "15 remedios",
                "17 dias"
            )
            pillList.add(pill5)
            val pill6 = Pill(
                R.drawable.remedio1,
                "remedio 6",
                "100 remedios",
                "14 dias"
            )
            pillList.add(pill6)
        }
}