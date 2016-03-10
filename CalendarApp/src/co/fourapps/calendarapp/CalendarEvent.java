package co.fourapps.calendarapp;


public class CalendarEvent {

    private String eventName,organizerName,startDate,endDate,access,calendarname,calendaraccess,rrule,desc,allday,attdata;

    public CalendarEvent(String eventName, String organizerName, String startDate, String endDate, String access, String calendarname,String calendaraccess,String rrule,String desc, String allday, String attdata) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.access = access;
        this.calendarname = calendarname;
        this.calendaraccess = calendaraccess;
        this.rrule=rrule;
        this.desc=desc;
        this.allday=allday;
        this.attdata=attdata;
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
    
    public String getCalendarName() {
        return calendarname;
    }

    public void setCalendarName(String calendarname) {
        this.calendarname = calendarname;
    }
    
    public String getCalendarAccess() {
        return calendaraccess;
    }

    public void setCalendarAccess(String calendaraccess) {
        this.calendaraccess = calendaraccess;
    }
    
    
    public String getRrule() {
        return rrule;
    }
    
    public void setRrule(String rrule) {
        this.rrule = rrule;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
    public String getAllday() {
        return allday;
    }
    
    public void setAllday(String allday) {
        this.allday = allday;
    }
    
    public String getAttdata() {
        return attdata;
    }
    
    public void setAttdata(String attdata) {
        this.attdata = attdata;
    }
    
    
    
    
    
}
