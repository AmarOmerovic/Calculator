package com.amaromerovic.calculator

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.amaromerovic.calculator.databinding.ActivityMainBinding
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var string: String
    private lateinit var snackbar: Snackbar
    private val regex = Regex("\\d+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        string = ""

        binding.clear.setOnClickListener {
            string = ""
            binding.textView.text = string
        }

        binding.dot.setOnClickListener {
            if (string == "-") {
                return@setOnClickListener
            }

            if (string.isEmpty() || string == "Infinity" || string == "NaN") {
                string = ""
                string += "0."
                binding.textView.text = string
            } else if (string == "0") {
                string += "."
                binding.textView.text = string
            } else {
                if (string.contains(" / ") || string.contains(" * ") || string.contains(" + ") || string.contains(
                        " - "
                    )
                ) {
                    val numbers: List<String> = string.split(" / ", " * ", " + ", " - ")
                    if (numbers[numbers.size - 1].contains(".")) {
                        return@setOnClickListener
                    } else {
                        if (numbers[numbers.size - 1].contains(regex)) {
                            string += "."
                            binding.textView.text = string
                        } else {
                            return@setOnClickListener
                        }
                    }

                } else {
                    if (string.contains(".")) {
                        return@setOnClickListener
                    } else {
                        string += "."
                        binding.textView.text = string
                    }
                }

            }
        }



        binding.number0.setOnClickListener {
            if (string.isEmpty()) {
                string += "0"
                binding.textView.text = string
                return@setOnClickListener
            } else if (string == "Infinity" || string == "NaN") {
                string = ""
                processNumberClick('0')
            } else {
                if (string.contains(" / ") || string.contains(" + ") || string.contains(" * ") || string.contains(
                        " - "
                    )
                ) {
                    val numbers: List<String> = string.split(" / ", " * ", " + ", " - ")
                    if (numbers[numbers.size - 1] == "0") {
                        return@setOnClickListener
                    } else {
                        processNumberClick('0')
                    }
                } else if (string.contains(".")) {
                    processNumberClick('0')
                }
            }
        }
        binding.number1.setOnClickListener { processNumberClick('1') }
        binding.number2.setOnClickListener { processNumberClick('2') }
        binding.number3.setOnClickListener { processNumberClick('3') }
        binding.number4.setOnClickListener { processNumberClick('4') }
        binding.number5.setOnClickListener { processNumberClick('5') }
        binding.number6.setOnClickListener { processNumberClick('6') }
        binding.number7.setOnClickListener { processNumberClick('7') }
        binding.number8.setOnClickListener { processNumberClick('8') }
        binding.number9.setOnClickListener { processNumberClick('9') }

        binding.sum.setOnClickListener { processOperatorClick('+') }
        binding.subtract.setOnClickListener {
            if (string.isEmpty() || string == "Infinity" || string == "NaN") {
                string = ""
                string += '-'
                binding.textView.text = string
            } else if (string.contains(regex)) {
                processOperatorClick('-')
            }
        }
        binding.multiply.setOnClickListener { processOperatorClick('*') }
        binding.divide.setOnClickListener { processOperatorClick('/') }

        binding.equals.setOnClickListener {

            if (string.endsWith(" ") || string.isEmpty() || string == "" || string == "-" || string.isDigitsOnly() || (string.contains(
                    regex
                ) && string.contains(".") && !string.contains(" + ") && !string.contains(" - ") && !string.contains(
                    " * "
                ) && !string.contains(" / ")) || (string.contains('-') && string.contains(regex) && !string.contains(
                    " + "
                ) && !string.contains(" - ") && !string.contains(
                    " * "
                ) && !string.contains(" / ") || string == "-.")
            ) {
                string = ""
                binding.textView.text = getString(com.amaromerovic.calculator.R.string.error)
                return@setOnClickListener
            }

            val operatorHelp = string.filter { !it.isWhitespace() && !it.isDigit() && it != '.' }

            val operator = operatorHelp[operatorHelp.length - 1].toString()

            val numbers: List<String> = string.split(" / ", " * ", " + ", " - ")

            for (i in numbers) {
                Log.d("Test123", "Number: $i")
            }

            Log.d("Test123", "Operator $operator")



            when (operator) {
                "+" -> {
                    string =
                        sum(numbers[0].toDouble(), numbers[1].toDouble()).toString()
                }
                "-" -> {
                    string =
                        subtract(numbers[0].toDouble(), numbers[1].toDouble()).toString()
                }
                "/" -> {
                    string =
                        divide(numbers[0].toDouble(), numbers[1].toDouble()).toString()
                }
                "*" -> {
                    string =
                        multiply(numbers[0].toDouble(), numbers[1].toDouble()).toString()
                }

            }
            binding.textView.text = string

        }

    }

    private fun sum(a: Double, b: Double): Double {
        return a + b
    }

    private fun subtract(a: Double, b: Double): Double {
        return a - b
    }

    private fun divide(a: Double, b: Double): Double {
        return a / b
    }

    private fun multiply(a: Double, b: Double): Double {
        return a * b
    }


    private fun processNumberClick(character: Char) {
        if (string.contains(" / ") || string.contains(" + ") || string.contains(" * ") || string.contains(
                " - "
            )
        ) {
            val numbers: List<String> = string.split(" / ", " * ", " + ", " - ")
            if (numbers[numbers.size - 1] != "0" && (string.length < 33  || (string.contains(".") && string.length < 34))) {
                addCharToString(character)
            } else if(numbers[numbers.size - 1] == "0" && !numbers[numbers.size - 1].contains(".")) {
                string = string.dropLast(1)
                addCharToString(character)
            }
            else {
                showSnackbar()
            }


        } else if (string == "0") {
            string = "$character"
            binding.textView.text = string
        } else {
            if (string.length < 15 || (string.contains(".") && string.length < 16)) {
                addCharToString(character)
            } else {
                showSnackbar()
            }
        }
    }


    private fun processOperatorClick(char: Char) {

        if (string.isEmpty() || string == "" || string == "NaN" || string == "Infinity") {
            string = ""
            return
        } else if (string == "-") {
            if (char == '+') {
                string = ""
                binding.textView.text = string
                return
            } else {
                return
            }
        } else if (string.contains(" + ") || string.contains(" - ") || string.contains(" / ") || string.contains(
                " * "
            )
        ) {
            val operatorType = string.filter { !it.isDigit() && it != '.' }

            var toReplace = ""
            for (i in operatorType.indices) {

                if (i == 0 && operatorType[i] == '-') {
                    continue
                } else {
                    toReplace += operatorType[i]
                }

            }

            string = string.replace(toReplace, " $char ")
        } else {
            string += " $char "
        }

        binding.textView.text = string
        binding.scrollView.post {
            binding.scrollView.smoothScrollBy(
                binding.textView.right, binding.textView.left
            )
        }

    }

    private fun showSnackbar() {
        snackbar = Snackbar.make(
            this.binding.root,
            "Can't enter more than 15 digits!",
            1000
        )
            .setBackgroundTint(
                ContextCompat.getColor(
                    this,
                    com.amaromerovic.calculator.R.color.black
                )
            )
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM or Gravity.CENTER
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT
        params.bottomMargin = 60
        val tv = view.findViewById<TextView>(R.id.snackbar_text)
        if (tv != null) {
            tv.gravity = Gravity.CENTER
            tv.textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        }
        view.layoutParams = params
        snackbar.show()
    }

    private fun addCharToString(character: Char) {
        string += character
        binding.textView.text = string

        binding.scrollView.post {
            binding.scrollView.smoothScrollBy(
                binding.textView.right, binding.textView.left
            )
        }
    }
}