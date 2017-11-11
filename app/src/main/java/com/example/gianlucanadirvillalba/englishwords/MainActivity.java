package com.example.gianlucanadirvillalba.englishwords;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private List mKeys = new ArrayList<>();
    private List mValues = new ArrayList<>();
    private Map<String, String> map = new LinkedHashMap<>();
    private List mWords = new ArrayList<>();
    private Random random = new Random();
    private TextView mKeyText;
    private TextView mValueText;
    private Button mNextButton;
    private int randomNumber;
    private boolean tap;
    private FirebaseDatabase firebaseDatabase;
    private int count = 0;
    private View mProgressBar;
    private int mDbSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mProgressBar = findViewById(R.id.loadingPanel);
        //readFile();
        readFromDB();
        //addElementToDB();
        //addToDB();
    }

    /**
     * Itero su tutti i dati nel db e li aggiungo negli array
     */
    private void readFromDB()
    {
        final DatabaseReference ref = firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                mDbSize = ((List) dataSnapshot.getValue()).size();
                //Log.d("Log", "DB size: "+((List) dataSnapshot.getValue()).size());
                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    mWords.add(d.child("english").getValue() +" - "+d.child("italian").getValue());
                    mKeys.add(d.child("english").getValue());
                    mValues.add(d.child("italian").getValue());
                }
                randomNumber = random.nextInt(mKeys.size());
                mProgressBar.setVisibility(View.GONE);
                setUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void addElementToDB()
    {
        firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/" +mDbSize+ "/english")
                .setValue("prova");
    }

    /**
     * Aggiungo i dati nel db
     */
    private void addToDB()
    {
        //rimuovo tutti gli elementi dal db se presenti
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.removeValue();

        for (String key : map.keySet())
        {
            firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/" + count + "/english")
                    .setValue(key.substring(0, key.length() - 1)); //rimuovo lo spazio finale
            firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/" + count + "/italian")
                    .setValue(map.get(key).substring(1, map.get(key).length())); //rimuovo lo spazio iniziale
            count++;
        }
    }

    /**
     * Leggo il txt locale con le parole (non mi serve)
     */
    private void readFile()
    {
        AssetManager am = this.getAssets();
        String line;

        try
        {
            InputStream is = am.open("englishwords.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true)
            {
                line = br.readLine();
                if (line != null && line.length() != 0)
                {
                    String[] columns = line.split("-");
                    map.put(columns[0], columns[1]);
                    mKeys.add(columns[0]);
                    mValues.add(columns[1]);
                } else break;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void setUI()
    {
        mKeyText = (TextView) findViewById(R.id.key_text);
        mValueText = (TextView) findViewById(R.id.value_text);
        mNextButton = (Button) findViewById(R.id.next_button);

        mKeyText.setVisibility(View.VISIBLE);
        mValueText.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);

        mKeyText.setText(mKeys.get(randomNumber).toString());
        mValueText.setVisibility(View.INVISIBLE);


        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!tap)
                {
                    mValueText.setText(mValues.get(randomNumber).toString());
                    mValueText.setVisibility(View.VISIBLE);
                    tap = true;
                } else
                {
                    mValueText.setVisibility(View.INVISIBLE);
                    randomNumber = random.nextInt(mKeys.size());
                    mKeyText.setText(mKeys.get(randomNumber).toString());
                    tap = false;
                }
                //addElementToDB();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}
