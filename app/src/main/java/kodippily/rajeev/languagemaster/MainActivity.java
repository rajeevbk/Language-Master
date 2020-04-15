package kodippily.rajeev.languagemaster;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected  Button addPhrases;
    protected  Button displayPhrases;
    protected  Button editPhrases;
    protected  Button languageSubscription;
    protected  Button translate;
    public static LanguageMasterDatabaseHelper data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getDatabase instance
        data = new LanguageMasterDatabaseHelper(this);

        //switch activity to addPhrases
        addPhrases = findViewById(R.id.button_addPhrases);
        addPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPhrases.class);
                startActivity(intent);
            }
        });

        //switch activity to displayPhrases
        displayPhrases = findViewById(R.id.button_displayPhrases);
        displayPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPhrases.class);
                startActivity(intent);
            }
        });

        //switch activity to editPhrases
        editPhrases = findViewById(R.id.button_editPhrases);
        editPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditPhrases.class);
                startActivity(intent);
            }
        });

        //switch activity to languageSubscription
        languageSubscription = findViewById(R.id.button_languageSubscription);
        languageSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LanguageSubscription.class);
                startActivity(intent);
            }
        });

        //switch activity to Translate
        translate = findViewById(R.id.button_translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Translate.class);
                startActivity(intent);
            }
        });
    }

    //alert box function - MainActivity
    private void alert(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();

    }
}
