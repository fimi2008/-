package com.ran.togglebtn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ran.togglebtn.view.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        toggleButton = (ToggleButton) findViewById(R.id.togglebtn);

        toggleButton.setSlideBackgroundResource(R.drawable.slidebg);
        toggleButton.setSwitchBackgroundResource(R.drawable.switchbg);
        toggleButton.setToggleState(ToggleButton.ToggleState.Open);

        toggleButton.setOnToggleStateChangeListener(new ToggleButton.OnToggleStateChangeListener() {
            @Override
            public void onToggleStateChange(ToggleButton.ToggleState state) {
                Toast.makeText(MainActivity.this, state == ToggleButton.ToggleState.Open? "开启" : "关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
