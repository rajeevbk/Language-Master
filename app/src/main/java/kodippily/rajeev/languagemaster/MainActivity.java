package kodippily.rajeev.languagemaster;

import androidx.appcompat.app.AppCompatActivity;
import  android.database.sqlite.*;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static Button addPhrases;
    private static Button displayPhrases;
    private static Button editPhrases;
    private static Button languageSubscription;
    private static Button translate;

    //Get database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteConnection db = SQLiteConnection.getInstance();
        db.test();

    }
}
