package com.sky.happyf.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.Model.Order;
import com.sky.happyf.R;
import com.sky.happyf.activity.OrderListActivity;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class OrderListAdapter extends BaseAdapter {
    private List<Order> orderList;
    private OrderListActivity ct;

    public OrderListAdapter(OrderListActivity ct) {
        this.ct = ct;
        orderList = new ArrayList<Order>();
    }

    public List<Order> getList() {
        return orderList;
    }

    public void applyData(List<Order> orders) {
        orderList.clear();
        orderList.addAll(orders);

        notifyDataSetChanged();
    }

    public void addData(List<Order> orders) {
        orderList.addAll(orders);
        notifyDataSetChanged();
    }

    public void updateItem(int index, Order order) {
        orderList.remove(index);
        orderList.add(index, order);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Order getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderListItem view = (OrderListItem) convertView;
        if (convertView == null) {
            view = new OrderListItem(parent.getContext());
        }

        view.setData(orderList.get(position));

        return view;
    }

    public class OrderListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvTimeClose, tvOrder, tvLastPrice, tvState, tvLastShellPrice;
        private Button btnCopy, btnRightbottom;
        private RelativeLayout rlRighttop;
        private LinearLayout llCartList, llLastShellPrice;

        public OrderListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_order, this);
            ivCover = findViewById(R.id.iv_cover);
            tvTimeClose = findViewById(R.id.tv_time_close);
            tvOrder = findViewById(R.id.tv_order);
            tvLastPrice = findViewById(R.id.tv_last_price);
            btnCopy = findViewById(R.id.btn_copy);
            btnRightbottom = findViewById(R.id.btn_rightbottom);
            rlRighttop = findViewById(R.id.rl_righttop);
            tvState = findViewById(R.id.tv_state);
            llCartList = findViewById(R.id.ll_cart_list);
            llLastShellPrice = findViewById(R.id.ll_last_shell_price);
            tvLastShellPrice = findViewById(R.id.tv_last_shell_price);
        }

        public void setData(final Order order) {
            tvTimeClose.setVisibility(View.GONE);
            rlRighttop.setVisibility(View.GONE);
            btnRightbottom.setVisibility(View.VISIBLE);
            if (Constants.ORDER_STATUS_DAIFUKUAN.equals(order.status)) {
                tvState.setText(getResources().getString(R.string.order_status_daifukuan));
                tvTimeClose.setVisibility(View.VISIBLE);
                tvTimeClose.setText(order.leftTime + getResources().getString(R.string.order_time_close));
                btnRightbottom.setBackground(ct.getDrawable(R.drawable.login_button));
                btnRightbottom.setText(ct.getResources().getString(R.string.order_btn_pay));
                btnRightbottom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processPay(order.id);
                    }
                });
            } else if (Constants.ORDER_STATUS_YIFUKUAN.equals(order.status)) {
                tvState.setText(getResources().getString(R.string.order_status_yifukuan));
                btnRightbottom.setBackground(ct.getDrawable(R.drawable.login_button));
                btnRightbottom.setText(ct.getResources().getString(R.string.order_btn_cuidan));
                btnRightbottom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ct, ct.getResources().getString(R.string.order_cuidan_succ), Toast.LENGTH_LONG).show();
                    }
                });
            } else if (Constants.ORDER_STATUS_YIFAHUO.equals(order.status)) {
                rlRighttop.setVisibility(View.VISIBLE);
                tvOrder.setText(order.postName + "：" + order.postNo);
                tvState.setText(getResources().getString(R.string.order_status_yifahuo));
                btnRightbottom.setBackground(ct.getDrawable(R.drawable.login_button));
                btnRightbottom.setText(ct.getResources().getString(R.string.order_btn_confirm));
                btnRightbottom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processConfirmOrder();
                    }
                });
            } else if (Constants.ORDER_STATUS_YISHOUHUO.equals(order.status)) {
                rlRighttop.setVisibility(View.VISIBLE);
                tvOrder.setText(getResources().getString(R.string.order_no) + "：" + order.orderNo);
                tvState.setText(getResources().getString(R.string.order_status_yishouhuo));
                btnRightbottom.setBackground(ct.getDrawable(R.drawable.login_button));
                btnRightbottom.setText(ct.getResources().getString(R.string.order_btn_confirm));
                btnRightbottom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processContact();
                    }
                });
            } else if (Constants.ORDER_STATUS_YIPINGJIA.equals(order.status)) {
                rlRighttop.setVisibility(View.VISIBLE);
                tvOrder.setText(getResources().getString(R.string.order_no) + "：" + order.orderNo);
                tvState.setText(getResources().getString(R.string.order_status_yipingjia));
                btnRightbottom.setVisibility(View.GONE);
            } else if (Constants.ORDER_STATUS_TUIHUOZHONG.equals(order.status)) {
                rlRighttop.setVisibility(View.VISIBLE);
                tvOrder.setText(getResources().getString(R.string.order_no) + "：" + order.orderNo);
                tvState.setText(getResources().getString(R.string.order_status_tuihuozhong));
                btnRightbottom.setBackground(ct.getDrawable(R.drawable.login_button));
                btnRightbottom.setText(ct.getResources().getString(R.string.order_btn_confirm));
                btnRightbottom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processContact();
                    }
                });
            } else if (Constants.ORDER_STATUS_YITUIHUO.equals(order.status)) {
                rlRighttop.setVisibility(View.VISIBLE);
                tvOrder.setText(getResources().getString(R.string.order_no) + "：" + order.orderNo);
                tvState.setText(getResources().getString(R.string.order_status_yituihuo));
                btnRightbottom.setVisibility(View.GONE);
            } else if (Constants.ORDER_STATUS_YIQUXIAO.equals(order.status)) {
                rlRighttop.setVisibility(View.VISIBLE);
                tvOrder.setText(getResources().getString(R.string.order_no) + "：" + order.orderNo);
                tvState.setText(getResources().getString(R.string.order_status_yiquxiao));
                btnRightbottom.setVisibility(View.GONE);
            }

            btnCopy.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) ct.getSystemService(Context.CLIPBOARD_SERVICE);
                    if (Constants.ORDER_STATUS_YIFAHUO.equals(order.status)) {
                        cm.setText(order.postNo);
                    } else {
                        cm.setText(order.orderNo);
                    }
                    Toast.makeText(ct, ct.getResources().getString(R.string.copy_succ), Toast.LENGTH_LONG).show();
                }
            });

            llCartList.removeAllViews();
            // 处理中间的列表
            for (Cart cart : order.cartList) {
                LinearLayout.LayoutParams rlItemParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(ct, 112));
                RelativeLayout rlItem = new RelativeLayout(ct);
                rlItem.setLayoutParams(rlItemParam);

                RelativeLayout.LayoutParams ivCoverParam = new RelativeLayout.LayoutParams(Utils.dip2px(ct, 100), Utils.dip2px(ct, 100));
                ImageView ivCover = new ImageView(ct);
                Glide.with(ct).load(cart.cover).into(ivCover);
                ivCoverParam.leftMargin = Utils.dip2px(ct, 6);
                ivCoverParam.topMargin = Utils.dip2px(ct, 6);
                ivCover.setLayoutParams(ivCoverParam);
                rlItem.addView(ivCover);

                RelativeLayout.LayoutParams tvTitle1Param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tvTitle1 = new TextView(ct);
                tvTitle1.setText(cart.title);
                tvTitle1.setTextColor(ct.getColor(R.color.black));
                tvTitle1.setTextSize(16);
                tvTitle1Param.leftMargin = Utils.dip2px(ct, 20);
                tvTitle1Param.topMargin = Utils.dip2px(ct, 8);
                tvTitle1Param.addRule(RelativeLayout.ALIGN_RIGHT, ivCover.getId());
                tvTitle1.setLayoutParams(tvTitle1Param);
                rlItem.addView(tvTitle1);

                RelativeLayout.LayoutParams tvParamParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tvParam = new TextView(ct);
                tvParam.setText(cart.selectedType);
                tvParam.setTextColor(ct.getColor(R.color.gray_text_2));
                tvParam.setTextSize(12);
                tvParamParam.leftMargin = Utils.dip2px(ct, 20);
                tvParamParam.topMargin = Utils.dip2px(ct, 5);
                tvParamParam.addRule(RelativeLayout.RIGHT_OF, ivCover.getId());
                tvParamParam.addRule(RelativeLayout.BELOW, tvTitle1.getId());
                tvParam.setLayoutParams(tvParamParam);
                rlItem.addView(tvParam);

                RelativeLayout.LayoutParams tvCountParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tvCount = new TextView(ct);
                tvCount.setText(cart.count);
                tvCount.setTextColor(ct.getColor(R.color.gray_text_2));
                tvCount.setTextSize(12);
                tvCountParam.rightMargin = Utils.dip2px(ct, 12);
                tvCountParam.bottomMargin = Utils.dip2px(ct, 5);
                tvCountParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvCountParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                tvCount.setLayoutParams(tvCountParam);
                rlItem.addView(tvCount);

                if (Constants.ORDER_PAY_TYPE_SHELL.equals(order.payType)) {
                    RelativeLayout.LayoutParams llShellPriceParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout llShellPrice = new LinearLayout(ct);
                    llShellPrice.setOrientation(LinearLayout.HORIZONTAL);
                    llShellPrice.setGravity(Gravity.CENTER_VERTICAL);
                    llShellPriceParam.leftMargin = Utils.dip2px(ct, 20);
                    llShellPriceParam.bottomMargin = Utils.dip2px(ct, 5);
                    llShellPriceParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    llShellPriceParam.addRule(RelativeLayout.RIGHT_OF, ivCover.getId());
                    llShellPrice.setLayoutParams(llShellPriceParam);

                    LinearLayout.LayoutParams ivShellParam = new LinearLayout.LayoutParams(Utils.dip2px(ct, 10), Utils.dip2px(ct, 10));
                    ImageView ivShell = new ImageView(ct);
                    Glide.with(ct).load(R.drawable.logo).into(ivShell);
                    ivShell.setLayoutParams(ivShellParam);
                    llShellPrice.addView(ivShell);

                    LinearLayout.LayoutParams tvShellPriceParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tvShellPrice = new TextView(ct);
                    tvShellPrice.setText(cart.shellPrice);
                    tvShellPrice.setTextColor(ct.getColor(R.color.gray_text_2));
                    tvShellPrice.setTextSize(12);
                    tvShellPriceParam.leftMargin = Utils.dip2px(ct, 3);
                    tvShellPrice.setLayoutParams(tvShellPriceParam);
                    llShellPrice.addView(tvShellPrice);

                    rlItem.addView(llShellPrice);
                } else {
                    RelativeLayout.LayoutParams tvPriceParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tvPrice = new TextView(ct);
                    tvPrice.setText(cart.price);
                    tvPrice.setTextColor(ct.getColor(R.color.gray_text_2));
                    tvPrice.setTextSize(12);
                    tvPriceParam.leftMargin = Utils.dip2px(ct, 20);
                    tvPriceParam.bottomMargin = Utils.dip2px(ct, 5);
                    tvPriceParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    tvPriceParam.addRule(RelativeLayout.RIGHT_OF, ivCover.getId());
                    tvPrice.setLayoutParams(tvPriceParam);
                    rlItem.addView(tvPrice);
                }

                llCartList.addView(rlItem);
            }

            if (Constants.ORDER_PAY_TYPE_SHELL.equals(order.payType)) {
                tvLastPrice.setVisibility(View.GONE);
                llLastShellPrice.setVisibility(View.VISIBLE);
                String priceDesc = "";
                if (new BigDecimal(order.postShellPrice).compareTo(BigDecimal.ZERO) > 0) {
                    priceDesc = order.totalShellPrice + "（运费" + order.postShellPrice + ")";
                } else {
                    priceDesc = order.totalShellPrice + "（" + order.postagePrice + ")";
                }
                tvLastShellPrice.setText(priceDesc);
            } else {
                tvLastPrice.setVisibility(View.VISIBLE);
                llLastShellPrice.setVisibility(View.GONE);
                String priceDesc = "";
                if (new BigDecimal(order.postPrice).compareTo(BigDecimal.ZERO) > 0) {
                    priceDesc = order.totalPrice + "（运费" + order.postPrice + ")";
                } else {
                    priceDesc = order.totalPrice + "（" + order.postagePrice + ")";
                }
                tvLastPrice.setText(priceDesc);
            }
        }
    }

    public void processPay(String orderId) {
        ct.processPay(orderId);
    }

    public void processConfirmOrder() {
        ct.processConfirmOrder();
    }

    public void processContact() {
        ct.processContact();
    }
}
