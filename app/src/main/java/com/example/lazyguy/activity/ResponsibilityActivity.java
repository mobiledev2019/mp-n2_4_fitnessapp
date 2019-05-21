package com.example.lazyguy.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lazyguy.R;

public class ResponsibilityActivity extends AppCompatActivity {

    private TextView res1, res2, res3, res4, res5;
    Toolbar tbRes;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibility);
        //getSupportActionBar().setTitle("Our responsibility");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();
        tbRes.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        res1.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        res2.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        res3.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        res4.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        res5.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void anhXa() {
        res1 = findViewById(R.id.tvRes1);
        res2 = findViewById(R.id.tvRes2);
        res3 = findViewById(R.id.tvRes3);
        res4 = findViewById(R.id.tvRes4);
        res5 = findViewById(R.id.tvRes5);
        tbRes = findViewById(R.id.tbRes);
    }

}
