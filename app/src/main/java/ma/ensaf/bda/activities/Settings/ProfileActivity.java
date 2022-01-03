package ma.ensaf.bda.activities.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ma.ensaf.bda.R;
import ma.ensaf.bda.activities.chat.BaseActivity;
import ma.ensaf.bda.activities.chat.ChatActivity;
import ma.ensaf.bda.databinding.ActivityProfileBinding;
import ma.ensaf.bda.listeners.UserListener;
import ma.ensaf.bda.models.User;
import ma.ensaf.bda.utilities.Constants;

public class ProfileActivity extends BaseActivity implements UserListener {

    ActivityProfileBinding binding;

    UserListener messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init()
    {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setListeners()
    {
        if(getIntent().getExtras() != null)
        {
            binding.editProfile.setVisibility(View.GONE);
            loadUserDetails();
        } else {

            fetchDataListener();
            binding.editProfile.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                finish();
            });
        }



    }

    private void loadUserDetails()
    {
        User user = (User) getIntent().getSerializableExtra(Constants.KEY_USER);

        binding.type.setText(user.getType());
        binding.name.setText(user.getName());
        binding.email.setText(user.getEmail());
        binding.bloodGroup.setText(user.getBloodGroup());

        if(user.getPhoneNumber() != null)
        {
            binding.phoneNumber.setVisibility(View.VISIBLE);
            binding.phoneNumber.setText(user.getPhoneNumber());
        }

        if(user.isAnonymous())
        {
            binding.profileImage.setImageResource(R.drawable.profile_image);
            binding.name.setText(R.string.anonymous_donor);
            binding.phoneNumber.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(user.getProfilePictureUrl())
                    .into(binding.profileImage);
        }

        if(user.getType().equals("donor")){
            binding.contactButton.setVisibility(View.VISIBLE);
        }

        binding.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Contact this Person?")
                        .setMessage("Contact " + user.getName() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                onUserClicked(user);

                            }})
                        .setNegativeButton("No", null)
                        .show();

            }
        });

    }


    private void fetchDataListener()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.KEY_COLLECTION_USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    binding.type.setText(snapshot.child(Constants.KEY_TYPE).getValue().toString());
                    binding.name.setText(snapshot.child(Constants.KEY_NAME).getValue().toString());
                    binding.bloodGroup.setText(snapshot.child(Constants.KEY_BLOOD_GROUP).getValue().toString());
                    binding.email.setText(snapshot.child(Constants.KEY_EMAIL).getValue().toString());

                    if(snapshot.child(Constants.KEY_PROFILE_PICTURE_URL).getValue() != null)
                    {
                        Glide.with(getApplicationContext())
                                .load(snapshot.child(Constants.KEY_PROFILE_PICTURE_URL)
                                        .getValue().toString())
                                .into(binding.profileImage);
                    }

                    if(snapshot.child(Constants.KEY_PHONE_NUMBER).getValue() != null)
                    {
                        binding.phoneNumber.setText(snapshot.child(Constants.KEY_PHONE_NUMBER).getValue().toString());
                        binding.phoneNumber.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}