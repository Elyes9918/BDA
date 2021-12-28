package ma.ensaf.bda.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ma.ensaf.bda.Login.LoginActivity;
import ma.ensaf.bda.MainApp.MainActivity;
import ma.ensaf.bda.databinding.ActivityRecipientRegistrationBinding;

import static ma.ensaf.bda.Utilities.Constants.KEY_BLOOD_GROUP;
import static ma.ensaf.bda.Utilities.Constants.KEY_EMAIL;
import static ma.ensaf.bda.Utilities.Constants.KEY_ID;
import static ma.ensaf.bda.Utilities.Constants.KEY_ID_NUMBER;
import static ma.ensaf.bda.Utilities.Constants.KEY_NAME;
import static ma.ensaf.bda.Utilities.Constants.KEY_PHONE_NUMBER;
import static ma.ensaf.bda.Utilities.Constants.KEY_PROFILE_PIC;
import static ma.ensaf.bda.Utilities.Constants.KEY_SEARCH;
import static ma.ensaf.bda.Utilities.Constants.KEY_TABLE;
import static ma.ensaf.bda.Utilities.Constants.KEY_TYPE;

public class RecipientRegistrationActivity extends AppCompatActivity {

    private ActivityRecipientRegistrationBinding binding;

    private Uri resultUri;
    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipientRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        setListeners();
    }

    private void setListeners() {

        binding.backButton.setOnClickListener(view -> {
            Intent intent = new Intent(RecipientRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,1);
        });

        binding.RegisterButton.setOnClickListener(view -> verifyAndRegister());

    }

    //To get the Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            resultUri = data.getData();
            binding.profileImage.setImageURI(resultUri);
        }
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void verifyAndRegister(){
        final String email = binding.registerEmail.getText().toString().trim();
        final String password = binding.registerPassword.getText().toString().trim();
        final String fullname = binding.registerFullName.getText().toString().trim();
        final String idNumber = binding.registerIdNumber.getText().toString().trim();
        final String phoneNumber = binding.registerPhoneNumber.getText().toString().trim();
        final String bloodGroup = binding.bloodGroupsSpinner.getSelectedItem().toString();

        if(TextUtils.isEmpty(email)){
            binding.registerEmail.setError("Email is required");
            return;
        }

        if(TextUtils.isEmpty(password)){
            binding.registerPassword.setError("Password is required");
            return;
        }

        if(TextUtils.isEmpty(fullname)){
            binding.registerFullName.setError("Name is required");
            return;
        }

        if(TextUtils.isEmpty(idNumber)){
            binding.registerIdNumber.setError("Id is required");
            return;
        }

        if(TextUtils.isEmpty(phoneNumber)){
            binding.registerPhoneNumber.setError("Phone is required");
            return;
        }

        if(bloodGroup.equals("Select your blood group")){
            showToast("Select Blood Group Please");
        }

        else{
            loader.setMessage("Registering you ...");
            loader.setCancelable(false);
            loader.show();
            RegesteringDonor();
        }
    }

    public void RegesteringDonor(){

        final String email = binding.registerEmail.getText().toString().trim();
        final String password = binding.registerPassword.getText().toString().trim();
        final String fullname = binding.registerFullName.getText().toString().trim();
        final String idNumber = binding.registerIdNumber.getText().toString().trim();
        final String phoneNumber = binding.registerPhoneNumber.getText().toString().trim();
        final String bloodGroup = binding.bloodGroupsSpinner.getSelectedItem().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    String error= task.getException().toString();
                    showToast("Error: "+error);
                }else{
                    String currentUserid = mAuth.getCurrentUser().getUid();
                    userDataBaseRef = FirebaseDatabase.getInstance().getReference()
                            .child(KEY_TABLE).child(currentUserid);
                    HashMap userInfo = new HashMap();
                    userInfo.put(KEY_ID,currentUserid);
                    userInfo.put(KEY_NAME,fullname);
                    userInfo.put(KEY_EMAIL,email);
                    userInfo.put(KEY_ID_NUMBER,idNumber);
                    userInfo.put(KEY_PHONE_NUMBER,phoneNumber);
                    userInfo.put(KEY_BLOOD_GROUP,bloodGroup);
                    userInfo.put(KEY_TYPE,"recipient");
                    userInfo.put(KEY_SEARCH,"recipient"+bloodGroup);

                    userDataBaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                showToast("Data set Successfull");
                            }else{
                                showToast(task.getException().toString());
                            }

                            finish();

                        }
                    });

                    if(resultUri != null ){
                        final StorageReference filePath= FirebaseStorage.getInstance().getReference()
                                .child("profile images").child(currentUserid);
                        Bitmap bitmap = null;

                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);

                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                        byte[] data = byteArrayOutputStream.toByteArray();
                        UploadTask uploadTask = filePath.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showToast("Image Upload Failed");
                            }
                        });

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if(taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null){
                                    Task<Uri> Result = taskSnapshot.getStorage().getDownloadUrl();
                                    Result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageURL = uri.toString();
                                            Map newImageMap = new HashMap();
                                            newImageMap.put(KEY_PROFILE_PIC,imageURL);
                                            userDataBaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if(task.isSuccessful()){
                                                        showToast("Image URL Added to Database Successfully");
                                                    }else{
                                                        showToast(task.getException().toString());
                                                    }
                                                }
                                            });

                                            finish();

                                        }
                                    });
                                }
                            }
                        });

                        Intent intent = new Intent(RecipientRegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        loader.dismiss();
                    }

                }
            }
        });

    }




}