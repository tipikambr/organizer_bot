package organizer.bot.utils.menus.task;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import organizer.bot.utils.menus.Calendar;

import java.util.Date;
import java.util.List;

public class TaskCalendarMenu extends Calendar {
    public static InlineKeyboardMarkup initForAlarm(Date date, int lang){
        List<List<InlineKeyboardButton>> btns_menu = getCalendar(date, lang);
        for(var btns: btns_menu)
            for (var btn:btns)
                btn.setCallbackData(btn.getCallbackData() + "task");
        InlineKeyboardMarkup res = new InlineKeyboardMarkup();
        res.setKeyboard(btns_menu);
        return res;
    }
}
