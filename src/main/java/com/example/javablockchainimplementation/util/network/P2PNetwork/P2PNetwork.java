package com.example.javablockchainimplementation.util.network.P2PNetwork;

import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.network.Network;
import com.example.javablockchainimplementation.util.network.NetworkNode;
import com.example.javablockchainimplementation.util.network.NetworkUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
  Реализация интерфейса Network
  Класс реализует одноранговую (P2P) сеть, хранящую
  транзакции в своем пространстве, а множество узлов
  сети, хранящих копии блокчейна

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public class P2PNetwork implements Network {
    private Set<NetworkNode> networkNodes; //Множество узлов сети
    private Set<NetworkUser> networkUsers; //Множество пользователей (кошельков) сети
    private Set<Transaction> untrustedTransactions; //Множество неподтвержденных транзакций в сети

    //Конструктор
    public P2PNetwork() {
        this.networkNodes = new HashSet<>();
        this.networkUsers = new HashSet<>();
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
    public Set<NetworkUser> getNetworksUsers() {
        return networkUsers;
    }

    @Override
    public NetworkUser addUser(String login, String password) {
        boolean isLoginOccupied = getNetworksUsers().stream().map(NetworkUser::getLogin).collect(Collectors.toSet()).contains(login);

        //Если логин не занят
        if (!isLoginOccupied) {

            //Добавить пользователя в сеть
            NetworkUser newUser = new NetworkUser(login, password, this);
            getNetworksUsers().add(newUser);
            return newUser;
        }
        else {
            System.out.println("user occupied");; //TODO что то поменять
            return null;
        }
    }

    @Override
    public NetworkUser findUserByWallet(String wallet) {
        Optional<NetworkUser> networkUser = getNetworksUsers().stream().filter(user -> user.getWallet().equals(wallet)).findFirst();

        return networkUser.orElse(null);
    }

    @Override
    public void updateNodes() {
        for (NetworkNode networkNode : getNetworkNodes()) {
            networkNode.updateNodeInformation();
        }
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
