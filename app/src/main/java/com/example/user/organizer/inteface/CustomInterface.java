package com.example.user.organizer.inteface;

import android.view.View;

//------------------------Интерфейс для передачи данных из фрагмент в активность-------------------
public interface CustomInterface {
    //закрыть приложение
    void closeApp();
    //при потверждении принятия участия в событии вносим новые данные про участника события
    void insertIntoParticipants(int event_id, int user_id);
    //при отказе от участия в событии удаляется запись из таблицы participants
    void leaveFromEvent(int event_id, int user_id);
    //при удалении события удаляется запись из таблицы events
    void deleteEvent(int event_id);

}
