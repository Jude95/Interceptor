package com.jude.interceptor.presenter;

import android.os.Bundle;

import com.jude.beam.expansion.list.BeamListActivityPresenter;
import com.jude.interceptor.service.FileManager;
import com.jude.interceptor.ui.MainActivity;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhuchenxi on 16/2/12.
 */
public class MainPresenter extends BeamListActivityPresenter<MainActivity,File> {

    @Override
    protected void onCreate(MainActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(aLong -> Observable.just(FileManager.listPacketFiles()))
                .map(Arrays::asList)
                .doOnNext(list -> Collections.sort(list, (lhs, rhs) -> rhs.getName().compareTo(lhs.getName())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
