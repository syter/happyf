package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.R;
import com.sky.happyf.util.GlideImageLoader;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends BaseActivity {
    private Goods goods;
    private ImageView ivBack, ivClose, ivMinus, ivAdd;
    private TextView tvCount;
    private Button btnBuy, btnConfirmSelect, btnConfirmService;
    private Banner banner;
    private RelativeLayout rlSelect, rlService, rlServiceDialog, rlSelectDialog, rlEmptySelect, rlEmptyService;
    private LinearLayout llCart;
    private boolean isShowDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setDarkMode(getWindow());
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.white), 0);


        initView();

        initListener();

        initData();
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int realBannerHeight = 353 * screenWidth / 375;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) banner.getLayoutParams();
        params.height = realBannerHeight;
        banner.setLayoutParams(params);

        ivBack = findViewById(R.id.iv_back);
        ivClose = findViewById(R.id.iv_close);
        ivMinus = findViewById(R.id.iv_minus);
        ivAdd = findViewById(R.id.iv_add);
        tvCount = findViewById(R.id.tv_count);
        btnConfirmSelect = findViewById(R.id.btn_confirm_select);
        btnConfirmService = findViewById(R.id.btn_confirm_service);
        rlSelect = findViewById(R.id.rl_select);
        rlService = findViewById(R.id.rl_service);
        llCart = findViewById(R.id.ll_cart);
        rlServiceDialog = findViewById(R.id.rl_service_dialog);
        rlSelectDialog = findViewById(R.id.rl_select_dialog);
        rlEmptySelect = findViewById(R.id.rl_empty_select);
        rlEmptyService = findViewById(R.id.rl_empty_service);
        int screenHeight = dm.heightPixels;
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) rlEmptySelect.getLayoutParams();
        params2.height = screenHeight - Utils.dip2px(getApplicationContext(), 400);
        rlEmptySelect.setLayoutParams(params2);
        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) rlEmptyService.getLayoutParams();
        params3.height = screenHeight - Utils.dip2px(getApplicationContext(), 400);
        rlEmptyService.setLayoutParams(params3);


        btnBuy = findViewById(R.id.btn_buy);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.sd_join_cart_succ), Toast.LENGTH_LONG).show();
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Logger.d("点击了banner - " + position);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
            }
        });
        rlEmptySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
            }
        });
        rlEmptyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideServiceDialog();
            }
        });
        rlSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog();
            }
        });
        rlService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServiceDialog();
            }
        });
        btnConfirmSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
            }
        });
        btnConfirmService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideServiceDialog();
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countStr = tvCount.getText().toString();
                int count = Integer.parseInt(countStr);
                if (count == 1) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sd_count_one), Toast.LENGTH_LONG).show();
                    return;
                }
                count--;
                tvCount.setText(count + "");
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countStr = tvCount.getText().toString();
                int count = Integer.parseInt(countStr);
                if (count == 10) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sd_count_ten), Toast.LENGTH_LONG).show();
                    return;
                }
                count++;
                tvCount.setText(count + "");
            }
        });

        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GoodsDetailActivity.this, CartListActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

    }

    private void hideSelectDialog() {
        if (!isShowDialog) {
            return;
        }
        isShowDialog = false;
        rlSelectDialog.setVisibility(View.GONE);
        rlSelectDialog.setAnimation(Utils.moveToViewBottom());
    }

    private void showSelectDialog() {
        if (isShowDialog) {
            return;
        }
        isShowDialog = true;
        rlSelectDialog.setVisibility(View.VISIBLE);
        rlSelectDialog.setAnimation(Utils.moveToViewLocation());
    }

    private void hideServiceDialog() {
        if (!isShowDialog) {
            return;
        }
        isShowDialog = false;
        rlServiceDialog.setVisibility(View.GONE);
        rlServiceDialog.setAnimation(Utils.moveToViewBottom());
    }

    private void showServiceDialog() {
        if (isShowDialog) {
            return;
        }
        isShowDialog = true;
        rlServiceDialog.setVisibility(View.VISIBLE);
        rlServiceDialog.setAnimation(Utils.moveToViewLocation());
    }

    private void initData() {
        List<String> images = new ArrayList<>();
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546591312549&di=5e6a35970d026692b6fd4dbbec89cdf1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D285966d73cd3d539d530078052ee8325%2Fb7003af33a87e950d499b5451a385343fbf2b409.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546591347518&di=05c38408f670c180b735a7a9d769746b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F9922720e0cf3d7caceea8850f81fbe096b63a9fc.jpg");
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
