package tk.atna.wikiaapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity
                          implements BaseFragment.FragmentActionCallback {

    /**
     * Resource to place content into
     */
    private static final int CONTENT_RESOURCE = R.id.content;

    /**
     * Flag of content state. No content, by default
     */
    private boolean content = false;
    private static final String CONTENT = "content";

    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ButterKnife.inject(this);

        // recall content state
        if(savedInstanceState != null)
            content = savedInstanceState.getBoolean(CONTENT, false);

        fm = getSupportFragmentManager();

        // loads header fragment
        Utils.parkFragment(fm, R.id.header, HeaderFragment.class, false, null, false);

        // loads content fragment if needed
        if(content)
            Utils.parkFragment(fm, CONTENT_RESOURCE, WikisFragment.class, false, null, true);

    }

    @Override
    public void onAction(int action, Bundle data) {
        switch (action) {

            // handles new state transferring
            case BaseFragment.CLICK_GO:
                // if content fragment is in place
                if(content) {
                    // just send it a new state
                    (Utils.findFragment(fm, WikisFragment.class)).transferData(data);

                // load new wikis fragment as a content
                } else {
                    Utils.parkFragment(fm, CONTENT_RESOURCE, WikisFragment.class, true, data, true);
                    content = true;
                }
                break;

            // removes empty content fragment
            case BaseFragment.CLICK_FINISH:
                Utils.unparkFragment(fm, WikisFragment.class);
                content = false;
                break;

            // calls details dialog. data holds wiki to show
            case BaseFragment.CLICK_DETAILS:
                Utils.showDialog(fm, WikiDetailsDialog.class, data);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // remember current content state
        outState.putBoolean(CONTENT, content);
    }

}
