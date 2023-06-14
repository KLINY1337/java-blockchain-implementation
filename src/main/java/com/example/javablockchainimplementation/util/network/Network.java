package com.example.javablockchainimplementation.util.network;

import com.example.javablockchainimplementation.util.blockchain.Transaction;

import java.util.Set;

/*
  Интерфейс Network
  Интерфейса содержит основной набор методов для
  работы с абстрактной сетью

  Версия: 1.0
  Автор: Черномуров Семён
  Последнее изменение: 14.06.2023
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
}
