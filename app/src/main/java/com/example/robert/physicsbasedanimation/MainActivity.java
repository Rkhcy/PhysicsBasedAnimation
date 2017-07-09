package com.example.robert.physicsbasedanimation;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.robert.physicsbasedanimation.fragment.BallSpringFragment;
import com.example.robert.physicsbasedanimation.fragment.ChainedSpringFragment;
import com.example.robert.physicsbasedanimation.fragment.MainFragment;
import com.example.robert.physicsbasedanimation.fragment.SimpleDemoFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnListItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, mainFragment)
                    .commit();
        }
    }

    @Override
    public void onListItemClick(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SimpleDemoFragment();
                break;
            case 1:
                fragment = new ChainedSpringFragment();
                break;
            case 2:
                fragment = new BallSpringFragment();
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }
}
