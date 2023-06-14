package com.example.javablockchainimplementation.util.network.P2PNetwork;

import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.network.Network;
import com.example.javablockchainimplementation.util.network.NetworkNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/*
  Реализация интерфейса Network
  Класс реализует одноранговую (P2P) сеть, хранящую
  транзакции в своем пространстве, а множество узлов
  сети, хранящих копии блокчейна

  Версия: 1.0
  Автор: Черномуров Семён
  Последнее изменение: 14.06.2023
*/
public class P2PNetwork implements Network {
    private Set<NetworkNode> networkNodes; //Множество узлов сети
    private Set<Transaction> untrustedTransactions; //Множество неподтвержденных транзакций в сети

    //Конструктор
    public P2PNetwork() {
        this.networkNodes = new HashSet<>();
        this.untrustedTransactions = new HashSet<>();
    }

    @Override
    public void addNode(NetworkNode node) {
        assert node != null;
        getNetworkNodes().add(node);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        assert transaction != null;
        getUntrustedTransactions().add(transaction);
    }

    @Override
    public Set<Transaction> getUntrustedTransactions() {
        return untrustedTransactions;
    }

    @Override
    public Set<NetworkNode> getNetworkNodes() {
        return networkNodes;
    }

    //Метод получения публичного IP-адреса (требует доработок)
    public static String getPublicIpAddress() { //TODO на будущее

        final String urlString = "http://checkip.amazonaws.com/";
        try {
            final URL url = new URL(urlString);

            final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
