package com.shashov.currency.mvp.presenters;

import com.shashov.currency.mvp.views.View;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends View> {
    private WeakReference<V> view;

    public void bindView(V view) {
        this.view = new WeakReference<>(view);
        onViewAttached(this.view.get());
    }

    public void unbindView() {
        this.view = null;
    }

    protected V getView() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    protected abstract void onViewAttached(V view);
}
