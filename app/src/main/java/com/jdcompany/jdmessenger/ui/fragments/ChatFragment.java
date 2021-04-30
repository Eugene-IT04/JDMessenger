package com.jdcompany.jdmessenger.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.Message;
import com.jdcompany.jdmessenger.domain.Chat;
import com.jdcompany.jdmessenger.ui.adapters.MessagesAdapter;

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

    public ChatFragment() {
    }

    public ChatFragment(Chat chat) {
        this.chat = chat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        TextView tv = view.findViewById(R.id.tvChatName);
//        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
//        etMessage = view.findViewById(R.id.editText);
//        imageButton = view.findViewById(R.id.ibSendButton);
//
//        tv.setText(chat.getDestination().getName());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
//        linearLayoutManager.setReverseLayout(true);
//        recyclerViewMessages.setLayoutManager(linearLayoutManager);
//        messagesAdapter = new MessagesAdapter(chat);
//        chat.setCallBackUpdate(() -> getActivity().runOnUiThread(()-> messagesAdapter.notifyDataSetChanged()));
//        recyclerViewMessages.setAdapter(messagesAdapter);
//        imageButton.setOnClickListener(this);
//    }

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
}

