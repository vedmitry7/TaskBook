package com.example.dmitryvedmed.taskbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import static com.example.dmitryvedmed.taskbook.MainActivity2.dbHelper;

public class TaskActivity extends AppCompatActivity {

    private int id;
    private EditText head,text;
    private Task task;
    private boolean wasChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initView();
    }

    private void initView() {
        head = ( EditText) findViewById(R.id.headEditText);
        text = ( EditText) findViewById(R.id.taskEditText);

       // Intent intent = getIntent();
       // id = intent.getIntExtra("id", -1);
       // Log.d("TAG", String.valueOf(id));

        task = getIntent().getParcelableExtra("Task");
        Log.d("TAG", "Eah, I've get task - " + String.valueOf(id));

      /*  if(id==-1){
            task = new Task();
            task.setId(id);
            return;
        }*/
       // task = dbHelper.getTask(id);
        head.setText(task.getHeadLine());
        text.setText(task.getContext());
    }

    private void saveTask() {
        String headline = String.valueOf(head.getText());
        String content = String.valueOf(text.getText());


        Log.d("TAG", "SAVE  !!! " +  headline + " " + content);

        Log.d("TAG", "SAVE id = " + id + "???");

        task.setHeadLine(headline);
        task.setContext(content);

        if(task.getId()==-1) {
            id = MainActivity2.dbHelper.addTask(task);
            task.setId(id);
            Log.d("TAG", "NEW TASK ID = " + id);
        }
        else{
            Log.d("TAG", "UPDATE id = " + id);
            dbHelper.updateTask(task);
        }
    }

    @Override
    protected void onPause() {
        saveTask();
        super.onPause();
    }
}