package com.example.graphicalauthenticator.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graphicalauthenticator.Modal.FileModal;
import com.example.graphicalauthenticator.R;
import com.example.graphicalauthenticator.ui.filemanager.FileDashbaordActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private final List<FileModal> fileModalList;
    private Context activity;
    private onItemClickListener listener;


    public FilesAdapter(List<FileModal> notesModelList, Context activity) {
        this.fileModalList = notesModelList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_files, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesAdapter.ViewHolder holder, int position) {

        FileModal modal = fileModalList.get(position);
        String textName = String.valueOf(modal.name);
        String type = modal.type.toLowerCase();
        long fileSize = modal.size;
        Date upDate = modal.uploadDateTime;

        String fileSizeInMb = formatFileSize(fileSize);
        String timeAgo = getTimeAgo(upDate);

        holder.tvFileName.setText(textName);
        holder.tvFileSize.setText(fileSizeInMb);
        holder.tvUploadDate.setText(timeAgo);

        int imgSource = R.drawable.file_unknown;

        switch (type) {
            case "pdf":
                imgSource = R.drawable.file_pdf;
                break;
            case "txt":
                imgSource = R.drawable.file_txt;
                break;
            case "zip":
                imgSource = R.drawable.file_zip;
                break;
            case "png":
            case "jpeg":
            case "jpg":
            case "gif":
            case "svg":
            case "raw":
            case "tiff":
            case "webp":
            case "bmp":
                imgSource = R.drawable.file_image;
                break;
            case "mp4":
            case "mkv":
            case "mpg":
            case "flv":
            case "wmv":
            case "mov":
            case "webm":
                imgSource = R.drawable.file_video;
                break;
            case "mp3":
            case "m4v":
            case "wav":
            case "aac":
            case "m4a":
            case "ogg":
                imgSource = R.drawable.file_audio;
                break;
        }

        holder.ivFileType.setImageResource(imgSource);
    }

    public String formatFileSize(long fileSizeInBytes) {
        if (fileSizeInBytes < 1024) {
            return fileSizeInBytes + " B";
        } else if (fileSizeInBytes < 1024 * 1024) {
            return String.format("%.2f kB", fileSizeInBytes / 1024.0);
        } else if (fileSizeInBytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", fileSizeInBytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", fileSizeInBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    public String getTimeAgo(Date date) {

        if (date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;
        Context ctx = activity;

        if (dim == 0) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim == 1) {
            return "Updated " + "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_an) + " " + ctx.getResources().getString(R.string.date_util_unit_hour);
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 60)) + " " + ctx.getResources().getString(R.string.date_util_unit_hours);
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " " + ctx.getResources().getString(R.string.date_util_unit_days);
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_month);
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " " + ctx.getResources().getString(R.string.date_util_unit_months);
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_over) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_almost) + " 2 " + ctx.getResources().getString(R.string.date_util_unit_years);
        } else {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + (Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
        }

        return "Updated " + timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
    }

    private int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    @Override
    public int getItemCount() {
        return fileModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFileName, tvUploadDate, tvFileSize;
        public ImageView ivFileType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFileType = itemView.findViewById(R.id.fileImageView);
            tvFileName = itemView.findViewById(R.id.fileName);
            tvUploadDate = itemView.findViewById(R.id.dateAndTime);
            tvFileSize = itemView.findViewById(R.id.fileSize);


            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(position);
                }
            });
        }
    }


    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
        Log.d("TAG", "TesteRRR: in adapter method");

    }

}
