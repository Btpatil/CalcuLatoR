package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.text.Annotation

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun allClear(view: android.view.View) {
        workings.text = ""
        results.text = ""
    }
    fun backspaceAction(view: android.view.View) {
        val length = workings.length()
        if (length > 0)
            workings.text = workings.text.subSequence(0,length-1)
    }
    fun equalAction(view: android.view.View) {
        results.text = calclateResult()
    }

    private fun calclateResult(): String {
        val digitsOperators = digitsOperator()
        if (digitsOperators.isEmpty())
            return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty())
            return ""

        val result = addSubCalc(timesDivision)
        return result.toString()
    }

    private fun addSubCalc(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDig = passedList[i + 1] as Float
                if (operator == "+")
                {
                    result += nextDig
                }
                if (operator == "-")
                {
                    result -= nextDig
                }
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('X') || list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevdig = passedList[i - 1] as Float
                val nextdig = passedList[i + 1] as Float
                when(operator)
                {
                    'X' ->
                    {
                        newList.add(prevdig * nextdig)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevdig / nextdig)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevdig)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex)
            {
                newList.add(passedList[i])
            }
        }
        return newList
    }

    private fun digitsOperator(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workings.text){
            if (character.isDigit() || character == '.'){
                currentDigit += character
            }
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != ""){
            list.add(currentDigit.toFloat())
        }
        return list
    }
        fun numAction(view: android.view.View)
    {
        if (view is Button)
        {
            if (view.text == ".")
            {
                if (canAddOperation)
                    workings.append(view.text)
                canAddDecimal = false
            }
            else
                workings.append(view.text)
            canAddOperation = true
        }
    }

    fun operatorAction(view: android.view.View) {
        if (view is Button && canAddOperation)
            workings.append(view.text)
        canAddOperation = false
        canAddDecimal = true
    }

}