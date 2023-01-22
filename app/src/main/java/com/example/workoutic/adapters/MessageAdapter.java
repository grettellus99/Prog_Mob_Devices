package com.example.workoutic.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.workoutic.R;
import com.example.workoutic.chat.MessageActivity;
import com.example.workoutic.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> listMessage;
    private String usericon;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;


    public MessageAdapter(Context context, List<Message> listMessage, String usericon) {
        this.context = context;
        this.listMessage = listMessage;
        this.usericon = usericon;

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDB = FirebaseDatabase.getInstance();
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

            if(!m.getImageURL().equals("")){
                msi.setTime1(m.getTime());
                msi.getTime1().setVisibility(View.GONE);
                msi.setTime2(m.getTime());
                msi.getTime2().setVisibility(View.VISIBLE);
                msi.getMessage().setVisibility(View.GONE);
                msi.getImgMessage().setVisibility(View.VISIBLE);
                msi.getFl().setVisibility(View.VISIBLE);
                //System.out.println("hola: "+m.getImageURL());
                Glide.with(context).load(m.getImageURL()).fitCenter().centerCrop().into(msi.imgMessage);
            }
            else if(!m.getMessage().equals("")){
                msi.getMessage().setVisibility(View.GONE);
                msi.getImgMessage().setVisibility(View.VISIBLE);
                msi.getFl().setVisibility(View.VISIBLE);

                Glide.with(context).load(m.getImageURL()).fitCenter().centerCrop().into(msi.imgMessage);
            }
            else if(!m.getMessage().equals("")){
                msi.setTime1(m.getTime());
                msi.getTime1().setVisibility(View.VISIBLE);
                msi.setTime2(m.getTime());
                msi.getTime2().setVisibility(View.GONE);
                msi.setMessage(m.getMessage());
                msi.getMessage().setVisibility(View.VISIBLE);
                msi.getFl().setVisibility(View.GONE);
            }

            msi.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectOptionSender(msi, m);
                    return false;
                }
            });

        }
        else {
            MessageReceiveItem mri = ((MessageReceiveItem) holder);
            mri.setMessage(m.getMessage());

            if (usericon.equals("default")){
                mri.setCusericon();
            } else {
                Glide.with(context).load(usericon).into(mri.getCusericon());
            }

            if(!m.getImageURL().equals("")){
                mri.setTime1(m.getTime());
                mri.getTime1().setVisibility(View.GONE);
                mri.setTime2(m.getTime());
                mri.getTime2().setVisibility(View.VISIBLE);
                mri.getMessage().setVisibility(View.GONE);
                mri.getImgMessage().setVisibility(View.VISIBLE);
                mri.getFl().setVisibility(View.VISIBLE);
                Glide.with(context).load(m.getImageURL()).fitCenter().centerCrop().into(mri.imgMessage);
            }
            else if(!m.getMessage().equals("")){

                mri.setTime1(m.getTime());
                mri.getTime1().setVisibility(View.VISIBLE);
                mri.setTime2(m.getTime());
                mri.getTime2().setVisibility(View.GONE);


                mri.setMessage(m.getMessage());
                mri.getMessage().setVisibility(View.VISIBLE);
                mri.getFl().setVisibility(View.GONE);
            }
            mri.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectOptionReceiver(mri, m);
                    return false;
                }
            });


        }

    }
    private void selectOptionSender(MessageSendItem msi, Message m){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        // Inicializando
        LinearLayout ly = (LinearLayout) inflater.inflate(R.layout.dialog_message_activity,null);
        TextView titleTV = ly.findViewById(R.id.dialog_msg_act_title);
        TextView op1 = ly.findViewById(R.id.dialog_msg_specific_message_op1);
        TextView op2 = ly.findViewById(R.id.dialog_msg_specific_message_op2);

        titleTV.setText(R.string.choose_an_option);
        op1.setText(R.string.share);
        op2.setText(R.string.delete);

        LinearLayout btn_n = ly.findViewById(R.id.btn_msg_option_negative);

        builder.setView(ly);

        final AlertDialog dialog = builder.create();

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setAlpha(0.6F);
                shareSender(msi, m);
                dialog.dismiss();
            }
        });

        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op2.setAlpha(0.6F);
                delete(m);
                dialog.dismiss();
            }
        });

        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_n.setAlpha(0.6F);
                dialog.dismiss();
            }
        });

           dialog.show();
    }

    private void selectOptionReceiver(MessageReceiveItem mri, Message m){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        // Inicializando
        LinearLayout ly = (LinearLayout) inflater.inflate(R.layout.dialog_message_activity,null);
        TextView titleTV = ly.findViewById(R.id.dialog_msg_act_title);
        TextView op1 = ly.findViewById(R.id.dialog_msg_specific_message_op1);
        TextView op2 = ly.findViewById(R.id.dialog_msg_specific_message_op2);

        titleTV.setText(R.string.choose_an_option);
        op1.setText(R.string.share);
        op2.setVisibility(View.GONE);

        LinearLayout btn_n = ly.findViewById(R.id.btn_msg_option_negative);

        builder.setView(ly);

        final AlertDialog dialog = builder.create();

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setAlpha(0.6F);
                shareReceiver(mri, m);
                dialog.dismiss();
            }
        });

        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_n.setAlpha(0.6F);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void delete(Message m){
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDB.getReference("Chats");

        databaseReference.child(m.getID()).removeValue();

        listMessage.remove(m);
    }

    private void shareSender(MessageSendItem msi, Message m){
        Intent shareIntent = new Intent();
        if(!m.getImageURL().equals("")){
            String url = m.getImageURL();
            String name = url.substring(url.lastIndexOf("/")+1);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) msi.imgMessage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap, name, null);

            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.parse(bitmapPath);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
        }
        else if(!m.getMessage().equals("")){
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, m.getMessage());
            shareIntent.setType("text/plain");
        }
        context.startActivity(Intent.createChooser(shareIntent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


    }

    private void shareReceiver(MessageReceiveItem mri, Message m){
        Intent shareIntent = new Intent();
        if(!m.getImageURL().equals("")){
            String url = m.getImageURL();
            String name = url.substring(url.lastIndexOf("/")+1);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) mri.imgMessage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap, name, null);

            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.parse(bitmapPath);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
        }
        else if(!m.getMessage().equals("")){
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, m.getMessage());
            shareIntent.setType("text/plain");
        }
        context.startActivity(Intent.createChooser(shareIntent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


    }


    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = firebaseAuth.getCurrentUser();
        if(listMessage.get(position).getSenderID().equals(firebaseUser.getUid())){
            return 0;
        }
        else{
            return 1;
        }
    }
}
