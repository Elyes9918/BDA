package ma.ensaf.bda.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ma.ensaf.bda.Login.LoginActivity;
import ma.ensaf.bda.databinding.ActivitySelectRegesitrationBinding;

public class SelectRegesitrationActivity extends AppCompatActivity {

    private ActivitySelectRegesitrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectRegesitrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectRegesitrationActivity.this,DonarRegistrationActivity.class);
                startActivity(intent);
            }
        });

        binding.recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SelectRegesitrationActivity.this,RecipientRegistrationActivity.class);
                startActivity(intent);

            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectRegesitrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}