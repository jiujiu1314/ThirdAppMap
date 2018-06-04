package net.lxj.thirdmapapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.baidumap)
    Button baidumap;
    @BindView(R.id.gaodemap)
    Button gaodemap;
    @BindView(R.id.webbaidumap)
    Button webbaidumap;
    private static String APP_NAME = "net.lxj.thirdmapapp";
    boolean isOpened;
    private String SNAME = "起点";
    private String DNAME="终点";
    //百度坐标
    private double Longitude = 100.081994;//经度
    private double Latitude = 23.898129;//纬度
    private String content = "临沧卫生学校附属医院";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

    }

    @OnClick({R.id.baidumap, R.id.gaodemap, R.id.webbaidumap})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.baidumap:
                openBaiduMap(Latitude, Longitude, DNAME);
                break;
            case R.id.gaodemap:
                openGaoDeMap(Latitude, Longitude, DNAME);
                break;
            case R.id.webbaidumap:
                openWebMap(Latitude,Longitude,DNAME,content);
                break;
        }
    }

//    /**
//     *
//     * @param slat 经纬度
//     * @param slon
//     * @param address 地点
//     */
//    private void chooseOpenMap(final double slat, final double slon, final String address) {
//        BottomSheet.Builder builder = new BottomSheet
//                .Builder(this, com.cocosw.bottomsheet.R.style.BottomSheet_Dialog)
//                .title("请选择");
//        builder.sheet(0, "百度地图").sheet(1, "高德地图")
//                .listener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            openBaiduMap(slat, slon, address);
//                        } else if (which == 1) {
//                            openGaoDeMap(slat, slon, address);
//                        }
//                    }
//                }).build().show();
//    }

    /**
     * 打开百度地图
     */
    /**
     *
     * @param slat 纬度
     * @param slon 经度
     * @param content 内容
     */
    private void openBaiduMap(double slat, double slon, String content) {
        if (OpenLocalMapUtil.isBaiduMapInstalled()) {
            try {
                String uri = OpenLocalMapUtil.getBaiduMapUri(String.valueOf(slat), String.valueOf(slon), content);
                Intent intent = new Intent();
                intent.setData(Uri.parse(uri));
                startActivity(intent); //启动调用
                isOpened = true;
            } catch (Exception e) {
                isOpened = false;
                e.printStackTrace();
            }
        } else {
            isOpened = false;
        }
    }

    /**
     * 打开浏览器进行百度地图导航
     */
    /**
     *
     * @param dlat 纬度
     * @param dlon 经度
     * @param dname 终点
     * @param content 地点内容
     */
    private void openWebMap(double dlat, double dlon, String dname, String content) {
        Uri mapUri = Uri.parse(OpenLocalMapUtil.getWebBaiduMapUri(
                String.valueOf(dlat), String.valueOf(dlon),
                dname, content, APP_NAME));
        Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
        startActivity(loction);
    }

    /**
     * 打开高德地图
     */
    /**
     *
     * @param dlat 纬度
     * @param dlon 纬度
     * @param content 终点
     */
    private void openGaoDeMap(double dlat, double dlon, String content) {
        if (OpenLocalMapUtil.isGdMapInstalled()) {
            try {
                //百度地图定位坐标转换成高德地图可识别坐标
                double[] loca = new double[2];
                loca = OpenLocalMapUtil.gcj02_To_Bd09(dlat, dlon);
                String uri = OpenLocalMapUtil.getGdMapUri(APP_NAME,
                        String.valueOf(loca[0]), String.valueOf(loca[1]), content);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setPackage("com.autonavi.minimap");
                intent.setData(Uri.parse(uri));
                startActivity(intent); //启动调用
                isOpened = true;

            } catch (Exception e) {
                isOpened = false;
                e.printStackTrace();
            }
        } else {
            isOpened = false;
        }
    }

}
