package com.example.workoutic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutic.R;
import com.example.workoutic.activity.MessageActivity;

public class MessageSendItem extends RecyclerView.ViewHolder{
    private TextView message;
    private TextView time1;
    private TextView time2;


    private View fl;
    public ImageView imgMessage;

    public MessageSendItem(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.txt_message_send_item);
        imgMessage = itemView.findViewById(R.id.img_message_send_item);
        time1 = itemView.findViewById(R.id.txt_message_send_time);
        time2 = itemView.findViewById(R.id.txt_imgmessage_send_time);
        fl = itemView.findViewById(R.id.fl_message_send_item);


    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public TextView getTime1() {
        return time1;
    }

    public void setTime1(String time) {
        this.time1.setText(time);
    }

    public TextView getTime2() {
        return time2;
    }

    public void setTime2(String time) {
        this.time2.setText(time);
    }

    public ImageView getImgMessage() {
        return imgMessage;
    }

    public void setImgMessage(ImageView imgMessage) {
        this.imgMessage = imgMessage;
    }

    public View getFl() {
        return fl;
    }

    public void setFl(View fl) {
        this.fl = fl;
    }
}
