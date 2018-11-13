package com.example.user.organizer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//-------------------------Утилита для работы с файлом---------------------------------------
public class FileUtility {
    //имя файла
    private Context context;
    private final static String DIR_SD = "Organizer";

    public FileUtility(Context context) {
        this.context = context;
    }//FileUtility

    //получить путь к рабочему каталогу папке
    public String getPathDir() {
        String pathDir = null;
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast toast = Toast.makeText(context, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            pathDir = null;
        }

        // получаем путь к SD и добавляем свой каталог к пути
        File sdPath = new File(context.getExternalFilesDir(DIR_SD).getPath());

        // если папка существует
        if (sdPath.exists()) pathDir = sdPath.getPath();

        return pathDir;
    }//getContentsFileInDir

    //создать рабочий каталог
    public void makeDir() {

        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast toast = Toast.makeText(context, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return;
        }

        // получаем путь к SD и добавляем свой каталог к пути
        File sdPath = new File(context.getExternalFilesDir(DIR_SD).getPath());

        // создаем каталог, если его нету
        if (!sdPath.exists()) sdPath.mkdirs();

    } // addNewFile

    // является ли внешнее хранилище только для чтения
    private static boolean isReadOnly() {
        String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState);
    }

    // проверяем есть ли доступ к внешнему хранилищу
    private static boolean isAvailable() {
        String storageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(storageState);
    }

//    //добавляем файл в папку
//    public void addNewFile(String fileName, double[] masNumber) {
//
//        // формируем объект File, который содержит путь к файлу (в т.ч. имя файла)
//        File sdFile = null;
//
//        if (!isAvailable() || isReadOnly()) {
//            // если доступа нет
//            Toast toast = Toast.makeText(context, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
//        } else {
//            // если доступ есть, то создаем файл в ExternalStorage
//            sdFile = new File(context.getExternalFilesDir(DIR_SD), fileName);
//
//            //формируем данные для записи и записываем файл
//            try (DataOutputStream dos
//                         = new DataOutputStream(new FileOutputStream(sdFile))) {
//                for (int i = 0; i < 20; i++) {
//                    dos.writeDouble(masNumber[i]);
//                }//for
////                Log.d("myLog", "Файл сохранен " + fileName);
//            } catch (Exception ex) {
//                Log.d("myLog", "Ошибка при записи файла" + ex.getMessage());
//            } // try-catch
//        }//if (!isAvailable() || isReadOnly())
//    } // addNewFile
//
//    //чтение файла из папку
//    public double[] readNewFile(Context context, String fileName) {
//
//        double[] masNumber = new double[20];
//
//        // формируем объект File, который содержит путь к файлу (в т.ч. имя файла)
//        File sdFile = null;
//
//        if (!isAvailable() || isReadOnly()) {
//            // если доступа нет
//            Toast toast = Toast.makeText(context, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
//        } else {
//            // если доступ есть, то создаем файл в ExternalStorage
//            sdFile = new File(context.getExternalFilesDir(DIR_SD), fileName);
//
//            //формируем данные для записи и записываем файл
//            try (DataInputStream dis
//                         = new DataInputStream(new FileInputStream(sdFile))) {
//                for (int i = 0; i < 20; i++) {
//                    masNumber[i] = dis.readDouble();
//                }//for
////                Log.d("myLog", "Файл сохранен " + fileName);
//            } catch (Exception ex) {
//                Log.d("myLog", "Ошибка при записи файла" + ex.getMessage());
//            } // try-catch
//        }//if (!isAvailable() || isReadOnly())
//
//        return masNumber;
//    } // readNewFile
}//FileUtility
