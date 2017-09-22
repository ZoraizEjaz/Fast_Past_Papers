package zoraiz.fast_past_papers.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zoraiz on 8/16/2017.
 */

public class DepartmentResponse {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;


    public DepartmentResponse() {
    }


    public DepartmentResponse(Integer id, String name, String createdAt, String updatedAt) {
        super();
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}