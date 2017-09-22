package zoraiz.fast_past_papers.Model;

/**
 * Created by Grabbit Media on 8/2/2017.
 */

public class HomeDepartmentAlbum {
    private String department_name;
    private int departmentId;
    private int thumbnail;


    public HomeDepartmentAlbum() {
    }

    public HomeDepartmentAlbum(String department_name, int departmentId, int thumbnail) {
        this.department_name = department_name;
        this.departmentId = departmentId;
        this.thumbnail = thumbnail;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setName(String name) {
        this.department_name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int numOfSongs) {
        this.departmentId = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }


}