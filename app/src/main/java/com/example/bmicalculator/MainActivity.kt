package com.example.bmicalculator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var weightText: EditText
    private lateinit var heightText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sf = getSharedPreferences("my_sf", MODE_PRIVATE)
        editor = sf.edit()

        weightText = findViewById<EditText>(R.id.etWeight)
        heightText = findViewById<EditText>(R.id.etHeight)
        val calcButton = findViewById<Button>(R.id.btnCalculate)

        calcButton.setOnClickListener {
            val weight = weightText.text.toString()
            val height = heightText.text.toString()
            if(validateInput(weight,height)){
                val bmi = weight.toFloat()/((height.toFloat()/100)*(height.toFloat()/100) )

                displayResult(bmi)
            }
        }
    }

    private fun validateInput(weight:String?, height:String?):Boolean{
        return when {
            weight.isNullOrEmpty() -> {
                Toast.makeText(this, "Weight is empty!", Toast.LENGTH_SHORT)
                return false
            }
            height.isNullOrEmpty() -> {
                Toast.makeText(this, "Weight is empty!", Toast.LENGTH_SHORT)
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun displayResult(bmi:Float){
        val resultIndex = findViewById<TextView>(R.id.tvIndex)
        val resultDescription = findViewById<TextView>(R.id.tvResult)
        val info = findViewById<TextView>(R.id.tvInfo)

        resultIndex.text = String.format("%.2f", bmi)
        info.text = "(Normal range is 18.5 - 24.9)"

        var resultText=""
        var color=0

        when {
            bmi<18.5 ->{
                resultText="Underweight"
                color=R.color.under_weight
            }
            bmi in 18.5..24.99 ->{
                resultText="Healthy"
                color=R.color.normal
            }
            bmi in 25.0..29.99 ->{
                resultText="Overweight"
                color=R.color.over_weight
            }
            bmi>29.99 ->{
                resultText="Obese"
                color=R.color.obese
            }
        }
        resultDescription.setTextColor(ContextCompat.getColor(this, color))
        resultDescription.text = resultText

    }

    override fun onPause() {
        super.onPause()
        val weight = weightText.text.toString().toInt()
        val height = heightText.text.toString().toInt()

        editor.apply {
            putInt("sf_weight", weight)
            putInt("sf_height", height)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val weight = sf.getInt("sf_weight", 0)
        val height = sf.getInt("sf_height", 0)


        if(weight!=0 && height!=0){
            weightText.setText(weight.toString())
            heightText.setText(height.toString())
        }
    }

}