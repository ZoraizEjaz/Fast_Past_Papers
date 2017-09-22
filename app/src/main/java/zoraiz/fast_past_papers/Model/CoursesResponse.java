package zoraiz.fast_past_papers.Model;

/**
 * Created by Grabbit Media on 8/3/2017.
 */

public class CoursesResponse {

    String departmentId, course_name, courseId;

    public CoursesResponse(String courseId, String course_name, String departmentId ) {
        this.courseId = courseId;
        this.course_name = course_name;
        this.departmentId = departmentId;
    }


    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}