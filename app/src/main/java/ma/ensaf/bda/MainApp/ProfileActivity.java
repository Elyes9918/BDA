package ma.ensaf.bda.MainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ma.ensaf.bda.Chat.Activities.BaseActivity;
import ma.ensaf.bda.databinding.ActivityProfileBinding;

import static ma.ensaf.bda.Utilities.Constants.KEY_AVAILABILITY;
import static ma.ensaf.bda.Utilities.Constants.KEY_BLOOD_GROUP;
import static ma.ensaf.bda.Utilities.Constants.KEY_EMAIL;
import static ma.ensaf.bda.Utilities.Constants.KEY_ID_NUMBER;
import static ma.ensaf.bda.Utilities.Constants.KEY_NAME;
import static ma.ensaf.bda.Utilities.Constants.KEY_PHONE_NUMBER;
import static ma.ensaf.bda.Utilities.Constants.KEY_PROFILE_PIC;
import static ma.ensaf.bda.Utilities.Constants.KEY_TABLE;
import static ma.ensaf.bda.Utilities.Constants.KEY_TYPE;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initiliaseToolbar();
        populateItems();
        setListeners();
    }

    private void initiliaseToolbar(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setListeners(){
        binding.backButton.setOnClickListener(view ->{
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void populateItems(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child(KEY_TABLE).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    binding.type.setText(snapshot.child(KEY_TYPE).getValue().toString());
                    binding.name.setText(snapshot.child(KEY_NAME).getValue().toString());
                    binding.idNumber.setText(snapshot.child(KEY_ID_NUMBER).getValue().toString());
                    binding.email.setText(snapshot.child(KEY_EMAIL).getValue().toString());
                    binding.bloodgroup.setText(snapshot.child(KEY_BLOOD_GROUP).getValue().toString());
                    binding.phoneNumber.setText(snapshot.child(KEY_PHONE_NUMBER).getValue().toString());


                    String imageURL = snapshot.child(KEY_PROFILE_PIC).getValue().toString();
                    Glide.with(getApplicationContext()).load(imageURL).into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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


}