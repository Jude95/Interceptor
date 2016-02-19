package com.jude.interceptor.ui;

import android.view.ViewGroup;

import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListFragment;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.interceptor.domain.entities.PacketItem;
import com.jude.interceptor.presenter.OriginalPacketListPresenter;
import com.jude.interceptor.ui.viewholder.PacketViewHolder;

/**
 * Created by zhuchenxi on 16/2/16.
 */
@RequiresPresenter(OriginalPacketListPresenter.class)
public class OriginalPacketListFragment extends BeamListFragment<OriginalPacketListPresenter,PacketItem> {

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new PacketViewHolder(parent);
    }
}
