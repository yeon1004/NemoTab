package com.example.iiny1.nemotab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectStageActivity extends FullScreenActivity {
    ListView stageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stage);

        stageList = (ListView)findViewById(R.id.stageList);
        String[] stages = new String[10];
        for(int i = 0; i < 10; i++)
        {
            stages[i] = "stage"+ (i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, stages);
        stageList.setAdapter(adapter);
        stageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), ""+ getFilesDir(), Toast.LENGTH_LONG).show();

                String itemValue = (String) stageList.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), StageActivity.class);
                intent.putExtra("selectedStage", itemValue);
                startActivityForResult(intent, 0);
            }
        });
    }
}
