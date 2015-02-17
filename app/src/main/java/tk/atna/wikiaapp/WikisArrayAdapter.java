package tk.atna.wikiaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WikisArrayAdapter extends ArrayAdapter<Wiki> {

    /**
     * Resource for adapter items
     */
    private final static int ITEM_RES = R.layout.list_item_layout;

    /**
     * Helper to get needed content data
     */
    private ContentHelper helper;


	public WikisArrayAdapter(Context context, ContentHelper helper) {
		super(context, ITEM_RES);
        this.helper = helper;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ItemViewHolder holder;
		if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(ITEM_RES, parent, false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);

        } else
            holder = (ItemViewHolder) convertView.getTag();

        // pull scaled image into ImageView using content helper
        helper.getWikiImage(getItem(position).image, holder.ivImage, true);

        holder.tvTitle.setText(getItem(position).title);
        holder.tvUrl.setText(getItem(position).url);
        holder.tvScore.setText(String.valueOf(getItem(position).wamScore));

		return convertView;
	}

    /**
     * Class for ViewHolder pattern implementation
     */
    static class ItemViewHolder {

        @InjectView(R.id.ivImage)
        ImageView ivImage;

        @InjectView(R.id.tvTitle)
        TextView tvTitle;

        @InjectView(R.id.tvUrl)
        TextView tvUrl;

        @InjectView(R.id.tvScore)
        TextView tvScore;


        ItemViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
	
}
