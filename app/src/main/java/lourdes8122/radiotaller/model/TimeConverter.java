package lourdes8122.radiotaller.model;

import java.sql.Date;

import androidx.room.TypeConverter;

public class TimeConverter {

   @TypeConverter
   public static Date toTime(Long timeLong){
       return timeLong == null ? null: new Date(timeLong);
   }

   @TypeConverter
   public static Long fromDate(Date time){
       return time == null ? null : time.getTime();
   }
}
