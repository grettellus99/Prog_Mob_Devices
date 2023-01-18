package com.example.workoutic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutic.R;
import com.example.workoutic.activity.MessageActivity;
import com.example.workoutic.model.Message;
import com.example.workoutic.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> listMessage;
    private String usericon;

    private FirebaseUser firebaseUser;


    public MessageAdapter(Context context, List<Message> listMessage, String usericon) {
        this.context = context;
        this.listMessage = listMessage;
        this.usericon = usericon;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            View v = LayoutInflater.from(context).inflate(R.layout.message_send_item, parent, false);
            return new MessageSendItem(v);
        }
        else{
            View v = LayoutInflater.from(context).inflate(R.layout.message_receive_item, parent, false);
            return new MessageReceiveItem(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message m = listMessage.get(position);

        if(getItemViewType(position) == 0){
            MessageSendItem msi = ((MessageSendItem) holder);
            msi.setMessage(m.getMessage());
            msi.setTime(m.getTime());
        }
        else {
            MessageReceiveItem mri = ((MessageReceiveItem) holder);
            mri.setMessage(m.getMessage());
            mri.setTime(m.getTime());
            if (usericon.equals("default")){
                mri.setCusericon();
            } else {
                Glide.with(context).load(usericon).into(mri.getCusericon());
            }
        }

    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(listMessage.get(position).getSenderID().equals(firebaseUser.getUid())){
            return 0;
        }
        else{
            return 1;
        }
    }
}
