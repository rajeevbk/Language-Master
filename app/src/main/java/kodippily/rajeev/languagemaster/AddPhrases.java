package kodippily.rajeev.languagemaster;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.PhraseConstants.TABLE_NAME_PHRASE;
import static kodippily.rajeev.languagemaster.PhraseConstants.COLUMN_NAME_PHRASE;
import static kodippily.rajeev.languagemaster.MainActivity.data;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


public class AddPhrases extends AppCompatActivity {

    private  EditText phrase;
    private  Button save;
    private  TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);
        save = findViewById(R.id.button_savePhrase);
        phrase = findViewById(R.id.editText_addphrase);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phraseString = phrase.getText().toString();
                System.out.println("String" +phraseString);
                if (phraseString.isEmpty())
                 alert("Error", "Please enter a valid word/phrase");
                else {
                    // Add String to sqlite db here
                    try {
                        addPhrase(phraseString);
                    } catch (Exception e) {
                        System.out.println("An error occured while trying to add a phrase.");
                    }
                    alert("Saved following phrase in Database!", phraseString);
                }
            }
        });
    }

    //alert box function - AddPhrases
    private void alert(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(AddPhrases.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
    private void addPhrase(String string) {
        /* Insert a new record into the phrases data source. You would do something similar for delete and update. */
        SQLiteDatabase db =  data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PHRASE , string);
        db.insertOrThrow(TABLE_NAME_PHRASE, null , values);
        db.close();
    }

}
