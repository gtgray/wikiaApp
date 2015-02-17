package tk.atna.wikiaapp;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import com.google.gson.JsonObject;

public class ContentHelper {

    /**
     * Link to itself
     */
    private static ContentHelper INSTANCE;

    /**
     * Helper to load data from server
     */
    private HttpHelper httpHelper;

    /**
     * Helper to pull data from DB
     */
//    private DBHelper dbHelper;
// was omitted as unnecessary in this task,
// but have to be used as capability for data persistence
// which can be pulled earlier then result from server

    private ContentHelper(Context context) {
        this.httpHelper = new HttpHelper(context);
//        this.dbHelper = new DBHelper(context);
    }

    /**
     * Initializes content helper singleton
     *
     * @param context context object
     */
    private static void init(Context context) {
        if(context == null)
            throw new NullPointerException("Can't create instance with null context");

        INSTANCE = new ContentHelper(context);
    }

    /**
     * Returns ContentHelper instance. Throws IllegalStateException if called from non-Ui-thread
     * or if content helper instance is null (was not initialized).
     *
     * @return content helper instance
     */
    public static ContentHelper get() {
        if(Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException("Must be called from UI thread");

        if(INSTANCE == null)
            throw new IllegalStateException("ContentHelper is null. It must have been created at application init");

        return INSTANCE;
    }

    public int getWikisList(String hub, int batch, final ContentCallback<WikisList> callback) {
        return httpHelper.loadWikisList(hub, batch, callback);
    }

    public int getWikisDetails(int[] ids, final ContentCallback<JsonObject> callback) {
        return httpHelper.loadWikisDetails(ids, callback);
    }

    public int getWikiImage(String url, ImageView target, boolean scaled) {
        return httpHelper.loadWikiImage(url, target, scaled);
    }

    public boolean cancelRequest(int id) {
        return httpHelper.cancelRequest(id);
    }

    public void cancelAllImages() {
        httpHelper.cancelAllImages();
    }

}
