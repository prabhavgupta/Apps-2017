package hr.ferit.mdudjak.healthdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mario on 29.5.2017..
 */

public class NewsAdapter extends BaseAdapter {
    List<News> mNews;

    public NewsAdapter(List<News> news) {
        mNews = news;
    }

    @Override
    public int getCount() {
        return this.mNews.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsViewHolder newsViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_news, parent, false);
            newsViewHolder = new NewsViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (NewsViewHolder) convertView.getTag();
        }
        News news = this.mNews.get(position);
        newsViewHolder.tvTitle.setText(news.getTitle());
        newsViewHolder.tvDescription.setText(news.getDescription());
        newsViewHolder.tvPubDate.setText(news.getPubDate());

        Picasso.with(parent.getContext())
                .load(news.getImage())
                .fit()
                .centerCrop()
                .into(newsViewHolder.ivImage);
        return convertView;
    }

    static class NewsViewHolder {
        TextView tvTitle, tvDescription, tvPubDate;
        ImageView ivImage;
        public NewsViewHolder(View newsView) {
            this.tvTitle = (TextView) newsView.findViewById(R.id.tvTitle);
            this.tvDescription = (TextView) newsView.findViewById(R.id.tvDescription);
            this.tvPubDate= (TextView) newsView.findViewById(R.id.tvPubDate);
            this.ivImage = (ImageView) newsView.findViewById(R.id.ivImage);
        }
    }
}
