package com.x64technology.linex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.x64technology.linex.R;
import com.x64technology.linex.databinding.LayoutChatBinding;
import com.x64technology.linex.interfaces.MainToChat;
import com.x64technology.linex.models.Chat;
import com.x64technology.linex.utils.diffUtils.ChatDiffUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>{
    Context context;
    List<Chat> chats = new ArrayList<>();
    MainToChat mainToChat;


    public ChatsAdapter(Context context) {
        this.context = context;
        this.mainToChat = (MainToChat) context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.chatBinding.nameTextview.setText(chat.name);
        holder.chatBinding.lastmsgTextView.setText(chat.lastMsg);
        holder.chatBinding.dateTextview.setText(chat.lastMsgTime);
        if (chat.unreadCount == 0)
            holder.chatBinding.msgCountTextview.setVisibility(View.GONE);
        else
            holder.chatBinding.msgCountTextview.setText(String.valueOf(chat.unreadCount));

        holder.itemView.setOnClickListener(view -> mainToChat.onChatClicked(chat));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void setChats(List<Chat> newChats){
        ChatDiffUtil chatDiffUtil = new ChatDiffUtil(chats, newChats);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(chatDiffUtil);

        chats.clear();
        chats.addAll(newChats);

        result.dispatchUpdatesTo(this);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        LayoutChatBinding chatBinding ;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatBinding = LayoutChatBinding.bind(itemView);
        }
    }

}
