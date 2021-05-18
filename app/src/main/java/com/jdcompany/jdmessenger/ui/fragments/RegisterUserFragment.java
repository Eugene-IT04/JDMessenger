package com.jdcompany.jdmessenger.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.callbacks.CallBackRegisterUser;
import com.jdcompany.jdmessenger.data.network.InternetService;
import com.jdcompany.jdmessenger.data.objects.User;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class RegisterUserFragment extends BaseFragment implements View.OnClickListener, CallBackRegisterUser {

    Button btnSignUp;
    EditText etSignUpName, etSignUpTag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        etSignUpName = view.findViewById(R.id.etSignUpName);
        etSignUpTag = view.findViewById(R.id.etSignUpTag);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (etSignUpName.getText().toString().isEmpty() || etSignUpTag.getText().toString().isEmpty()) {
            showToastMessage("Incorrect name or tag");
        } else {
            btnSignUp.setEnabled(false);
            User user = new User();
            user.setName(etSignUpName.getText().toString());
            user.setTag(etSignUpTag.getText().toString());
            InternetService.getInstance().registerUser(user, this);
        }

    }

    @Override
    public void onUserRegistered(User user) {
        btnSignUp.setEnabled(true);
        InfoLoader.getInstance().setCurrentUser(user);
        saveUserDataForInfoLoader(user);
        hideKeyboard();
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_registerUserFragment_to_mainScreenFragment);
    }

    @Override
    public void onUserTagIsTaken() {
        btnSignUp.setEnabled(true);
        showToastMessage("Tag is already taken");
    }

    @Override
    public void onFailure() {
        btnSignUp.setEnabled(true);
        showToastMessage("Something went wrong");
    }

    private void saveUserDataForInfoLoader(User user) {
        try {
            FileOutputStream fos = context.openFileOutput(InfoLoader.USER_DATA_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
        } catch (Exception e) {
            showToastMessage("Failed to save data");
        }
    }
}
