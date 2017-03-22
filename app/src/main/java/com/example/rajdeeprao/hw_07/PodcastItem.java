/*
    HomeWork 07
    PodcastItem.java
    Yateen Kedare | Rajdeep Rao
 */
package com.example.rajdeeprao.hw_07;

/**
 * Created by rajdeeprao on 3/10/17.
 */

public class PodcastItem {
    String title,description,publicationDate,imageURL,MP3URL,duration;

    public PodcastItem() {
    }

    public PodcastItem(String title, String description, String publicationDate, String imageURL, String MP3URL, String duration) {

        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.imageURL = imageURL;
        this.MP3URL = MP3URL;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "PodcastItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", MP3URL='" + MP3URL + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMP3URL() {
        return MP3URL;
    }

    public void setMP3URL(String MP3URL) {
        this.MP3URL = MP3URL;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
