package org.example.aidemo.application.entity;

/**
 * AI结果
 */
public class AiDemoResult {
    private String match;
    private String match_confidence;
    private String Song_Name;
    private String Artist_Name;
    private String Album_Name;
    private String Genre;
    private String Price;
    private String CopyRight;
    private String Time;
    private String Released;


    public String getMatch() {
        return "1".equals(match) ? "是" : "否";
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getMatch_confidence() {
        return match_confidence;
    }

    public void setMatch_confidence(String match_confidence) {
        this.match_confidence = match_confidence;
    }

    public String getSong_Name() {
        return Song_Name;
    }

    public void setSong_Name(String song_Name) {
        Song_Name = song_Name;
    }

    public String getArtist_Name() {
        return Artist_Name;
    }

    public void setArtist_Name(String artist_Name) {
        Artist_Name = artist_Name;
    }

    public String getAlbum_Name() {
        return Album_Name;
    }

    public void setAlbum_Name(String album_Name) {
        Album_Name = album_Name;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCopyRight() {
        return CopyRight;
    }

    public void setCopyRight(String copyRight) {
        CopyRight = copyRight;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }
}
