package com.jude.interceptor.ui.viewholder;

import android.content.Intent;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.interceptor.R;
import com.jude.interceptor.ui.FileDetailActivity;
import com.jude.interceptor.utils.SizeFormat;
import com.jude.utils.JTimeTransform;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 16/2/15.
 */
public class FileViewHolder extends BaseViewHolder<File> {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_size)
    TextView tvSize;
    @Bind(R.id.tv_path)
    TextView tvPath;
    private File file;

    public FileViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_file);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), FileDetailActivity.class);
            i.putExtra("file", file.getAbsolutePath());
            getContext().startActivity(i);
        });
    }

    @Override
    public void setData(File data) {
        file = data;
        String valuePath = data.getAbsolutePath().replace(Environment.getExternalStorageDirectory().getAbsolutePath(),"");
        tvPath.setText(valuePath);
        String time = data.getName().substring(0,data.getName().length()-5);
        String name = new JTimeTransform().parse("yyyyMMdd_hhmmss",time).toString("yyyy年MM月dd日 hh:mm:ss");
        tvName.setText(name);
        tvSize.setText(SizeFormat.convertToStringRepresentation(data.length()));
    }
}
