package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.semantics.text
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private var currentInput = ""
    private var operand1: Double? = null
    private var operator: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)

        // Set click listeners for number buttons
        val numberButtonIds = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9
        )
        for (id in numberButtonIds) {
            findViewById<Button>(id).setOnClickListener { onNumberClick(it as Button) }
        }

        // Set click listeners for operator buttons
        val operatorButtonIds = listOf(
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide
        )
        for (id in operatorButtonIds) {
            findViewById<Button>(id).setOnClickListener { onOperatorClick(it as Button) }
        }

        // Set click listeners for equals and decimal buttons
        findViewById<Button>(R.id.buttonEquals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.buttonDecimal).setOnClickListener { onDecimalClick() }
    }

    private fun onNumberClick(button: Button) {
        currentInput += button.text
        updateResultTextView()
    }

    private fun onOperatorClick(button: Button) {
        if (currentInput.isNotEmpty()) {
            operand1 = currentInput.toDouble()
            currentInput = ""
        }
        operator = button.text.toString()
        updateResultTextView()
    }

    private fun onEqualsClick() {
        if (operand1 != null && currentInput.isNotEmpty() && operator != null) {
            try {
                val operand2 = currentInput.toDouble()
                val result = performOperation(operand1!!, operand2, operator!!)
                resultTextView.text = result.toString()
                currentInput = result.toString()
                operand1 = null
                operator = null
            } catch (e: NumberFormatException) {
                resultTextView.text = "Error"
                currentInput = ""
                operand1 = null
                operator = null
            }
        }
    }

    private fun onDecimalClick() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            updateResultTextView()
        }
    }

    private fun performOperation(operand1: Double, operand2: Double, operator: String): Double {
        return when (operator) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "*" -> operand1 * operand2
            "/" -> if (operand2 != 0.0) operand1 / operand2 else throw ArithmeticException("Division by zero")
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }

    private fun updateResultTextView() {
        resultTextView.text = currentInput
    }
}
