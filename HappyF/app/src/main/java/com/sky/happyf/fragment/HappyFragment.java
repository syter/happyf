package com.sky.happyf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.sky.happyf.Model.Happy;
import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.GoodsDetailActivity;
import com.sky.happyf.activity.WannaHappyActivity;
import com.sky.happyf.manager.HappyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HappyFragment extends Fragment {
    private View view;

    private MapView mMapView;
    private BaiduMap mBaidumap;
    private InfoWindow infoWindow;
    private List<LatLng> pointList;
    private HappyManager happyManager;
    private List<Happy> currentHappyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_happy, container, false);

        initView();

        initListener();

        initData();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView() {
        mMapView = view.findViewById(R.id.map_view);
        mBaidumap = mMapView.getMap();
    }

    private void initListener() {
        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                String pos = bundle.getString("pos");
                processInfoWindowView(Integer.parseInt(pos));
                return false;
            }
        });
    }

    private void initData() {
        LatLng GEO_SHANGHAI = new LatLng(29.926068, 121.970601);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(GEO_SHANGHAI)
                .zoom(10)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaidumap.setMapStatus(mMapStatusUpdate);

        happyManager = new HappyManager(getActivity());
        pointList = new ArrayList<>();

        initMapInfo();
    }

    private void processInfoWindowView(final int pos) {
        LinearLayout llInfoWindow = new LinearLayout(getActivity());
        llInfoWindow.setOrientation(LinearLayout.VERTICAL);
        llInfoWindow.setBackgroundResource(R.drawable.popup);
        llInfoWindow.setGravity(Gravity.CENTER);
        TextView tvInfo = new TextView(getActivity());
        tvInfo.setText(currentHappyList.get(pos).title1);
        tvInfo.setTextSize(12);
        tvInfo.setGravity(Gravity.CENTER_HORIZONTAL);
        tvInfo.setTextColor(getActivity().getColor(R.color.gray_text_2));
        TextView tvInto = new TextView(getActivity());
        tvInto.setText(getResources().getString(R.string.happy_into));
        tvInto.setTextSize(8);
        tvInto.setGravity(Gravity.CENTER_HORIZONTAL);
        tvInto.setTextColor(getActivity().getColor(R.color.gray_text_1));
        llInfoWindow.addView(tvInfo);
        llInfoWindow.addView(tvInto);

        infoWindow = new InfoWindow(llInfoWindow, pointList.get(pos), -100);
        mBaidumap.showInfoWindow(infoWindow);

        llInfoWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WannaHappyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", currentHappyList.get(pos).id);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void initMapInfo() {
        happyManager.getMapInfo(new HappyManager.FetchHappysCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Happy> happyList) {
                currentHappyList = happyList;
                int i = 0;
                for (Happy happy : currentHappyList) {
                    LatLng point = new LatLng(Double.parseDouble(happy.latitude), Double.parseDouble(happy.longitude));
                    pointList.add(point);
                    BitmapDescriptor bitmapDes = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmapDes);
                    Marker marker = (Marker) mBaidumap.addOverlay(option);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("pos", i + "");
                    marker.setExtraInfo(mBundle);
                    i++;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
    }

}
