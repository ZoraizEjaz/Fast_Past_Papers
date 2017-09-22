package zoraiz.fast_past_papers.Activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import zoraiz.fast_past_papers.Adapter.CourseAdapter;
import zoraiz.fast_past_papers.Model.Course;
import zoraiz.fast_past_papers.Model.CoursesResponse;
import zoraiz.fast_past_papers.Model.Department;
import zoraiz.fast_past_papers.Model.DepartmentResponse;
import zoraiz.fast_past_papers.R;
import zoraiz.fast_past_papers.Rest.ApiClient;
import zoraiz.fast_past_papers.Rest.ApiInterface;
import zoraiz.fast_past_papers.Utils.InternetConnection;

import static java.security.AccessController.getContext;
import static zoraiz.fast_past_papers.Rest.ApiClient.BASE_URL;

public class CoursesName extends AppCompatActivity {

    public static ArrayList<CoursesResponse> temp_search_list=new ArrayList<CoursesResponse>();

    JsonObjectRequest jsonRequest;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    private String departmentId;
    private String departmentName;
    private static Context mContext;

    String position="";
    String activity_name="";
    String mSearchString="";

    int move_back=0;

    static InputStream is = null;
    static String result="";
    JSONArray json_array;
    JSONObject json_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_name);

        mContext =this;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Courses");

        //get department name and department id for consuming api
        departmentId = getIntent().getStringExtra("departmentId");
        departmentName = getIntent().getStringExtra("departmentName");


        mRecyclerView = (RecyclerView) findViewById(R.id.courses_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MainActivity.courses_list.clear();
        temp_search_list.clear();

        //populate data in cardview


        if(new InternetConnection().InternetConnection(getApplicationContext()))
        {
            getCoursesFromServer(departmentId);
        }
        else
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
        }




    }


    private void getCoursesFromServer(String departmentId)
    {
        final ProgressDialog loading;
        loading =  ProgressDialog.show(CoursesName.this,getResources().getString(R.string.gettingData),
                getResources().getString(R.string.pleaseWait),true,true);

        String url= BASE_URL+"/api/getDepartmentCourses/"+departmentId;


         jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jObject)
                    {
                        String status="";

                        try
                        {
                            status=jObject.getString("status");
                            if(status.equals("1"))
                            {
                                JSONArray jsonArray=jObject.getJSONArray("result");
                                for(int i=0; i<jsonArray.length();i++)
                                {
                                    json_obj = jsonArray.getJSONObject(i);
                                    String courseId=json_obj.getString("id");
                                    String courseName=json_obj.getString("name");
                                    String depId=json_obj.getString("dept_id");

                                    MainActivity.courses_list.add(
                                            new CoursesResponse(courseId, courseName, depId));

                                    temp_search_list.add(
                                            new CoursesResponse(courseId, courseName, depId));

                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Unable To Get Courses", Toast.LENGTH_LONG).show();
                            }

                        } catch(JSONException e) { Log.e("CourseNameParsing",e.toString());}


                        if(status.equals("1"))
                        {
                            //populate cardview
                            mAdapter = new CourseAdapter(mContext,MainActivity.courses_list);
                            mRecyclerView.setAdapter(mAdapter);
                        }


                        loading.dismiss();

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loading.dismiss();
                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(getApplicationContext(), "Oops. Timeout error!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_courses_name, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));


        if (mSearchView != null )
        {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconifiedByDefault(false);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
            {
                public boolean onQueryTextChange(String newText)
                {
                    mSearchString = newText;

                    return true;
                }

                public boolean onQueryTextSubmit(String query)
                {
                    move_back++;
                    mSearchString = query.toLowerCase();

                    int size = MainActivity.courses_list.size();
                    for(int i=0 ; i<size ; i++)
                    {
                        String x = MainActivity.courses_list.get(i).getCourse_name().toString().toLowerCase();
                        if(x.contains(mSearchString))
                        {

                        }
                        else
                        {
                            MainActivity.courses_list.remove(i);
                            i--;
                            size--;
                        }
                    }


                    mAdapter.notifyDataSetChanged();


                    return true;
                }
            };

            mSearchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
            {
                move_back++;
                if(move_back==2)
                {
                    MainActivity.courses_list.clear();
                    for(int i=0 ; i< temp_search_list.size(); i++)
                    {
                        MainActivity.courses_list.add(new CoursesResponse(temp_search_list.get(i).getDepartmentId(),
                                temp_search_list.get(i).getCourse_name(),
                                temp_search_list.get(i).getCourseId()));
                    }
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    finish();
                }


            }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {

        Volley.newRequestQueue(this).cancelAll(jsonRequest);
        temp_search_list.clear();
        MainActivity.courses_list.clear();

        super.onDestroy();
    }

}
