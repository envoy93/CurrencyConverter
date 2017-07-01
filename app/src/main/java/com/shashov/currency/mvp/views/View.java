package com.shashov.currency.mvp.views;

import com.shashov.currency.mvp.presenters.BasePresenter;

public interface View {
    BasePresenter createPresenter();
}
