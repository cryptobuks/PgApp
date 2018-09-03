package com.app.qingyi.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.utils.GlobleValue;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

import static android.graphics.Color.BLACK;

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private String address = "";
    private TextView tvAddress, tvethToDo, tvminEth,tvinfo;
    private ImageView imageView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideDialog();
            switch (msg.what) {
                case GlobleValue.SUCCESS://isMustUpdate
                    Bitmap bitmap = null;
                    try {
                        bitmap = createQRCode(address, imageView.getMeasuredWidth());
                        imageView.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        TextView tv_tit = (TextView) findViewById(R.id.tv_tit);
        tv_tit.setText("账户充值");

        tvinfo = (TextView) findViewById(R.id.info);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvethToDo = (TextView) findViewById(R.id.ethToDo);
        tvminEth = (TextView) findViewById(R.id.minEth);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.getString("address").equals("")) {
            address = bundle.getString("address");
            String minEth = bundle.getString("minEth");
            String ethToDo = bundle.getString("ethToDo");
            tvethToDo.setText(ethToDo + " Do");
            tvminEth.setText(minEth + " ETH");
            tvAddress.setText(address);
            tvinfo.setText("DO币可用来购买资源联系方式，1ETH="+ethToDo+"DO。将ETH打入下方地址后，会通过智能合约自动将Do币打入用户的ETH地址，请勿使用交易所地址打币。");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(GlobleValue.SUCCESS);
                }
            }, 500);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
//                clipboardManager.setText(tvAddress.getText());
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null,tvAddress.getText()));
                if (clipboardManager.hasPrimaryClip()){
                    clipboardManager.getPrimaryClip().getItemAt(0).getText();
                }
                Snackbar.make(tvAddress, "复制成功", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    public Bitmap createQRCode(String str, int widthAndHeight)
            throws WriterException {
        if (str.equals("")) return null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
