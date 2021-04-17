package organizer.bot.utils.menus.task;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import organizer.bot.utils.menus.Clock;

import java.util.List;

public class TaskClockMenu extends Clock {
    public static InlineKeyboardMarkup initHourForAlarm(Integer year, Integer month, Integer day){
        List<List<InlineKeyboardButton>> btns_menu = getHourMenu();
        for(var btns: btns_menu)
            for (var btn:btns)
                btn.setCallbackData( month + "/" + year + "/" + day  + "/" + btn.getCallbackData() + "task");
        InlineKeyboardMarkup res = new InlineKeyboardMarkup();
        res.setKeyboard(btns_menu);
        return res;
    }

    public static InlineKeyboardMarkup initMinuteForAlarm(Integer year, Integer month, Integer day, Integer hour ){
        List<List<InlineKeyboardButton>> btns_menu = getMinuteMenu();
        for(var btns: btns_menu)
            for (var btn:btns)
                btn.setCallbackData(month + "/" + year + "/" + day + "/" + hour + "/" + btn.getCallbackData() + "task");
        InlineKeyboardMarkup res = new InlineKeyboardMarkup();
        res.setKeyboard(btns_menu);
        return res;
    }
}
