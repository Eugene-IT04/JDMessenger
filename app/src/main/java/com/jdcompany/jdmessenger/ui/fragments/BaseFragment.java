package com.jdcompany.jdmessenger.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;

abstract public class BaseFragment extends Fragment {

    public static final int PICK_IMAGE = 1;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Context context;


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
    public void onDestroyView() {
        super.onDestroyView();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    protected void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    protected void showToastMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard(){
        Activity activity = requireActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else {
            return true;

        }
    }
}
