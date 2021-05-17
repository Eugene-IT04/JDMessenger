package com.jdcompany.jdmessenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.data.network.IncomeMessagesChecker;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.database.daos.UserDao;
import com.jdcompany.jdmessenger.domain.IncomeMessagesHandler;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class HomeActivity extends AppCompatActivity {

    MessageDao messageDao;
    UserDao userDao;
    IncomeMessagesHandler incomeMessagesHandler;
    IncomeMessagesChecker incomeMessagesChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        messageDao = AppDatabase.getInstance(this).messageDao();
        userDao = AppDatabase.getInstance(this).userDao();
        incomeMessagesHandler = new IncomeMessagesHandler(userDao, messageDao);

        chooseAndStartFirstFragment();

        incomeMessagesChecker = new IncomeMessagesChecker(incomeMessagesHandler::handle);
        incomeMessagesChecker.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        incomeMessagesChecker.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        incomeMessagesChecker.pause();
    }

    private void chooseAndStartFirstFragment(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.nav_graph);

        //if there is user's data in memory -> start mainScreenFragment
        //else -> start registerUserUserFragment
        if (!readUserDataForInfoLoader()) graph.setStartDestination(R.id.registerUserFragment);
        else graph.setStartDestination(R.id.mainScreenFragment);
        navHostFragment.getNavController().setGraph(graph);
    }

    boolean readUserDataForInfoLoader() {
        User user;
        try {
            FileInputStream fileIn = openFileInput(InfoLoader.USER_DATA_FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileIn);
            user = (User) objectInputStream.readObject();
            InfoLoader.getInstance().setCurrentUser(user);
            objectInputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}