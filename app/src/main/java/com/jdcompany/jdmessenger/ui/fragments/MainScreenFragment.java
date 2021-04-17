package com.jdcompany.jdmessenger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;

public class MainScreenFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    static class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        static class UserViewHolder extends RecyclerView.ViewHolder{

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
