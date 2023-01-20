package com.example.workoutic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutic.R;
import com.example.workoutic.chat.MessageActivity;
import com.example.workoutic.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserAdapter extends RecyclerView.Adapter<UserItem> {
    private Context context;
    private List<User> listUser;
    private List<User> listUserOriginal;
    private int userOchat;

    public UserAdapter(Context context, List<User> listUser, int userOchat) {
        this.context = context;
        this.listUser = listUser;
        this.listUserOriginal = new ArrayList<User>();
        this.listUserOriginal.addAll(listUser);
        this.userOchat = userOchat;
    }

    @NonNull
    @Override
    public UserItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItem ui, int pos) {
        User u = listUser.get(pos);
        ui.setUsername(u.getUsername());
        ui.setEmail(u.getEmail());
        if(u.getUsericon().equals("default")){
            ui.setUsericon();
        }
        else{
            Glide.with(context).load(u.getUsericon()).into(ui.getUsericon());
        }

        ui.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", u.getId());
                if(userOchat == 0){
                    intent.putExtra("caller", "UserActivity");
                }
                else{
                    intent.putExtra("caller", "ChatActivity");
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void filter(String search){
        int longitud = search.length();
        if (longitud == 0) {
            listUser.clear();
            listUser.addAll(listUserOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<User> collecion = listUser.stream().filter(i -> i.getUsername().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
                listUser.clear();
                listUser.addAll(collecion);
            } else {
                for (User u : listUserOriginal) {
                    if (u.getUsername().toLowerCase().contains(search.toLowerCase())) {
                        listUser.add(u);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
