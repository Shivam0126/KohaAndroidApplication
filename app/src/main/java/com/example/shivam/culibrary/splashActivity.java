package com.example.shivam.culibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splashActivity extends AppCompatActivity {
    public static final String COMPLETED_ONBOARDING_PREF_NAME = "onboarding";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = this.getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean x = sharedpreferences.getBoolean("userloggedin", false);
        if (x) {
            Intent i = new Intent(splashActivity.this, MainActivity.class);
            startActivity(i);
        } else {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            // Check if we need to display our OnboardingFragment
            if (!sharedPreferences.getBoolean(
                    COMPLETED_ONBOARDING_PREF_NAME, false)) {
                // The user hasn't seen the OnboardingFragment yet, so show it
                startActivity(new Intent(this, paperonboarding.class));

            } else {
                Intent i = new Intent(splashActivity.this, LoginActivity.class);
                startActivity(i);
            }

        }

    }
}
