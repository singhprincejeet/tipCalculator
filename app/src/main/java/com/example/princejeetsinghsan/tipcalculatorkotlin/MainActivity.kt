package com.example.princejeetsinghsan.tipcalculatorkotlin

import android.app.Dialog
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        tipPercentageET.setText(prefs.getString("tip_percentage", ""))
        suggestTipButton.onClick {
            val dialog:Dialog = Dialog(this)
            dialog.setContentView(R.layout.suggest_a_tip_dialog)
            val rb:RatingBar= dialog.findViewById(R.id.ratingBar) as RatingBar
            val suggestedTip: TextView = dialog.findViewById(R.id.suggestedTipValue) as TextView
            rb.onRatingBarChange { bar, fl, b ->
                suggestedTip.text = (rb.rating*2+10).toString()
            }
            val nb:Button = dialog.findViewById(R.id.negativeButton) as Button
            nb.onClick {
                dialog.dismiss()
            }
            val pb:Button = dialog.findViewById(R.id.positiveButton) as Button
            pb.onClick {
                tipPercentageET.setText(suggestedTip.text)
                dialog.dismiss()
            }
            dialog.show()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun OnCalculateButtonClick(view: View){
        var tipAmount:Double = -1.0
        try {
            tipAmount = calculateTip(billAmountET.textValue(),tipPercentageET.textValue())
        } catch(e: Exception) {
            toast(e.message.toString())
        }
        if ( tipAmount != -1.0) {
            var tipPerPerson: Double = -1.0
            try {
                tipPerPerson = calculateTipPerPerson(tipAmount, noOfPeopleET.textValue())
            } catch(e: Exception) {
                toast(e.message.toString())
            }
            if (tipPerPerson != -1.0) {
                val billPerPerson: Double = calculateBillPerPerson(billAmountET.textValue(), tipAmount, noOfPeopleET.textValue())
                startActivity<ResultActivity>("billAmount" to billAmountET.textValue(),
                        "tipAmount" to tipAmount.toString(),
                        "tipPerPerson" to tipPerPerson.toString(),
                        "billPerPerson" to billPerPerson.toString())
            }
        }
    }

    private fun calculateBillPerPerson(billAmount: String, tipAmount: Double, noOfPeople: String): Double {
        if (noOfPeople == ""){
            throw IllegalArgumentException("Please enter no of people")
        }

        if (noOfPeople.toInt() < 1){
            throw IllegalArgumentException("Please enter no of people greater than 1")
        }

        return ("%.2f".format((billAmount.toDouble())/noOfPeople.toInt())).toDouble()
    }

    private fun calculateTipPerPerson(tipAmount: Double, noOfPeople: String): Double {
        if (noOfPeople == ""){
            return ("%.2f".format(tipAmount)).toDouble()
        }

        if (noOfPeople.toInt() < 1){
            throw IllegalArgumentException("Please enter no of people greater than 1")
        }

        return ("%.2f".format(tipAmount/noOfPeople.toInt())).toDouble()
    }

    private fun calculateTip(billAmount: String, tipPercentage: String): Double {
        if (billAmount.equals(""))
            throw IllegalArgumentException("Please enter a bill amount")
        else if (tipPercentage.equals(""))
            return ("%.2f".format(billAmount.toDouble()*PreferenceManager.getDefaultSharedPreferences(this).getString("tip_percentage", "").toDouble()*0.01)).toDouble()
        return ("%.2f".format(billAmount.toDouble()*tipPercentage.toDouble()*0.01)).toDouble()
    }

    fun EditText.textValue() = text.toString()


}
