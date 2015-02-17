package tk.atna.wikiaapp;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class WikisFragment extends BaseFragment {

    public static String TAG = WikisFragment.class.getSimpleName();

    /**
     * Helper to get needed content data
     */
    private ContentHelper helper = ContentHelper.get();

    /**
     * Adapter for the list
     */
    private WikisArrayAdapter adapter;

    /**
     * Current state of showing content
     */
    private State state = new State();

    /**
     * Temporary number of remained pages
     */
    private int tempTogo;

    /**
     * Array of currently running request hashes
     */
    private SparseIntArray runningRequests = new SparseIntArray();

    @InjectView(R.id.progress)
    ProgressBar progress;

    @InjectView(R.id.lvData)
    ListView lvData;

    /**
     * List position to recall on configuration changes
     */
    private int currItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, container, false);
        ButterKnife.inject(this, v);

        // non re-creatable if exists
        if(adapter == null)
            adapter = new WikisArrayAdapter(inflater.getContext(), helper) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    // detect coming end of the list
                    if(position > getCount() - 4)
                        // make additional loading
                        if(state.togo > 0) {
                            getList(state.hub, ++state.batch);
                        }
                    return super.getView(position, convertView, parent);
                }
            };
        adapter.setNotifyOnChange(true);

        lvData.setAdapter(adapter);

        // listview decorations
        lvData.setFocusable(false);
        lvData.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvData.setDividerHeight(1);
        lvData.setCacheColorHint(0);
        lvData.setVerticalFadingEdgeEnabled(false);

        // recall position
        lvData.setSelection(currItem);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // fill the list if it is empty
        if(adapter.getCount() == 0)
            getList(state.hub, state.batch);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // remember list position
        currItem = lvData.getFirstVisiblePosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // cancel all current requests
        flushCurrent();
    }

    @OnItemClick(R.id.lvData)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // call details dialog for selected wiki
        makeFragmentAction(CLICK_DETAILS, Utils.toBundle(adapter.getItem(position), Wiki.class));
    }

    @Override
    protected void processInitArguments(Bundle args) {
        // update content state when started
        state.update(Utils.fromBundle(args, State.class));
    }

    @Override
    public void transferData(Bundle data) {
        if(data != null) {
            State newState = Utils.fromBundle(data, State.class);
            // hub from new state is not the same with current
            if(!state.hub.equals(newState.hub)) {
                // remember new state
                state.update(newState);
                // cancel all previous requests
                flushCurrent();
                // pull new data to the list
                getList(state.hub, state.batch);
            }
        }
    }

    /**
     * Cancel all current requests to content helper and flush adapter data
     */
    private void flushCurrent() {
        int size = runningRequests.size();
        for(int i = 0; i < size; i++) {
            helper.cancelRequest(runningRequests.get(i));
        }
        helper.cancelAllImages();
        adapter.clear();
    }

    /**
     * Pull list of wikis filtered by hub and page number
     *
     * @param hub needed hub name to pull
     * @param batch page number to pull
     */
    private void getList(String hub, int batch) {
        showProgress();
        int request = helper.getWikisList(hub, batch,
                                          new ContentCallback<WikisList>() {

            @Override
            public void onResult(WikisList root, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                    if(adapter.getCount() < 1)
                        finish();
                    return;
                }

                getDetails(root.listToArray());
                tempTogo = root.batches - root.currentBatch;
            }
        });
        runningRequests.put(runningRequests.size(), request);
    }


    /**
     * Pull detailes about wikis by ids
     *
     * @param ids array of wiki ids to get detailed info
     */
    private void getDetails(final int[] ids) {
        int request = helper.getWikisDetails(ids, new ContentCallback<JsonObject>() {

            @Override
            public void onResult(JsonObject root, Exception e) {
                if(e != null) {
                    e.printStackTrace();
                    if(adapter.getCount() < 1)
                        finish();
                    return;
                }

                updateAdapter(root, ids);
            }
        });
        runningRequests.put(runningRequests.size(), request);
    }

    /**
     * Parse received json object for new wikis by ids and fill list adapter with new data
     *
     * @param json json to parse
     * @param ids ids to search wikis
     */
    private void updateAdapter(JsonObject json, int[] ids) {
        for(int id : ids) {
            adapter.add((new Gson()).fromJson(json.get(String.valueOf(id)), Wiki.class));
        }

        state.togo = tempTogo;
        hideProgress();
    }

    /**
     * Finish this fragment, it is empty
     */
    private void finish() {
        hideProgress();
        makeFragmentAction(CLICK_FINISH, null);
    }

    /**
     * Show progress bar
     */
    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progress bar
     */
    private void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}
