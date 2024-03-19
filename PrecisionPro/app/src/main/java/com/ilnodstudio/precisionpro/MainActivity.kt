package com.ilnodstudio.precisionpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.ilnodstudio.precisionpro.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        binding.apply {

        binding.buttonClear.setOnClickListener {
            input.text = ""
            output.text = ""
        }

        buttonBracket.setOnClickListener {

            input.text = addToInputText("(")

        }
        buttonBracketR.setOnClickListener {

            input.text = addToInputText(")")

        }

        buttonCroxx.setOnClickListener {
            val removedLast = input.text.toString().dropLast(1)
            input.text = removedLast
        }

        button0.setOnClickListener {
            input.text = addToInputText("0")
        }
        button1.setOnClickListener {
            input.text = addToInputText("1")
        }
            button2.setOnClickListener {
            input.text = addToInputText("2")
        }
            button3.setOnClickListener {
            input.text = addToInputText("3")
        }
            button4.setOnClickListener {
            input.text = addToInputText("4")
        }
            button5.setOnClickListener {
            input.text = addToInputText("5")
        }
            button6.setOnClickListener {
            input.text = addToInputText("6")
        }
            button7.setOnClickListener {
            input.text = addToInputText("7")
        }
            button8.setOnClickListener {
            input.text = addToInputText("8")
        }
            button9.setOnClickListener {
            input.text = addToInputText("9")
        }
            buttonDot.setOnClickListener {
            input.text = addToInputText(".")
        }
            buttonDivision.setOnClickListener {
            input.text = addToInputText("÷") // ALT + 0247
        }
            buttonMultiply.setOnClickListener {
            input.text = addToInputText("×") // ALT + 0215
        }

            buttonSubtraction.setOnClickListener {
            input.text = addToInputText("-")
        }
            buttonAddition.setOnClickListener {
            input.text = addToInputText("+")
        }

            buttonEquals.setOnClickListener {
            showResult()
        }
        }
    }

    private fun addToInputText(buttonValue: String): String {

        return binding.input.text.toString() + "" + buttonValue
    }

    private fun getInputExpression(): String {
        var expression = binding.input.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        return expression
    }

    private fun showResult() {
        try {
            val expression = getInputExpression()
            val result = Expression(expression).calculate()
            if (result.isNaN()) {
                // Show Error Message
                binding.output.text = ""
                binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
            } else {
                // Show Result
                binding.output.text = DecimalFormat("0.######").format(result).toString()
                binding.output.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
        } catch (e: Exception) {
            // Show Error Message
            binding.output.text = ""
            binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }
}