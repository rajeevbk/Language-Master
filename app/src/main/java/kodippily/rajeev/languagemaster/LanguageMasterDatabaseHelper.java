package kodippily.rajeev.languagemaster;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.PhraseConstants.*;
import static kodippily.rajeev.languagemaster.LanguageConstants.*;


public class LanguageMasterDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "languageMaster.db";
    private static final int DATABASE_VERSION = 1;

    /* Create a helper object for the phrases database */
    public LanguageMasterDatabaseHelper(Context ctx) { super(ctx, DATABASE_NAME , null , DATABASE_VERSION); }

    //onCreate() is only run when the database file did not exist and was just created
    @Override public void onCreate(SQLiteDatabase db)
    {
        //create phrase table
        db.execSQL("CREATE TABLE " + TABLE_NAME_PHRASE + " ("
                + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_NAME_PHRASE
                + " TEXT NOT NULL);"
        );

        //create language table
        db.execSQL("CREATE TABLE " + TABLE_NAME_LANGUAGE + " ("
                + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_NAME_LANGUAGE
                + " TEXT NOT NULL , "
                + COLUMN_NAME_IS_SELECTED
                + " INT);"

        );

        //run once - populate language table with all languages and set isSelected to zero
        ContentValues values = new ContentValues();
        for(int i = 0; i < languageList.size(); i++){
            values.put(COLUMN_NAME_LANGUAGE, languageList.get(i));
            values.put(COLUMN_NAME_IS_SELECTED, 0);
            db.insertOrThrow(TABLE_NAME_LANGUAGE, null , values);
        }

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PHRASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LANGUAGE);
        onCreate(db);
    }


}
