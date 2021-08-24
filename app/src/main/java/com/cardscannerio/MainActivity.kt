package com.cardscannerio

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import io.card.payment.CardIOActivity
import io.card.payment.CardIOActivity.*
import io.card.payment.CreditCard
import java.util.*

class MainActivity : Activity() {

    private val TAG: String = "CardScannerIOApplication"

    val REQUEST_SCAN = 100
    val REQUEST_AUTOTEST = 200

    var version: TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         version = findViewById<View>(R.id.version) as TextView
    }

    fun onScan(pressed: View?) {
        val intent = Intent(this@MainActivity, CardIOActivity::class.java)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
            .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true)
            .putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "en")
            .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
            .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true)
        startActivityForResult(intent, REQUEST_SCAN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            var resultDisplayStr: String
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
                // Never log a raw card number. Avoid displaying it, but if necessary use scanResult.formattedCardNumber can intend of .redactedCardNumber
                resultDisplayStr = "Card Number: " + scanResult?.formattedCardNumber + "\n"
                if (scanResult?.isExpiryValid == true) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n"
                }
                if (scanResult?.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length + " digits.\n"
                }
                if (scanResult?.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n"
                }
            } else {
                resultDisplayStr = "Scan was canceled."
            }
            version?.text  = resultDisplayStr
        }
    }
}