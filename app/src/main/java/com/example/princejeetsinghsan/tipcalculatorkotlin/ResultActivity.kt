package com.example.princejeetsinghsan.tipcalculatorkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val billAmount = intent.extras.getString("billAmount")
        val tipAmount = intent.extras.getString("tipAmount")
        val eachPersonPaysTip = intent.extras.getString("tipPerPerson")
        val eachPersonPaysBill = intent.extras.getString("billPerPerson")
        billAmountValue.setText(billAmount)
        tipAmountValue.setText(tipAmount)
        totalAmountValue.setText((billAmount.toDouble() + tipAmount.toDouble()).toString())
        if (eachPersonPaysTip.toDouble() >= 0.0 && (eachPersonPaysBill.toDouble()+eachPersonPaysTip.toDouble()) != totalAmountValue.text.toString().toDouble()) {
            tipPerPersonValue.text = eachPersonPaysTip
            billPerPersonValue.text = eachPersonPaysBill
            totalPerPersonValue.text = (eachPersonPaysBill.toDouble()+eachPersonPaysTip.toDouble()).toString()
        }
        else{
            tipPerPersonDescription.text = ""
            billPerPersonDescription.text = ""
            totalPerPersonDescription.text = ""
        }
    }
}
