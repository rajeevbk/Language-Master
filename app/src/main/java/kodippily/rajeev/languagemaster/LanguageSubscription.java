package kodippily.rajeev.languagemaster;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import static kodippily.rajeev.languagemaster.LanguageConstants.COLUMN_NAME_IS_SELECTED;
import static kodippily.rajeev.languagemaster.LanguageConstants.COLUMN_NAME_LANGUAGE;
import static kodippily.rajeev.languagemaster.LanguageConstants.TABLE_NAME_LANGUAGE;
import static kodippily.rajeev.languagemaster.LanguageConstants.languageList;
import static kodippily.rajeev.languagemaster.MainActivity.data;

public class LanguageSubscription extends AppCompatActivity {

    private ListView list;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);
        list = findViewById(R.id.listView_languages);
        Button updateButton = findViewById(R.id.button_update);

        //get language from database into array list
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_multiple_choice,languageList);
        list.setAdapter(arrayAdapter);

        //show the selected languages in the database as ticked in the listView
        SQLiteDatabase db = data.getWritableDatabase();
        //db.execSQL("DROP TABLE " + TABLE_NAME_PHRASE);
        Cursor cursor = db.rawQuery("SELECT languageString FROM language WHERE isSelected = 1",null); //query to get selected langs
        while (cursor.moveToNext()) {
            String selectedLanguage = cursor.getString(0); //get lang string
            for(int i = 0; i < languageList.size(); i++){   //get index of lang string in list
                if(languageList.get(i).equals(selectedLanguage)){   //find index in list
                    list.setItemChecked(i, true);               //set language as checked
                }
            }
        }
        cursor.close();
        db.close();
    }

    public void updateDatabase(View view) { //onClick for update Button
        //update database with new selections
        //write the languages to the database with the ticked one with isSelected = 1
        SQLiteDatabase db = data.getWritableDatabase();
        int len = list.getCount();
        SparseBooleanArray checked = list.getCheckedItemPositions();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                //use array index to set isSelected in database (database _id = i +1) since db index starts at 1
                db.execSQL("UPDATE language SET isSelected = 1 WHERE _id = " + (i+1));
            }
        }
        db.close();
        alert("Success!", "Your changes have been saved.");
    }

    //alert box function - LanguageSubscription
    private void alert(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(LanguageSubscription.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
