package organizer.data.base.connection.utils;

import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.data.base.connection.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Task {
    private static final String DELETE_TASK = "{? = call data.delete_task(?, ?)}";
    private static final String ADD_TASK = "{? = call data.insert_task(?, ?, ?, ?)}";
    private static final String COMPLETE_TASK = "{? = call data.task_done(?, ?)}";

    private static final String USERS_TASK = "select chat_id, name_task, start_time, minute_period, task_id from data._task where chat_id = ?";
    private static final String ALL_TASK_FOR_NOW = "select chat_id, name_task, start_time, minute_period, task_id  from data._task where completed = false and start_time > ? and start_time < ?";


    public static synchronized ArrayList<TaskEntity> getTasksForNow(){
        try{
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            Connection connection = new Connection();
            PreparedStatement stmt = connection.getConnection().prepareStatement(ALL_TASK_FOR_NOW);
            stmt.setTimestamp(1, new Timestamp(cal.getTime().getTime()));
            cal.add(Calendar.MINUTE, 5);
            stmt.setTimestamp(2, new Timestamp(cal.getTime().getTime()));
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            connection.close();
            ArrayList<TaskEntity> tmp = new ArrayList<>(0);
            while (res.next()) {
                tmp.add(new TaskEntity(res.getLong(1),res.getString(2),res.getTimestamp(3),res.getInt(4), res.getLong(5)));
            }
            return tmp;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized ArrayList<TaskEntity> getUserTasks(long chatId){
        try{
            Connection connection = new Connection();
            PreparedStatement stmt = connection.getConnection().prepareStatement(USERS_TASK);
            stmt.setLong(1, chatId);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<TaskEntity> tmp = new ArrayList<>(0);
            while (res.next()) {
                tmp.add(new TaskEntity(res.getLong(1),res.getString(2),res.getTimestamp(3),res.getInt(4), res.getLong(5)));
            }
            System.out.println(tmp.size());
            return tmp;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized boolean deleteTask(long chat_id, long task_id){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(DELETE_TASK);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, chat_id);
            stmt.setLong(3, task_id);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean completeTask(long chat_id, long task_id){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(COMPLETE_TASK);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, chat_id);
            stmt.setLong(3, task_id);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean addTask(long chatId, String task, Timestamp time, int repite){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(ADD_TASK);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, chatId);
            stmt.setString(3, task);
            stmt.setTimestamp(4, time);
            stmt.setInt(5, repite);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }
}
