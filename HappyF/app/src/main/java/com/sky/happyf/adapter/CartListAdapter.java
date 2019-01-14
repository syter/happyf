package com.sky.happyf.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.GoodsDetailActivity;
import com.sky.happyf.manager.CartManager;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.SmoothCheckBox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartListAdapter extends BaseAdapter {
    private List<Cart> cartList;
    private Map<String, Cart> cartMap;
    private CartListActivity act;
    private boolean isEdit;
    private CartManager cartManager;

    public CartListAdapter(CartListActivity act, boolean isEdit, CartManager cartManager) {
        this.act = act;
        this.isEdit = isEdit;
        cartList = new ArrayList<Cart>();
        cartMap = new HashMap<>();
        this.cartManager = cartManager;
    }

    public List<Cart> getList() {
        return cartList;
    }

    public void applyData(List<Cart> carts) {
        cartList.clear();
        cartList.addAll(carts);

        cartMap.clear();
        for (Cart cart : cartList) {
            cartMap.put(cart.id, cart);
        }

        notifyDataSetChanged();
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public void checkAll(boolean isChecked) {
        for (Cart c : cartList) {
            c.removeSelected = isChecked;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Cart getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartListItem view = (CartListItem) convertView;
        if (convertView == null) {
            view = new CartListItem(parent.getContext());
        }

        view.setData(cartList.get(position));

        return view;
    }

    public class CartListItem extends LinearLayout {
        private ImageView ivCover, ivAdd, ivMinus;
        private TextView tvTitle, tvParam, tvPrice, tvShell, tvCount, tvInvalid;
        private LinearLayout llcb1, llcb2, llCartItem, llRight;
        private RelativeLayout rlBottom;
        private SmoothCheckBox cb1, cb2;

        public CartListItem(final Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_cart, this);
            ivCover = findViewById(R.id.iv_cover);
            ivAdd = findViewById(R.id.iv_add);
            ivMinus = findViewById(R.id.iv_minus);
            llCartItem = findViewById(R.id.ll_cart_item);
            llRight = findViewById(R.id.ll_right);
            rlBottom = findViewById(R.id.rl_bottom);
            tvTitle = findViewById(R.id.tv_title);
            tvParam = findViewById(R.id.tv_param);
            tvPrice = findViewById(R.id.tv_price);
            tvShell = findViewById(R.id.tv_shell);
            tvCount = findViewById(R.id.tv_count);
            tvInvalid = findViewById(R.id.tv_invalid);
            cb1 = findViewById(R.id.cb1);
            cb2 = findViewById(R.id.cb2);
            llcb1 = findViewById(R.id.ll_cb1);
            llcb2 = findViewById(R.id.ll_cb2);


        }

        public void setData(final Cart cart) {
            Glide.with(act).load(cart.cover).into(ivCover);
            tvTitle.setText(cart.title);
            tvParam.setText(cart.selectedType);
            tvShell.setText(cart.shellPrice);
            tvPrice.setText(act.getResources().getString(R.string.rmb) + cart.price);
            tvCount.setText(cart.count + "");

            cb2.setCheckedAttr(cart.removeSelected, true);
            cb2.setTag(cart.id);
            cb1.setCheckedAttr(cart.selected, true);
            cb1.setTag(cart.id);

            if (isEdit) {
                llcb1.setVisibility(View.GONE);
                llcb2.setVisibility(View.VISIBLE);
            } else {
                llcb1.setVisibility(View.VISIBLE);
                llcb2.setVisibility(View.GONE);
            }


            if (cart.isInvalid) {
                llCartItem.setBackgroundColor(act.getColor(R.color.cart_invalid_gray));
                rlBottom.setVisibility(View.GONE);
                tvInvalid.setVisibility(View.VISIBLE);

            } else {
                if (cart.isSeaFood) {
                    llCartItem.setBackgroundColor(act.getColor(R.color.fish_green));
                } else {
                    llCartItem.setBackgroundColor(act.getColor(R.color.white));
                }
                rlBottom.setVisibility(View.VISIBLE);
                tvInvalid.setVisibility(View.GONE);
                cb1.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        String id = (String) checkBox.getTag();
                        cartMap.get(id).selected = isChecked;
                        processPriceAndShellPrice();
                    }
                });
                llRight.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(act, GoodsDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", cart.goodsId);
                        intent.putExtras(bundle);
                        act.startActivity(intent);
                        act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                    }
                });
            }
            cb2.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    String id = (String) checkBox.getTag();
                    cartMap.get(id).removeSelected = isChecked;
                }
            });

            ivMinus.setTag(cart.id);
            ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(cart.count);
                    if (count == 1) {
                        Toast.makeText(act, getResources().getString(R.string.sd_count_one), Toast.LENGTH_LONG).show();
                        return;
                    }
                    count--;
                    cart.count = count + "";
                    tvCount.setText(cart.count);
                    processPriceAndShellPrice();

                    String cartId = (String) view.getTag();
                    cartManager.updateCount(cartId, cart.count, new CartManager.FetchCommonCallback() {
                        @Override
                        public void onFailure(String errorMsg) {
                            Toast.makeText(act, errorMsg, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinish(String text) {

                        }
                    });
                }
            });
            ivAdd.setTag(cart.id);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(cart.count);
                    if (count == 10) {
                        Toast.makeText(act, getResources().getString(R.string.sd_count_ten), Toast.LENGTH_LONG).show();
                        return;
                    }
                    count++;
                    cart.count = count + "";
                    tvCount.setText(cart.count);
                    processPriceAndShellPrice();

                    String cartId = (String) view.getTag();
                    cartManager.updateCount(cartId, cart.count, new CartManager.FetchCommonCallback() {
                        @Override
                        public void onFailure(String errorMsg) {
                            Toast.makeText(act, errorMsg, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinish(String text) {

                        }
                    });
                }
            });

            processPriceAndShellPrice();
        }
    }

    public void processPriceAndShellPrice() {
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal shellPrice = BigDecimal.ZERO;

        for (Cart c : cartList) {
            if (c.selected) {
                price = price.add(new BigDecimal(c.count).multiply(new BigDecimal(c.price)));
                shellPrice = shellPrice.add(new BigDecimal(c.count).multiply(new BigDecimal(c.shellPrice)));
            }
        }
        act.tvPrice.setText(act.getResources().getString(R.string.rmb) + price.toString());
        act.tvShellPrice.setText(shellPrice.toString());
    }

    public void removeCartGoods() {
        String cartIds = "";
        for (Cart c : cartList) {
            if (c.removeSelected) {
                cartIds = cartIds + c.id + ",";
                cartList.remove(c);
            }
        }
        if (Utils.isEmptyString(cartIds)) {
            return;
        }
        cartIds = cartIds.substring(0, cartIds.length() - 1);

        applyData(cartList);
        processPriceAndShellPrice();
        cartManager.removeCarts(cartIds, new CartManager.FetchCommonCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(act, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(String text) {
                Toast.makeText(act, act.getResources().getString(R.string.cart_remove_succ), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getSelectCartIds() {
        String cartIds = "";
        for (Cart c : cartList) {
            if (c.selected) {
                cartIds = cartIds + c.id + ",";
            }
        }
        if (Utils.isEmptyString(cartIds)) {
            return "";
        }
        cartIds = cartIds.substring(0, cartIds.length() - 1);
        return cartIds;
    }


}
