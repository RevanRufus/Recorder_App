package com.example.intern_assignment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;


public class Record_Screen_List extends Fragment implements List_Adpater.onItemClicked {

    private ConstraintLayout mediasheet;
    BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView list_audio;

    private File[] audiofiles;

    private List_Adpater list_adpater;

    private MediaPlayer mediaPlayer = null;
    private boolean isplaying = false;

    private File toplay;
    private Handler seekhandler;
    private Runnable run_seekbar;
    Button play_btn;
    TextView file_nm;
    SeekBar seekBar;

    public Record_Screen_List() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record__screen__list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediasheet = view.findViewById(R.id.media_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(mediasheet);
        list_audio = view.findViewById(R.id.record_list);
        play_btn = view.findViewById(R.id.play_btn);
        seekBar = view.findViewById(R.id.seek_bar);


        file_nm = view.findViewById(R.id.file);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        audiofiles = directory.listFiles();

        list_adpater = new List_Adpater(audiofiles, this);
        list_audio.setHasFixedSize(true);
        list_audio.setLayoutManager(new LinearLayoutManager(getContext()));
        list_audio.setAdapter(list_adpater);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isplaying) {
                    pause();
                } else {
                    resume();
                }
            }
        });

    }

    @Override
    public void OnClickListener(File file, int position) {
        toplay = file;
        if (isplaying) {
            audiostop();

        } else {
            play(toplay);

        }
    }

    private void resume() {
        mediaPlayer.start();
    }

    private void pause() {
        mediaPlayer.pause();
    }

    private void audiostop() {
        play_btn.setBackgroundResource(R.drawable.play);
        isplaying = false;
        mediaPlayer.stop();

    }

    private void play(File toplay) {

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(toplay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        play_btn.setBackgroundResource(R.drawable.pause);
        file_nm.setText(toplay.getName());
        isplaying = true;

        seekBar.setMax(mediaPlayer.getDuration());

        seekhandler = new Handler();
        run_seekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekhandler.postDelayed(this, 500);
            }
        };
        seekhandler.postDelayed(run_seekbar, 0);
    }

}

