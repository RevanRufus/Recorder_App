package com.example.intern_assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;


public class Record_Screen extends Fragment implements View.OnClickListener {
    NavController navController;
    Button list_btn, rec_btn, pause_btn;
    boolean isRecording;
    TextView header_txt;

    MediaRecorder mediaRecorder;

    public String recfile;

    Chronometer timer;

    public Record_Screen() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record__screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        list_btn = view.findViewById(R.id.listbtn);
        rec_btn = view.findViewById(R.id.strt_btn);
        timer = view.findViewById(R.id.timer_rec);
        header_txt = view.findViewById(R.id.save_txt);
        pause_btn = view.findViewById(R.id.pause_btn);

        rec_btn.setOnClickListener(this);

        list_btn.setOnClickListener(this);

        pause_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listbtn:
                navController.navigate(R.id.action_record_Screen_to_record_Screen_List);
                break;

            case R.id.strt_btn:
                if (isRecording) {
                    //stop Recording
                    stoprecord();
                    rec_btn.setText("Click Me");
                    header_txt.setText("Start Recording");
                    isRecording = false;
                } else {
                    if (checkpermission()) {
                        //start Recording
                        startrecord();
                        rec_btn.setText("Stop");
                        header_txt.setText("Stop Recording");
                        pause_btn.setVisibility(View.VISIBLE);
                        isRecording = true;
                    }

                }
                break;
            case R.id.pause_btn:
                pauserecord();
                break;
        }
    }

    private void pauserecord() {

    }

    private void startrecord() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recpath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_mm_dd hh_mm_ss", Locale.ENGLISH);
        Date now = new Date();

        recfile = "recorder" + simpleDateFormat.format(now) + ".3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recpath + "/" + recfile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stoprecord() {
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private boolean checkpermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return false;
        }
    }
}


