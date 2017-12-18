package com.bhimtemachine.cointickerirl.cointickerirl

import android.app.Activity
import android.os.Bundle

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.NetworkImageView


import android.widget.Button
import android.widget.Toast
import com.android.volley.toolbox.Volley

import android.content.Intent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


import org.json.JSONObject;

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://api.coinmarketcap.com/v1/ticker/?limit=4"

        val requestQueue = Volley.newRequestQueue(this@MainActivity)

        try{
            val request = JsonArrayRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        Toast.makeText(this, "Data came through!", Toast.LENGTH_SHORT).show()

                        for(i in 0..(response.length() - 1)){
                            val coin =  response.getJSONObject(i)

                            val coinName = coin.optString("name").toString();
                            val coinSymbol = coin.optString("symbol").toString();
                            val price = coin.optString("price_usd").toString();
                            val priceChange = coin.optString("percent_change_24h").toString();
                            android.util.Log.i("tag", "Coin Data:   $coinName ($coinSymbol): $price [$priceChange]")
                        }

                    },
                    Response.ErrorListener {
                        e ->
                        Toast.makeText(this, "That didn't work!", Toast.LENGTH_SHORT).show()
                        android.util.Log.e("tag", "That didn't work!, $e")
                    })

            requestQueue.add(request)
            requestQueue.start()
        }catch(e: Exception){
            e.printStackTrace()
        }

        //  Interval function to switch between coins
//        Timer().scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                changeCoin ()
//            }
//        }, 0, 3000)

    }

    fun changeCoin (){
        android.util.Log.i("tag", "Coin change, mama")
    }

    fun openCoinList( view: View ) {
        val i = Intent(this, EditFormActivity::class.java)
        startActivity(i)
    }

    fun showPrevCoin( view: View ) {
        Toast.makeText(this, "This will show the Previous Coin", Toast.LENGTH_SHORT).show()
    }
    fun showNextCoin( view: View ) {
        Toast.makeText(this, "This will show the Next Coin", Toast.LENGTH_SHORT).show()
    }
}
