package zoraiz.fast_past_papers.Activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import zoraiz.fast_past_papers.Adapter.CourseAdapter;
import zoraiz.fast_past_papers.Adapter.PaperAdapter;
import zoraiz.fast_past_papers.Model.CoursesResponse;
import zoraiz.fast_past_papers.Model.Paper;
import zoraiz.fast_past_papers.R;
import zoraiz.fast_past_papers.Utils.InternetConnection;

import static zoraiz.fast_past_papers.Rest.ApiClient.BASE_URL;

public class Papers extends AppCompatActivity {

    public static ArrayList<Paper> temp_search_list=new ArrayList<Paper>();

    private static Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    String position="";
    String activity_name="";
    String mSearchString="";

    int move_back=0;

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = Paper.class.getSimpleName();
    private String selectedFilePath;
    private String SERVER_URL = "http://coderefer.com/extras/UploadToServer.php";
    ImageView ivAttachment;
    Button bUpload;
    TextView tvFileName;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers);

        mContext=this;


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Papers");


        mRecyclerView = (RecyclerView) findViewById(R.id.papers_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MainActivity.paper_list.clear();
        temp_search_list.clear();
        //populate data in cardview

        String courseID = getIntent().getStringExtra("courseId");

        if(new InternetConnection().InternetConnection(getApplicationContext()))
        {
            getPapersFromServer(courseID);
        }
        else
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
        }

    }


    private void getPapersFromServer(String courseId)
    {
        final ProgressDialog loading;
        loading =  ProgressDialog.show(Papers.this,getResources().getString(R.string.gettingData),
                getResources().getString(R.string.pleaseWait),true,true);

        String url= BASE_URL+"/api/getCoursePapers/"+ courseId;


        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jObject)
                    {
                        String status="";

                        try
                        {
                            JSONObject json_obj;
                            status=jObject.getString("status");
                            if(status.equals("1"))
                            {
                                JSONArray jsonArray=jObject.getJSONArray("result");

                                for(int i=0; i<jsonArray.length();i++)
                                {
                                    json_obj = jsonArray.getJSONObject(i);
                                    String paperId=json_obj.getString("id");
                                    String paperLink=json_obj.getString("link");
                                    String year=json_obj.getString("year");
                                    String doc_type=json_obj.getString("type");
                                    String paper_name=json_obj.getString("paper_name");
                                    String semester=json_obj.getString("semester");
                                    String deptId=json_obj.getString("dept_id");
                                    String courseId=json_obj.getString("course_id");
                                    String courseName=json_obj.getString("course_name");


                                    paperLink = BASE_URL+"papers/"+paperLink;
                                    paper_name=paper_name+"_"+courseName+"_"+semester;

                                    MainActivity.paper_list.add(new Paper(paper_name,semester,year,doc_type,paperLink,""));

                                    temp_search_list.add(new Paper(paper_name,semester,year,doc_type,paperLink,""));

                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"No Paper Found", Toast.LENGTH_LONG).show();
                            }

                        } catch(JSONException e) { Log.e("CourseNameParsing",e.toString());}


                        if(status.equals("1"))
                        {
                            //populate cardview
                            mAdapter = new PaperAdapter(mContext,MainActivity.paper_list);
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
        getMenuInflater().inflate(R.menu.menu_paper, menu);

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
                    //doFilterAsync(mSearchString);

                    return true;
                }

                public boolean onQueryTextSubmit(String query)
                {
                    move_back++;
                    mSearchString = query.toLowerCase();

                    int size = MainActivity.paper_list.size();
                    for(int i=0 ; i<size ; i++)
                    {
                        String x=MainActivity.paper_list.get(i).getPaperName().toString().toLowerCase();
                        if(x.contains(mSearchString))
                        {

                        }
                        else
                        {
                            MainActivity.paper_list.remove(i);
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
                    MainActivity.paper_list.clear();
                    for(int i=0 ; i< temp_search_list.size(); i++)
                    {
                        MainActivity.paper_list.add(new Paper(temp_search_list.get(i).getPaperName(),
                                temp_search_list.get(i).getSemester(),
                                temp_search_list.get(i).getYear(),
                                temp_search_list.get(i).getDocument_type(),
                                temp_search_list.get(i).getLink1(),
                                ""));
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

        temp_search_list.clear();
        MainActivity.paper_list.clear();

        super.onDestroy();
    }

}
