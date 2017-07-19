package com.shashov.currency.mvp.models;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import com.shashov.currency.CurrencyConverterApp;
import com.shashov.currency.common.CurrenciesXmlParser;
import com.shashov.currency.common.Currency;
import com.shashov.currency.common.NetworkCall;
import com.shashov.currency.mvp.common.MainViewInputData;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrencyModel {
    private static final String API_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String CURRENCIES_XML_KEY = "CURRENCIES_XML_KEY";
    private static final String INPUT_DATA_FROM = "INPUT_DATA_FROM";
    private static final String INPUT_DATA_TO = "INPUT_DATA_TO";
    private static final String INPUT_DATA_INPUT = "INPUT_DATA_INPUT";
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

    private void saveXmlToCache(@NonNull String xml) {
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

    public void convertCurrency(@NonNull OnConvertedListener listener, @NonNull MainViewInputData inputData) {
        saveInputData(inputData);
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

        listener.onSuccess(format(result), getCaptionForCurrency(from, to, k1), getCaptionForCurrency(to, from, k2));
    }

    private String format(double number) {
        return String.format(CurrencyConverterApp.getLocale(), (number == (long) number) ? "%s" : "%.3f", number);
    }

    private String getCaptionForCurrency(@NonNull Currency left, @NonNull Currency right, double value) {
        return new StringBuilder("1 ")
                .append(left.getCharCode())
                .append(" = ")
                .append(format(value))
                .append(" ")
                .append(right.getCharCode())
                .toString();
    }

    public MainViewInputData restoreInputData() {
        String from = PreferenceManager
                .getDefaultSharedPreferences(CurrencyConverterApp.getContext())
                .getString(INPUT_DATA_FROM, "USD");

        String to = PreferenceManager
                .getDefaultSharedPreferences(CurrencyConverterApp.getContext())
                .getString(INPUT_DATA_TO, "USD");

        String input = PreferenceManager
                .getDefaultSharedPreferences(CurrencyConverterApp.getContext())
                .getString(INPUT_DATA_INPUT, "0");

        Map.Entry<Integer, Integer> ind = findCurrencies(from, to);

        return new MainViewInputData(ind.getKey(), ind.getValue(), input);
    }

    public void saveInputData(@NonNull MainViewInputData inputData) {
        if (inputData == null || (inputData.getInput() == null)) {
            return;
        }

        PreferenceManager
                .getDefaultSharedPreferences(CurrencyConverterApp.getContext())
                .edit()
                .putString(INPUT_DATA_FROM, currencies.get(inputData.getInputCurrencyIndex()).getCharCode())
                .putString(INPUT_DATA_TO, currencies.get(inputData.getOutputCurrencyIndex()).getCharCode())
                .putString(INPUT_DATA_INPUT, inputData.getInput())
                .apply();
    }

    private Map.Entry<Integer, Integer> findCurrencies(@NonNull String from, @NonNull String to) {
        int first = 0, second = 0;
        if (currencies != null) {
            for (int i = 0; i < currencies.size(); i++) {
                if (currencies.get(i).getCharCode().equals(from)) {
                    first = i;
                }
                if (currencies.get(i).getCharCode().equals(to)) {
                    second = i;
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(first, second);
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
