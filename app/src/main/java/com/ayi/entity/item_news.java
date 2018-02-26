package com.ayi.entity;

/**
 * Created by Administrator on 2016/8/29.
 */
public class item_news {
    private String tpdz;
    private String link;
    private String title;

    public String getTpdz() {
        return tpdz == null ? "" : tpdz;
    }

    public void setTpdz(String tpdz) {
        this.tpdz = tpdz == null ? "" : tpdz;
    }

    public String getLink() {
        return link == null ? "" : link;
    }

    public void setLink(String link) {
        this.link = link == null ? "" : link;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    @Override
    public String toString() {
        return "item_news{" +
                "tpdz='" + tpdz + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
