package tk.atna.wikiaapp;

import android.os.Bundle;

/**
 * Interface to deliver data from activity to fragment
 */
public interface ListeningFragment {

    /**
     * Called in activity for chosen fragment when transfer is needed
     *
     * @param data data to transfer
     */
    public void transferData(Bundle data);
}
