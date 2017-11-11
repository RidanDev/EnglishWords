package com.example.gianlucanadirvillalba.englishwords;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private List fromDb = new ArrayList<>();
    private Random random = new Random();
    private TextView mKeyText;
    private TextView mValueText;
    private Button mNextButton;
    private int randomNumber;
    private boolean tap;
    private FirebaseDatabase firebaseDatabase;
    private int count = 0;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mProgressBar = findViewById(R.id.loadingPanel);
        //readFile();
        readFromDB();
        //addToDB();
    }

    /**
     * Itero su tutti i dati nel db e li aggiungo negli array
     */
    private void readFromDB()
    {
        DatabaseReference ref = firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                fromDb = (List) dataSnapshot.getValue();
                for (int i = 0; i < fromDb.size(); i++)
                {
                    String removeEnglish = "\\s*\\benglish=\\b\\s*";
                    String removeItalian = "\\s*\\bitalian=\\b\\s*";
                    String data = fromDb.get(i).toString();
                    String s = data.substring(1, data.length() - 1);
                    String[] array = s.split(", ");
                    array[0] = array[0].replaceAll(removeEnglish, "");
                    array[1] = array[1].replaceAll(removeItalian, "");
                    mKeys.add(array[0]);
                    mValues.add(array[1]);
                    Log.d("Log", "kv: " + mKeys.get(i) + ", " + mValues.get(i));
                }

                //quando ho tutti i dati negli arraylist allora nascondo la progress bar e mostro la UI
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

    /**
     * Aggiungo i dati nel db
     */
    private void addToDB()
    {
        for (String key : map.keySet())
        {
            Log.d("log", "toDB:" + map.get(key).substring(1, map.get(key).length()));

            //TODO devo rimuovere lo spazio finale
            firebaseDatabase.getReferenceFromUrl("https://myenglishwordsdb.firebaseio.com/" + count + "/english").setValue(key);
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
                    //Log.d("Log", columns[0] +", "+columns[1]);
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
            }
        });

    }
}
