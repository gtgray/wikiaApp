package tk.atna.wikiaapp;

/**
 * Contains all information scheme about communication with server
 */
abstract class ServerApi {

    static final String SERVER_URL = "http://www.wikia.com/wikia.php";

    static final String DEFAULT_CONTROLLER = "WikisApi";
    static final String DEFAULT_LANG = "en";
    static final int DEFAULT_LIMIT = 25;

    static final String GETLIST_METHOD = "getList";
    static final String GETDETAILS_METHOD = "getDetails";

    static final String CONTROLLER = "controller";
    static final String METHOD = "method";
    static final String LANG = "lang";
    static final String LIMIT = "limit";
    static final String HUB = "hub";
    static final String BATCH = "batch";
    static final String IDS = "ids";

    static final String ITEMS = "items";

    /**
     * Default image cale ratio
     */
    static final int DEFAULT_IMAGE_SCALE = 200;

    /**
     * Scaled image url ending
     */
    static final String IMAGE_SCALE_URL_TAIL = "/scale-to-width/" + DEFAULT_IMAGE_SCALE;

    /**
     * Base url beginning
     */
    static final String DEFAULT_URL = SERVER_URL + "?" + CONTROLLER + "=" + DEFAULT_CONTROLLER;

    /**
     * Base url ending
     */
    static final String DEFAULT_URL_TAIL = "&" + LANG + "=" + DEFAULT_LANG
                                         + "&" + LIMIT + "=" + DEFAULT_LIMIT;

    /**
     * Concatenates url for getList request
     *
     * @param hub needed hub name
     * @param batch needed page number
     * @return url to load list
     */
    static String buildListUrl(String hub, int batch) {
        return DEFAULT_URL + "&" + METHOD + "=" + GETLIST_METHOD
                           + "&" + HUB + "=" + (hub == null ? "" : hub)
                           + "&" + BATCH + "=" + (batch < 1 ? 1 : batch)
                           + DEFAULT_URL_TAIL;
    }

    /**
     * Concatenates url for getDetails request
     *
     * @param ids array of ids
     * @return url to load details
     */
    static String buildDetailsUrl(int[] ids) {
        String idz = ArrayToString(ids);
        if(idz != null)
            return DEFAULT_URL + "&" + METHOD + "=" + GETDETAILS_METHOD
                               + "&" + IDS + "=" + idz;

        return null;
    }

    /**
     * Represents int array as string with commas between elements
     *
     * @param array array of ints
     * @return string representation of array
     */
    static String ArrayToString(int[] array) {
        if(array == null || array.length == 0)
            return null;

        StringBuilder sb = new StringBuilder();
        int size = array.length;
        for(int i = 0; i < size; i++) {
            sb.append(array[i])
              .append(i != (size - 1) ? "," : "");
        }
        return sb.toString();
    }

    /**
     * Converts original image url into one to load scaled
     *
     * @param imageUrl original image url
     * @return url to load scaled image
     */
    static String buildScaledImageUrl(String imageUrl) {
        return trimImageUrl(imageUrl) + IMAGE_SCALE_URL_TAIL;
    }

    /**
     * Searches for '?' symbol in original image url and cutes tail behind it
     *
     * @param url original image url
     * @return trimmed image url
     */
    static String trimImageUrl(String url) {
        int what = url.indexOf(0x3f);
        return what != -1 ? url.substring(0, what) : url;
    }


    /**
     * Interface to keep available hub names
     */
    interface Hub {

        static final String ALL = "";
        static final String GAMING = "Gaming";
        static final String ENTERTAINMENT = "Entertainment";
        static final String LIFESTYLE ="Lifestyle";
        static final String EDUCATION = "Education";

        static final String[] HUBS = {ALL,
                                      GAMING,
                                      ENTERTAINMENT,
                                      LIFESTYLE,
                                      EDUCATION};
    }
}
