package co.fourapps.calendarapp;


public class CalendarEvent {

    private String eventName,organizerName,startDate,endDate,access;

    public CalendarEvent(String eventName, String organizerName, String startDate, String endDate, String access) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.access = access;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    
    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
    
    
    
}
