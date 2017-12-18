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
import android.graphics.Color
import android.os.Handler
import android.os.Looper
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

        //  Fetching new Coin data and updating it every 5 minutes
        initiateSlide()
        fetchCoinData()
    }


    private var currentIndex = 0;

    private var coinData = JSONArray()

    //  Function to fetch new coin data
    private fun fetchCoinData(){

        val url = "https://api.coinmarketcap.com/v1/ticker/?limit=10 "

        //  Initiating request queue here
        val requestQueue = Volley.newRequestQueue(this@MainActivity)

        val request = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    Toast.makeText(this, "Data came through!", Toast.LENGTH_SHORT).show()

                    //  Creating a empty JSONArray to switch with the current coinData JSONArray with new data
                    val newCoinData = JSONArray()

                    for(i in 0..(response.length() - 1)){
                        val coinObject =  response.getJSONObject(i)

                        //  Creating another JSON Object to get only those keys which are usefull for the app
                        val coin = JSONObject()
                        coin.put("name", coinObject.optString("name").toString())
                        coin.put( "symbol",coinObject.optString("symbol").toString())
                        coin.put( "price_usd",coinObject.optString("price_usd").toString())
                        coin.put( "percent_change_24h",coinObject.optString("percent_change_24h").toString())

                        //  Adding to the JSONArray
                        newCoinData.put(coin)
                    }

                    //  Giving the new data to the main Object
                    coinData = newCoinData
                },
                Response.ErrorListener {
                    e ->
                    Toast.makeText(this, "That didn't work!", Toast.LENGTH_SHORT).show()
                    android.util.Log.e("tag", "That didn't work!, $e")
                })

        requestQueue.add(request)
        requestQueue.start()

    }


    //        Interval function to iterate over coinData
    private fun initiateSlide(){

            //  This looks if this is the first time since app has started or not, prevent multiple timers
            if(currentIndex > 0) currentIndex = 0
            else{
                val changeCoinTimer = Timer()

                changeCoinTimer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        //  changes coin iterating through index
                        changeCoin (currentIndex)
                    }
                }, 0, 3000)
            }

    }

    //  Temp function to just show bitcoin price
    fun showCoin(coinObject: JSONObject){
        val coinName = coinObject.optString("name").toString()
        val coinSymbol = coinObject.optString("symbol").toString()
        val price = coinObject.optString("price_usd").toString()
        val priceChange = coinObject.optString("percent_change_24h").toString()
        val profitInLast24 = coinObject.optDouble("percent_change_24h").toDouble() > 0

        val handler = Handler(Looper.getMainLooper()) // write in onCreate function

        //below piece of code is written in function of class that extends from AsyncTask
        handler.post(Runnable {
            CoinLabel.text = "$coinName ($coinSymbol)   $priceChange"
            PriceView.text = "$" + price

            //  To give color on the basis of price change in last 24 hours
            if(!!profitInLast24){
                PriceView.setTextColor(Color.GREEN)
            }else{
                PriceView.setTextColor(Color.RED)
            }
        })

    }

    // class Coin
    data class Coin(
            val coinName: String,
            val coinSymbol: String,
            val price: String,
            val priceChange: String
    )

    //  Function that will change the text on the views with the next coin in the queue
    fun changeCoin (index: Int){

        if(coinData.length() > 0) {
            val coinObject = coinData.getJSONObject(index)

            showCoin(coinObject)

            if(currentIndex < coinData.length() - 1) currentIndex++
            else currentIndex = 0

        }
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
