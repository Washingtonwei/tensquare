package com.tensquare.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_problem")
public class Problem implements Serializable {

    @Id
    private String id;//ID
    private String title;
    private String content;
    private Date createtime;
    private Date updatetime;
    private String userid;
    private String nickname;
    private Long visits;
    private Long thumbup;
    private Long reply;
    private String solve;
    private String replyname;
    private Date replytime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public Long getThumbup() {
        return thumbup;
    }

    public void setThumbup(Long thumbup) {
        this.thumbup = thumbup;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public String getSolve() {
        return solve;
    }

    public void setSolve(String solve) {
        this.solve = solve;
    }

    public String getReplyname() {
        return replyname;
    }

    public void setReplyname(String replyname) {
        this.replyname = replyname;
    }

    public Date getReplytime() {
        return replytime;
    }

    public void setReplytime(Date replytime) {
        this.replytime = replytime;
    }
}
