package com.shashov.currency.mvp;

import com.shashov.currency.mvp.presenters.BasePresenter;
import com.shashov.currency.mvp.views.View;

import java.util.HashMap;
import java.util.Map;

public class PresenterManager {
    private Map<Class, BasePresenter> presenters;
    private static PresenterManager instance;

    public static PresenterManager getInstance() {
        if (instance == null) {
            instance = new PresenterManager();
            instance.presenters = new HashMap<>();
        }
        return instance;
    }

    public BasePresenter getPresenter(View view){
        if (!presenters.containsKey(view.getClass())){
            presenters.put(view.getClass(), view.createPresenter());
        }

        return presenters.get(view.getClass());
    }

    public BasePresenter resetPresenter(View view){
        presenters.put(view.getClass(), view.createPresenter());
        return presenters.get(view.getClass());
    }
}
