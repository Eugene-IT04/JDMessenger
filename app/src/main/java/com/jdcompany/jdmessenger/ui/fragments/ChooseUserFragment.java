package com.jdcompany.jdmessenger.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.User;
import com.jdcompany.jdmessenger.ui.HomeActivity;

public class ChooseUserFragment extends Fragment implements View.OnClickListener {
    Button btnEugene, btnDenya;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDenya = view.findViewById(R.id.btnDenya);
        btnEugene = view.findViewById(R.id.btnEugene);
        btnDenya.setOnClickListener(this);
        btnEugene.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        User eugene = new User();
        eugene.setName("Eugene");
        eugene.setId(1);
        User denya = new User();
        denya.setName("Denya");
        denya.setId(10);
        boolean isDenya = false;
        switch(v.getId()){
            case R.id.btnDenya:
                isDenya = true;
                break;
            case R.id.btnEugene:
                isDenya = false;
                break;
        }
        if(isDenya){
            InfoLoader.getInstance().setCurrentUser(denya);
            ((HomeActivity)requireActivity()).choseUserCallBack(eugene);
        }
        else{
            InfoLoader.getInstance().setCurrentUser(eugene);
            ((HomeActivity)requireActivity()).choseUserCallBack(eugene);
        }
    }
}
