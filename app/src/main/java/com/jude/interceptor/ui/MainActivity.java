package com.jude.interceptor.ui;

import android.Manifest;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.interceptor.R;
import com.jude.interceptor.presenter.MainPresenter;
import com.jude.interceptor.service.FileManager;
import com.jude.interceptor.ui.viewholder.FileViewHolder;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.utils.JUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.taosoftware.android.packetcapture.PacketCaptureService;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BeamListActivity<MainPresenter,File> implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler)
    EasyRecyclerView recycler;
    @Bind(R.id.fab_menu)
    FloatingActionButton fabMenu;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        ButterKnife.bind(this);
        if (getListView().getAdapter()!=null){
            RecyclerArrayAdapter a = (RecyclerArrayAdapter) getListView().getAdapter();
            JUtils.Log("has adapter, size:"+a.getCount()+":"+a.getHeaderCount()+":"+a.getFooterCount());
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);


        PacketCaptureService.getIsRunning().subscribe(b->{
            fabMenu.setImageResource(b?R.drawable.stop:R.drawable.play);
        });

        fabMenu.setOnClickListener(v->{
            Intent intent = VpnService.prepare(this);
            if (intent!=null){
                startActivityForResult(intent,0);
            }
            else{
                onActivityResult(0, RESULT_OK, null);
            }
        });

        RxPermissions.getInstance(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        FileManager.initFileManager();
                    } else {
                        // Oups permission denied
                        JUtils.Toast("讨厌～");
                        finish();
                    }
                });
    }

    @Override
    public void showError(Throwable e) {
        super.showError(e);
        JUtils.Log("Error:"+e.getMessage());
    }


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new FileViewHolder(parent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.information) {
//            startActivity(new Intent(this, TimeActivity.class));
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
            Intent intent2 = new Intent(this, PacketCaptureService.class);
            intent2.putExtra(PacketCaptureService.KEY_FILE, FileManager.createNewPacketFile().getPath());
            intent2.putExtra(PacketCaptureService.KEY_CMD,
                    PacketCaptureService.isRunning()?PacketCaptureService.CMD_STOPVPN:PacketCaptureService.CMD_STARTVPN);
            startService(intent2);
        }
    }
}
