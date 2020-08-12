package com.xiaoshu.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

public class Content implements Serializable {
    @Id
    @Column(name = "contentId")
    private Integer contentid;

    @Column(name = "contentCategoryId")
    private Integer contentcategoryid;

    private String contenttile;

    private String contenturl;

    private String picpath;

    private Integer price;

    private String status;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    private static final long serialVersionUID = 1L;
    
    

    public Content() {
		super();
	}

	public Content(Integer contentid, Integer contentcategoryid, String contenttile, String contenturl, String picpath,
			Integer price, String status, Date createtime) {
		super();
		this.contentid = contentid;
		this.contentcategoryid = contentcategoryid;
		this.contenttile = contenttile;
		this.contenturl = contenturl;
		this.picpath = picpath;
		this.price = price;
		this.status = status;
		this.createtime = createtime;
	}

	/**
     * @return contentId
     */
    public Integer getContentid() {
        return contentid;
    }

    /**
     * @param contentid
     */
    public void setContentid(Integer contentid) {
        this.contentid = contentid;
    }

    /**
     * @return contentCategoryId
     */
    public Integer getContentcategoryid() {
        return contentcategoryid;
    }

    /**
     * @param contentcategoryid
     */
    public void setContentcategoryid(Integer contentcategoryid) {
        this.contentcategoryid = contentcategoryid;
    }

    /**
     * @return contenttile
     */
    public String getContenttile() {
        return contenttile;
    }

    /**
     * @param contenttile
     */
    public void setContenttile(String contenttile) {
        this.contenttile = contenttile == null ? null : contenttile.trim();
    }

    /**
     * @return contenturl
     */
    public String getContenturl() {
        return contenturl;
    }

    /**
     * @param contenturl
     */
    public void setContenturl(String contenturl) {
        this.contenturl = contenturl == null ? null : contenturl.trim();
    }

    /**
     * @return picpath
     */
    public String getPicpath() {
        return picpath;
    }

    /**
     * @param picpath
     */
    public void setPicpath(String picpath) {
        this.picpath = picpath == null ? null : picpath.trim();
    }

    /**
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", contentid=").append(contentid);
        sb.append(", contentcategoryid=").append(contentcategoryid);
        sb.append(", contenttile=").append(contenttile);
        sb.append(", contenturl=").append(contenturl);
        sb.append(", picpath=").append(picpath);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", createtime=").append(createtime);
        sb.append("]");
        return sb.toString();
    }
}