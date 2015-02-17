package tk.atna.wikiaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HeaderFragment extends BaseFragment {

    public static String TAG = HeaderFragment.class.getSimpleName();

    /**
     * View to show available hubs to be chosen
     */
    @InjectView(R.id.spnHub)
    Spinner spnHub;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.header_layout, container, false);
        ButterKnife.inject(this, v);

        // fill spinner with data
        spnHub.setAdapter(new ArrayAdapter<>(inflater.getContext(),
                          android.R.layout.simple_spinner_dropdown_item,
                          ServerApi.Hub.HUBS));
        // all hubs chosen by default
        spnHub.setSelection(0);

        return v;
    }

    @OnClick(R.id.btnGo)
    public void onClick(View v) {
        if(v.getId() == R.id.btnGo) {
            // send new state to content fragment
            Bundle data = Utils.toBundle(new State((String) spnHub.getSelectedItem()), State.class);
            makeFragmentAction(CLICK_GO, data);
        }
    }

}
