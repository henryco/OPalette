package net.henryco.opalette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.application.main.ProtoActivity;
import net.henryco.opalette.application.test.TestActivity;


public class MainBlanc extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blanc);
        init();
    }

    public void init() {

        findViewById(R.id.startButton).setOnClickListener(
                v -> startActivity(new Intent(this, ProtoActivity.class)));
        findViewById(R.id.buttonTest).setOnClickListener(
                v -> startActivity(new Intent(this, TestActivity.class)));
    }

}
