package kodippily.rajeev.languagemaster;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.MainActivity.data;
import static kodippily.rajeev.languagemaster.PhraseConstants.COLUMN_NAME_PHRASE;
import static kodippily.rajeev.languagemaster.PhraseConstants.TABLE_NAME_PHRASE;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import java.util.ArrayList;
import java.util.List;

public class DisplayPhrases extends AppCompatActivity {

    private TextView displayPhrasesTextview;
    ArrayAdapter arrayAdapterSpinner;
    private Spinner languageList;
    private LanguageTranslator translationService;
    private List translations;
    private List list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        displayPhrasesTextview = findViewById(R.id.textView_translations);
        languageList = findViewById(R.id.spinner_select_language);

        SQLiteDatabase db = data.getReadableDatabase();
        String ORDER_BY = COLUMN_NAME_PHRASE + " ASC";
        String[] FROM = new String[]{_ID, COLUMN_NAME_PHRASE};
        Cursor cursor = db.query(TABLE_NAME_PHRASE, FROM, null, null, null, null, ORDER_BY);
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            /* Could use getColumnIndexOrThrow() to get indexes */
            String phrase = cursor.getString(1);
            builder.append(phrase).append("\n");
        }
        cursor.close();
        db.close();
        displayPhrasesTextview.setText(builder);

        //get language list into spinner
        arrayAdapterSpinner =
                new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, LanguageConstants.languageList);
        arrayAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageList.setAdapter(arrayAdapterSpinner);
        translationService = initLanguageTranslatorService();
    }

    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(getString(R.string.language_translator_url));
        return service;
    }

    public void save(View view) {
        //for given language
        //delete all current entries
        //add translations of all saved phrases

        //get all english phrases into array list
        translations  = new ArrayList<String>();
        List phrases = new ArrayList<String>();
        SQLiteDatabase db = data.getReadableDatabase();
        String ORDER_BY = COLUMN_NAME_PHRASE + " ASC";
        String[] FROM = new String[]{_ID, COLUMN_NAME_PHRASE};
        Cursor cursor = db.query(TABLE_NAME_PHRASE, FROM, null, null, null, null, ORDER_BY);
        while (cursor.moveToNext()) {
            /* Could use getColumnIndexOrThrow() to get indexes */
            String phrase = cursor.getString(1);
            phrases.add(phrase);
        }
        cursor.close();
        db.close();

        //get translations into translations array
        String translateToLanguage = languageList.getSelectedItem().toString();
        for (int i = 0; i < phrases.size(); i++) {
           new TranslationTask().execute(phrases.get(i).toString(), translateToLanguage);

        }
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog;
        String language;

        @Override
        protected String doInBackground(String... params) { // means .execute(s1, s2, s3)
            TranslateOptions translateOptions = new TranslateOptions.Builder().addText(params[0]).source("English").target(params[1]).build();
            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
            String firstTranslation = result.getTranslations().get(0).getTranslation();
            language = params[1];
            return firstTranslation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(DisplayPhrases.this);
            progDailog.setMessage("Saving data...Please wait.");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            translations.add(s);

            //after translating replace db values with array list
          SQLiteDatabase db = data.getReadableDatabase();
          ContentValues values = new ContentValues();
            values.put(language , s);
            db.insertOrThrow("translations", null , values);
            db.close();
          progDailog.dismiss();

        }
    }
}
