package tk.atna.wikiaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class BaseFragment extends Fragment implements ListeningFragment {

    /**
     * Fragment action to transfer new state to content fragment
     */
    public static final int CLICK_GO = 0x00000a1;

    /**
     * Fragment action to finish content fragment
     */
    public static final int CLICK_FINISH = 0x00000a2;

    /**
     * Fragment action to invoke wiki details dialog
     */
    public static final int CLICK_DETAILS = 0x00000a3;

    /**
     * Action callback
     */
	private FragmentActionCallback callback;

    /**
     * Initializes fragment of type clazz with data
     *
     * @param clazz type of fragment
     * @param data data to initialize with
     */
    public static <T extends Fragment> T init(Class<T> clazz, Bundle data) {
        try {
            T fragment = clazz.newInstance();
            fragment.setArguments(data);
            return fragment;

        } catch (InstantiationException | java.lang.InstantiationException
                        | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Invokes action callback
     *
     * @param action needed fragment command
     * @param data additional data to send
     */
	public void makeFragmentAction(int action, Bundle data) {
		if (callback != null)
			callback.onAction(action, data);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // process arguments for initialized fragment
        if(getArguments() != null)
            processInitArguments(getArguments());
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // initialize actions callback and
        // prove that hoster activity implements it
		try {
			callback = (FragmentActionCallback) getActivity();

		} catch (ClassCastException e) {
			e.printStackTrace();
			Log.d("myLogs", BaseFragment.class.getSimpleName()
							        + ".onActivityCreated: activity must implement "
							        + FragmentActionCallback.class.getSimpleName());
		}
	}

    /**
     * Handles arguments supplied to fragment on initialization.
     * Called in onCreate() method to ensure transferring args only
     * ones when fragment was created (for retained fragments).
     *
     * @param args non null bundle object with init arguments
     */
    protected void processInitArguments(Bundle args) {
        // override to use
    }

    @Override
    public void transferData(Bundle data) {
        // override to use
    }

    /**
     * Callback interface to deliver fragment actions to activity
     */
    public interface FragmentActionCallback {

        /**
         * Called on fragment action event
         *
         * @param action needed command
         * @param data additional data to send
         */
		public void onAction(int action, Bundle data);
	}


}
