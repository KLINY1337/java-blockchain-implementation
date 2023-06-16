package com.example.javablockchainimplementation.util.network;

import com.example.javablockchainimplementation.util.blockchain.Transaction;

/*
  Абстрактный класс узла сети
  Класс содержит основную информацию, необходимую
  для идентификации узла в сети, а также
  информацию, содержащуюся в узле (блокчейн)

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public abstract class NetworkNode {

    protected String ipAddress; //IP-адрес узал
    protected int port; //Открытый порт узла
    protected Network parentNetwork; //Сеть, к которой относится узел
    protected NodeInformation nodeInformation; //Информация, содержащаяся на узле (в данном случае блокчейн)

    //Метод добавления транзакции в сеть
    abstract public void addTransaction(Transaction transaction);

    //Метод получения IP-адреса узал
    abstract public String getIpAddress();

    //Метод получения сети, в которой находится узел
    abstract public Network getParentNetwork();

    //Метод получения информации, содержащейся на узле
    abstract public NodeInformation getNodeInformation();

    //Метод обновления блокчейна на узле
    abstract public void updateNodeInformation();
}
