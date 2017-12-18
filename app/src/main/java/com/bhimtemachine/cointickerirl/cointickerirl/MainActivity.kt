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

        val coinData = kotlin.collections.ArrayList()

        //  Fetching new Coin data and updating it every 5 minutes
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                fetchCoinData()
            }
        }, 0, 300000)


//        Interval function to iterate over coinData
//        Timer().scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                changeCoin ()
//            }
//        }, 0, 3000)

    }


    //  Function to fetch new coin data
    private fun fetchCoinData{
        val requestQueue = Volley.newRequestQueue(this@MainActivity)

        val request = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    Toast.makeText(this, "Data came through!", Toast.LENGTH_SHORT).show()
//                    val newCoinData = kotlin.collections.ArrayList()

                    for(i in 0..(response.length() - 1)){
                        val coinObject =  response.getJSONObject(i)

                        val coinName = coinObject.optString("name").toString();
                        val coinSymbol = coinObject.optString("symbol").toString();
                        val price = coinObject.optString("price_usd").toString();
                        val priceChange = coinObject.optString("percent_change_24h").toString();

//                        newCoinData.add(coin("7.2", "San Francisco", "Feb 2, 2016"))

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
    }

    //  Coin Class
    fun coin (coinName: String, coinSymbol: String, price: String, priceChange: String){
    }


    //  Function that will change the text on the views with the next coin in the queue
    fun changeCoin (){
        android.util.Log.i("tag", "Coin change, mama")
    }

    //  Redirecting to the EditFormActivity
    fun openCoinList( view: View ) {
        val i = Intent(this, EditFormActivity::class.java)
        startActivity(i)
    }

    //  Function that will show the previous coin in the queue
    fun showPrevCoin( view: View ) {
        Toast.makeText(this, "This will show the Previous Coin", Toast.LENGTH_SHORT).show()
    }


    //  Function that will show the next coin in the queue
    fun showNextCoin( view: View ) {
        Toast.makeText(this, "This will show the Next Coin", Toast.LENGTH_SHORT).show()
    }
}
