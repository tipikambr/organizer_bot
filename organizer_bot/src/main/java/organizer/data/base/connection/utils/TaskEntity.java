package organizer.data.base.connection.utils;

import java.sql.Timestamp;

public class TaskEntity {
    private long chatID;
    private String name;
    private Timestamp time;
    private Integer period;
    private Long task_id;

    public TaskEntity(String chat, String name, String time, String period, String task_id){
        this.chatID = Long.parseLong(chat);
        this.name = name;
        this.time = Timestamp.valueOf(time);
        this.period = Integer.parseInt(period);
        this.task_id = Long.parseLong(task_id);
    }
    public TaskEntity(Long chat, String name, Timestamp time, Integer period, Long task_id){
        this.chatID = chat;
        this.name = name;
        this.time = time;
        this.period = period;
        this.task_id = task_id;
    }

    public long getChatID() {
        return chatID;
    }

    public String getName() {
        return name;
    }

    public Timestamp getTime() {
        return time;
    }

    public Integer getPeriod() {
        return period;
    }

    public Long getTask_id() {
        return task_id;
    }
}
