package com.jdcompany.jdmessenger.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.InfoLoader;
import com.jdcompany.jdmessenger.data.User;

import java.util.List;

public class MainScreenFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvUsers);
        recyclerView.setAdapter(new UsersAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    static class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
        List<User> data;

        public UsersAdapter(){
            data = InfoLoader.getInstance().getAllUsers();
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
            return new UserViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            holder.tvUserName.setText(data.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class UserViewHolder extends RecyclerView.ViewHolder {

            ImageView ivUserPicture;
            TextView tvUserName;
            TextView tvLastMessage;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                ivUserPicture = itemView.findViewById(R.id.ivUserPicture);
                tvUserName = itemView.findViewById(R.id.tvUserName);
                tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            }
        }
    }

}
