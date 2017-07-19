package com.shashov.currency.mvp.presenters;

import android.support.annotation.NonNull;
import com.shashov.currency.common.Currency;
import com.shashov.currency.mvp.common.MainViewInputData;
import com.shashov.currency.mvp.models.CurrencyModel;
import com.shashov.currency.mvp.views.MainView;

import java.util.List;

public class MainPresenter extends BasePresenter<MainView> {
    private MainViewInputData inputData;

    public MainPresenter() {
        inputData = CurrencyModel.getInstance().restoreInputData();
    }

    @Override
    protected void onViewAttached(@NonNull final MainView view) {
        CurrencyModel.getInstance().getCurrencies(new CurrencyModel.OnCurrenciesLoadedListener() {
            @Override
            public void onSuccess(@NonNull List<Currency> currencies) {
                restoreUI(currencies);
            }

            @Override
            public void onError() {
                if (getView() != null) {
                    getView().showLoadingScreen();
                }
            }
        });
    }

    private void restoreUI(@NonNull List<Currency> currencies) {
        if (getView() != null) {
            getView().populateCurrencies(currencies);
            getView().showInputData(new MainViewInputData(inputData));
            convert();
        }
    }

    private void convert(){
        CurrencyModel.getInstance().convertCurrency(new CurrencyModel.OnConvertedListener() {
            @Override
            public void onSuccess(@NonNull String result, @NonNull String caption1, @NonNull String caption2) {
                if (getView() != null) {
                    getView().showResult(result, caption1, caption2);
                }
            }

            @Override
            public void onError() {
                getView().showError();
            }
        }, inputData);
    }


    public void onClickConvert(@NonNull String input, int from, int to) {
        inputData.setInputCurrencyIndex(from);
        inputData.setOutputCurrencyIndex(to);
        inputData.setInput(input);
        convert();
    }

    public void onClickSwapCurrencies(){
        int r = inputData.getInputCurrencyIndex();
        inputData.setInputCurrencyIndex(inputData.getOutputCurrencyIndex());
        inputData.setOutputCurrencyIndex(r);
        if (getView() != null) {
            getView().swapCurrencies();
        }
    }
}
