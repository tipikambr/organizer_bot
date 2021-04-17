package organizer.data.base.connection.utils;

import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.data.base.connection.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Alarm {
    private static final String ADD_ALARM = "{? = call data.insert_alarm(?, ?, ?)}";
    private static final String DELETE_ALARM = "{? = call data.delete_alarm(?)}";
    private static final String GET_USER_ALARM = "select alarm_id, when_alarm, day_alarm from data.alarm where user_id = ?";
    private static final String GET_ALARM_FOR_NOW = "select user_id from data.alarm where (when_alarm > ? and when_alarm < ? and day_alarm = -1) or (when_alarm::time > ? and when_alarm::time < ? and day_alarm = ?) group by user_id";

    private static String dayFromIntToString(int day){
        return switch (day){
            case 0 -> "понедельникам";
            case 1 -> "вторникам";
            case 2 -> "средам";
            case 3 -> "четвергам";
            case 4 -> "пятницам";
            case 5 -> "суббботам";
            case 6 -> "воскресеньям";
            default -> "... да хрен его знает ^_^";
        };
    }

    public static synchronized ArrayList<Long> getAllAlarmForNow(){
        try {
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(GET_ALARM_FOR_NOW);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -5);
            stmt.setTimestamp(1, new Timestamp(cal.getTime().getTime()));
            cal.add(Calendar.MINUTE, 10);
            stmt.setTimestamp(2, new Timestamp(cal.getTime().getTime()));
            stmt.setTime(3, new Time(cal.getTime().getTime()));
            stmt.setTime(4, new Time(cal.getTime().getTime()));
            cal = Calendar.getInstance();
            stmt.setInt(5, (cal.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7);
            stmt.execute();

            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<Long> tmp = new ArrayList<>(0);
            while (res.next()) {
                tmp.add(res.getLong(1));
            }
            System.out.println(tmp.size());
            return tmp;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized ArrayList<String[]> getUserAlarm(long chatId){
        try {
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(GET_USER_ALARM);
            stmt.setLong(1, chatId);
            stmt.execute();

            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<String[]> tmp = new ArrayList<>(0);
            while (res.next()) {
                String[] t = new String[2];
                t[0] = String.valueOf(res.getInt(1));
                String time = String.valueOf(res.getTimestamp(2));
                int day = res.getInt(3);
                if (day == -1)
                    t[1] = "Будильник зазвенит вот в это время:\n" + time;
                else
                    t[1] = "Будильник будет звонить по " + dayFromIntToString(day) + " в " + time.split(" ")[1];
                tmp.add(t);
            }
            return tmp;

        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }


    public static boolean deleteAlarm(int id){
        try {
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(DELETE_ALARM);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setInt(2, id);

            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();
            return res;


        } catch (SQLException throwables) {
            BotLogger.severe("ALARM", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static int addAlarm(long id, Timestamp date, Integer day){
        try {
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(ADD_ALARM);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, id);
            stmt.setTimestamp(3, date);
            stmt.setInt(4, day);

            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();
            if (res)
                return 1;
            return 0;


        } catch (SQLException throwables) {
            BotLogger.severe("ALARM", throwables.getMessage(), throwables);
            return -1;
        }
    }

}
