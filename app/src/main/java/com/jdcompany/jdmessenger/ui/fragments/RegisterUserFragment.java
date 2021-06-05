package com.jdcompany.jdmessenger.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.callbacks.CallBackRegisterUser;
import com.jdcompany.jdmessenger.data.network.InternetService;
import com.jdcompany.jdmessenger.data.objects.User;
import com.jdcompany.jdmessenger.domain.ImageConverter;
import com.jdcompany.jdmessenger.ui.HomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import static android.app.Activity.RESULT_OK;

public class RegisterUserFragment extends BaseFragment implements View.OnClickListener, CallBackRegisterUser {

    Button btnSignUp;
    EditText etSignUpName, etSignUpTag;
    ImageButton ibEditUserPhoto;
    boolean isUpdate = false;
    Bitmap currentPhoto;
    boolean photoChanged = false;
    Switch swDartMode;

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
        ibEditUserPhoto = view.findViewById(R.id.ibEditUserPhoto);
        swDartMode = view.findViewById(R.id.swDartMode);
        swDartMode.setChecked(((HomeActivity)requireActivity()).getIsDarkMode());
        swDartMode.setOnCheckedChangeListener((buttonView, isChecked) -> ((HomeActivity)requireActivity()).setAndSaveIsDarkMode(isChecked));
        ibEditUserPhoto.setOnClickListener(this::ibChoosePhotoClick);
        btnSignUp.setOnClickListener(this);
        if(getArguments() != null)
            isUpdate = getArguments().getBoolean("isUpdate", false);
        if (isUpdate) {
            btnSignUp.setText("Update");
            TextView tvHeader = view.findViewById(R.id.tvHeader);
            tvHeader.setText("Update profile");
            User currentUser = InfoLoader.getInstance().getCurrentUser();
            etSignUpName.setText(currentUser.getName());
            etSignUpTag.setText(currentUser.getTag());
            if (currentUser.getPhoto() != null && !currentUser.getPhoto().equals("")) {
                try {
                    File file = new File(currentUser.getPhoto());
                    InputStream is = new FileInputStream(file);
                    currentPhoto = BitmapFactory.decodeStream(is);
                    is.close();
                    ibEditUserPhoto.setImageURI(Uri.parse(currentUser.getPhoto()));
                } catch (Exception e) {
                    Log.d("MYLOG", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (etSignUpName.getText().toString().isEmpty() || etSignUpTag.getText().toString().isEmpty()) {
            showToastMessage("Incorrect name or tag");
        } else {
            if (!isUpdate) {
                btnSignUp.setEnabled(false);
                User user = new User();
                user.setName(etSignUpName.getText().toString());
                user.setTag(etSignUpTag.getText().toString());
                if(currentPhoto != null){
                    ImageConverter imageConverter = new ImageConverter();
                    user.setPhoto(imageConverter.bitmapToString(currentPhoto));
                }
                InternetService.getInstance().registerUser(user, this);
            } else {
                btnSignUp.setEnabled(false);
                User user = new User();
                user.setId(InfoLoader.getInstance().getCurrentUser().getId());
                user.setName(etSignUpName.getText().toString());
                user.setTag(etSignUpTag.getText().toString());
                if(currentPhoto != null){
                    ImageConverter imageConverter = new ImageConverter();
                    user.setPhoto(imageConverter.bitmapToString(currentPhoto));
                }
                InternetService.getInstance().updateUser(user, this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            try {
                currentPhoto = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                photoChanged = true;
                ibEditUserPhoto.setImageBitmap(currentPhoto);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onUserRegistered(User user) {
        if(isUpdate){
            user.setPhoto(InfoLoader.getInstance().getCurrentUser().getPhoto());
        }
        if(photoChanged && currentPhoto != null) {
            String photoPath = ((HomeActivity)requireActivity()).storeImage(currentPhoto);
            user.setPhoto(photoPath);
        }
        InfoLoader.getInstance().setCurrentUser(user);
        saveUserDataForInfoLoader(user);
        hideKeyboard();
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_registerUserFragment_to_mainScreenFragment);
    }

    private void ibChoosePhotoClick(View v) {
        pickImage();
    }

    @Override
    protected void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
