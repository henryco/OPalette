package net.henryco.opalette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.activity.grad.ImageActivity;
import net.henryco.opalette.activity.t1.T1Activity;
import net.henryco.opalette.activity.triangle.TriActivity;

public class MainBlanc extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blanc);
        init();
    }

    public void init() {

        findViewById(R.id.startButton).setOnClickListener(
                v -> startActivity(new Intent(this, ImageActivity.class)));
        findViewById(R.id.triButton).setOnClickListener(
                v -> startActivity(new Intent(this, TriActivity.class)));
        findViewById(R.id.t1Button).setOnClickListener(
				v -> startActivity(new Intent(this, T1Activity.class)));
    }

}
