package com.sky.happyf.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.CartPrice;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.SelectType;
import com.sky.happyf.R;
import com.sky.happyf.manager.FinishActivityManager;
import com.sky.happyf.manager.GoodsManager;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.message.MessageEvent;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.GlideImageLoader;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends BaseActivity {
    private GoodsManager goodsManager;
    private UserManager userManager;
    private ImageView ivBack, ivClose, ivMinus, ivAdd, ivGoodsCover;
    private TextView tvCount, tvTitle1, tvPostageRule, tvSellCount, tvDetail, tvSelectParam, tvPrice,
            tvShellPrice, tvCartPrice, tvGoodsPrice, tvGoodsShellPrice, tvSelected, tvServiceTitle1, tvServiceTitle2,
            tvServiceDesc1, tvServiceDesc2, tvServiceDesc3, tvServiceDesc4, tvService;
    private Button btnBuy, btnConfirmSelect, btnConfirmService;
    private Banner banner;
    private RelativeLayout rlSelect, rlService, rlServiceDialog, rlSelectDialog, rlEmptySelect, rlEmptyService, rlDialogBlack;
    private LinearLayout llCart, llImage, llSelectParam;
    private boolean isShowDialog;
    private Goods currentGoods;
    private SelectType currentSelectType;
    private List<Button> allSelectTypeButtons;
    private int currentNumber = 1;
    private boolean isJoinCartDirectly = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        FinishActivityManager.getManager().addActivity(this);

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
        banner.setDelayTime(3000);

        tvService = findViewById(R.id.tv_service);
        tvServiceTitle1 = findViewById(R.id.tv_service_title1);
        tvServiceTitle2 = findViewById(R.id.tv_service_title2);
        tvServiceDesc1 = findViewById(R.id.tv_service_desc1);
        tvServiceDesc2 = findViewById(R.id.tv_service_desc2);
        tvServiceDesc3 = findViewById(R.id.tv_service_desc3);
        tvServiceDesc4 = findViewById(R.id.tv_service_desc4);
        ivGoodsCover = findViewById(R.id.iv_goods_cover);
        tvGoodsPrice = findViewById(R.id.tv_goods_price);
        tvGoodsShellPrice = findViewById(R.id.tv_goods_shell_price);
        tvSelected = findViewById(R.id.tv_selectd);
        llSelectParam = findViewById(R.id.ll_select_param);
        tvTitle1 = findViewById(R.id.tv_title1);
        tvPrice = findViewById(R.id.tv_price);
        tvShellPrice = findViewById(R.id.tv_shell_price);
        tvCartPrice = findViewById(R.id.tv_cart_price);
        tvDetail = findViewById(R.id.tv_detail);
        llImage = findViewById(R.id.ll_images);
        tvPostageRule = findViewById(R.id.tv_postage_rule);
        tvSelectParam = findViewById(R.id.tv_select_param);
        tvSellCount = findViewById(R.id.tv_sell_count);
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
        rlDialogBlack = findViewById(R.id.rl_dialog_black);
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
                if (currentSelectType == null) {
                    showSelectDialog();
                    isJoinCartDirectly = true;

//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sd_select_error), Toast.LENGTH_LONG).show();
                } else {
                    joinGoodToCart();
                }
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
                isJoinCartDirectly = false;
            }
        });
        rlEmptySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
                isJoinCartDirectly = false;
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
                isJoinCartDirectly = false;
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
                if (isJoinCartDirectly) {
                    joinGoodToCart();
                    isJoinCartDirectly = false;
                }
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
                currentNumber = count;
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
                currentNumber = count;
                tvCount.setText(count + "");
            }
        });


        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userManager.isUserLogin()) {
                    startActivity(new Intent(GoodsDetailActivity.this, CartListActivity.class));
                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    startActivity(new Intent(GoodsDetailActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });


    }

    private void hideSelectDialog() {
        if (!isShowDialog) {
            return;
        }
        isShowDialog = false;
        rlDialogBlack.setVisibility(View.GONE);
        rlSelectDialog.setVisibility(View.GONE);
        rlSelectDialog.setAnimation(Utils.moveToViewBottom());
    }

    private void showSelectDialog() {
        if (isShowDialog) {
            return;
        }
        isShowDialog = true;
        rlDialogBlack.setVisibility(View.VISIBLE);
        rlSelectDialog.setVisibility(View.VISIBLE);
        rlSelectDialog.setAnimation(Utils.moveToViewLocation());
    }

    private void hideServiceDialog() {
        if (!isShowDialog) {
            return;
        }
        isShowDialog = false;
        rlDialogBlack.setVisibility(View.GONE);
        rlServiceDialog.setVisibility(View.GONE);
        rlServiceDialog.setAnimation(Utils.moveToViewBottom());
    }

    private void showServiceDialog() {
        if (isShowDialog) {
            return;
        }
        isShowDialog = true;
        rlDialogBlack.setVisibility(View.VISIBLE);
        rlServiceDialog.setVisibility(View.VISIBLE);
        rlServiceDialog.setAnimation(Utils.moveToViewLocation());
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");

        goodsManager = new GoodsManager(this);
        userManager = new UserManager(this);

        initGoods(id);

        initCart();
    }

    private void initGoods(String id) {
        goodsManager.getGoodsDetail(id, new GoodsManager.FetchGoodsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Goods> data) {
                currentGoods = data.get(0);
                allSelectTypeButtons = new ArrayList<>();
                tvTitle1.setText(currentGoods.title1);
                tvPostageRule.setText(currentGoods.postageRule);
                tvSellCount.setText(currentGoods.sellCount + getResources().getString(R.string.sd_selled));
                banner.setImages(currentGoods.covers).setImageLoader(new GlideImageLoader()).start();
                if (currentGoods.serviceType.equals(Constants.SERVICE_TYPE_ONE)) {
                    tvService.setText(getResources().getString(R.string.sd_service1));
                    tvServiceTitle1.setText(getResources().getString(R.string.sd_service_baotui));
                    tvServiceTitle2.setText(getResources().getString(R.string.sd_service_base));
                    tvServiceDesc1.setText(getResources().getString(R.string.sd_service_baotui_desc));
                    tvServiceDesc2.setText(getResources().getString(R.string.sd_service_base_1));
                    tvServiceDesc3.setText(getResources().getString(R.string.sd_service_base_2));
                    tvServiceDesc4.setText(getResources().getString(R.string.sd_service_base_3));
                } else {
                    tvService.setText(getResources().getString(R.string.sd_service2));
                    tvServiceTitle1.setText(getResources().getString(R.string.sd_service_tuihuan));
                    tvServiceTitle2.setText(getResources().getString(R.string.sd_service_base));
                    tvServiceDesc1.setText(getResources().getString(R.string.sd_service_tuihuan_desc));
                    tvServiceDesc2.setText(getResources().getString(R.string.sd_service_base_4));
                    tvServiceDesc3.setText(getResources().getString(R.string.sd_service_base_5));
                    tvServiceDesc4.setText(getResources().getString(R.string.sd_service_base_6));
                }
                tvDetail.setText(currentGoods.desc);
                for (final String coverUrl : currentGoods.descCovers) {
                    final ImageView ivDescCover = new ImageView(GoodsDetailActivity.this);

                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    final int screenWidth = dm.widthPixels;

                    llImage.addView(ivDescCover);


                    Glide.with(GoodsDetailActivity.this)
                            .load(coverUrl)
                            .asBitmap()//强制Glide返回一个Bitmap对象
                            .into(new SimpleTarget<Bitmap>() {
                                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    int realBannerHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                                    if (width > screenWidth) {
                                        realBannerHeight = height * screenWidth / width;
                                    }
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, realBannerHeight);
                                    ivDescCover.setLayoutParams(params);
                                    Glide.with(GoodsDetailActivity.this).load(coverUrl).into(ivDescCover);
                                }
                            });
                }

                List<SelectType> stList = currentGoods.selectTypes;
                String lowestPrice = "";
                String lowestShellPrice = "";
                for (int i = 0; i < stList.size(); i++) {
                    SelectType st = stList.get(i);

                    String price = st.price;
                    String shellPrice = st.shellPrice;
                    if (Utils.isEmptyString(lowestPrice)) {
                        lowestPrice = price;
                        lowestShellPrice = shellPrice;
                    } else {
                        BigDecimal tempLowPrice = new BigDecimal(lowestPrice);
                        BigDecimal tempLowShellPrice = new BigDecimal(lowestShellPrice);
                        BigDecimal tempPrice = new BigDecimal(lowestPrice);
                        BigDecimal tempShellPrice = new BigDecimal(lowestShellPrice);
                        if (tempLowPrice.compareTo(tempPrice) > 0) {
                            lowestPrice = price;
                        }
                        if (tempLowShellPrice.compareTo(tempShellPrice) > 0) {
                            lowestShellPrice = shellPrice;
                        }
                    }

                    Button btnSelectType = new Button(GoodsDetailActivity.this);
                    btnSelectType.setText(st.name);
                    btnSelectType.setBackground(getDrawable(R.drawable.shop_unselect_param_button));
                    btnSelectType.setTextSize(14);
                    btnSelectType.setTextColor(getColor(R.color.black));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            Utils.dip2px(GoodsDetailActivity.this, 36));
                    params.topMargin = Utils.dip2px(GoodsDetailActivity.this, 12);
                    btnSelectType.setPadding(10, 0, 10, 0);
                    llSelectParam.addView(btnSelectType, params);
                    btnSelectType.setTag(st.id);

                    btnSelectType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String stId = (String) view.getTag();
                            for (SelectType st : currentGoods.selectTypes) {
                                if (st.id.equals(stId)) {
                                    currentSelectType = st;
                                    break;
                                }
                            }

                            for (Button btn : allSelectTypeButtons) {
                                btn.setBackground(getDrawable(R.drawable.shop_unselect_param_button));
                                btn.setTextColor(getColor(R.color.black));
                            }
                            view.setBackground(getDrawable(R.drawable.shop_select_param_button));
                            ((Button) view).setTextColor(getColor(R.color.white));

                            processSelectType();


                        }
                    });
                    allSelectTypeButtons.add(btnSelectType);
                }
                if (stList.size() == 1) {
                    tvPrice.setText(getResources().getString(R.string.rmb) + lowestPrice);
                    tvShellPrice.setText(lowestShellPrice);
                    tvGoodsPrice.setText(getResources().getString(R.string.rmb) + lowestPrice);
                    tvGoodsShellPrice.setText(lowestShellPrice);
                    currentSelectType = stList.get(0);

                    allSelectTypeButtons.get(0).setBackground(getDrawable(R.drawable.shop_select_param_button));
                    allSelectTypeButtons.get(0).setTextColor(getColor(R.color.white));
                    processSelectType();
                } else {
                    tvPrice.setText(getResources().getString(R.string.rmb) + lowestPrice + getResources().getString(R.string.at_least));
                    tvShellPrice.setText(lowestShellPrice + getResources().getString(R.string.at_least));
                    tvGoodsPrice.setText(getResources().getString(R.string.rmb) + lowestPrice + getResources().getString(R.string.at_least));
                    tvGoodsShellPrice.setText(lowestShellPrice + getResources().getString(R.string.at_least));
                }

                Glide.with(GoodsDetailActivity.this).load(currentGoods.covers.get(0)).into(ivGoodsCover);
            }
        });
    }

    private void processSelectType() {
        tvSelected.setText(currentSelectType.name);
        tvGoodsPrice.setText(getResources().getString(R.string.rmb) + currentSelectType.price);
        tvGoodsShellPrice.setText(currentSelectType.shellPrice);
        tvPrice.setText(getResources().getString(R.string.rmb) + currentSelectType.price);
        tvShellPrice.setText(currentSelectType.shellPrice);
        tvSelectParam.setText(currentSelectType.name);
//        tvSelectParam.setTextColor(getColor(R.color.black));
    }

    private void initCart() {
        if (userManager.isUserLogin()) {
            userManager.getMyCartPrice(new UserManager.FetchCartPriceCallback() {
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(GoodsDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish(CartPrice cp) {
                    tvCartPrice.setVisibility(View.VISIBLE);
                    if (Utils.isEmptyString(cp.price)) {
                        tvCartPrice.setText(getResources().getString(R.string.rmb) + "0");
                    } else {
                        BigDecimal tempPrice = new BigDecimal(cp.price);
                        if (tempPrice.compareTo(new BigDecimal("10000")) >= 0) {
                            tvCartPrice.setText(getResources().getString(R.string.rmb) + "9999+");
                        } else {
                            tvCartPrice.setText(getResources().getString(R.string.rmb) + cp.price);
                        }
                    }
                }
            });
        } else {
            tvCartPrice.setText(getResources().getString(R.string.rmb) + "0");
        }
    }

    private void joinGoodToCart() {
        if (userManager.isUserLogin()) {
            goodsManager.joinGoodsToCart(currentGoods.id, currentSelectType.id, currentNumber, new GoodsManager.FetchCommonCallback() {
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(GoodsDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish(String text) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sd_join_cart_succ), Toast.LENGTH_LONG).show();
                    initCart();
                    EventBus.getDefault().post(new MessageEvent(Constants.EVENT_MESSAGE_CART));
                }
            });
        } else {
            startActivity(new Intent(GoodsDetailActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
