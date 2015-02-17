package tk.atna.wikiaapp;

/**
 * Callback interface to deliver content from ContentHelper
 */
public interface ContentCallback<T> {

    /**
     * Called when there is a result to deliver
     *
     * @param root root POJO of parsed result on success
     * @param e possible exception on failure
     */
    public void onResult(T root, Exception e);
}