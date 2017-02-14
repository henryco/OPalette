package net.henryco.opalette;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import net.henryco.opalette.activity.grad.GradientActivity;
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
                v -> startActivity(new Intent(this, GradientActivity.class)));
        findViewById(R.id.triButton).setOnClickListener(
                v -> startActivity(new Intent(this, TriActivity.class)));
    }

}
