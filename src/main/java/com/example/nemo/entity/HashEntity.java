package com.example.nemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "hash")
public class HashEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "url")
    private String url;
    /*
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /*
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user=user;
    }
    */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashEntity hash = (HashEntity) o;

        if (id != null ? !id.equals(hash.id) : hash.id != null) return false;
        if (url != null ? !url.equals(hash.url) : hash.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }


}
