package com.shashov.currency.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.shashov.currency.currencyconverter.R;
import com.shashov.currency.mvp.PresenterManager;
import com.shashov.currency.mvp.presenters.BasePresenter;
import com.shashov.currency.mvp.presenters.LoadingPresenter;
import com.shashov.currency.mvp.views.LoadingView;

public class LoadingActivity extends AppCompatActivity implements LoadingView {
    private LinearLayout llNoData;
    private LinearLayout llLoading;
    private Button ibReload;
    private LoadingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        llNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        ibReload = (Button) findViewById(R.id.btn_reload);

        presenter = (LoadingPresenter) PresenterManager.getInstance().getPresenter(this);
    }

    @Override
    public void showLoading() {
        llLoading.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showApp() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showError() {
        llLoading.setVisibility(View.INVISIBLE);
        llNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public BasePresenter createPresenter() {
        return new LoadingPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }
}
