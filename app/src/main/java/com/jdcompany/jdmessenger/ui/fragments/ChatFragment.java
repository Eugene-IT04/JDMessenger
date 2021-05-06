package com.jdcompany.jdmessenger.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.MessageDao;
import com.jdcompany.jdmessenger.database.UserDao;
import com.jdcompany.jdmessenger.domain.Chat;
import com.jdcompany.jdmessenger.ui.adapters.MessagesAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChatFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerViewMessages;
    MessagesAdapter messagesAdapter;
    Chat currentChat;
    Context context;
    EditText etMessageText;
    ImageButton ibSendMessage;
    MessageDao messageDao;
    UserDao userDao;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //find views
        TextView tvDestinationName = view.findViewById(R.id.tvChatName);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        etMessageText = view.findViewById(R.id.editText);
        ibSendMessage = view.findViewById(R.id.ibSendButton);

        //config views
        ibSendMessage.setEnabled(false);
        ibSendMessage.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        messagesAdapter = new MessagesAdapter();
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setAdapter(messagesAdapter);

        //get Dao-s
        userDao = AppDatabase.getInstance(getContext()).userDao();
        messageDao = AppDatabase.getInstance(getContext()).messageDao();

        long destinationId = getArguments().getLong("userId");

        //load user's profile from database and
        compositeDisposable.add(userDao.getUserById(destinationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    currentChat = new Chat(user, messageDao);
                    messagesAdapter.setChat(currentChat);

                    //update UI when user is loaded
                    ibSendMessage.setEnabled(true);
                    tvDestinationName.setText(currentChat.getDestination().getName());
                }));

        //load messages from database
        compositeDisposable.add(
                messageDao.getAllForKey(destinationId + InfoLoader.getInstance().getCurrentUser().getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                            messagesAdapter.setMessagesCollection(list);
                        }));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onClick(View v) {
        String string = etMessageText.getText().toString();
        currentChat.sendTextMessage(string);
        etMessageText.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(compositeDisposable != null && !compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }
}

