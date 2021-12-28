package ma.ensaf.bda.MainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ensaf.bda.Chat.Activities.ChatActivity;
import ma.ensaf.bda.Chat.Activities.MainChatActivity;
import ma.ensaf.bda.Chat.Listeners.UserListeners;
import ma.ensaf.bda.Login.LoginActivity;
import ma.ensaf.bda.Models.User;
import ma.ensaf.bda.R;
import ma.ensaf.bda.Utilities.UserAdapter;
import ma.ensaf.bda.databinding.ActivityMainBinding;

import static ma.ensaf.bda.Utilities.Constants.KEY_AVAILABILITY;
import static ma.ensaf.bda.Utilities.Constants.KEY_BLOOD_GROUP;
import static ma.ensaf.bda.Utilities.Constants.KEY_EMAIL;
import static ma.ensaf.bda.Utilities.Constants.KEY_NAME;
import static ma.ensaf.bda.Utilities.Constants.KEY_PROFILE_PIC;
import static ma.ensaf.bda.Utilities.Constants.KEY_TABLE;
import static ma.ensaf.bda.Utilities.Constants.KEY_TYPE;
import static ma.ensaf.bda.Utilities.Constants.KEY_USER;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserListeners {

    private ActivityMainBinding binding;

    private CircleImageView nav_profile_image;
    private TextView nav_fullname,nav_email,nav_bloodgroup,nav_type;

    private List<User> userList;
    private UserAdapter userAdapter;

    private boolean loggedIn=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialiseToolbar();
        populateHeader();
        populateRecyclerView();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private void populateRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this,userList,this);

        binding.recyclerView.setAdapter(userAdapter);

        //by This we get the user currently logged in
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(KEY_TABLE).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child(KEY_TYPE).getValue().toString();
                if(type.equals("donor")){
                    readReceipeints();
                }else{
                    readDonors();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readDonors() {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference()
                .child(KEY_TABLE);
        Query query=reference.orderByChild(KEY_TYPE).equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    showToast("No Donors");
                    binding.progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readReceipeints() {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference()
                .child(KEY_TABLE);
        Query query=reference.orderByChild(KEY_TYPE).equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    showToast("No Receipients");
                    binding.progressBar.setVisibility(View.GONE);
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialiseToolbar(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Blood Donation App");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                binding.drawerLayout,binding.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void populateHeader(){

        nav_profile_image= binding.navView.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname= binding.navView.getHeaderView(0).findViewById(R.id. nav_user_fullname);
        nav_email= binding.navView.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodgroup= binding.navView.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_type= binding.navView.getHeaderView(0).findViewById(R.id.nav_user_type);

        //To get Data from Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(KEY_TABLE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String name=snapshot.child(KEY_NAME).getValue().toString();
                    nav_fullname.setText(name);

                    String email=snapshot.child(KEY_EMAIL).getValue().toString();
                    nav_email.setText(email);

                    String blood_group=snapshot.child(KEY_BLOOD_GROUP).getValue().toString();
                    nav_bloodgroup.setText(blood_group);

                    String type=snapshot.child(KEY_TYPE).getValue().toString();
                    nav_type.setText(type);

                    //to upload the image using GLIDE(depandancy)
                    if(snapshot.hasChild(KEY_PROFILE_PIC)) {
                        String imageURL = snapshot.child(KEY_PROFILE_PIC).getValue().toString();
                        Glide.with(getApplicationContext()).load(imageURL).into(nav_profile_image);
                    }else{
                        nav_profile_image.setImageResource(R.drawable.profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.aplus:
                Intent intent3 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent3.putExtra("group","A+");
                startActivity(intent3);
                break;

            case R.id.aminus:
                Intent intent4 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent4.putExtra("group","A-");
                startActivity(intent4);
                break;

            case R.id.bplus:
                Intent intent5 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent5.putExtra("group","B+");
                startActivity(intent5);
                break;

            case R.id.bminus:
                Intent intent6 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent6.putExtra("group","B-");
                startActivity(intent6);
                break;

            case R.id.abminus:
                Intent intent7 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent7.putExtra("group","AB-");
                startActivity(intent7);
                break;

            case R.id.abplus:
                Intent intent8 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent8.putExtra("group","AB+");
                startActivity(intent8);
                break;

            case R.id.ominus:
                Intent intent9 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent9.putExtra("group","O-");
                startActivity(intent9);
                break;

            case R.id.oplus:
                Intent intent10 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent10.putExtra("group","O+");
                startActivity(intent10);
                break;

            case R.id.compatible:
                Intent intent11 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                intent11.putExtra("group","Compatible with me");
                startActivity(intent11);
                break;

            case R.id.profile:
                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent1);
                break;

            case R.id.logout:
                status("offline");
                loggedIn=false;
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.home:
                Intent intent12 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent12);
                break;

            case R.id.chat:
                Intent intent13 = new Intent(MainActivity.this, MainChatActivity.class);
                startActivity(intent13);
                break;



        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnUserClicked(User user) {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra(KEY_USER, user);
        startActivity(intent);
    }

    private void status(String status){
        if(loggedIn) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(KEY_TABLE).child(firebaseUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(KEY_AVAILABILITY, status);

            reference.updateChildren(hashMap);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}