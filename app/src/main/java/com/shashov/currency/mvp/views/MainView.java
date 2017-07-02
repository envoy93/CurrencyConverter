package com.shashov.currency.mvp.views;

import android.support.annotation.NonNull;
import com.shashov.currency.common.Currency;
import com.shashov.currency.mvp.common.MainViewInputData;

import java.util.List;

public interface MainView extends View{
    void showResult(@NonNull String result);

    void showInputData(@NonNull MainViewInputData inputData);

    void populateCurrencies(@NonNull List<Currency> currencies);

    void showLoadingScreen();

    void swapCurrencies();

    void showError();
}
