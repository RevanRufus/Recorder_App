package com.example.intern_assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class List_Adpater extends RecyclerView.Adapter<List_Adpater.list_viewholder> {

    File[] audiofiles;
    private onItemClicked onItemClicked;

    public List_Adpater(File[] audiofiles ,onItemClicked onItemClicked) {
        this.audiofiles = audiofiles;
        this.onItemClicked=onItemClicked;
    }

    @NonNull
    @Override
    public list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent,false);
        return new list_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull list_viewholder holder, int position) {
holder.list_filenm.setText(audiofiles[position].getName());

    }

    @Override
    public int getItemCount() {
        return audiofiles.length;
    }

    public class list_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_img;
        private TextView list_filenm;


        public list_viewholder(@NonNull View itemView) {
            super(itemView);

            list_img = itemView.findViewById(R.id.list_image);
            list_filenm =itemView.findViewById(R.id.list_filename);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
onItemClicked.OnClickListener(audiofiles[getAdapterPosition()],getAdapterPosition());
        }
    }
    public interface onItemClicked{
        void OnClickListener(File file, int position);
    }
}
