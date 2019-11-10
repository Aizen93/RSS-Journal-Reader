package com.example.oussama_achraf.mplrss.XMLParsingUtils;

public class ItemXml {

    private String title = null;
    private String description = null;
    private String link = null;



    public ItemXml(String title, String description, String link){
        this.title = title;
        this.description = description;
        this.link = link;

    }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

}
