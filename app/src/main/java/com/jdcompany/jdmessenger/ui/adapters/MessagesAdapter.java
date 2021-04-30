package com.jdcompany.jdmessenger.ui.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.domain.Chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    List<Message> data;
    Chat chat;
    SimpleDateFormat simpleDateFormat;

    public MessagesAdapter(Chat chat) {
        super();
        this.chat = chat;
        data = chat.getMessagesList();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        holder.setText(data.get(position).getBody());
        holder.setLeftSide(chat.isLeftSide(data.get(position)));
        holder.tvMessageTime.setText(simpleDateFormat.format(new Date(data.get(position).getTime())));
        //holder.setLeftSide(true);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvMessageTime;
        ConstraintLayout clMessageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
            clMessageContainer = itemView.findViewById(R.id.flMessageContainer);
        }

        void setText(String text) {
            tvMessage.setText(text);
        }

        void setLeftSide(boolean side) {
            FrameLayout.LayoutParams frameLaoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            if (side) {
                frameLaoutParams.gravity = Gravity.LEFT;
                clMessageContainer.setBackgroundResource(R.drawable.message_left_drawable);
                frameLaoutParams.leftMargin = 20;
            } else {
                frameLaoutParams.gravity = Gravity.RIGHT;
                clMessageContainer.setBackgroundResource(R.drawable.message_right_drawable);
                frameLaoutParams.rightMargin = 20;
            }
            clMessageContainer.setLayoutParams(frameLaoutParams);
        }
    }
}
