package kodippily.rajeev.languagemaster;

import android.database.sqlite.SQLiteDatabase;
import static android.content.Context.MODE_PRIVATE;
import  android.database.sqlite.*;

//Uses Singleton Design pattern with  thread-safe Syncronized block
public class SQLiteConnection  {

    private static SQLiteConnection instance;
   // private SQLiteDatabase mydatabase = SQLiteDatabase.openOrCreateDatabase();

    private SQLiteConnection(){}; //private constructor
    public static SQLiteConnection getInstance(){
        if (instance == null)
        {
            //synchronized block to remove overhead
            synchronized (SQLiteConnection.class)
            {
                if(instance==null)
                {
                    // if instance is null, initialize
                    instance = new SQLiteConnection();
                }
            }
        }
        return instance;
    }

    public  void test () {
        System.out.println("called Test(!!)");
    }
}

