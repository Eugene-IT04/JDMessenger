package com.jdcompany.jdmessenger.ui.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.domain.Chat;
import com.jdcompany.jdmessenger.domain.ChatManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerViewMessages;
    MessagesAdapter messagesAdapter;
    Chat chat;
    Context context;
    EditText etMessage;
    ImageButton imageButton;

    public ChatFragment(Chat chat){
        this.chat = chat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tv = view.findViewById(R.id.tvChatName);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        etMessage = view.findViewById(R.id.editText);
        imageButton = view.findViewById(R.id.ibSendButton);

        tv.setText(chat.getDestination().getName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(chat);
        chat.setCallBackUpdate(() -> getActivity().runOnUiThread(()-> messagesAdapter.notifyDataSetChanged()));
        recyclerViewMessages.setAdapter(messagesAdapter);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        String string = etMessage.getText().toString();
            chat.sendTextMessage(string);
            etMessage.setText("");

    }

    static class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>{
        List<Message> data;
        Chat chat;
        SimpleDateFormat simpleDateFormat;

        public MessagesAdapter(Chat chat){
            super();
            this.chat = chat;
            data = chat.getMessagesList();
            simpleDateFormat = new SimpleDateFormat("HH:mm");
        }

        @NonNull
        @Override
        public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
            return new MessagesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {

            holder.setText(data.get(position).getBody());
            holder.setLeftSide(chat.isLeftSide(data.get(position)));
            holder.tvMessageTime.setText(simpleDateFormat.format(new Date(data.get(position).getTime())));
            //holder.setLeftSide(true);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class MessagesViewHolder extends RecyclerView.ViewHolder{
            TextView tvMessage;
            TextView tvMessageTime;
            ConstraintLayout clMessageContainer;

            public MessagesViewHolder(@NonNull View itemView) {
                super(itemView);
                tvMessage = itemView.findViewById(R.id.tvMessage);
                tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
                clMessageContainer = itemView.findViewById(R.id.flMessageContainer);
            }

            void setText(String text){
                tvMessage.setText(text);
            }

            void setLeftSide(boolean side){
                FrameLayout.LayoutParams frameLaoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                if(side) {
                    frameLaoutParams.gravity = Gravity.LEFT;
                    clMessageContainer.setBackgroundResource(R.drawable.message_left_drawable);
                    frameLaoutParams.leftMargin = 20;
                }
                else{
                    frameLaoutParams.gravity = Gravity.RIGHT;
                    clMessageContainer.setBackgroundResource(R.drawable.message_right_drawable);
                    frameLaoutParams.rightMargin = 20;
                }
                clMessageContainer.setLayoutParams(frameLaoutParams);
            }
        }
    }
}

