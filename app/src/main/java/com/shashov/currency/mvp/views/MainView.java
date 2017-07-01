package com.shashov.currency.mvp.views;

import com.shashov.currency.common.Currency;
import com.shashov.currency.common.MainViewInputData;

import java.util.List;

public interface MainView extends View{
    void showResult(String result);
    void showInputData(MainViewInputData inputData);
    void populateCurrencies(List<Currency> currencies);
}
