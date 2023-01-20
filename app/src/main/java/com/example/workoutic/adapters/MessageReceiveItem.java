package com.example.workoutic.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageReceiveItem extends RecyclerView.ViewHolder{

    private CircleImageView cusericon;
    private TextView message;
    private TextView time1;
    private TextView time2;

    public ImageView imgMessage;
    public MessageReceiveItem(View itemView) {
        super(itemView);
        cusericon = itemView.findViewById(R.id.cimg_message_receive_item);
        message = itemView.findViewById(R.id.txt_message_receive_item);
        time1 = itemView.findViewById(R.id.txt_message_receive_time);
        time2 = itemView.findViewById(R.id.txt_imgmessage_receive_time);

        imgMessage = itemView.findViewById(R.id.img_message_receive_item);
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
}
