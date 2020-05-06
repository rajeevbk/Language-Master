package kodippily.rajeev.languagemaster;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static kodippily.rajeev.languagemaster.PhraseConstants.*;
import static kodippily.rajeev.languagemaster.LanguageConstants.*;
import static kodippily.rajeev.languagemaster.TranslationsConstants.*;


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

        //create table for translated phrases (offline use)
        db.execSQL("CREATE TABLE " + TABLE_NAME_TRANSLATOINS + " ("
                + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + languageList.get(0)
                + " TEXT, "
                + languageList.get(1)
                + " TEXT, "
                + languageList.get(2)
                + " TEXT, "
                + languageList.get(3)
                + " TEXT, "
                + languageList.get(4)
                + " TEXT, "
                + languageList.get(5)
                + " TEXT, "
                + languageList.get(6)
                + " TEXT, "
                + languageList.get(7)
                + " TEXT, "
                + languageList.get(8)
                + " TEXT, "
                + languageList.get(9)
                + " TEXT, "
                + languageList.get(10)
                + " TEXT, "
                + languageList.get(11)
                + " TEXT, "
                + languageList.get(12)
                + " TEXT, "
                + languageList.get(13)
                + " TEXT, "
                + languageList.get(14)
                + " TEXT, "
                + languageList.get(15)
                + " TEXT, "
                + languageList.get(16)
                + " TEXT, "
                + languageList.get(17)
                + " TEXT, "
                + languageList.get(18)
                + " TEXT, "
                + languageList.get(19)
                + " TEXT, "
                + languageList.get(20)
                + " TEXT, "
                + languageList.get(21)
                + " TEXT, "
                + languageList.get(22)
                + " TEXT, "
                + languageList.get(23)
                + " TEXT, "
                + languageList.get(24)
                + " TEXT, "
                + languageList.get(25)
                + " TEXT, "
                + languageList.get(26)
                + " TEXT, "
                + languageList.get(27)
                + " TEXT, "
                + languageList.get(28)
                + " TEXT, "
                + languageList.get(29)
                + " TEXT, "
                + languageList.get(30)
                + " TEXT, "
                + languageList.get(31)
                + " TEXT);"
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
