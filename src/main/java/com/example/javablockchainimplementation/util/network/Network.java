package com.example.javablockchainimplementation.util.network;

import com.example.javablockchainimplementation.util.blockchain.Transaction;

import java.util.Set;

/*
  Интерфейс Network
  Интерфейса содержит основной набор методов для
  работы с абстрактной сетью

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public interface Network {

    //Метод добавления узла в сеть
    void addNode(NetworkNode networkNode);

    //Метод добавления транзакции в сеть
    void addTransaction(Transaction transaction);

    //Метод получения узлов сети
    Set<NetworkNode> getNetworkNodes();

    //Метод получения неподтвержденных транзакций в сети
    Set<Transaction> getUntrustedTransactions();

    //Метод получения всех пользователей сети
    Set<NetworkUser> getNetworksUsers();

    //Метод добавления пользователя в сеть
    NetworkUser addUser(String login, String password);

    //Метод поиска пользователя по кошельку
    NetworkUser findUserByWallet(String wallet);

    //Метод обновления узлов сети
    void updateNodes();
}
