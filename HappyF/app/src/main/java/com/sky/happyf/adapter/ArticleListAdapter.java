package com.sky.happyf.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.happyf.Model.Article;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class ArticleListAdapter extends BaseAdapter {
    private List<Article> articleList;
    private Context ct;

    public ArticleListAdapter(Context ct) {
        this.ct = ct;
        articleList = new ArrayList<Article>();
    }

    public List<Article> getList() {
        return articleList;
    }

    public void applyData(List<Article> articles) {
        articleList.clear();
        articleList.addAll(articles);

        notifyDataSetChanged();
    }

    public void addData(List<Article> articles) {
        articleList.addAll(articles);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Article getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleListItem view = (ArticleListItem) convertView;
        if (convertView == null) {
            view = new ArticleListItem(parent.getContext());
        }

        view.setData(articleList.get(position));

        return view;
    }

    public class ArticleListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvTitle1, tvtitle2, tvReadCount, tvDate, tvAuthorName;

        public ArticleListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_article, this);
            ivCover = findViewById(R.id.iv_cover);
            tvTitle1 = findViewById(R.id.tv_title1);
            tvtitle2 = findViewById(R.id.tv_title2);
            tvAuthorName = findViewById(R.id.tv_author_name);
            tvDate = findViewById(R.id.tv_date);
            tvReadCount = findViewById(R.id.tv_read_count);
        }

        public void setData(final Article article) {
            Glide.with(ct).load(article.cover).into(ivCover);
            tvTitle1.setText(article.title);
            tvtitle2.setText(article.desc);
            tvAuthorName.setText(article.authorName);
            tvDate.setText(article.date);
            tvReadCount.setText(article.readCount + ct.getResources().getString(R.string.article_read_count));


        }
    }
}
