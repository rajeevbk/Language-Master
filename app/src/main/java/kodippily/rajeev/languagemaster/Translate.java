package kodippily.rajeev.languagemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.media.AudioAttributes;

import java.util.ArrayList;

import android.os.AsyncTask;

import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.LanguageConstants.languageList;
import static kodippily.rajeev.languagemaster.LanguageConstants.voiceOptions;
import static kodippily.rajeev.languagemaster.MainActivity.data;
import static kodippily.rajeev.languagemaster.PhraseConstants.COLUMN_NAME_PHRASE;
import static kodippily.rajeev.languagemaster.PhraseConstants.TABLE_NAME_PHRASE;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Translate extends AppCompatActivity {

    protected ListView phrasesList;
    protected Spinner languageSubscriptionList;
    protected Button translate;
    protected TextView translation;
    private ArrayList<String> arrayListListView;
    private ArrayList<String> arrayListSpinner;
    ArrayAdapter arrayAdapterListView;
    ArrayAdapter arrayAdapterSpinner;
    private static String[] FROM = {_ID, COLUMN_NAME_PHRASE};
    private static String ORDER_BY = COLUMN_NAME_PHRASE + " ASC";
    private LanguageTranslator translationService;

    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;
    private String translateToLanguage = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        phrasesList = findViewById(R.id.listView_listToTranslate);
        languageSubscriptionList = findViewById(R.id.spinner_languageList);
        translate = findViewById(R.id.button_translate);
        translation = findViewById(R.id.textView_translation);

        //set phrases data in list view
        //get phrases into an array list
        arrayListListView = new ArrayList();
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor1 = db.query(TABLE_NAME_PHRASE, FROM, null, null, null, null, ORDER_BY);
        while (cursor1.moveToNext()) {
            /* Could use getColumnIndexOrThrow() to get indexes */
            long id = cursor1.getLong(0);
            String phrase = cursor1.getString(1);
            arrayListListView.add(phrase);
        }
        cursor1.close();
        //set arrayAdapterListView with above array list
        arrayAdapterListView = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_single_choice, arrayListListView);
        phrasesList.setAdapter(arrayAdapterListView); //display array list in list view

        //set language subscriptions in spinner
        //get checked langs into array list
        arrayListSpinner = new ArrayList<>();
        Cursor cursor2 = db.rawQuery("SELECT languageString FROM language WHERE isSelected = 1", null); //query to get selected langs
        while (cursor2.moveToNext()) {
            String selectedLanguage = cursor2.getString(0); //get lang string
            arrayListSpinner.add(selectedLanguage);
        }
        cursor2.close();
        db.close();
        arrayAdapterSpinner =
                new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayListSpinner);
        arrayAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSubscriptionList.setAdapter(arrayAdapterSpinner);

        textService = initTextToSpeechService();
        translationService = initLanguageTranslatorService();
    }


    //translate the phrase and set in text view
    public void translate(View view) {
        String phraseToTranslate = " ";
        int len = phrasesList.getCount();
        SparseBooleanArray checked = phrasesList.getCheckedItemPositions();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                phraseToTranslate = arrayListListView.get(i);
            }
        }
        translateToLanguage = languageSubscriptionList.getSelectedItem().toString();

        new TranslationTask().execute(phraseToTranslate, translateToLanguage);
    }

    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(getString(R.string.language_translator_url));
        return service;
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog;

        @Override
        protected String doInBackground(String... params) { // means .execute(s1, s2, s3)
            TranslateOptions translateOptions = new TranslateOptions.Builder().addText(params[0]).source("English").target(params[1]).build();
            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
            String firstTranslation = result.getTranslations().get(0).getTranslation();
            return firstTranslation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(Translate.this);
            progDailog.setMessage("Translating...Please wait.");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            translation.setText(s);
            progDailog.dismiss();
        }
    }

    public void pronounce(View view) {
        //get correct voice option from map
        String voice = " ";
        if(voiceOptions.get(translateToLanguage) != null){
            voice = voiceOptions.get(translateToLanguage);
        }else {voice = "en-US_AllisonV3Voice";}
        //get the text to pronounce
        String textToPronounce = translation.getText().toString();
        System.out.println(textToPronounce);
        new SynthesisTask().execute(textToPronounce,voice);
    }

    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.text_speech_apikey));
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl(getString(R.string.text_speech_url));
        return service;
    }
    private class SynthesisTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog progDailog;

        @Override
        protected String doInBackground(String... params){
            SynthesizeOptions synthesizeOptions =
                    new SynthesizeOptions.Builder()
                            .text(params[0])
                            .voice(params[1]) //get voice programatically according to text
                            .accept(HttpMediaType.AUDIO_WAV)
                            .build();
            player.playStream(textService.synthesize(synthesizeOptions).execute().getResult());
            return "Did synthesize";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(Translate.this);
            progDailog.setMessage("Synthesizing audio...Please wait.");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progDailog.dismiss();
        }


    }


}
