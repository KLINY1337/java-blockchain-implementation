package com.example.javablockchainimplementation.util.network.P2PNetwork;

import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.hash.SHA256;
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

  Версия: 3.0
  Автор: Черномуров Семён
  Последнее изменение: 25.06.2023
*/
public class P2PNetwork {
    private static final double MINIMAL_TRANSACTION_SIZE = 0.01;
    private final Set<P2PNetworkNode> networkNodes; //Множество узлов сети
    private final Set<NetworkUser> networkUsers; //Множество пользователей (кошельков) сети
    private final Set<Transaction> untrustedTransactions; //Множество неподтвержденных транзакций в сети
    private final NetworkUser feeDistributingUser; //Пользователь для распределения наград за майнинг и содержание сети

    //Конструктор
    public P2PNetwork() {
        this.networkNodes = new HashSet<>();
        this.networkUsers = new HashSet<>();
        this.untrustedTransactions = new HashSet<>();
        this.feeDistributingUser = addUser(SHA256.generateHash(NetworkUser.generateRandomString()),
                NetworkUser.generateRandomString() + NetworkUser.generateRandomString());
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

    //Метод добавления узла в сеть
    public void addNode(P2PNetworkNode node) {
        assert node != null;
        networkNodes.add(node);
    }

    //Метод добавления неподтвержденной транзакции в сеть
    public void addTransaction(Transaction transaction) {
        assert transaction != null;
        untrustedTransactions.add(transaction);
    }

    //Метод добавления пользователя в сеть
    public NetworkUser addUser(String login, String password) {

        //Если логин не занят
        if (!isLoginOccupied(login)) {

            //Добавить пользователя в сеть
            NetworkUser newUser = new NetworkUser(login, password, this);
            networkUsers.add(newUser);

            return newUser;
        }
        else {
            System.out.println("user occupied");//TODO что то поменять
            return null;
        }
    }

    //Метод поиска объекта пользователя по его кошельку
    public NetworkUser findUserByWallet(String wallet) {
        Optional<NetworkUser> networkUser = getNetworksUsers()
                .stream()
                .filter(user -> user.getWallet().equals(wallet))
                .findFirst();

        return networkUser.orElse(null);
    }

    //Метод получения распределяющего награды пользователя
    public NetworkUser getFeeDistributingUser() {
        return feeDistributingUser;
    }

    //Метод получения минимального объема транзакции
    public double getMinimalTransactionSize() {
        return MINIMAL_TRANSACTION_SIZE;
    }

    //Метод получения узлов сети
    public Set<P2PNetworkNode> getNetworkNodes() {
        return networkNodes;
    }

    //Метод получения пользователей сети
    public Set<NetworkUser> getNetworksUsers() {
        return networkUsers;
    }

    //Метод получения неподтвержденных транзакций сети
    public Set<Transaction> getUntrustedTransactions() {
        return untrustedTransactions;
    }

    //Метод обновления узлов сети
    public void updateNodes() {
        networkNodes.forEach(P2PNetworkNode::updateBlockchain);
    }

    //Метод проверки занятости логина в сети
    private boolean isLoginOccupied(String login) {
        return getNetworksUsers()
                .stream()
                .map(NetworkUser::getLogin)
                .collect(Collectors.toSet())
                .contains(login);
    }
}
