package com.avs.sendme.main;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.avs.sendme.R;
import com.avs.sendme.provider.SendMeContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendMeAdapter extends RecyclerView.Adapter<SendMeAdapter.SendMeViewHolder> {


    private Cursor data;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());


    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;


    @Override
    public SendMeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sendme_list, parent, false);

        return new SendMeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SendMeViewHolder holder, int position) {
        data.moveToPosition(position);

        String message = data.getString(MainActivity.COL_NUM_MESSAGE);
        String author = data.getString(MainActivity.COL_NUM_AUTHOR);
        String authorKey = data.getString(MainActivity.COL_NUM_AUTHOR_KEY);

        // Get the date for displaying
        long dateMillis = data.getLong(MainActivity.COL_NUM_DATE);
        String date;
        long now = System.currentTimeMillis();

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        if (now - dateMillis < (DAY_MILLIS)) {
            if (now - dateMillis < (HOUR_MILLIS)) {
                long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                date = minutes + "m";
            } else {
                long minutes = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = minutes + "h";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            date = dateFormat.format(dateDate);
        }

        // Add a dot to the date string
        date = "\u2022 " + date;

        holder.messageTextView.setText(message);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(date);

        // Choose the correct, and in this case, locally stored asset for the instructor. If there
        // were more users, you'd probably download this as part of the message.
        switch (authorKey) {
            case SendMeContract.ASSER_KEY:
                holder.authorImageView.setImageResource(R.drawable.ic_face_1);
                break;
            case SendMeContract.CEZANNE_KEY:
                holder.authorImageView.setImageResource(R.drawable.ic_face_2);
                break;
            case SendMeContract.JLIN_KEY:
                holder.authorImageView.setImageResource(R.drawable.ic_face_3);
                break;
            case SendMeContract.LYLA_KEY:
                holder.authorImageView.setImageResource(R.drawable.ic_face_4);
                break;
            case SendMeContract.NIKITA_KEY:
                holder.authorImageView.setImageResource(R.drawable.ic_face_5);
                break;
            default:
                holder.authorImageView.setImageResource(R.drawable.ic_face_6);
        }
    }

    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        data = newCursor;
        notifyDataSetChanged();
    }

    public class SendMeViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTextView;
        final TextView messageTextView;
        final TextView dateTextView;
        final ImageView authorImageView;

        public SendMeViewHolder(View layoutView) {
            super(layoutView);
            authorTextView = layoutView.findViewById(R.id.author_text_view);
            messageTextView = layoutView.findViewById(R.id.message_text_view);
            dateTextView = layoutView.findViewById(R.id.date_text_view);
            authorImageView = layoutView.findViewById(R.id.author_image_view);
        }
    }
}
