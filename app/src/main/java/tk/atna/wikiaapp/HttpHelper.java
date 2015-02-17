package tk.atna.wikiaapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.SparseArray;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ImageViewFuture;

import java.io.IOException;

public class HttpHelper {

    /**
     * Link to image placeholder
     */
    private final static int DEFAULT_PICTURE = R.drawable.ic_default_picture;

    public final static String NETWORK_DOWN = "Network is unavailable at the moment";

    private final Context context;

    /**
     * Array to hold text requests
     */
    private SparseArray<Future> currentRequests;

    /**
     * Array to hold image requests
     */
    private SparseArray<ImageViewFuture> currentImageRequests;


    public HttpHelper(Context context) {
        this.context = context;
        this.currentRequests = new SparseArray<>();
        this.currentImageRequests = new SparseArray<>();

        // logging for debug
//        Ion.getDefault(context)
//           .configure()
//           .setLogging("myLogs", Log.DEBUG);
    }

    /**
     * Loads list of wikis filtered by hub name and page number
     *
     * @param hub needed hub name
     * @param batch needed page number
     * @param callback typed callback to deliver result
     * @return request id or 0
     */
    public int loadWikisList(String hub, int batch, final ContentCallback<WikisList> callback) {
        if(!isNetworkUp()) {
            callback.onResult(null, new IOException(NETWORK_DOWN));
            return 0;
        }

        String url = ServerApi.buildListUrl(hub, batch);
        final int hash = url.hashCode();

        Future<WikisList> request = Ion.with(context)
                .load(url)
                .as(new TypeToken<WikisList>(){})
                .setCallback(new FutureCallback<WikisList>() {
                    @Override
                    public void onCompleted(Exception e, WikisList result) {
                        // deliver result
                        callback.onResult(result, e);
                        // forget request
                        currentRequests.remove(hash);
                    }
                });

        // remember request
        currentRequests.put(hash, request);

        return hash;
    }

    /**
     * Loads details of wikis filtered by ids
     *
     * @param ids ids of wikis to search
     * @param callback typed callback to deliver result
     * @return request id or 0
     */
    public int loadWikisDetails(int[] ids, final ContentCallback<JsonObject> callback) {
        if(!isNetworkUp()) {
            callback.onResult(null, new IOException(NETWORK_DOWN));
            return 0;
        }

        String url = ServerApi.buildDetailsUrl(ids);
        final int hash = url.hashCode();

        Future<JsonObject> request = Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // deliver result
                        callback.onResult(result.getAsJsonObject(ServerApi.ITEMS), e);
                        // forget request
                        currentRequests.remove(hash);
                    }
                });

        // remember request
        currentRequests.put(hash, request);

        return hash;
    }

    /**
     * Loads image represented by url and places it into the target ImageView.
     * Image can be loaded scaled, optional.
     *
     * @param url original image url
     * @param target ImageView to load image into
     * @param scaled scaled image needed or not
     * @return request id or 0
     */
    public int loadWikiImage(String url, ImageView target, boolean scaled) {
        if(!isNetworkUp())
            return 0;

        final int hash = url.hashCode();

        ImageViewFuture request = (ImageViewFuture) Ion.with(target)
                .placeholder(DEFAULT_PICTURE)
                .error(DEFAULT_PICTURE)
                .smartSize(true)
                .centerCrop()
                .animateIn(android.R.anim.fade_in)
                .load(scaled ? ServerApi.buildScaledImageUrl(url) : url)
                .setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView result) {
                        // forget request
                        currentImageRequests.remove(hash);
                    }
                });

        // remember request
        currentImageRequests.put(hash, request);

        return hash;
    }

    /**
     * Cancels request by id
     *
     * @param id id of cancelable request
     * @return true if such request found and successfully cancelled, false otherwise
     */
    public boolean cancelRequest(int id) {
        Future f = currentRequests.get(id);
        return (f != null) && f.cancel();
    }

    /**
     * Cancels all known image requests
     */
    public void cancelAllImages() {
        int size = currentImageRequests.size();
        for (int i = 0; i < size; i++) {
            currentImageRequests.valueAt(i).cancel();
        }
    }

    /**
     * Checks if network is ready to process server requests
     *
     * @return true if network is available, false otherwise
     */
    private boolean isNetworkUp() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // network is not available at all
        if(cm == null)
            return false;

        NetworkInfo ni = cm.getActiveNetworkInfo();
        // default network is presented and it is able to connect through and it is connected now
        return (ni != null) && ni.isAvailable() && ni.isConnected();
    }

}

