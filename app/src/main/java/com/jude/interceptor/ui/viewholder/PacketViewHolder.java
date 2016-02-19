package com.jude.interceptor.ui.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.interceptor.R;
import com.jude.interceptor.domain.entities.PacketItem;
import com.jude.interceptor.utils.SizeFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/2/16.
 */
public class PacketViewHolder extends BaseViewHolder<PacketItem> {
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_address_from)
    TextView tvAddressFrom;
    @Bind(R.id.tv_address_to)
    TextView tvAddressTo;
    @Bind(R.id.tv_size)
    TextView tvSize;

    public PacketViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_packet);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(PacketItem data) {
        switch (data.getType()){
            case PacketItem.TCP:tvType.setText("TCP");break;
            case PacketItem.UDP:tvType.setText("UDP");break;
            case PacketItem.ARP:tvType.setText("ARP");break;
            case PacketItem.UNKNOW:tvType.setText("UNKNOW");break;
        }
        tvAddressFrom.setText(data.getSip()+":"+data.getSport());
        tvAddressTo.setText(data.getDip()+":"+data.getDport());
        tvSize.setText(SizeFormat.convertToStringRepresentation(data.getLength()));
    }
}
