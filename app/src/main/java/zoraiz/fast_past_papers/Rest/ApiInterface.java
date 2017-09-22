package zoraiz.fast_past_papers.Rest;

/**
 * Created by Zoraiz on 8/16/2017.
 */

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import zoraiz.fast_past_papers.Model.Course;
import zoraiz.fast_past_papers.Model.Department;

public interface ApiInterface {

    @GET("api/department")
    Call<Department> getDepartment();

    @GET("api/course")
    Call<Course> getCourse( @Query("id") int id);

}