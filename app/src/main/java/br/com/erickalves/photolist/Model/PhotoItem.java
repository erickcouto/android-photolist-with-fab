package br.com.erickalves.photolist.Model;

import java.io.Serializable;
import java.util.Date;

public class PhotoItem implements Serializable {

    private String imageSource;
    private String name;
    private Date created;


    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String source) {
        this.imageSource = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
