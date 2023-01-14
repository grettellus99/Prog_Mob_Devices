package com.example.workoutic.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutic.R;

public class MessageSendItem extends RecyclerView.ViewHolder{
    private TextView message;
    private TextView time;

    public MessageSendItem(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.txt_message_send_item);
        time = itemView.findViewById(R.id.txt_message_send_time);
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time.setText(time);
    }
}
