package speakeasy.brycelanglotz.com.speakeasy;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by brycelanglotz on 5/31/15.
 */
public class ReviewFormBaseAdapter extends BaseAdapter {

    Context context;
    String[] sections;
    float[] ratings;
    String[] reviews;
    private static LayoutInflater inflater = null;

    public ReviewFormBaseAdapter(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        int numberOfSections = data.length;

        this.context = context;
        this.sections = data;
        this.ratings = new float[numberOfSections];
        this.reviews = new String[numberOfSections];
        Arrays.fill(this.ratings, 0.00f);
        for (int i = 0; i < numberOfSections; i++) {
            this.reviews[i] = "";
        }
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sections.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return sections[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.review_form_list_item, null);

        TextView text = (TextView) vi.findViewById(R.id.section_header);
        text.setText(sections[position]);

        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar);
        ratingBar.setTag(position);
        ratingBar.setRating(ratings[position]);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings[(int) ratingBar.getTag()] = rating;
            }
        });

        return vi;
    }
}
