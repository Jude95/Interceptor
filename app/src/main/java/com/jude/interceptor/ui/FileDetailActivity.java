package com.jude.interceptor.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.interceptor.R;
import com.jude.interceptor.presenter.FileDetailPresenter;

import java.io.File;

/**
 * Created by zhuchenxi on 16/2/15.
 */
@RequiresPresenter(FileDetailPresenter.class)
public class FileDetailActivity extends BeamDataActivity<FileDetailPresenter, File> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        Fragment fragment = new OriginalPacketListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("file",getIntent().getStringExtra("file"));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment,"Original").commit();
    }
}
