package rikka.searchbyimage.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rikka.searchbyimage.R;
import rikka.searchbyimage.staticdata.CustomEngine;
import rikka.searchbyimage.utils.URLUtils;

public class ChromeCustomTabsActivity extends BaseActivity {
    public static final String EXTRA_URL =
            "rikka.searchbyimage.ui.ChromeCustomTabsActivity.EXTRA_URL";

    public static final String EXTRA_SITE_ID =
            "rikka.searchbyimage.ui.ChromeCustomTabsActivity.EXTRA_EDIT_LOCATION";

    boolean mIsURLOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome_custom_tabs);

        if (mIsURLOpened) {
            finish();

            return;
        }

        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int siteId = intent.getIntExtra(EXTRA_SITE_ID, 2);

            String siteName;
            if (siteId <= 5) {
                siteName = getResources().getStringArray(R.array.search_engines)[siteId];
            } else {
                CustomEngine item = CustomEngine.getItemById(siteId);
                siteName = item.getName();
            }

            String label = String.format(getString(R.string.search_result), siteName);

            setTaskDescription(new ActivityManager.TaskDescription(
                    label,
                    null,
                    getResources().getColor(R.color.colorPrimary)));
            setTitle(label);
        }

        URLUtils.Open(intent.getStringExtra(EXTRA_URL), this);

        // 只能用这种奇怪的办法了
        getWindow().getDecorView().postDelayed(URLOpened, 500);
    }

    private Runnable URLOpened = new Runnable() {
        @Override
        public void run() {
            mIsURLOpened = true;
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (mIsURLOpened) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int siteId = intent.getIntExtra(EXTRA_SITE_ID, 2);
            String siteName = getResources().getStringArray(R.array.search_engines)[siteId];
            String label = String.format(getString(R.string.search_result), siteName);

            setTaskDescription(new ActivityManager.TaskDescription(
                    label,
                    null,
                    getResources().getColor(R.color.colorPrimary)));
            setTitle(label);
        }

        URLUtils.Open(intent.getStringExtra(EXTRA_URL), this);

        // 只能用这种奇怪的办法了
        getWindow().getDecorView().postDelayed(URLOpened, 500);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mIsURLOpened)
            finish();
    }
}
