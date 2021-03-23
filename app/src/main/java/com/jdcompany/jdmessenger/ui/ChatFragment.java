package com.jdcompany.jdmessenger.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdcompany.jdmessenger.R;

import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView recyclerViewMessages;
    MessagesAdapter messagesAdapter;
    List<String> data;

    public ChatFragment(List<String> messages){
        data = messages;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(data);
        recyclerViewMessages.setAdapter(messagesAdapter);
    }
}

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>{
    List<String> data;

    public MessagesAdapter(List<String> data){
        super();
        this.data = data;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(v);
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        holder.setText(data.get(position));
        if(position % 2 == 0) holder.setLeftSide(true);
        else holder.setLeftSide(false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MessagesViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessage;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }

        void setText(String text){
            tvMessage.setText(text);
        }

        void setLeftSide(boolean side){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            if(side) {
                params.gravity = Gravity.LEFT;
                tvMessage.setBackgroundResource(R.drawable.message_left_drawable);
                params.leftMargin = 20;
            }
            else{
                params.gravity = Gravity.RIGHT;
                tvMessage.setBackgroundResource(R.drawable.message_right_drawable);
                params.rightMargin = 20;
            }
            tvMessage.setLayoutParams(params);
        }
    }
}
