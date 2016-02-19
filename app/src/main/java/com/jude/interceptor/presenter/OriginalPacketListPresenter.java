package com.jude.interceptor.presenter;

import android.os.Bundle;

import com.jude.beam.expansion.list.BeamListFragmentPresenter;
import com.jude.interceptor.data.PacketModel;
import com.jude.interceptor.domain.entities.PacketItem;
import com.jude.interceptor.ui.OriginalPacketListFragment;
import com.jude.utils.JUtils;

/**
 * Created by zhuchenxi on 16/2/16.
 */
public class OriginalPacketListPresenter extends BeamListFragmentPresenter<OriginalPacketListFragment,PacketItem> {
    private String file;
    @Override
    protected void onCreate(OriginalPacketListFragment view, Bundle savedState) {
        super.onCreate(view, savedState);
        file = getView().getArguments().getString("file");
        PacketModel.getInstance().getPacketsFromFile(file)
                .buffer(30)
                .unsafeSubscribe(getMoreSubscriber());
    }
}
