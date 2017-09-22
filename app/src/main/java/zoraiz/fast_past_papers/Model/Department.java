package zoraiz.fast_past_papers.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zoraiz on 8/16/2017.
 */

public class Department {

    @SerializedName("status")
    private Integer status;
    @SerializedName("result")
    private List<DepartmentResponse> departmentResponse = null;

    public Department() {
    }

    public Department(Integer status, List<DepartmentResponse> departmentResponse) {
        super();
        this.status = status;
        this.departmentResponse = departmentResponse;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DepartmentResponse> getDepartmentResponse() {
        return departmentResponse;
    }

    public void setDepartmentResponse(List<DepartmentResponse> departmentResponse) {
        this.departmentResponse = departmentResponse;
    }

}