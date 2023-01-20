package com.example.workoutic.adapters;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutic.R;

public class UserItem extends RecyclerView.ViewHolder{
    private TextView username;
    private TextView email;
    private ImageView usericon;

    public UserItem(View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.txt_useritem_username);
        email = itemView.findViewById(R.id.txt_useritem_email);
        usericon = itemView.findViewById(R.id.img_useritem_icon_user);
    }

    public TextView getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public TextView getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setText(email);
    }

    public ImageView getUsericon() {
        return usericon;
    }

    public void setUsericon() {
        this.usericon.setImageResource(R.drawable.ic_user_account_45);
    }
}
