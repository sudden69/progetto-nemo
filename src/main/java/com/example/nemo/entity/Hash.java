package com.example.nemo.entity;

import javax.persistence.*;

@Entity
//@Table(name = "hash") //Pare intellij abbia deciso di far passare Table e column name come errori
public class Hash {
    @Id
  //  @Column(name = "id")   commento da togliere da te in locale
    private String id;
    @Basic
    //@Column(name = "url")  vedi sopra
    private String url;
    @Basic
    private String user;

    public String getId() {
        return id;
    }
    public void setUser(String user)
    {this.user=user;
    }
    public String getUser()
    {return user;}

    public void setId(String id) {
        this.id = id;
    }


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

        Hash hash = (Hash) o;

        if (id != null ? !id.equals(hash.id) : hash.id != null) return false;
        if (url != null ? !url.equals(hash.url) : hash.url != null) return false;

        return true;
    }
    public void makeId()
    {StringBuilder Sb=new StringBuilder(30);
     Sb.append("https://nemoswimmer.it/"); //https si ci voglio credere :P
     Sb.append(this.url.hashCode());
     setId(Sb.toString());
    }
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
    public int getSize(String url)  //tinyurl lo fa, noi non possiamo essere da meno
    {return url.length();
    }

}
