package tk.atna.wikiaapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WikiDetailsDialog extends DialogFragment {
	
	public final static String TAG = WikiDetailsDialog.class.getSimpleName();

    /**
     * Views for attributes of wiki object representation
     */
    @InjectView(R.id.ivImage)
    ImageView ivImage;

    @InjectView(R.id.tvTitle)
    TextView tvTitle;

    @InjectView(R.id.tvHub)
    TextView tvHub;

    @InjectView(R.id.tvUrl)
    TextView tvUrl;

    @InjectView(R.id.tvDesc)
    TextView tvDesc;

    @InjectView(R.id.tvEdits)
    TextView tvEdits;

    @InjectView(R.id.tvArticles)
    TextView tvArticles;

    @InjectView(R.id.tvPages)
    TextView tvPages;

    @InjectView(R.id.tvUsers)
    TextView tvUsers;

    @InjectView(R.id.tvActiveUsers)
    TextView tvActiveUsers;

    @InjectView(R.id.tvImages)
    TextView tvImages;

    @InjectView(R.id.tvVideos)
    TextView tvVideos;

    @InjectView(R.id.tvAdmins)
    TextView tvAdmins;

    /**
     * Initializes dialog with data
     *
     * @param data data to initialize with
     */
	public static WikiDetailsDialog init(Bundle data) {
		WikiDetailsDialog dialog = new WikiDetailsDialog();
		dialog.setArguments(data);
		return dialog;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// sets style and theme to dialog window
		setStyle(STYLE_NO_FRAME, R.style.theme_dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_layout, container, false);
        ButterKnife.inject(this, v);

		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // parse initial arguments
		if(getArguments() != null) {
			Wiki wiki = Utils.fromBundle(getArguments(), Wiki.class);
			if(wiki != null)
				// fill views with detailed data
				fillViews(wiki);
		}
	}

    /**
     * Fills all views with data about representable wiki
     */
    private void fillViews(Wiki wiki) {

        ContentHelper.get().getWikiImage(wiki.image, ivImage, false);

        tvTitle.setText(wiki.title);
        tvHub.setText(tvHub.getText() + wiki.hub);
        tvUrl.setText(tvUrl.getText() + wiki.url);
        tvDesc.setText(tvDesc.getText() + wiki.desc);
        tvEdits.setText(tvEdits.getText() + String.valueOf(wiki.stats.edits));
        tvArticles.setText(tvArticles.getText() + String.valueOf(wiki.stats.articles));
        tvPages.setText(tvPages.getText() + String.valueOf(wiki.stats.pages));
        tvUsers.setText(tvUsers.getText() + String.valueOf(wiki.stats.users));
        tvActiveUsers.setText(tvActiveUsers.getText() + String.valueOf(wiki.stats.activeUsers));
        tvImages.setText(tvImages.getText() + String.valueOf(wiki.stats.images));
        tvVideos.setText(tvVideos.getText() + String.valueOf(wiki.stats.videos));
        tvAdmins.setText(tvAdmins.getText() + String.valueOf(wiki.stats.admins));

    }

}
