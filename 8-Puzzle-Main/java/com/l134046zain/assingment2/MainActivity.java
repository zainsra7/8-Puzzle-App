package com.l134046zain.assingment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("L134046 | 8 Puzzle");

        b1=(Button) findViewById(R.id.Button1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"New Game !",Toast.LENGTH_SHORT).show();


                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();
            }
        });

    }
}
