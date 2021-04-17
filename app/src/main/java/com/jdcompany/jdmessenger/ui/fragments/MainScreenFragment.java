package com.jdcompany.jdmessenger.ui.fragments;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MainScreenFragment extends Fragment {


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
