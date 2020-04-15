package kodippily.rajeev.languagemaster;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//constants used in table language
public interface LanguageConstants extends BaseColumns {
    public static final String TABLE_NAME_LANGUAGE = "language";
    public static final String COLUMN_NAME_LANGUAGE = "languageString";
    public static final String COLUMN_NAME_IS_SELECTED = "isSelected";

    //hard code all available languages into list
    public static final List<String> languageList = new ArrayList<>(Arrays.asList(
            new String[]{"Arabic","Bulgarian","Croatian", "Czech", "Danish", "Dutch", "Estonian", "Finnish", "French", "German", "Greek", "Indonesian", "Irish", "Italian", "Japanese", "Korean", "Latvian", "Lithuanian", "Malay", "Norwegian Bokmal", "Polish", "Portuguese",  "Romanian", "Russian", "Slovakian", "Slovenian", "Spanish", "Swedish", "Thai", "Turkish", "Urdu", "Vietnamese"}));

    //hard code language codes to use in text to speech synthersizer
    public static final Map<String, String> voiceOptions = new HashMap<String, String>() {{
        put("Arabic", "ar-AR_OmarVoice");
        put("German", "de-DE_BirgitV3Voice");
        put("Spanish", "es-LA_SofiaV3Voice");
        put("French", "fr-FR_ReneeVoice");
        put("Italian", "it-IT_FrancescaVoice");
        put("Japanese", "ja-JP_EmiV3Voice");
        put("Dutch", "nl-NL_EmmaVoice");
        put("Portuguese", "pt-BR_IsabelaV3Voice");
    }};
}
