package org.o7planning.spookycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var input = ""
    var inputValue = 0.0
    var currentString = ""
    var current = 0.0
    var mathToDo = ""
    var history = mutableListOf<Double>(0.0)
    var typing = false
    var afterDecimal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun doMath(): Double {
        return when (mathToDo) {
            "+" -> current + inputValue
            "-" -> current - inputValue
            "x" -> current * inputValue
            "/" -> current / inputValue
            else -> inputValue
        }
    }

    fun keyPress(view: View) {
        val button = view as Button
        when (val key = button.text.toString()) {
            "C" -> clear()
            "=" -> equalsKey()
            "+", "-", "x", "/" -> mathKeyPress(key)
            //Everything else calling this function should be a number key.
            else -> numberKeyPress(key)
        }
    }

    private fun clear() {
        if (input == "") {
            typing = false
            inputValue = 0.0
            //current = 0.0
            input = ""
            currentString = ""
        }
        else {
            typing = false
            inputValue = 0.0
            input = ""
        }
        update()
    }

    private fun equalsKey() {
        typing = false
        inputValue = if (input == "") {
            0.0
        } else {
            input.toDouble()
        }
        val result = doMath()
        currentString += "$input ="
        mathToDo = "="
        current = result
        history.add(result)
        update()
        currentString = result.toString()
    }

    private fun numberKeyPress(num: String) {
        if (!typing) {
            inputValue = 0.0
            input = ""
            startTyping()
        }
        if (num == "." && afterDecimal) {
            return
        }
        input += num
        update()
    }

    private fun mathKeyPress(math: String) {
        if (!typing) {
            input = current.toString()
            currentString = ""
        }
        typing = false
        inputValue = input.toDouble()
        currentString += "$input $math "
        current = doMath()
        mathToDo = math
        update()
    }

    private fun startTyping() {
        if (mathToDo == "=")
            currentString = ""
        typing = true
        afterDecimal = false
        update()
    }

    private fun update() {
        val last = history.last().toString()
        if (typing) {
            findViewById<TextView>(R.id.last).text = "Previous: $last"
            findViewById<TextView>(R.id.input).text = input
        }
        else {
            findViewById<TextView>(R.id.input).text = current.toString()
        }
        findViewById<TextView>(R.id.current).text = currentString
        if (history.size > 10) {
            history.removeAt(0)
        }
    }
}