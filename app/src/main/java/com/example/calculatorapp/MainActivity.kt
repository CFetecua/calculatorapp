    package com.example.calculatorapp

    import android.os.Bundle
    import android.widget.Button
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import com.example.calculatorapp.R

    class MainActivity : AppCompatActivity() {

        private lateinit var tvResult: TextView
        private var input = ""
        private var lastResult = ""
        private var lastOperator = ""
        private var expression = ""
        private var justEvaluated = false


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            tvResult = findViewById(R.id.tvResult)

            val root = findViewById<android.widget.GridLayout>(R.id.gridLayout)

            for (i in 0 until root.childCount) {
                val button = root.getChildAt(i) as Button
                button.setOnClickListener {
                    val text = button.text.toString()
                    when (text) {
                        "C" -> clear()
                        "=" -> calculate()
                        "+", "-", "×", "÷" -> operator(text)
                        else -> number(text)
                    }
                }
            }
        }

        private fun number(digit: String) {
            input += digit
            tvResult.text = input
        }

        private fun operator(op: String) {
            if (input.isNotEmpty()) {
                // Si ya hay resultado, continuar con nueva operación
                if (lastResult.isNotEmpty()) {
                    input = lastResult
                    lastResult = ""
                }

                // Evitar doble operador seguido
                if (!input.last().toString().matches(Regex("[+\\-×÷]"))) {
                    input += op
                    tvResult.text = input
                }
            }
        }

        private fun calculate() {
            try {
                val expression = input
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace(Regex("([+\\-*/])"), " $1 ")  // Esto pone espacios entre operadores

                val result = evalSimple(expression.trim())  // Quitamos espacios al principio y final
                tvResult.text = result.toString()
                lastResult = result.toString()
                input = ""
            } catch (e: Exception) {
                tvResult.text = "Error"
                input = ""
            }
        }

        private fun clear() {
            input = ""
            lastResult = ""
            lastOperator = ""
            tvResult.text = "0"
        }

        private fun evalSimple(expression: String): Double {
            val tokens = expression.split(" ")
            var result = tokens[0].toDouble()

            var i = 1
            while (i < tokens.size) {
                val operator = tokens[i]
                val number = tokens[i + 1].toDouble()
                result = when (operator) {
                    "+" -> result + number
                    "-" -> result - number
                    "*" -> result * number
                    "/" -> result / number
                    else -> throw IllegalArgumentException("Operador no válido: $operator")
                }
                i += 2
            }
            return result
        }
    }
