package jp.co.taosoftware.android.packetcapture;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.jude.utils.JTimeTransform;
import com.jude.utils.JUtils;

import java.util.List;

import rx.subjects.BehaviorSubject;


public class PacketCaptureService extends VpnService implements Runnable {
    public static final String KEY_CMD = "cmd";
    public static final String KEY_FILE = "file";
    public static final int CMD_STARTVPN = 1;
    public static final int CMD_STOPVPN = 2;

    private static String mLogFileName;
    private PendingIntent a;
    private Thread mThread;
    private ParcelFileDescriptor mParcelFileDescriptor;

    private static BehaviorSubject<Boolean> isRunning = BehaviorSubject.create();

    static {
        isRunning.subscribe(v->JUtils.Log("VPNService","status changed:"+v));
        isRunning.onNext(false);
        System.loadLibrary("tPacketCapture");
    }

    public static String getCurFile() {
        return mLogFileName;
    }

    public static boolean isRunning(){
        return isRunning.getValue();
    }

    public static BehaviorSubject<Boolean> getIsRunning(){
        return isRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.Log("VPNService","onCreate");
    }

    public void onDestroy() {
        JUtils.Log("VPNService","onDestroy");
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
    }

    public void onRevoke() {
        JUtils.Log("VPNService","passive stop");
        stopVPN();
    }

    public void stopVPN(){
        JUtils.Log("VPNService","stop "+ new JTimeTransform(System.currentTimeMillis()/1000).toString("yyyy/MM/dd hh:mm:ss"));
        stopSelf();
        stopCapture();
        isRunning.onNext(false);
        JUtils.Toast("数据记录本地VPN已关闭");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
        int cmd = intent.getIntExtra(KEY_CMD,CMD_STARTVPN);
        switch (cmd){
            case CMD_STARTVPN:
                if (isRunning()){
                    //Why come here?
                    JUtils.Log("VPNService","duplicate launch");
                }else{
                    mLogFileName = intent.getStringExtra(KEY_FILE);
                    this.mThread = new Thread(this, "PacketCaptureThread");
                    this.mThread.start();
                    isRunning.onNext(true);
                    JUtils.Toast("数据记录本地VPN已开启");
                    JUtils.Log("VPNService","launch "+new JTimeTransform(System.currentTimeMillis()/1000).toString("yyyy/MM/dd hh:mm:ss"));
                }
                break;
            case CMD_STOPVPN:
                JUtils.Log("VPNService","active stop");
                stopVPN();
                break;
        }
        return 3;
    }

    public synchronized void run() {
        try {
            setUidCaptureStatus(false);
            setPCapFileName(mLogFileName);
            Builder builder = new Builder();
            builder.addAddress("10.8.0.1", 32);
            builder.addRoute("0.0.0.0", 0);
            builder.setSession("tPacketCapture");
            builder.setConfigureIntent(this.a);
            this.mParcelFileDescriptor = builder.establish();
            startCapture(this.mParcelFileDescriptor.getFd());
            try {
                this.mParcelFileDescriptor.close();
            } catch (Exception e) {
                JUtils.Log("A"+e.getMessage());
            }
            this.mParcelFileDescriptor = null;
        } catch (Exception e2) {
            JUtils.Log("B"+e2.getMessage());
            new StringBuilder("Got ").append(e2.toString());
            try {
                this.mParcelFileDescriptor.close();
            } catch (Exception e3) {
                JUtils.Log("E"+e3.getMessage());
            }
            this.mParcelFileDescriptor = null;
        } catch (Throwable th) {
            JUtils.Log("C"+th.getMessage());
            try {
                this.mParcelFileDescriptor.close();
            } catch (Exception e4) {
                JUtils.Log("D"+e4.getMessage());
            }
            this.mParcelFileDescriptor = null;
        }
        return;
    }


    private native void insertUid(int i);

    private native void setUidCaptureStatus(boolean z);

    public native void setPCapFileName(String str);

    public native void startCapture(int i);

    public native void stopCapture();
}