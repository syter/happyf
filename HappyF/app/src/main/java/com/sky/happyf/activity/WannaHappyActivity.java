package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sky.happyf.R;
import com.sky.happyf.util.GlideImageLoader;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

public class WannaHappyActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private Button btnWanna;
    private Banner banner;
    private RelativeLayout rlSelectDialog, rlSelectType, rlSelectDate;
    private TextView tvPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanna_happy);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        btnWanna = findViewById(R.id.btn_wanna);
        titleBar = findViewById(R.id.titlebar);

        banner = findViewById(R.id.banner);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int realBannerHeight = 250 * screenWidth / 375;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
        params.height = realBannerHeight;
        banner.setLayoutParams(params);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                }
            }
        });

        btnWanna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WannaHappyActivity.this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("price", "100");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Logger.d("点击了banner - " + position);
            }
        });
    }

    private void initData() {
        titleBar.getCenterTextView().setText("xx海鱼");

//        banner.setImages(currentGoods.covers).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
