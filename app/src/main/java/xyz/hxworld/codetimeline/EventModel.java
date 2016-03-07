package xyz.hxworld.codetimeline;

/**
 * Created by Kshitij Nagvekar on 3/7/2016.
 */
public class EventModel {
    private String title;
    private String description;
    private String url;
    private String startTime;
    private String endTime;

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
