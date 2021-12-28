package ma.ensaf.bda.Chat.UtilitiesChat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ma.ensaf.bda.Chat.Listeners.UserListeners;
import ma.ensaf.bda.Models.Message;
import ma.ensaf.bda.Models.User;
import ma.ensaf.bda.databinding.UserDisplayedChatLayoutBinding;

import static ma.ensaf.bda.Utilities.Constants.KEY_TABLE_CHAT;

public class UserAdapterChat extends RecyclerView.Adapter<UserAdapterChat.UserChatViewHolder> {

    private Context context;
    private List<User> userList;
    private static UserListeners userListeners;

    private String lastMessage;


    public UserAdapterChat(Context context, List userList,UserListeners userListeners) {
        this.context = context;
        this.userList = userList;
        this.userListeners = userListeners;
    }


    @NonNull
    @Override
    public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UserDisplayedChatLayoutBinding userDisplayedChatLayoutBinding = UserDisplayedChatLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent,
                false
        );
        return new UserChatViewHolder(userDisplayedChatLayoutBinding);

    }


    @Override
    public void onBindViewHolder(@NonNull UserChatViewHolder holder, int position) {

        holder.setUserData(userList.get(position));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public  class UserChatViewHolder extends RecyclerView.ViewHolder{

        UserDisplayedChatLayoutBinding binding;

        public UserChatViewHolder(UserDisplayedChatLayoutBinding userDisplayedChatLayoutBinding) {
            super(userDisplayedChatLayoutBinding.getRoot());
            binding = userDisplayedChatLayoutBinding;
        }

        public void setUserData(User user){
            Glide.with(context).load(user.getProfilepictureurl()).into(binding.userPorfileImageChat);
            binding.userNameChat.setText(user.getName());
            lastMessage(user.getId());


            if(user.getAvailability().equals("online")){
                binding.userPorfileImageChat.setBorderColor(Color.parseColor("#45FF00"));
                binding.userPorfileImageChat.setBorderWidth(4);
            }else{
                binding.userPorfileImageChat.setBorderColor(Color.parseColor("#FFBE50"));
                binding.userPorfileImageChat.setBorderColor(4);
            }

            binding.getRoot().setOnClickListener(v -> userListeners.OnUserClicked(user));
        }

        private void lastMessage(String userid){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_TABLE_CHAT);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Message message =dataSnapshot.getValue(Message.class);

                        if(message.getReceiverId().equals(firebaseUser.getUid()) && message.getSenderId().equals(userid) ||
                                message.getReceiverId().equals(userid) && message.getSenderId().equals(firebaseUser.getUid()) ){
                            lastMessage = message.getMessage();
                        }
                    }
                    if(lastMessage==null){ lastMessage=""; }
                    binding.LastMessage.setText(lastMessage);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}
