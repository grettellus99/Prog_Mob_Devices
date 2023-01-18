package com.example.workoutic.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageReceiveItem extends RecyclerView.ViewHolder{

    private CircleImageView cusericon;
    private TextView message;
    private TextView time;

    public MessageReceiveItem(View itemView) {
        super(itemView);
        cusericon = itemView.findViewById(R.id.cimg_message_receive_item);
        message = itemView.findViewById(R.id.txt_message_receive_item);
        time = itemView.findViewById(R.id.txt_message_receive_time);
    }

    public CircleImageView getCusericon() {
        return cusericon;
    }

    public void setCusericon() {
        this.cusericon.setImageResource(R.drawable.ic_user_account_45);
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
