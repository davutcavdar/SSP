package co.fourapps.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class EventAdapter extends ArrayAdapter<CalendarEvent> {

    private Context context;
    private CalendarEvent[] values;
    

    public EventAdapter(Context context, int resource, CalendarEvent[] objects) {
        super(context, resource, objects);

        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_child, parent, false);

        UiElements.title = (TextView) rowView.findViewById(R.id.title);
        UiElements.organizer = (TextView) rowView.findViewById(R.id.organizer);
        UiElements.start = (TextView) rowView.findViewById(R.id.startDate);
        UiElements.end = (TextView) rowView.findViewById(R.id.endDate);
        UiElements.eventaccess= (TextView) rowView.findViewById(R.id.eventaccess);
        UiElements.calendarname= (TextView) rowView.findViewById(R.id.calendarname);
        UiElements.calendaraccess= (TextView) rowView.findViewById(R.id.calendaraccess);
      
        

        UiElements.title.setText("Event Title: " + values[position].getEventName());
        UiElements.organizer.setText("Event Owner: "+values[position].getOrganizerName());
        UiElements.start.setText(values[position].getStartDate());
        UiElements.end.setText(values[position].getEndDate());
        UiElements.eventaccess.setText("Event Privacy: "+values[position].getAccess());
        UiElements.calendarname.setText("Calendar: "+values[position].getCalendarName());
        UiElements.calendaraccess.setText("Calendar Privacy: "+values[position].getCalendarAccess());
       
        return rowView;
    }

    static final class UiElements {
  
		public static TextView title,organizer,start,end,eventaccess,calendarname,calendaraccess,volumelevel;
    }
}
