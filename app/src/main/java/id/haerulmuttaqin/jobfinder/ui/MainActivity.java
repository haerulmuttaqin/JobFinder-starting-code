package id.haerulmuttaqin.jobfinder.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import javax.inject.Inject;

import id.haerulmuttaqin.jobfinder.R;
import id.haerulmuttaqin.jobfinder.base.BaseActivity;
import id.haerulmuttaqin.jobfinder.data.api.ConnectionServer;
import id.haerulmuttaqin.jobfinder.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
    }
}