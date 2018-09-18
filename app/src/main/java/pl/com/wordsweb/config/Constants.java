package pl.com.wordsweb.config;

/**
 * Created by wewe on 11.04.16.
 */
public class Constants {

    //WEB Constants
    public static final String BASE_URL = "https://phraseweb-dziadzior.rhcloud.com/";
    public static final String AUTH_URL = "https://auth-dziadzior.rhcloud.com/";
    public static final String BASE_PHOTO_URL = "https://s396300.students.wmi.amu.edu.pl/flags/";
    public static final String PHOTO_FILE_EXTENSION = ".svg";
    public static final String COMMENT_ELEMENT = "comment";
    public static final String LIST_ELEMENT = "list";
    public static final String EXPRESSION_ELEMENT = "expression";
    public static final String COMMENTS = "comments";
    public static final String ANSWERS = "answers";


    //Application credentials
    public static final String USER = "wwebbackend";
    public static final String PASSWORD = "wwebbackendsecret";

    //LocalStorage
    public static final String TOKEN_PREFERENCES = "TOKEN_STORAGE";
    public static final String REGISTER_TOKEN_PREFERENCES = "REGISTER_TOKEN_STORAGE";
    public static final String TOKEN = "TOKEN";
    public static final String TOKEN_TYPE = "TOKEN_TYPE";
    public static final String TOKEN_REFRESH = "TOKEN_REFRESH";

    //db
    public static final String LEARN_LEVELS = "LEARN_LEVELS";
    public static final String LEARN_LISTS = "LEARN_LISTS";
    public static final String LEARN_DIRECTIONS = "LEARN_DIRECTIONS";
    public static final String LANGUAGES = "LANGUAGES";
    public static final String LEARN_MODE = "LEARN_MODE";
    public static final String PHRASE_ONE = "PHRASE_ONE";
    public static final String PHRASE_TWO = "PHRASE_TWO";

    //Extra
    public static final String CURRENT_LESSONS_LIST = "currentLessonList";

    //Bundle
    public static final String QUIZ_ARGUMENT = "quiz_argument";
    public static final String QUIZ_TO_LEARN_ARGUMENT = "quiz_to_learn_argument";
    public static final String QUIZ_MODE_WRITE = "quiz_mode_write";
    public static final String QUIZ_ID = "quiz_id";
    public static final String LESSON_LIST = "lesson_list";
    public static final String COMMENT = "comment";

    //Snackbar
    public static final String FOUR_ZERO_ONE = " Invalid user or password";
    //NO_AUTH -> only temporary for testing
    public static final String NO_AUTH = "Authorization problem";
    public static final String NO_INTERNET_CONNECTION = "There was a Internet connection problem";
    public static final String QUIZ_SEND_ANSWER_ERROR = "Something goes wrong, please send response again";
    public static final String SERVICE_UNAVAILABLE = "Service temporary unavailable. Try again later.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String UNKNOWN_ERROR = "Unknown error";
    public static final String NOT_FOUND = "Resource not found";


}
