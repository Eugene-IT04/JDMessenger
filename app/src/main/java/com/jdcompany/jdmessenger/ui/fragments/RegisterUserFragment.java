package com.jdcompany.jdmessenger.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.callbacks.CallBackRegisterUser;
import com.jdcompany.jdmessenger.data.InternetService;
import com.jdcompany.jdmessenger.data.User;

public class RegisterUserFragment extends Fragment implements View.OnClickListener, CallBackRegisterUser {
    Button btnSignUp;
    EditText etSignUpName, etSignUpTag;
    Context context;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void onClick(View v) {
        if (etSignUpName.getText().toString().isEmpty() || etSignUpTag.getText().toString().isEmpty()) {
            Toast.makeText(context, "Incorrect name or tag", Toast.LENGTH_SHORT).show();
        } else {
            btnSignUp.setEnabled(false);
            User user = new User();
            user.setName(etSignUpName.getText().toString());
            user.setTag(etSignUpTag.getText().toString());
            InternetService.getInstance().tryRegisterUser(user, this);
        }

    }

    @Override
    public void onUserRegistered(User user) {
        btnSignUp.setEnabled(true);
        //
        //add register user
        //
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_registerUserFragment_to_chooseUserFragment);
    }

    @Override
    public void onUserTagIsTaken() {
        btnSignUp.setEnabled(true);
        Toast.makeText(context, "Tag is already taken", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure() {
        btnSignUp.setEnabled(true);
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();


        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_registerUserFragment_to_chooseUserFragment);
    }
}
