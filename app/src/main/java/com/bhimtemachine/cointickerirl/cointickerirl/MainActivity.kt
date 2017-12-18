package com.bhimtemachine.cointickerirl.cointickerirl


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.bhimtemachine.cointickerirl.model.CoinDataResponse
import com.bhimtemachine.cointickerirl.network.CoinMarketAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.ArrayList


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


    private var currentIndex = 0

    private var coinData: ArrayList<CoinDataResponse>? = ArrayList()

    //  Function to fetch new coin data
    private fun fetchCoinData() {
        val coinMarketApi = CoinMarketAPI.retrofit.create(CoinMarketAPI::class.java)
        val coinMarketCall = coinMarketApi.coinData()
        coinMarketCall.enqueue(object : Callback<ArrayList<CoinDataResponse>> {
            override fun onResponse(call: Call<ArrayList<CoinDataResponse>>?, response: retrofit2.Response<ArrayList<CoinDataResponse>>?) {
                if(response != null && response.isSuccessful) {
                    coinData = response.body()
                }
            }
            override fun onFailure(call: Call<ArrayList<CoinDataResponse>>?, t: Throwable?) {
                // What if it couldn't fetch data?
            }
        })
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

    @SuppressLint("SetTextI18n")
    //  Temp function to just show bitcoin price
    private fun showCoin(coinObject: CoinDataResponse){
        val profitInLast24 = coinObject.percentChangeDay.toDouble()

        val handler = Handler(Looper.getMainLooper()) // write in onCreate function

        //below piece of code is written in function of class that extends from AsyncTask
        handler.post(Runnable {
            CoinLabel.text = "${coinObject.name} (${coinObject.symbol})   $profitInLast24"
            PriceView.text = coinObject.priceUsd

            //  To give color on the basis of price change in last 24 hours
            if(profitInLast24 != 0.0){
                PriceView.setTextColor(Color.WHITE)
            }else{
                PriceView.setTextColor(Color.RED)
            }
        })

    }

    //  Function that will change the text on the views with the next coin in the queue
    fun changeCoin (index: Int){

        if(coinData!!.size > 0) {
            val coinObject = coinData!![index]

            showCoin(coinObject)

            if(currentIndex < coinData!!.size - 1) currentIndex++
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
