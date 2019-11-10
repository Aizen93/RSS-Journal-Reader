package com.example.oussama_achraf.mplrss.XMLParsingUtils;

public class RssElement {

    private int id;
    private String link;
    private String description;
    private String date;
    private String titre;

    public RssElement(int id, String link, String description, String date, String titre) {
        this.id = id;
        this.link = link;
        this.description = description;
        this.date = date;
        this.titre = titre;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTitre() {
        return titre;
    }
}
