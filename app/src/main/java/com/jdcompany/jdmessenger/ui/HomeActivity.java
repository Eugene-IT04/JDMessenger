package com.jdcompany.jdmessenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.User;
import com.jdcompany.jdmessenger.domain.ChatManager;
import com.jdcompany.jdmessenger.ui.fragments.ChatFragment;
import com.jdcompany.jdmessenger.ui.fragments.ChooseUserFragment;
import com.jdcompany.jdmessenger.ui.fragments.RegisterUserFragment;

public class HomeActivity extends AppCompatActivity {
    ChatFragment chatFragment;
    ChooseUserFragment chooseUserFragment;
    ChatManager chatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InternetService.startService(null);

        if(savedInstanceState == null) {
            chooseUserFragment = new ChooseUserFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new RegisterUserFragment())
                    .commit();
        }
    }

    //FIXME
    public void choseUserCallBack(User destination) {
        chatManager = ChatManager.getInstance(destination);
        //InternetService.startService(chatManager::updateMessages);
        chatFragment = new ChatFragment(chatManager.getChatList().get(0));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, chatFragment)
                .addToBackStack(null)
                .commit();
    }

    public void returnRegisteredUser(User user) {
    }
}