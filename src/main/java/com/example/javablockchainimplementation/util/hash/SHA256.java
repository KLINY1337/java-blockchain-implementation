package com.example.javablockchainimplementation.util.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
  Класс создания хэша на основе строки данных
  Используемый алгоритм хэширования SHA256

  Версия: 1.0
  Автор: Черномуров Семён
  Последнее изменение: 14.06.2023
*/
public class SHA256 {
    //Метод генерации хэша (SHA256) для данных
    public static String generateHash(String data) {

        assert data != null;

        String hash = null;
        try {
            //Получить генератор хэша
            final MessageDigest md = MessageDigest.getInstance("SHA-256");

            //Захэшировать входные да
            final byte[] bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));

            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return hash;
    }
}
