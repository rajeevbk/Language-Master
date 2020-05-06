package kodippily.rajeev.languagemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import static kodippily.rajeev.languagemaster.MainActivity.data;

public class OfflineMode extends AppCompatActivity {

    private Spinner savedLanguagesSpinner;
    private Button showSavedPhrases;
    private TextView savedLanguagesListView;
    ArrayAdapter arrayAdapterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_mode);
        savedLanguagesSpinner = findViewById(R.id.spinner_saved_lang_list);
        showSavedPhrases = findViewById(R.id.button_offline_view);
        savedLanguagesListView = findViewById(R.id.textView_translations);


        //get the languages into spinner list

        arrayAdapterSpinner =
                new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, LanguageConstants.languageList);
        arrayAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        savedLanguagesSpinner.setAdapter(arrayAdapterSpinner);

    }

    public void view(View view) {

        //savedLanguagesListView.setText(" ");
        String language = savedLanguagesSpinner.getSelectedItem().toString();
        SQLiteDatabase db = data.getReadableDatabase();
        String query = "SELECT " + language + " FROM translations";
        Cursor cursor = db.rawQuery(query,null);
        StringBuilder builder = new StringBuilder();
//        if(cursor.isNull(1)){
//            System.out.println("no data");
//        }
        while (cursor.moveToNext()) {
            /* Could use getColumnIndexOrThrow() to get indexes */
            String phrase = cursor.getString(0);
            if(phrase != null)
                builder.append(phrase).append("\n");
        }
        cursor.close();
        db.close();

        savedLanguagesListView.setText(builder);

    }

    private void alert(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(OfflineMode.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
