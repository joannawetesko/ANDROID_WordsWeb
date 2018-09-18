package pl.com.wordsweb.storage;

import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.PhraseUse;
import pl.com.wordsweb.entities.quizzes.Mode;

import static pl.com.wordsweb.config.Constants.LANGUAGES;
import static pl.com.wordsweb.config.Constants.LEARN_DIRECTIONS;
import static pl.com.wordsweb.config.Constants.LEARN_LEVELS;
import static pl.com.wordsweb.config.Constants.LEARN_LISTS;
import static pl.com.wordsweb.config.Constants.LEARN_MODE;
import static pl.com.wordsweb.config.Constants.PHRASE_ONE;
import static pl.com.wordsweb.config.Constants.PHRASE_TWO;

/**
 * Created by wewe on 06.11.16.
 */

public class DbProvider {
    private static final String TAG = "DbProvider";

    public static void addLevels(Context context, Integer[] levels) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(LEARN_LEVELS, levels);
            snappyDb.close();

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());

        }

    }

    public static Integer[] getLevels(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            Integer[] levels = snappyDb.getArray(LEARN_LEVELS, Integer.class);
            snappyDb.close();
            return levels;

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }

    public static void addLists(Context context, Integer[] learnLists) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(LEARN_LISTS, learnLists);
            snappyDb.close();

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public static Integer[] getLists(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            Integer[] levels = snappyDb.getArray(LEARN_LISTS, Integer.class);
            snappyDb.close();
            return levels;

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }

    public static void addDirections(Context context, String direction) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(LEARN_DIRECTIONS, direction);
            snappyDb.close();

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public static String getDirections(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            String directions = snappyDb.get(LEARN_DIRECTIONS);
            snappyDb.close();
            return directions;

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }


    public static void addLanguage(Context context, ArrayList<Language> languages) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(LANGUAGES, languages.toArray(new Language[languages.size()]));
            snappyDb.close();

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());

        }

    }

    public static List<Language> getLanguage(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            Language[] languages = snappyDb.getObjectArray(LANGUAGES, Language.class);
            snappyDb.close();
            return Arrays.asList(languages);

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }




    public static void addMode(Context context, Mode mode) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(LEARN_MODE, mode);
            snappyDb.close();

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public static Mode getMode(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            Mode mode = snappyDb.getObject(LEARN_MODE, Mode.class);
            snappyDb.close();
            return mode;
        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static void addPhraseOne(Context context, PhraseUse phrase) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(PHRASE_ONE, phrase);
            snappyDb.close();
        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static PhraseUse getPhraseOne(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            PhraseUse phrase = snappyDb.getObject(PHRASE_ONE, PhraseUse.class);
            snappyDb.close();
            return phrase;
        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static void addPhraseTwo(Context context, PhraseUse phrase) {
        try {
            DB snappyDb = DBFactory.open(context);
            snappyDb.put(PHRASE_TWO, phrase);
            snappyDb.close();
        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static PhraseUse getPhraseTwo(Context context) {
        try {
            DB snappyDb = DBFactory.open(context);
            PhraseUse phrase = snappyDb.getObject(PHRASE_TWO, PhraseUse.class);
            snappyDb.close();
            return phrase;

        } catch (SnappydbException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

    }


}
