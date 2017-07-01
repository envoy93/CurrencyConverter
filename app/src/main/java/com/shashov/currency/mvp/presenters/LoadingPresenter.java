package com.shashov.currency.mvp.presenters;

import com.shashov.currency.common.Currency;
import com.shashov.currency.mvp.models.CurrencyModel;
import com.shashov.currency.mvp.views.LoadingView;

import java.util.List;

public class LoadingPresenter extends BasePresenter<LoadingView> {
    private State state = State.LOADING;

    @Override
    protected void onViewAttached(LoadingView view) {
        if (state == State.LOADING) {
            loadData();
        } else {
            getView().showError();
        }
    }

    public void onReloadClick(){
        state = State.LOADING;
        loadData();
    }

    private void loadData(){
        getView().showLoading();
        CurrencyModel.getInstance().loadCurrencies(new CurrencyModel.OnCurrenciesLoadedListener() {
            @Override
            public void onSuccess(List<Currency> currencies) {
                if (getView() != null) {
                    state = State.LOADING;
                    getView().showApp();
                }
            }

            @Override
            public void onError(String error) {
                if (getView() != null) {
                    state = State.ERROR;
                    getView().showError();
                }
            }
        }, false);
    }

    private enum State {
        LOADING,
        ERROR
    }
}
