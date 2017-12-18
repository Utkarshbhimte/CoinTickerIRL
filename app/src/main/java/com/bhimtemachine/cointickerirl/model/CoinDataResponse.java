package com.bhimtemachine.cointickerirl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bapspatil
 */

public class CoinDataResponse implements Parcelable {
    @SerializedName("name") String name;
    @SerializedName("symbol") String symbol;
    @SerializedName("price_usd") String priceUsd;
    @SerializedName("percent_change_24h") String percentChangeDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public String getPercentChangeDay() {
        return percentChangeDay;
    }

    public void setPercentChangeDay(String percentChangeDay) {
        this.percentChangeDay = percentChangeDay;
    }

    public CoinDataResponse() {

    }

    public CoinDataResponse(String name, String symbol, String priceUsd, String percentChangeDay) {

        this.name = name;
        this.symbol = symbol;
        this.priceUsd = priceUsd;
        this.percentChangeDay = percentChangeDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.priceUsd);
        dest.writeString(this.percentChangeDay);
    }

    protected CoinDataResponse(Parcel in) {
        this.name = in.readString();
        this.symbol = in.readString();
        this.priceUsd = in.readString();
        this.percentChangeDay = in.readString();
    }

    public static final Creator<CoinDataResponse> CREATOR = new Creator<CoinDataResponse>() {
        @Override
        public CoinDataResponse createFromParcel(Parcel source) {
            return new CoinDataResponse(source);
        }

        @Override
        public CoinDataResponse[] newArray(int size) {
            return new CoinDataResponse[size];
        }
    };
}
