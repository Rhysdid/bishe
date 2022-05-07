package com.example.demo.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class ResState {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "native")
    private String msg;
    private boolean state;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

}
