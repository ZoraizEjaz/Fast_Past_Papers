package zoraiz.fast_past_papers.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zoraiz on 8/16/2017.
 */

public class Course {

    @SerializedName("status")
    private Integer status;
    @SerializedName("result")
    private List<CoursesResponse> courseResponse = null;

    public Course() {
    }

    public Course(Integer status, List<CoursesResponse> coursesResponse) {
        super();
        this.status = status;
        this.courseResponse = coursesResponse;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CoursesResponse> getCourseResponse() {
        return courseResponse;
    }

    public void setCourseResponse(List<CoursesResponse> courseResponse) {
        this.courseResponse = courseResponse;
    }
}
