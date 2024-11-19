package cn.xgs.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="access")
public class SnowAccessLog{

    @Column(name = "tag")
    private String tag;

    @Id
    @Column(name = "time")
    private String time;

    @Column(name = "data")
    private String data;

    public String getTag() {
        return tag;
    }

    public String getTime() {
        return time;
    }

    public String getData() {
        return data;
    }
}
