package ma.ensaf.bda.fragments.thirdPartyLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import ma.ensaf.bda.R;
import ma.ensaf.bda.adapters.LoginAdapter;
import ma.ensaf.bda.adapters.SignUpTPAdapter;
import ma.ensaf.bda.databinding.ActivityThirdPartySignUpBinding;

public class thirdPartySignUp extends AppCompatActivity {

    ActivityThirdPartySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThirdPartySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("New Donor"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("New Recipient"));

        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final SignUpTPAdapter adapter = new SignUpTPAdapter(getSupportFragmentManager(), this, binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

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