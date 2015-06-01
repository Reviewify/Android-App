package speakeasy.brycelanglotz.com.speakeasy;

import android.content.Context;
import android.media.Rating;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        //ViewHolder holder = null;
        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.review_form_list_item, null);
            holder.sectionHeader = (TextView) convertView.findViewById(R.id.section_header);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.reviewEditText = (EditText) convertView.findViewById(R.id.reviewEditText);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ref = position;

        holder.sectionHeader.setText(sections[position]);

        holder.ratingBar.setRating(ratings[position]);
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings[holder.ref] = rating;
            }
        });

        holder.reviewEditText.setText(reviews[position]);
        holder.reviewEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
                reviews[holder.ref] = editable.toString();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView sectionHeader;
        EditText reviewEditText;
        RatingBar ratingBar;
        int ref;
    }
}
