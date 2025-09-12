package com.example.planerwyprawy

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val imieBohatera = findViewById<EditText>(R.id.imieBohatera)

        val spinnerRasa = findViewById<Spinner>(R.id.spinnerlist)
        val imageBohater = findViewById<ImageView>(R.id.imageBohater)
        val rasa = arrayOf("hobbit", "człowiek", "elf", "krasnolud", "czarodziej")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, rasa)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRasa.adapter = adapter


        val dataPicker = findViewById<DatePicker>(R.id.datePicker)


        val sciezkiSwitch = findViewById<Switch>(R.id.siezkiSwitch)

        val checkPlaszcz = findViewById<CheckBox>(R.id.checkPlaszcz)
        val checkLembas = findViewById<CheckBox>(R.id.checkLembas)
        val checkPochodnia = findViewById<CheckBox>(R.id.checkPochodnia)

        val podsumowanie = findViewById<TextView>(R.id.podsumowanie)
        val podsumowanieButton = findViewById<Button>(R.id.podsumowanieButton)




        podsumowanieButton.setOnClickListener {

            val imie = imieBohatera.text.toString()

            val wybranaRasa = spinnerRasa.selectedItem.toString()

            val dzien = dataPicker.dayOfMonth
            val miesiac = dataPicker.month + 1
            val rok = dataPicker.year

            val dataTekst = "$dzien.$miesiac.$rok"
            podsumowanie.text = "Bohater: $imie \n Rasa: $wybranaRasa \n Data: $dataTekst"
        }


        spinnerRasa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (rasa[position]) {
                    "hobbit" -> imageBohater.setImageResource(R.drawable.pierscien)
                    "człowiek" -> imageBohater.setImageResource(R.drawable.miecz)
                    "elf" -> imageBohater.setImageResource(R.drawable.luk)
                    "krasnolud" -> imageBohater.setImageResource(R.drawable.miecz)
                    "czarodziej" -> imageBohater.setImageResource(R.drawable.pierscien)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }



    }
}