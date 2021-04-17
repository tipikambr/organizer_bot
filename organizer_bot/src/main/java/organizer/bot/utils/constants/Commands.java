package organizer.bot.utils.constants;

public class Commands {

    public static boolean isCancel(String message){
        return message.equals(cancelCommandRU);
    }

    //ОБЩЕЕ
    public static final String soundCommandRU = "Звук";
    public static final String backCommandRU = "Назад";
    public static final String cancelCommandRU = "Отмена";
    public static final String confirmCommandRU = "Подтвердить";


    //ПОЧТА
    public static final String soundMailsCommandRU = "Уведомления почт";
    public static final String addMailRU = "Добавить почту";
    public static final String lookMailsRU = "Просмотр моих почт";

    public static final String soundSpecificMailCommandRU = "Уведомления почты";
    public static final String addMailFilterCommandRU = "Добавить фильтр";
    public static final String lookMailFilterCommandRU = "Просмотр активных рассылок";


    //СПИСКИ
    public static final String soundListsCommandRU = "Уведомления списков";
    public static final String addListCommandRU = "Добавить список";
    public static final String lookAllListsCommandRU = "Просмотр списков";

    public static final String soundSpecificListCommandRU = "Уведомления списка";

    public static final String addItemRU = "Добавить элемент";
    public static final String shareListRU = "Поделиться списком";
    public static final String lookSharePersonsRU = "Посмотреть, кому доступен этот список";
    public static final String removeListRU = "Удалить список";

    public static final String addFilterRU = "Добавить фильтр";
    public static final String removeMailRU = "Удалить почту";


    //ТАСКИ
    public static final String addTasksRU = "Добавить задачу";
    public static final String lookTasksRU = "Посмотреть задачи";

    public static final String skipRU = "Пропустить";


    //Будильник

    public static final String addAlarmRU = "Добавить будильник";
    public static final String lookAlarmRU = "Просмотр будильников";



    //ЗАДАЧИ


    //ЗВУК
    public static final String soundOnCommandRU = "Включить";
    public static final String soundOffCommandRU = "Выключить";
    public static final String soundNotChangeCommandRU = "Вернуться";


    //ЯЗЫКИ
    public static final String russianLanguageCommand = "Русский";


    //ГЛАВНОЕ МЕНЮ
    public static final String infoCommandRU = "Информация";
    public static final String mailSettingCommandRU = "Настройки почт";
    public static final String listSettingCommandRU = "Настройки списков";
    public static final String languageCommandRU = "Язык";
    public static final String alarmSettingCommandRU = "Настройки будильников";
    public static final String taskSettingCommandRU = "Настройки задач";





}
