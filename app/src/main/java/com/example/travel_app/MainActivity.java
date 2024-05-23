package com.example.travel_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.travel_app.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Account.AccountLogInFragment;
import Account.AccountNotLogInFragment;
import Bookmarks.BookmarksFragment;
import Home.Country_Frag.HomeFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private int selectedNavItem = R.id.home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment(), "HOME_FRAGMENT");

        mAuth = FirebaseAuth.getInstance();

        //Bottom navigation

        binding.BottomNavigView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.home) {
                replaceFragment(new HomeFragment(), "HOME_FRAGMENT");
                selectedNavItem = R.id.home;
            } else if (itemID == R.id.bookmarks) {
                replaceFragment(new BookmarksFragment(), "BOOKMARKS_FRAGMENT");
                selectedNavItem = R.id.bookmarks;
            } else if (itemID == R.id.account) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    replaceFragment(new AccountLogInFragment(), "ACC_NOT_LOGIN_FRAGMENT");
                } else {
                    replaceFragment(new AccountNotLogInFragment(), "ACC_LOG_IN_FRAGMENT");
                }
                selectedNavItem = R.id.account;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.BottomNavigView.setSelectedItemId(selectedNavItem);
    }

    private void replaceFragment(Fragment fragment, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.relativelayout, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }
}


