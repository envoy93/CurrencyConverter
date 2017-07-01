package com.shashov.currency.mvp.presenters;

import com.shashov.currency.common.Currency;
import com.shashov.currency.common.MainViewInputData;
import com.shashov.currency.mvp.models.CurrencyModel;
import com.shashov.currency.mvp.views.MainView;

import java.util.List;

public class MainPresenter extends BasePresenter<MainView> {
    private MainViewInputData inputData;
    private String result;

    public MainPresenter() {
        //TODO check currencies in model

        // TODO use sharedpreferences for load last settings
        inputData = new MainViewInputData(0,0,"0");
        result = "0";
    }

    @Override
    protected void onViewAttached(final MainView view) {
        CurrencyModel.getInstance().getCurrencies(new CurrencyModel.OnCurrenciesLoadedListener() {
            @Override
            public void onSuccess(List<Currency> currencies) {
                restoreUI(currencies);
            }

            @Override
            public void onError(String error) {
                //TODO go to the loading Screen
            }
        });
    }


    private void restoreUI(List<Currency> currencies) {
        if (getView() != null) {
            getView().populateCurrencies(currencies);
            getView().showInputData(inputData);
            getView().showResult(result);
        }
    }

    private void convert(){
        CurrencyModel.getInstance().convertCurrency(new CurrencyModel.OnConvertedListener() {
            @Override
            public void onSuccess(String result) {
                if (getView() != null) {
                    getView().showResult(result);
                }
            }

            @Override
            public void onError(String error) {
                //TODO
            }
        }, inputData);
    }

    public void onClickConvert(String input, int from, int to){
        inputData.setInputCurrencyIndex(from);
        inputData.setOutputCurrencyIndex(to);
        inputData.setInput(input);
        convert();
    }

    public void onClickSwapCurrencies(){
        //TODO
    }
}
