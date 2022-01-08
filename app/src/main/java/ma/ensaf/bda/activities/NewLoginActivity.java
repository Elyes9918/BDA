package ma.ensaf.bda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import ma.ensaf.bda.adapters.LoginAdapter;
import ma.ensaf.bda.databinding.ActivityNewLoginBinding;

public class NewLoginActivity extends AppCompatActivity {

    ActivityNewLoginBinding binding;

    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startAnimation();
    }

    private void startAnimation() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Login"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("New Recipient"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("New Donor"));

        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.fabFacebook.setTranslationY(300);
        binding.fabGoogle.setTranslationY(300);
        binding.fabTwiiter.setTranslationY(300);
        binding.tabLayout.setTranslationY(300);

        binding.fabFacebook.setAlpha(v);
        binding.fabTwiiter.setAlpha(v);
        binding.fabGoogle.setAlpha(v);
        binding.tabLayout.setAlpha(v);

        binding.fabFacebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        binding.fabGoogle.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        binding.fabTwiiter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        binding.tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}