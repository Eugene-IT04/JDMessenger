package com.jdcompany.jdmessenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.jdcompany.jdmessenger.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> messages;
    ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(messages == null) {
            messages = new ArrayList<>();
            messages.add("First");
            messages.add("dsfkfsd");
            messages.add("dfjdgfdf234g");
            messages.add("dsfk234fsd");
            messages.add("dfjdsdfjndsfnjfdsjsdfknldsfkldsfkmldsfmkldsfkmldsfmkldsfkmldsfmkldsfmkldsfmkldsfmkldsfmkldsfkmldsfmkgfdfg");
            messages.add("dsfk234987324789kjdsfjkdsfkjsdfkjdks\n jkdsfjkndsfjkdsfkjdsfjknfdsjknfdsjksdf324fsd");
            messages.add("dfjdgfdf234g");
            messages.add("dsfk234fsd");
            messages.add("dfjdgfdfg");
            messages.add("dsfkfsd");
            messages.add("dfjdgfdf234g");
            messages.add("dsfk234fsd");
            messages.add("dfjdgfdfg");
            messages.add("dsfkfsd");
            messages.add("dfjdgfdf234g");
            messages.add("dsfk234fsd");
            messages.add("dfjdgfdfg");
            messages.add("dsfkfsd");
            messages.add("dfjdgfdf234g");
            messages.add("dsfk234fsd");
            messages.add("dfjdgfdfg");
            messages.add("dsfkfsd");
            messages.add("dfjdgfdf234g");
            messages.add("Last");
        }
        if(chatFragment == null) {
            chatFragment = new ChatFragment(messages);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, chatFragment).commit();
        }
    }
}