package com.osb.mobiledev.kpi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.osb.mobiledev.kpi.fragments.ListFragment;
import com.osb.mobiledev.kpi.shake.ShakeEventManager;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements ShakeEventManager.ShakeListener {

    private ShakeEventManager sem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_screen);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (fragment == null) {
                fragment = new ListFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }

        sem = new ShakeEventManager();
        sem.setListener(this);
        sem.init(this);
    }

    @Override
    public void onShake() {
        Toast.makeText(this, "Happy Easter!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EasterEggActivity.class);
        startActivity(intent);
    }
}
