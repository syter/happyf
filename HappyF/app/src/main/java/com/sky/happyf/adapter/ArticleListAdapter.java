package com.sky.happyf.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public void updateItem(int index, Article article) {
        articleList.remove(index);
        articleList.add(index, article);
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
        private TextView tvTitle, tvDesc, tvReadCount, tvDate;

        public ArticleListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_article, this);
            ivCover = (ImageView) findViewById(R.id.iv_cover);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvDesc = (TextView) findViewById(R.id.tv_desc);
            tvDate = (TextView) findViewById(R.id.tv_date);
            tvReadCount = (TextView) findViewById(R.id.tv_read_count);
        }

        public void setData(final Article article) {
            tvTitle.setText(article.title);
            tvDesc.setText(article.desc);
            tvDate.setText(article.date);
            tvReadCount.setText("阅读量" + article.read_count);


        }
    }
}
