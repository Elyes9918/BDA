package ma.ensaf.bda.Chat.Activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import ma.ensaf.bda.Models.User;
import ma.ensaf.bda.databinding.ActivityReceiverProfileBinding;

import static ma.ensaf.bda.Utilities.Constants.KEY_USER;

public class ReceiverProfile extends BaseActivity {

    private ActivityReceiverProfileBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiverProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiveUserInfo();
        initiliaseToolbar();
        setListeners();
        populateItems();

    }


    private void receiveUserInfo(){
        receiverUser = (User) getIntent().getSerializableExtra(KEY_USER);
    }

    private void populateItems() {

        binding.type.setText(receiverUser.getType());
        binding.name.setText(receiverUser.getName());
        binding.idNumber.setText(receiverUser.getIdnumber());
        binding.email.setText(receiverUser.getEmail());
        binding.phoneNumber.setText(receiverUser.getPhonenumber());
        binding.bloodgroup.setText(receiverUser.getBloodgroup());
        Glide.with(getApplicationContext()).load(receiverUser.getProfilepictureurl()).into(binding.profileImage);

    }

    private void initiliaseToolbar(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(receiverUser.getName()+" Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setListeners() {
        binding.backButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
            finish();
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