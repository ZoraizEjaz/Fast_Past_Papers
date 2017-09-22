package zoraiz.fast_past_papers.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import zoraiz.fast_past_papers.Adapter.DepartmentAdapter;
import zoraiz.fast_past_papers.Model.CoursesResponse;
import zoraiz.fast_past_papers.Model.HomeDepartmentAlbum;
import zoraiz.fast_past_papers.Model.DepartmentResponse;
import zoraiz.fast_past_papers.Model.Department;
import zoraiz.fast_past_papers.Model.Paper;
import zoraiz.fast_past_papers.R;
import zoraiz.fast_past_papers.Rest.ApiClient;
import zoraiz.fast_past_papers.Rest.ApiInterface;
import zoraiz.fast_past_papers.Utils.InternetConnection;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private List<HomeDepartmentAlbum> albumList;

    private AdView mAdView;

    public static ArrayList<CoursesResponse> courses_list = new ArrayList<CoursesResponse>();
    public static ArrayList<DepartmentResponse> department_list = new ArrayList<DepartmentResponse>();
    public static ArrayList<Paper> paper_list = new ArrayList<Paper>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        try{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

        }catch (Exception ex){

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new DepartmentAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MainActivity.GridSpacingItemDecoration(2, dpToPx(0), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();



        if(new InternetConnection().InternetConnection(getApplicationContext()))
        {
            getDepartmentFromServer();
        }
        else
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
        }

    }


    //name of the department should be the same on server they are mention in prepare album function
    void getDepartmentFromServer() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<Department> call = service.getDepartment();

        call.enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Response<Department> response, Retrofit retrofit) {

                try {

                    if(response.body().getStatus()==1)
                    {
                        for(int i=0; i < response.body().getDepartmentResponse().size(); i++)
                        {
                            int courseId = response.body().getDepartmentResponse().get(i).getId();
                            String coursename = response.body().getDepartmentResponse().get(i).getName();
                            department_list.add(new DepartmentResponse(courseId,coursename,"",""));

                            if(coursename.equals("AAF"))
                            {
                                albumList.get(0).setDepartmentId(response.body().getDepartmentResponse().get(i).getId());
                            }
                            else if(coursename.equals("BBA"))
                            {
                                albumList.get(1).setDepartmentId(response.body().getDepartmentResponse().get(i).getId());
                            }
                            else if(coursename.equals("CS"))
                            {
                                albumList.get(2).setDepartmentId(response.body().getDepartmentResponse().get(i).getId());
                            }
                            else if(coursename.equals("CV"))
                            {
                                albumList.get(3).setDepartmentId(response.body().getDepartmentResponse().get(i).getId());
                            }
                            else if(coursename.equals("EE"))
                            {
                                albumList.get(4).setDepartmentId(response.body().getDepartmentResponse().get(i).getId());
                            }
                        }


                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.aaf1,
                R.drawable.bba1,
                R.drawable.cs1,
                R.drawable.cv,
                R.drawable.ee};

        HomeDepartmentAlbum a = new HomeDepartmentAlbum("AAF", 1, covers[0]);
        albumList.add(a);

        a = new HomeDepartmentAlbum("BBA", 2, covers[1]);
        albumList.add(a);

        a = new HomeDepartmentAlbum("CS", 3, covers[2]);
        albumList.add(a);

        a = new HomeDepartmentAlbum("CV", 4, covers[3]);
        albumList.add(a);

        a = new HomeDepartmentAlbum("EE", 5, covers[4]);
        albumList.add(a);


        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 5) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    //ga0RGNYHvNM5d0SLGQfpQWAPGJ8=

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.downloads)
        {
            Toast.makeText(getApplicationContext(),"Please Download Any Office App To Open Any Document",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, FileManager.class);
            startActivity(intent);

        }

        else if (id == R.id.addPaper)
        {
            Intent intent = new Intent(MainActivity.this, AddPaperWebview.class);
            startActivity(intent);
        }

        else if (id == R.id.rate)
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + "com.fast.zoraiz.papers"));
            startActivity(i);
        }
        else if (id == R.id.suggestions)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"zoriaz93@gmail.com"});
            startActivity(Intent.createChooser(intent, ""));
        }
        else if (id == R.id.nav_share)
        {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.fast.zoraiz.papers&hl=en");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));



        }
        else if (id == R.id.logout)
        {
            finish();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        courses_list.clear();
        department_list.clear();
        paper_list.clear();

        super.onDestroy();
    }
}
