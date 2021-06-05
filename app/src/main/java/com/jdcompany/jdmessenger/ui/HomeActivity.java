package com.jdcompany.jdmessenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.network.IncomeMessagesChecker;
import com.jdcompany.jdmessenger.database.AppDatabase;
import com.jdcompany.jdmessenger.database.daos.MessageDao;
import com.jdcompany.jdmessenger.database.daos.UserDao;
import com.jdcompany.jdmessenger.domain.ImageConverter;
import com.jdcompany.jdmessenger.domain.IncomeMessagesHandler;
;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    MessageDao messageDao;
    UserDao userDao;
    IncomeMessagesHandler incomeMessagesHandler;
    IncomeMessagesChecker incomeMessagesChecker;
    SharedPreferences preferences;
    boolean isDarkModeNow = false;

    public final static String PREF_DARK_THEME = "isDarkTheme";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = getPreferences(MODE_PRIVATE);
        isDarkModeNow = preferences.getBoolean(PREF_DARK_THEME, false);
        setDartMode(isDarkModeNow);

        messageDao = AppDatabase.getInstance(this).messageDao();
        userDao = AppDatabase.getInstance(this).userDao();

        chooseAndStartFirstFragment();

        incomeMessagesHandler = new IncomeMessagesHandler(userDao, messageDao, this::storeImage, new ImageConverter());
        incomeMessagesChecker = new IncomeMessagesChecker(incomeMessagesHandler::handle);
        incomeMessagesChecker.start();
    }

    public void setAndSaveIsDarkMode(boolean isDarkMode){
        isDarkModeNow = isDarkMode;
        setDartMode(isDarkMode);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_DARK_THEME, isDarkMode);
        editor.apply();
    }

    public boolean getIsDarkMode(){
        return isDarkModeNow;
    }

    private void setDartMode(boolean isDarkMode){
        if(isDarkMode)AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(incomeMessagesChecker != null)
        incomeMessagesChecker.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(incomeMessagesChecker != null)
        incomeMessagesChecker.pause();
    }


    private void chooseAndStartFirstFragment() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.nav_graph);

        boolean managedReadUser;

        try {
            managedReadUser = InfoLoader.getInstance().readUserData(openFileInput(InfoLoader.USER_DATA_FILE_NAME));
        } catch (IOException e) {
            managedReadUser = false;
        }

        //if there is user's data in memory -> start mainScreenFragment
        //else -> start registerUserUserFragment
        if (managedReadUser) graph.setStartDestination(R.id.mainScreenFragment);
        else graph.setStartDestination(R.id.registerUserFragment);
        navHostFragment.getNavController().setGraph(graph);
    }

    public String storeImage(Bitmap imageData) {
        File storePath = this.getDir("images", MODE_PRIVATE);
        String filePath = storePath.toString() + File.separator + "img" + System.currentTimeMillis() + ".jpeg";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            return null;
        }
        return filePath;
    }
}