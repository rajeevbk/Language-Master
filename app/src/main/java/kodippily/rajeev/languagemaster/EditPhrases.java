package kodippily.rajeev.languagemaster;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.MainActivity.data;
import static kodippily.rajeev.languagemaster.PhraseConstants.COLUMN_NAME_PHRASE;
import static kodippily.rajeev.languagemaster.PhraseConstants.TABLE_NAME_PHRASE;


public class EditPhrases extends AppCompatActivity {
    private ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    private ListView listView;
    private EditText text;
    private Button edit;
    private Button save;
    private static String[] FROM = { _ID, COLUMN_NAME_PHRASE};
    private static String ORDER_BY = COLUMN_NAME_PHRASE + " ASC";
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);
        listView = findViewById(R.id.listView_listToTranslate);
        text = findViewById(R.id.editText_editPhrase);
        edit = findViewById(R.id.button_edit);
        save = findViewById(R.id.button_save);

        //get phrases into an array list
        arrayList = new ArrayList();
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_PHRASE, FROM , null , null , null , null , ORDER_BY);
        while (cursor.moveToNext()) {
            /* Could use getColumnIndexOrThrow() to get indexes */
            long id = cursor.getLong(0);
            String phrase = cursor.getString(1);
            arrayList.add(phrase);
        }
        db.close();
        cursor.close();

        //set arrayAdapterListView with above array list
        arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_single_choice,arrayList);
        listView.setAdapter(arrayAdapter); //display array list in list view
    }

    public void editPhrase(View view) {
        //when clicked go thru list and get checked value and return to text view
        String phrase;
        //listView = findViewById(R.id.listView_list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView arg0, View view, int position, //view = current selected item, position = index of item
                                        long itemId) {
                    CheckedTextView textView = (CheckedTextView) view;
                    listView.invalidate();
                    textView = (CheckedTextView) view;
                    if (textView != null) {
                        text.setText(textView.getText());
                    }
                }
            });

        //set current checked item to text field for editting
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < len; i++)
            if (checked.get(i)) {
                String item = arrayList.get(i);
                position = i;
                text.setText(item);
            }
    }

    public void savePhrase(View view) {
        //save current phrase in database (position)
        String phraseToSave = text.getText().toString();
        if(phraseToSave.isEmpty())
            alert("Error", "please select a string first");

        else {
            String originalPhrase = arrayList.get(position);
            SQLiteDatabase db = data.getReadableDatabase();
            db.execSQL("UPDATE phrase SET phraseString = " + "\"" + phraseToSave + "\"" + " WHERE phraseString = " + "\"" + originalPhrase + "\"");
            alert("Saved following phrase in database!", phraseToSave);
            db.close();
        }
    }

    //alert box function - AddPhrases
    private void alert(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(EditPhrases.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
