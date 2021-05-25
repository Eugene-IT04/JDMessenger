package com.jdcompany.jdmessenger.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.database.daos.UserDao;
import com.jdcompany.jdmessenger.domain.Chat;
import com.jdcompany.jdmessenger.domain.ImageConverter;
import com.jdcompany.jdmessenger.domain.callbacks.CallBackFailedSendMessage;
import com.jdcompany.jdmessenger.ui.HomeActivity;
import com.jdcompany.jdmessenger.ui.adapters.MessagesAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends BaseFragment implements View.OnClickListener {

    RecyclerView recyclerViewMessages;
    MessagesAdapter messagesAdapter;
    EditText etMessageText;
    ImageButton ibSendMessage;
    ImageButton ibPickImage;
    boolean editMessage = false;
    long editId = 0;

    MessageDao messageDao;
    UserDao userDao;

    Chat currentChat;

    CallBackFailedSendMessage callBackFailedSendMessage = t -> showToastMessage("Failed to send message");


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
        ibSendMessage = view.findViewById(R.id.btnSendMessage);
        ibPickImage = view.findViewById(R.id.ibPickImage);

        //config views
        ibSendMessage.setEnabled(false);
        ibSendMessage.setOnClickListener(this);
        ibPickImage.setOnClickListener(v -> {
            isReadStoragePermissionGranted();
            pickImage();
        });
        ibSendMessage.bringToFront();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        messagesAdapter = new MessagesAdapter();
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setAdapter(messagesAdapter);
        String destinationName = getArguments().getString("userName");
        if(destinationName != null)
        tvDestinationName.setText(destinationName);
        else throw new IllegalStateException("Need userName");
        etMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) ibSendMessage.setVisibility(View.VISIBLE);
                else {
                    ibSendMessage.setVisibility(View.GONE);
                    editMessage = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //get Dao-s
        userDao = AppDatabase.getInstance(getContext()).userDao();
        messageDao = AppDatabase.getInstance(getContext()).messageDao();

        long destinationId = getArguments().getLong("userId");

        //load user's profile from database and
        compositeDisposable.add(userDao.getUserById(destinationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    currentChat = new Chat(user, messageDao, new ImageConverter(), ((HomeActivity)requireActivity())::storeImage);
                    messagesAdapter.setChat(currentChat);

                    //update UI when user is loaded
                    ibSendMessage.setEnabled(true);
                }));

        //load messages from database
        compositeDisposable.add(
                messageDao.getAllForKey(destinationId + InfoLoader.getInstance().getCurrentUser().getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                            //update UI when messages are loaded
                            messagesAdapter.setMessagesCollection(list);
                        }));
    }

    @Override
    public void onClick(View v) {
        if(!editMessage) {
            String string = etMessageText.getText().toString().trim();
            if (!string.isEmpty())
                currentChat.sendTextMessage(string, callBackFailedSendMessage);
        }
        else {
            currentChat.sendEditTextMessage(etMessageText.getText().toString().trim(), editId, callBackFailedSendMessage);
            editMessage = false;
        }
        etMessageText.setText("");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = messagesAdapter.getPosition();
        switch (item.getItemId()){
            case R.id.optionDeleteMessageLocally:
                messageDao.delete(messagesAdapter.getData().get(position))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                break;
            case R.id.optionDeleteMessageGlobally:
                currentChat.sendDeleteMessageMessage(messagesAdapter.getData().get(position).getId(), callBackFailedSendMessage);
                break;
            case R.id.optionEditTextMessage:
                etMessageText.setText(messagesAdapter.getData().get(position).getBody());
                editMessage = true;
                editId = messagesAdapter.getData().get(position).getId();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            try {
                Bitmap newPhoto = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                if (newPhoto != null)
                    currentChat.sendPicture(newPhoto, callBackFailedSendMessage);
            } catch (Exception e){}
        }
    }
}

