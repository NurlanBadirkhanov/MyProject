package com.richmedia.mathgenius

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {


    private lateinit var resultTextView: TextView
    private lateinit var prevTextView: TextView
    private var prevNumber: String = ""
    private var currNumber: String = "0"
    private var operator: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Global
        resultTextView = findViewById(R.id.resultTextView)
        prevTextView = findViewById(R.id.prevTextView)

        // Numbers
        val btn0 = findViewById<Button>(R.id.button0)
        val btn1 = findViewById<Button>(R.id.button1)
        val btn2 = findViewById<Button>(R.id.button2)
        val btn3 = findViewById<Button>(R.id.button3)
        val btn4 = findViewById<Button>(R.id.button4)
        val btn5 = findViewById<Button>(R.id.button5)
        val btn6 = findViewById<Button>(R.id.button6)
        val btn7 = findViewById<Button>(R.id.button7)
        val btn8 = findViewById<Button>(R.id.button8)
        val btn9 = findViewById<Button>(R.id.button9)
        val btnDot = findViewById<Button>(R.id.buttonDot)

        val buttons = arrayListOf<Button>(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8,btn9, btnDot)
        buttons.forEach { button ->
            button.setOnClickListener{
                val value = button.text
                numberClick(value)
            }
        }

        // Functions
        val clearBtn = findViewById<Button>(R.id.buttonC)
        val rootBtn = findViewById<Button>(R.id.buttonRoot)
        val squareBtn = findViewById<Button>(R.id.buttonSquare)
        val divideBtn = findViewById<Button>(R.id.buttonDivide)
        val multiplyBtn = findViewById<Button>(R.id.buttonMultiply)
        val minusBtn = findViewById<Button>(R.id.buttonMinus)
        val plusBtn = findViewById<Button>(R.id.buttonPlus)
        val equalsBtn = findViewById<Button>(R.id.buttonEquals)
        val plusMinusBtn = findViewById<Button>(R.id.buttonPlusMinus)

        val operatorButtons = arrayListOf<Button>(rootBtn, squareBtn, divideBtn, multiplyBtn, minusBtn, plusBtn)
        operatorButtons.forEach { button ->
            button.setOnClickListener{
                val buttonOperator = button.text.toString()
                operatorClick(buttonOperator)
            }
        }

        clearBtn.setOnClickListener { clearNumbers() }
        equalsBtn.setOnClickListener { calculate() }
        squareBtn.setOnClickListener { square() }
        rootBtn.setOnClickListener { root() }
        plusMinusBtn.setOnClickListener { convertSign() }

    }
    private fun numberClick(value: CharSequence){
        if (value == "." && currNumber.contains(".")) return
        else if ((currNumber == "0" && value != ".")) {
            currNumber = value.toString()
        }else{
            currNumber += value.toString()
        }
        updateTextView(currNumber, prevNumber)
    }

    private fun operatorClick(op: String){
        if(currNumber.isNotEmpty()){
            operator = op
            prevNumber = currNumber
            updateTextView(currNumber, prevNumber)
            currNumber = ""
        }
    }

    private fun calculate(){
        if(prevNumber.isNotEmpty() || currNumber.isNotEmpty()){
            val num1 = if(prevNumber.isNotEmpty()) prevNumber.toDouble() else currNumber.toDouble()
            val num2 = if(currNumber.isNotEmpty()) currNumber.toDouble() else prevNumber.toDouble()

            var result = 0.0

            when(operator){
                "+" -> result = num1 + num2
                "-" -> result = num1 - num2
                "x" -> result = num1 * num2
                "/" -> {
                    if (num2 != 0.0) result = num1 / num2 else {
                        clearNumbers()
                        prevTextView.text = "Division by zero"
                        resultTextView.text = "Error"
                        return
                    }
                }
                else -> return
            }

            prevNumber = num1.toString()
            currNumber = result.toString()
            updateTextView(currNumber, prevNumber)
        }
    }

    private fun square(){
        if(currNumber.isNotEmpty()){
            val num = currNumber.toDouble()
            val result = num * num
            prevNumber = num.toString()
            currNumber = result.toString()
            operator = "**2"
            updateTextView(currNumber, prevNumber)

        }
    }

    private fun root(){
        if(currNumber.isNotEmpty()){
            val num = currNumber.toDouble()
            val result = sqrt(num)
            prevNumber = num.toString()
            currNumber = result.toString()
            operator = "√"
            updateTextView(currNumber, prevNumber)
        }
    }

    private fun clearNumbers(){
        currNumber = "0"
        prevNumber=""
        operator=""
        updateTextView("0", "")
    }

    private fun convertSign(){
        if(currNumber.isNotEmpty()){
            val num = currNumber.toDouble()
            currNumber = if(num != 0.0) (-num).toString() else currNumber
            updateTextView(currNumber, prevNumber)
        }
    }

    private fun updateTextView(value: String, prevValue: String){
        var formattedValue = if (value.isNotEmpty() && value.toDoubleOrNull()?.rem(1) == 0.0) value.toDouble().toInt().toString() else value
        var formattedPrevValue = if(prevValue.isNotEmpty() && prevValue.toDoubleOrNull()?.rem(1) == 0.0) prevValue.toDouble().toInt().toString() else prevValue

        val maxLength = 11
        if((formattedValue.length > maxLength && prevValue.length > maxLength)){
            formattedValue = formattedValue.substring(0, maxLength)
            formattedPrevValue = formattedPrevValue.substring(0, maxLength)
            resultTextView.text = formattedValue
            prevTextView.text = "$formattedPrevValue$operator"
            return
        }
        resultTextView.text = formattedValue
        prevTextView.text = if(formattedPrevValue.isNotEmpty()) {
            if(operator == "√") "$operator$formattedPrevValue" else "$formattedPrevValue$operator"
        } else ""
    }

}