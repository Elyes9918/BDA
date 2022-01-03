package ma.ensaf.bda.activities.chat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.ensaf.bda.adapters.UserAdapterChat;
import ma.ensaf.bda.databinding.ActivityMainChatBinding;
import ma.ensaf.bda.listeners.UserListener;
import ma.ensaf.bda.models.Message;
import ma.ensaf.bda.models.User;
import ma.ensaf.bda.utilities.Constants;

public class MainChatActivity extends BaseActivity implements UserListener {

    ActivityMainChatBinding binding;


    private List<User> userList;
    private List<String> userListId;
    private UserAdapterChat userChatAdapter;

    private FirebaseUser senderUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        init();
        getToken();
        intialiseToolbar();
        populateRecyclerView();
    }

    private void init(){
        senderUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::UpdateToken);
    }
    private void UpdateToken(String token){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.KEY_COLLECTION_USERS).child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.KEY_FCM_TOKEN, token);

        reference.updateChildren(hashMap);
    }

    private void populateRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userListId = new ArrayList<>();
        readUsers();
        userChatAdapter = new UserAdapterChat(MainChatActivity.this,userList,this);
        binding.recyclerView.setAdapter(userChatAdapter);
    }


    private void readUsers(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_TABLE_CHAT);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userListId.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getSenderId().equals(senderUser.getUid())) {
                        userListId.add(message.getReceiverId());
                    }
                    if (message.getReceiverId().equals(senderUser.getUid())) {
                        userListId.add(message.getSenderId());
                    }

                }
                UsersWhichTalkedtoCurrentId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // updateToken(FirebaseMessaging.getInstance().getToken().toString());

    }

    private void UsersWhichTalkedtoCurrentId() {

        DatabaseReference reference =FirebaseDatabase.getInstance().getReference()
                .child(Constants.KEY_COLLECTION_USERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    for (String id : userListId) {
                        if (user.getId().equals(id)) {
                            if(!userList.contains(user)){
                                userList.add(user);
                            }
                        }
                    }
                }
                userChatAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void intialiseToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onUserClicked(User user) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(MainChatActivity.this, ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        binding.progressBar.setVisibility(View.GONE);
    }


}
