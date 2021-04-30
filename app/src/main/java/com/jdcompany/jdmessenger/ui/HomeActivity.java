package com.jdcompany.jdmessenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.User;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.MessageDao;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class HomeActivity extends AppCompatActivity {

    MessageDao messageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("MYLOG", "onCreate");

        messageDao = AppDatabase.getInstance(this).messageDao();

        InternetService.startService(messages -> messageDao.insert(messages).subscribe());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.nav_graph);
        if (!readUserDataForInfoLoader()) graph.setStartDestination(R.id.registerUserFragment);
        else graph.setStartDestination(R.id.mainScreenFragment);
        navHostFragment.getNavController().setGraph(graph);
    }


    boolean readUserDataForInfoLoader() {
        User user;
        try {
            FileInputStream fileIn = openFileInput(InfoLoader.USER_DATA_FILE_NAME);
            user = (User) new ObjectInputStream(fileIn).readObject();
            InfoLoader.getInstance().setCurrentUser(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MYLOG", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MYLOG", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MYLOG", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MYLOG", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MYLOG", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MYLOG", "onDestroy");
    }
}