package kodippily.rajeev.languagemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.MainActivity.data;
import static kodippily.rajeev.languagemaster.PhraseConstants.COLUMN_NAME_PHRASE;
import static kodippily.rajeev.languagemaster.PhraseConstants.TABLE_NAME_PHRASE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DisplayPhrases extends AppCompatActivity {

    private TextView displayPhrasesTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        displayPhrasesTextview = findViewById(R.id.textView_displayPhrases);
        showPhrases(getPhrase());
    }

    private Cursor getPhrase(){
        /* Perform a managed query. The Activity will handle closing and re-querying the cursor when needed. */
        SQLiteDatabase db = data.getReadableDatabase();
        String ORDER_BY = COLUMN_NAME_PHRASE + " ASC";
        String[] FROM = new String[]{_ID, COLUMN_NAME_PHRASE};
        Cursor cursor = db.query(TABLE_NAME_PHRASE, FROM, null, null, null, null, ORDER_BY);
        //db.close(); -  this breaks the app HOW to fix??
        return cursor;
    }

    private void showPhrases(Cursor cursor)
    {
        // Stuff them all into a big string
        StringBuilder builder = new StringBuilder( "Saved English phrases:\n");
        while (cursor.moveToNext()) {
            /* Could use getColumnIndexOrThrow() to get indexes */
            long id = cursor.getLong(0);
            String phrase = cursor.getString(1);
            builder.append(id).append(": ");
            builder.append(phrase).append("\n");
        }
        cursor.close();
       // displayPhrasesTextview =  findViewById(R.id.textView_displayPhrases);
        displayPhrasesTextview.setText(builder);
    }
}
