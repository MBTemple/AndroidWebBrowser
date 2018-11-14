package edu.temple.webbrowser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WebFragment.GetURL {

    Button goButton;
    EditText urlBar;
    ViewPager viewPager;
    Toolbar toolbar;
    ArrayList<WebFragment> fragArrList = new ArrayList();

    FragmentStatePagerAdapter fragState = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int i) {
            return fragArrList.get(i);
        }

        @Override
        public int getCount() {
            return fragArrList.size();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goButton = findViewById(R.id.goButton);
        urlBar = findViewById(R.id.urlBar);
        viewPager = findViewById(R.id.viewPager);

        fragArrList.add(WebFragment.newInstance(""));

        viewPager.setAdapter(fragState);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                urlBar.setText(fragArrList.get(i).URL);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlGo = urlBar.getText().toString();
                if (urlGo.startsWith("http://") || (urlGo.startsWith("https://"))) {
                    urlGo = urlGo;
                } else if (!urlGo.startsWith("http://") || (!urlGo.startsWith("https://"))) {
                    urlGo = "https://" + urlGo;
                }
                int i = viewPager.getCurrentItem();
                fragArrList.get(i).loadURL(urlBar.getText().toString());
                fragArrList.get(i).loadURL(urlGo);
            }
        });

        //respond to implicit intents
        String myURL;
        Intent intent = getIntent();
        if (intent.getData() != null) {
            myURL = intent.getData().toString();
            Uri data = intent.getData();
            intent = new Intent(Intent.ACTION_VIEW, data);
            intent.getAction();
            if (myURL != null) {
                int tab = fragArrList.size();
                fragArrList.add(WebFragment.newInstance(myURL));
                fragState.notifyDataSetChanged();
                viewPager.setCurrentItem(tab);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tabmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Integer currentTab;

        switch (item.getItemId()) {
            case R.id.add_tab: {
                fragArrList.add(WebFragment.newInstance(""));
                fragState.notifyDataSetChanged();
                currentTab = fragArrList.size() - 1;
                viewPager.setCurrentItem(currentTab);

                return true;
            }
            case R.id.prev_tab: {
                currentTab = viewPager.getCurrentItem();
                if (currentTab > 0) {
                    currentTab--;
                    fragState.notifyDataSetChanged();
                    viewPager.setCurrentItem(currentTab);
                    return true;
                }
            }
            case R.id.next_tab: {
                currentTab = viewPager.getCurrentItem();
                if (currentTab < fragArrList.size() - 1) {
                    currentTab++;
                    fragState.notifyDataSetChanged();
                    viewPager.setCurrentItem(currentTab);
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getURL(String URL, WebView view) {
        if (fragArrList.get(viewPager.getCurrentItem()).webView == view)
            urlBar.setText(URL);
    }

    /*
    public class PagerAdapter extends FragmentStatePagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragArrList.get(i);
        }

        @Override
        public int getCount() {
            return fragArrList.size();
        }
    }
    */
}