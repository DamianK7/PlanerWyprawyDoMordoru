package com.example.planerwyprawy

import android.content.Context
import android.media.Rating
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Chronometer
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
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
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        val sciezkiSwitch = findViewById<Switch>(R.id.siezkiSwitch)

        val checkPlaszcz = findViewById<CheckBox>(R.id.checkPlaszcz)
        val checkLembas = findViewById<CheckBox>(R.id.checkLembas)
        val checkPochodnia = findViewById<CheckBox>(R.id.checkPochodnia)

        val priorytet = findViewById<RadioGroup>(R.id.priorytet)

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val wartoscSuwak = findViewById<TextView>(R.id.wartoscSuwaka)
        var aktProgress = 0

        val chronometer = findViewById<Chronometer>(R.id.myChronometr)
        val startBtn = findViewById<Button>(R.id.startButton)
        val stopBtn = findViewById<Button>(R.id.stopButton)
        val resetBtn = findViewById<Button>(R.id.resetButton)
        var running = false
        var pauseOffset: Long = 0

        val ratingBar = findViewById<RatingBar>(R.id.ocenaRB)
        val ratingValue = findViewById<TextView>(R.id.ocenaTextView)
        var aktOcena: Double = 0.0

        val podsumowanie = findViewById<TextView>(R.id.podsumowanie)
        val podsumowanieButton = findViewById<Button>(R.id.podsumowanieButton)




        podsumowanieButton.setOnClickListener {

            val imie = imieBohatera.text.toString()

            val wybranaRasa = spinnerRasa.selectedItem.toString()

            val dzien = dataPicker.dayOfMonth
            val miesiac = dataPicker.month + 1
            val rok = dataPicker.year

            val godzina = timePicker.hour
            val minuta = timePicker.minute

            val dataTekst = "$dzien.$miesiac.$rok"


            val sciezkiText = if (sciezkiSwitch.isChecked) {
                "Włączono tajne ścieżki elfów"
            } else {
                "Wyłączono tajne ścieżki elfów"
            }


            val wyposazenie = mutableListOf<String>()
            if(checkPlaszcz.isChecked) wyposazenie.add("Płaszcz elfów")
            if(checkLembas.isChecked) wyposazenie.add("Lembas")
            if(checkPochodnia.isChecked) wyposazenie.add("Pochodnia")

            val wypText = if (wyposazenie.isNotEmpty()) wyposazenie.joinToString(", ") else "Brak dodatkowego wyposażenia"


            val prio = priorytet.checkedRadioButtonId
            val priomarszu = when(prio){
                R.id.prioukryty -> "Ukryty"
                R.id.priozbalansowany -> "Zbalansowany"
                R.id.prioforsowny -> "Forsowny"
                else -> "Nie wybrano priorytetu marszu"
            }


            podsumowanie.text = "Bohater: $imie \n Rasa: $wybranaRasa \n Data: $dataTekst \n Godzina: $godzina:$minuta \n $sciezkiText \n Wyposażenie: $wypText \n Priorytet marszu: $priomarszu \n Czas marszu: $aktProgress min \n Ocena morali: $aktOcena /5"
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                aktProgress = progress
                wartoscSuwak.text = "Czas marszu: $progress min "
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })



        startBtn.setOnClickListener {

            if (!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
            }
        }

        stopBtn.setOnClickListener {
            if (running) {
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                Log.i("offset", "pauseOffset: $pauseOffset")
                chronometer.stop()
                running = false
            }
        }

        resetBtn.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            Log.i("offset", "base: ${chronometer.base}")
            pauseOffset = 0
            chronometer.stop()
            running = false
        }



        val sharedPreferences = getSharedPreferences("RatingPrefs", Context.MODE_PRIVATE)


        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            aktOcena = rating.toDouble()
            ratingValue.text = "Oceń morale: $rating"

            val editor = sharedPreferences.edit()
            editor.putFloat("rating", rating)
            editor.apply()

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