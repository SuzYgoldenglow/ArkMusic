package com.prts.arkmusic.ui.gallery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.prts.arkmusic.MainActivity;
import com.prts.arkmusic.MusicService;
import com.prts.arkmusic.R;
import com.prts.arkmusic.databinding.FragmentGalleryBinding;
import com.prts.arkmusic.databinding.FragmentHomeBinding;
import com.prts.arkmusic.ms2;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

public class GalleryFragment extends Fragment{
    public static final String CTL_ACTION = "arkmusic.action.CTL_ACTION";
    public static final String UPDATE_ACTION = "arkmusic.action.UPDATE_ACTION";
    private FragmentGalleryBinding binding;

    ImageButton play,stop,previous,next;
    ActivityReceiver activityReceiver;
    TextView name, usage;
    ImageView image,suzy;
    int status=0x11;


    String[] titleStrs = {"Dash!", "Dash!", "皇帝的利刃","皇帝的利刃"};
    String[] authorStrs = {"燃就完事了", "燃就完事了", "《遗尘漫步》","《遗尘漫步》"};
    int[] im={R.mipmap.ic_arkmusic,R.mipmap.ic_arkmusic,R.mipmap.ic_wd,R.mipmap.ic_wd};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,Bundle savedInstanceState) {
        Log.d("CREATE","CREATE");

        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);



        binding = FragmentGalleryBinding.inflate(inflater, container, false);


        View root = binding.getRoot();


        return root;


    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d("START","START");
        Intent intent=new Intent(CTL_ACTION);
        getActivity().startService(new Intent(getActivity(), ms2.class));
        play=getView().findViewById(R.id.play2);
        stop=getView().findViewById(R.id.stop2);
        previous=getView().findViewById(R.id.previous2);
        next=getView().findViewById(R.id.next2);
        name = getView().findViewById(R.id.name2);
        usage = getView().findViewById(R.id.usage2);
        image=getView().findViewById(R.id.cover2);
        suzy=getView().findViewById(R.id.suzy2);

        activityReceiver = new ActivityReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(UPDATE_ACTION);
        // 注册BroadcastReceiver
        getActivity().registerReceiver(activityReceiver, filter);

        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        getActivity().registerReceiver(headsetPlugReceiver, intentFilter);
        // for bluetooth headset connection receiver
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        getActivity().registerReceiver(headsetPlugReceiver, bluetoothFilter);



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("control2",12);
                getActivity().sendBroadcast(intent);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("control2",22);
                getActivity().sendBroadcast(intent);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("control2",32);
                getActivity().sendBroadcast(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("control2",42);
                getActivity().sendBroadcast(intent);
            }
        });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent=new Intent(CTL_ACTION);
        intent.putExtra("control2",52);
        getActivity().sendBroadcast(intent);
        getActivity().stopService(new Intent(getActivity(),ms2.class));
        binding = null;
    }
    public class ActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int update = intent.getIntExtra("update2",-1);
            int current = intent.getIntExtra("current2", -1);
            int suzyy=intent.getIntExtra("suzy2",-1);

            if (current >= 0){
                name.setText(titleStrs[current]);
                usage.setText(authorStrs[current]);
                image.setImageResource(im[current]);
            }
            switch (update){
                case 0x11:
                    play.setImageResource(R.drawable.ic_play);
                    status = 0x11;
                    break;
                case 0x12:
                    play.setImageResource(R.drawable.ic_pause);
                    status = 0x12;
                    break;

                case 0x13:
                    play.setImageResource(R.drawable.ic_play);
                    status = 0x13;
                    break;
            }
            switch(suzyy){
                case 0x11:
                    suzy.setImageResource(R.drawable.head);
                    break;
                case 0x12:
                    suzy.setImageResource(R.drawable.head2);
                    break;
            }
        }
    }
    private BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver() {
        Intent intentt=new Intent(CTL_ACTION);
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if(BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                    //Bluetooth headset is now disconnected
                    Log.d("BLUE","BLUE");
                    intentt.putExtra("control2",62);
                    getActivity().sendBroadcast(intentt);
                }
            } if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)){
                intentt.putExtra("control2",62);
                getActivity().sendBroadcast(intentt);
            }
        }

    };
}