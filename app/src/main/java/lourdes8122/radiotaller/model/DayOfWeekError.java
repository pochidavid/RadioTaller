package lourdes8122.radiotaller.model;

public class DayOfWeekError extends IllegalArgumentException{
    public DayOfWeekError(){ super("The day of week number is invalid");}
}
