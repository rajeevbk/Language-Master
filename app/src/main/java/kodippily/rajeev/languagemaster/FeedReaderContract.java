package kodippily.rajeev.languagemaster;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "phrase";
        public static final String COLUMN_NAME_PHRASEID = "phraseId";
        public static final String COLUMN_NAME_PHRASE = "phraseString";
    }


}
