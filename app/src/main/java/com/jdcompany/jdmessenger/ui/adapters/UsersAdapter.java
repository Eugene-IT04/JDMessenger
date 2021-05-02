package com.jdcompany.jdmessenger.ui.adapters;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;
import com.jdcompany.jdmessenger.data.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    public interface OnItemClickListener {
        void onUserItemClicked(User userModel);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    List<User> data;
    private int position;

    public int getPosition(){
        return position;
    }

    public List<User> getData(){
        return data;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public UsersAdapter(){
        data = new ArrayList<>();
    }

    public void setUsersCollection(List<User> data){
        this.data = data;;
        notifyDataSetChanged();
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
        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
        holder.itemView.setOnClickListener(v -> {
            if (UsersAdapter.this.onItemClickListener != null)
                UsersAdapter.this.onItemClickListener.onUserItemClicked(data.get(position));
        });

    }

    @Override
    public void onViewRecycled(@NonNull UserViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView ivUserPicture;
        TextView tvUserName;
        TextView tvLastMessage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPicture = itemView.findViewById(R.id.ivUserPicture);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.optionDeleteUser, Menu.NONE, R.string.optionDeleteUser);
            menu.add(Menu.NONE, R.id.optionClearChat, Menu.NONE, R.string.optionClearChat);
        }
    }
}
