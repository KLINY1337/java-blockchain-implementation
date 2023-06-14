package com.example.javablockchainimplementation.util.blockchain;

import java.io.Serializable;

/*
  Класс транзакции сети
  Транзакция содержит адреса отправителя, получателя,
  Отправляемую сумму, а также хэш времени совершения транзакции

  Версия: 1.0
  Автор: Черномуров Семён
  Последнее изменение: 14.06.2023
*/
public record Transaction(String sender, //Отправитель
                          String recipient, //Получатель
                          int amount, //Отправляемая сумма
                          String timeStampHash //Хэш времени транзакции
                                                ) implements Serializable {

}
