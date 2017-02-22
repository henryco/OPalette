package net.henryco.opalette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.activity.ProtoActivity;
import net.henryco.opalette.activity.T1Activity;

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
        findViewById(R.id.t1Button).setOnClickListener(
				v -> startActivity(new Intent(this, T1Activity.class)));
    }

}
