package com.zq.vo;

public class CustomObject {
    private Long cid;
    private String cname;
    private String email;
    private Integer age;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "CustomObject{" +
                "cid=" + cid +
                ", cname='" + cname + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
