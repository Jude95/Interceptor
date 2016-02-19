package com.jude.interceptor.presenter;

import android.os.Bundle;

import com.jude.beam.expansion.data.BeamDataActivityPresenter;
import com.jude.interceptor.ui.FileDetailActivity;

import java.io.File;

/**
 * Created by zhuchenxi on 16/2/15.
 */
public class FileDetailPresenter extends BeamDataActivityPresenter<FileDetailActivity,File> {

    @Override
    protected void onCreate(FileDetailActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        publishObject(new File(getView().getIntent().getStringExtra("file")));
    }



}
