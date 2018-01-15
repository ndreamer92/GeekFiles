package Client;

public interface NCEventListener {
    void onFileListRefresh ();//Событие по обновлению списка файлов
    void onSuccesfullAuthorization();//Событие по успешной авторизации
    void onSuccesfullConnection();//Событие по успешному подключению
}
