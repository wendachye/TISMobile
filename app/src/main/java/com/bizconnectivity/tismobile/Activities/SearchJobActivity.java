package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.fragments.SearchByCustomerNameFragment;
import com.bizconnectivity.tismobile.fragments.SearchByOrderIDFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class SearchJobActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_search)
    LinearLayout mLinearLayoutHeader;
    @BindView(R.id.text_header)
    TextView mTextViewHeader;

    //content
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    //footer
    @BindView(R.id.footer_search)
    LinearLayout mLinearLayoutFooter;
    @BindView(R.id.button_home)
    ImageButton mImageButtonHome;
    @BindView(R.id.button_search)
    ImageButton mImageButtonSearch;
    @BindView(R.id.button_switch)
    ImageButton mImageButtonSwitch;
    @BindView(R.id.button_settings)
    ImageButton mImageButtonSettings;

    Realm realm;
    LoadingBayDetail loadingBayDetail;
    Dialog exitDialog;
    PopupMenu popupMenu;
    SharedPreferences sharedPref;

    boolean isActivityStarted = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //action bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //header
        mTextViewHeader.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));

        //tab setup
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //region view pager
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new SearchByOrderIDFragment(), "Order ID");
        adapter.addFragment(new SearchByCustomerNameFragment(), "Customer Name");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {

            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }
    //endregion

    //region Footer
    @OnClick(R.id.button_home)
    public void btnHome(View view) {

        Intent intent = new Intent(this, DashboardActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_search)
    public void btnSearch(View view) {

        Intent intent = new Intent(this, SearchJobActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_switch)
    public void btnSwitch(View view) {

        Intent intent = new Intent(this, SwitchJobActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_settings)
    public void btnSettings(View view) {

        popupMenu = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menu_check_in:
                        Intent intentCheckIn = new Intent(getApplicationContext(), CheckInActivity.class);
                        isActivityStarted = true;
                        startActivity(intentCheckIn);
                        return true;

                    case R.id.menu_check_out:
                        Intent intentCheckOut = new Intent(getApplicationContext(), CheckOutActivity.class);
                        isActivityStarted = true;
                        startActivity(intentCheckOut);
                        return true;

                    case R.id.menu_sync_data:
                        Intent intentSyncData = new Intent(getApplicationContext(), SyncDataActivity.class);
                        isActivityStarted = true;
                        startActivity(intentSyncData);
                        return true;

                    case R.id.menu_exit:
                        exitApplication();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.settings_menu);
        popupMenu.show();
    }

    public void exitApplication() {

        if (exitDialog != null && exitDialog.isShowing()) return;

        exitDialog = new Dialog(this);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_exit_app);
        Button btnConfirm = (Button) exitDialog.findViewById(R.id.button_confirm);

        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear all shared preferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear().apply();

                //check out all loading bay
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        for (LoadingBayDetail results : realm.where(LoadingBayDetail.class).equalTo("status", LOADING_BAY_NO_CHECK_IN).findAll()) {

                            loadingBayDetail = new LoadingBayDetail();
                            loadingBayDetail.setLoadingBayNo(results.getLoadingBayNo());
                            loadingBayDetail.setStatus(LOADING_BAY_NO_CHECK_OUT);

                            realm.copyToRealmOrUpdate(loadingBayDetail);
                        }
                    }
                });

                //close exit dialog
                exitDialog.dismiss();

                //clear all activity and start login activity
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                isActivityStarted = true;
                startActivity(intentLogin);
            }
        });

        Button btnCancel = (Button) exitDialog.findViewById(R.id.button_cancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();
            }
        });

        int dividerId = exitDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = exitDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert exitDialog.getWindow() != null;
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        exitDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        exitDialog.show();
    }
    //endregion

    @Override
    public void onBackPressed() {

        exitApplication();
    }

    @Override
    public void onStop() {

        super.onStop();

        if (isActivityStarted) {

            finish();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        realm.close();
    }

}
