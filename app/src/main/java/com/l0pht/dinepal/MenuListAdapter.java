package com.l0pht.dinepal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.l0pht.dinepal.R;

public class MenuListAdapter extends AppCompatActivity implements CustomDiaglog.CustomDialogListner {

    private TextView txview1;
    private TextView txview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editmenu);

        // Find the TextViews and Button by their IDs
        txview1 = (TextView) findViewById(R.id.tv1);
         txview2 =(TextView) findViewById(R.id.tv2);
        Button btnEdit = findViewById(R.id.btnEdit);

        // Set click listener for the Edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog()
    {
        CustomDiaglog customDiaglog =new CustomDiaglog();
        customDiaglog.show(getSupportFragmentManager(),"custom dialog");
    }

    @Override
    public void applyTexts(String dishname, String price) {
        txview1.setText(dishname);
        txview2.setText(price);
    }
}
