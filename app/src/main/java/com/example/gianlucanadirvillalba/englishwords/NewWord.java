package com.example.gianlucanadirvillalba.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class NewWord extends AppCompatActivity
{
    private EditText mEditEnglish;
    private EditText mEditItalian;
    private Button mAddButton;
    private FirebaseDatabase firebaseDatabase;
    private int mDbSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        mDbSize = intent.getIntExtra("size", 0);
        Log.d("Log", "Size: "+mDbSize);


        mEditEnglish = (EditText) findViewById(R.id.edit_english);
        mEditItalian = (EditText) findViewById(R.id.edit_italian);
        mAddButton = (Button) findViewById(R.id.button_add);

        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/" + mDbSize + "/english")
                        .setValue(mEditEnglish.getText().toString());

                firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/" + mDbSize + "/italian")
                        .setValue(mEditItalian.getText().toString());

                Toast.makeText(MyApplication.getAppContext(),"Added word: "+ mEditEnglish.getText() +" - "+mEditItalian.getText(),
                        Toast.LENGTH_SHORT).show();
                mDbSize++; //TODO mancano tutti i controlli per aggiungere correttamente un elemento in coda alla lista nel db
            }
        });
    }
}
