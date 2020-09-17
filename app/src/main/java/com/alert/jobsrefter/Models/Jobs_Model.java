package com.alert.jobsrefter.Models;

public class Jobs_Model {
    String jobid,image,qualification,department,profession,type;
    String date;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    String link;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    String Tag;

    public Jobs_Model() {
    }

    public Jobs_Model(String jobid, String image, String qualification, String department, String profession, String type, String date, String link, String tag) {
        this.jobid = jobid;
        this.image = image;
        this.qualification = qualification;
        this.department = department;
        this.profession = profession;
        this.type = type;
        this.date = date;
        this.link = link;
        Tag = tag;
    }


    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
