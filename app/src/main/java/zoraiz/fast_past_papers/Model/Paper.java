package zoraiz.fast_past_papers.Model;

/**
 * Created by Zoraiz on 8/5/2017.
 */

public class Paper {

    String  paper_name, semester, year, document_type, link1, link2;

    public Paper(String paper_name, String semester, String year
            ,String document_type, String link1, String link2) {

        this.paper_name = paper_name;
        this.semester = semester;
        this.year = year;
        this.document_type = document_type;
        this.link1 = link1;
        this.link2 = link2;

    }


    public String getPaperName() {
        return paper_name;
    }

    public void setPaperName (String paper_name) {
        this.paper_name = paper_name;
    }


    public String getYear() {
        return year;
    }

    public void setYear (String year ) {
        this.year = year;
    }

    public String getSemester() {
        return year;
    }

    public void setSemester (String semester ) {
        this.semester = semester;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocumentType (String type ) {
        this.document_type = type;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1 (String link ) {
        this.link1 = link;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2 (String link ) {
        this.link2 = link;
    }

}