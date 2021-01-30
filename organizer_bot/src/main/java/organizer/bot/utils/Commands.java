package organizer.bot.utils;

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

    public static final String writeNewNameCommandRU = "Изменить название списка";
    public static final String shareListForUserCommandRU = "Поделиться списком";
    public static final String ListUsersCommandRU = "Просмотр людей, с кем поделился";


    //ЗАДАЧИ


    //ЗВУК
    public static final String soundOnCommandRU = "Включить";
    public static final String soundOffCommandRU = "Выключить";
    public static final String soundNotChangeCommandRU = "Не изменять";


    //ЯЗЫКИ
    public static final String russianLanguageCommand = "Русский";


    //ГЛАВНОЕ МЕНЮ
    public static final String infoCommandRU = "Информация";
    public static final String mailSettingCommandRU = "Настройки почт";
    public static final String listSettingCommandRU = "Настройки списков";
    public static final String languageCommandRU = "Язык";





}
