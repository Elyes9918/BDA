package ma.ensaf.bda.MainApp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ma.ensaf.bda.Chat.Activities.BaseActivity;
import ma.ensaf.bda.Chat.Activities.ChatActivity;
import ma.ensaf.bda.Chat.Listeners.UserListeners;
import ma.ensaf.bda.Models.User;
import ma.ensaf.bda.Utilities.UserAdapter;
import ma.ensaf.bda.databinding.ActivityCategorySelectedBinding;

import static ma.ensaf.bda.Utilities.Constants.KEY_BLOOD_GROUP;
import static ma.ensaf.bda.Utilities.Constants.KEY_SEARCH;
import static ma.ensaf.bda.Utilities.Constants.KEY_TABLE;
import static ma.ensaf.bda.Utilities.Constants.KEY_TYPE;
import static ma.ensaf.bda.Utilities.Constants.KEY_USER;

public class CategorySelectedActivity extends BaseActivity implements UserListeners {

    ActivityCategorySelectedBinding binding;

    private List<User> userList;
    private UserAdapter userAdapter;
    private String title="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCategorySelectedBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        initialiseToolbar();
        initialiseRecyclerView();
        populateRecyclrerView();
    }


    private void initialiseToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initialiseRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void populateRecyclrerView() {
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(CategorySelectedActivity.this,userList, this);
        binding.recyclerView.setAdapter(userAdapter);

        if(getIntent().getExtras() != null){
            title = getIntent().getStringExtra("group");
            
            if(title.equals("Compatible with me")){
                getCompatibleUsers();
                getSupportActionBar().setTitle("Compatible with me" );
            }else{
                getSupportActionBar().setTitle("Blood group "+title );
                readUsers();
            }

        }
    }

    private void getCompatibleUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(KEY_TABLE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type = snapshot.child(KEY_TYPE).toString();

                if(type.equals("donor")){
                    result="donor";
                }else{
                    result="recipient";
                }

                String bloodgroup = snapshot.child(KEY_BLOOD_GROUP).getValue().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference().child(KEY_TABLE);
                Query query=reference.orderByChild(KEY_SEARCH).equalTo(result+bloodgroup);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(KEY_TABLE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type = snapshot.child(KEY_TYPE).toString();
                if(type.equals("donor")){
                    result="recipient";
                }else{
                    result="donor";
                }

                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference().child(KEY_TABLE);
                Query query=reference.orderByChild(KEY_SEARCH).equalTo(result+title);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //to get directed back to the first activity when you tap on the back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void OnUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(KEY_USER, user);
        startActivity(intent);
    }

}