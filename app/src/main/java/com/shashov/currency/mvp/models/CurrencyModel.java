package com.shashov.currency.mvp.models;

import com.shashov.currency.common.Currency;
import com.shashov.currency.common.MainViewInputData;

import java.util.ArrayList;
import java.util.List;

public class CurrencyModel {
    private static CurrencyModel instance;
    private List<Currency> currencies;

    public static CurrencyModel getInstance() {
        if (instance == null) {
            instance = new CurrencyModel();
            instance.currencies = new ArrayList<>();
        }
        return instance;
    }

    public void loadCurrencies(OnCurrenciesLoadedListener listener, boolean isUseCache) {
        //TODO load
        currencies.clear();
        currencies.add(new Currency("","$", 1 ,"dollar", 1.0));
        currencies.add(new Currency("","r", 1 ,"rub", 1.0));
        listener.onSuccess(currencies);
    }

    public void getCurrencies(OnCurrenciesLoadedListener listener) {
        if ((currencies != null) && !currencies.isEmpty()) {
            listener.onSuccess(currencies);
        } else {
            listener.onError("");
        }
    }

    public void convertCurrency(OnConvertedListener listener, MainViewInputData inputData) {
        //TODO convert
        listener.onSuccess(inputData.getInput() + inputData.getInputCurrencyIndex()+inputData.getOutputCurrencyIndex());
    }

    public interface OnConvertedListener {
        void onSuccess(String result);

        void onError(String error);
    }

    public interface OnCurrenciesLoadedListener {
        void onSuccess(List<Currency> currencies);

        void onError(String error);
    }
}
