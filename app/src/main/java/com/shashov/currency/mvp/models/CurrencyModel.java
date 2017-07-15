package com.shashov.currency.mvp.models;

import android.annotation.SuppressLint;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import com.shashov.currency.CurrencyConverterApp;
import com.shashov.currency.common.Currency;
import com.shashov.currency.common.CurrenciesXmlParser;
import com.shashov.currency.mvp.common.MainViewInputData;
import com.shashov.currency.common.NetworkCall;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CurrencyModel {
    private static final String API_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String CURRENCIES_XML_KEY = "CURRENCIES_XML_KEY";
    private static CurrencyModel instance;
    private List<Currency> currencies;

    public static CurrencyModel getInstance() {
        if (instance == null) {
            instance = new CurrencyModel();
            instance.currencies = new ArrayList<>();
        }
        return instance;
    }

    public void loadCurrencies(@NonNull final OnCurrenciesLoadedListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                currencies.clear();
                String xml;

                if (!CurrencyConverterApp.isNetworkConnected()) {
                    //load without internet connection
                    xml = loadXmlFromCache();
                } else {
                    try {
                        //load from internet
                        xml = NetworkCall.downloadUrl(new URL(API_URL));
                    } catch (IOException e) {
                        xml = loadXmlFromCache();
                    }
                }

                //try to parse and add currencies
                try {
                    currencies.addAll(CurrenciesXmlParser.parseCurrencies(xml));
                } catch (Exception e) {
                    // do nothing
                }

                if (currencies.isEmpty()) {
                    listener.onError();
                } else {
                    saveXmlToCache(xml);
                    listener.onSuccess(currencies);
                }
            }
        }).start();
    }

    private void saveXmlToCache(String xml) {
        PreferenceManager
                .getDefaultSharedPreferences(CurrencyConverterApp.getContext())
                .edit()
                .putString(CURRENCIES_XML_KEY, xml)
                .apply();
    }

    private String loadXmlFromCache() {
        return PreferenceManager
                .getDefaultSharedPreferences(CurrencyConverterApp.getContext())
                .getString(CURRENCIES_XML_KEY, "");
    }

    public void getCurrencies(@NonNull OnCurrenciesLoadedListener listener) {
        if ((currencies != null) && !currencies.isEmpty()) {
            listener.onSuccess(currencies);
        } else {
            listener.onError();
        }
    }

    @SuppressLint("DefaultLocale")
    public void convertCurrency(@NonNull OnConvertedListener listener, @NonNull MainViewInputData inputData) {
        if (inputData.getInputCurrencyIndex() >= currencies.size() || (inputData.getOutputCurrencyIndex() >= currencies.size())) {
            listener.onError();
            return;
        }

        double result;
        try {
            result = Double.parseDouble(inputData.getInput());
        } catch (Exception e) {
            listener.onError();
            return;
        }

        Currency from = currencies.get(inputData.getInputCurrencyIndex());
        Currency to = currencies.get(inputData.getOutputCurrencyIndex());
        double k1 = (from.getValue() / from.getNominal()) / (to.getValue() / to.getNominal());
        double k2 = (to.getValue() / to.getNominal()) / (from.getValue() / from.getNominal());
        result = result * k1;

        listener.onSuccess(format(result), getCaptionForCurrency(from, to, format(k1)), getCaptionForCurrency(to, from, format(k2)));
    }

    private String format(double number) {
        return String.format((number == (long) number) ? "%s" : "%.3f", number);
    }

    private String getCaptionForCurrency(Currency left, Currency right, String value) {
        return new StringBuilder("1 ")
                .append(left.getCharCode())
                .append(" = ")
                .append(value)
                .append(" ")
                .append(right.getCharCode())
                .toString();
    }

    public interface OnConvertedListener {
        void onSuccess(@NonNull String result, @NonNull String caption1, @NonNull String caption2);

        void onError();
    }

    public interface OnCurrenciesLoadedListener {
        void onSuccess(@NonNull List<Currency> currencies);

        void onError();
    }
}
