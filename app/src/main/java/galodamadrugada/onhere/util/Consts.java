package galodamadrugada.onhere.util;

public class Consts {
    // Strings constantes do servidor
    public static final String SERVER         = "https://pure-sea-25119.herokuapp.com/";
    public static final String LOGIN          = "api/usuario/login";
    public static final String NEW_USER       = "api/usuario/novo";
    public static final String NEW_EVENT      = "api/evento/novo";
    public static final String CHECK_IN       = "api/evento/entrar";
    public static final String LIST_EVENTS    = "api/evento/lista";
    public static final String LIST_MY_EVENTS = "api/evento/mine";
    public static final String CHECK_TOKEN    = "api/token/valida";

    // Strings constantes do app
    public static final String PREFS_FILE_NAME = "mPrefsFile";

    // Códigos de erro
    public static final String URL_NOT_FOUND               = "404";
    public static final String EMAIL_NOT_FOUND             = "405";
    public static final String INVALID_PASSWORD_OR_EMAIL   = "406";
    public static final String EMPTY_EMAIL_OR_PASSWORD     = "407";
    public static final String EMAIL_ALREADY_USED          = "408";
    public static final String USER_REGISTER_SUCCESS       = "409";
    public static final String EXPIRED_ACCESS              = "410";
    public static final String TOKEN_USER_NOT_FOUND        = "411";
    public static final String INVALID_TOKEN               = "412";
    public static final String TOKEN_NOT_FOUND             = "413";
    public static final String EVENT_SEARCHING_ERROR       = "414";
    public static final String EVENT_ENTER_ERROR           = "415";
    public static final String EVENT_OWNER_SEARCHING_ERROR = "416";
    public static final String EVENT_ENTER_SUCCESS         = "417";
    public static final String REGISTER_ERROR              = "418";
    public static final String LOGIN_SUCCESS               = "666";
}
