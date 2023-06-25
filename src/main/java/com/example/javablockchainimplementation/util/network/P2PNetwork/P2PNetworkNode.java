package com.example.javablockchainimplementation.util.network.P2PNetwork;

import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Blockchain;
import com.example.javablockchainimplementation.util.network.NetworkUser;
import org.apache.commons.lang3.SerializationUtils;

import java.util.function.Consumer;

/*
  Реализация абстрактного класса NetworkNode
  Класс реализует узел одноранговой (P2P) сети, хранящей
  транзакции в своем пространстве. Узел сети хранит в
  себе копию информации о блокчейне

  Версия: 3.0
  Автор: Черномуров Семён
  Последнее изменение: 25.06.2023
*/
public class P2PNetworkNode {

    private final NetworkUser networkUser;
    private final String ipAddress;
    private final int port;
    private final P2PNetwork parentNetwork;
    private Blockchain blockchain;


    //Конструктор
    public P2PNetworkNode(
            final String ipAddress, //IP-адрес узла
            final int port, //Открытый порт узла
            final P2PNetwork parentNetwork, //Сеть, к которой относится узел
            String login, //Логин для кошелька, презентующего узел
            String password //Пароль для кошелька, презентующего узел
    ) {

        assert 0 <= port && port <= 65535;
        assert parentNetwork != null;

        this.ipAddress = ipAddress;
        this.port = port;
        this.parentNetwork = parentNetwork;
        this.parentNetwork.addNode(this);
        this.networkUser = parentNetwork.addUser(login, password);

        //Получить корректную версию блокчейна
        updateBlockchain();

    }

    //Метод добавления блока в цепь
    public void addBlockToChain(final Block block) {

        assert block != null;

        //Добавление блока в цепь (true - если блок добавился в цепь, false - в противном случае)
        final boolean isBlockValid = blockchain.addBlock(block);

        //Если блок добавился в цепь корректно
        if (isBlockValid) {

            double transactionsFeeSum = 0;

            //Удаляем транзакции блока из множества неподтвержденных транзакций
            for (final Transaction transaction : block.getTransactions()) {
                NetworkUser sender = getParentNetwork().findUserByWallet(transaction.sender());
                NetworkUser recipient = getParentNetwork().findUserByWallet(transaction.recipient());

                if (sender.getBalance() >= transaction.amount()) {
                    sender.changeBalance(-transaction.amount());
                }
                else {
                    System.out.println("not enough money on sender");
                }

                //Если не происходит сжигание токенов и получатель существует
                if (recipient != null) {
                    //Начисляем получателю отправленную сумму
                    recipient.changeBalance(transaction.amount());
                }

                //Рассчитываем общую сумму комиссий за транзакции из блока
                transactionsFeeSum += transaction.transactionFee();

                //Удаляем транзакцию из списка неподтвержденных
                parentNetwork.getUntrustedTransactions().remove(transaction);
            }

            //Распределяем общую сумму комиссий за транзакции между узлами сети в качестве награды
            boolean isNetworkHasUsers = !parentNetwork.getNetworksUsers().isEmpty();
            boolean isTransactionsFeeSumEnoughForRewardAllNodes =
                    transactionsFeeSum > parentNetwork.getMinimalTransactionSize() * parentNetwork.getNetworksUsers().size();

            if (isNetworkHasUsers && isTransactionsFeeSumEnoughForRewardAllNodes) {

                double rewardForNode = Math.max(
                        transactionsFeeSum / parentNetwork.getNetworksUsers().size(),
                        parentNetwork.getMinimalTransactionSize()
                );

                NetworkUser feeDistributingUser = parentNetwork.getFeeDistributingUser();

                Consumer<P2PNetworkNode> sendRewardForNode =
                        node -> feeDistributingUser.sendCurrency(node.getNetworkUser().getWallet(), rewardForNode);

                parentNetwork
                        .getNetworkNodes()
                        .forEach(sendRewardForNode)
                ;

            }
        }
        else {
            System.out.println("block is invalid");
        }
    }

    //Метод добавления пользователя в сеть
    public NetworkUser addUser(String login, String password) {
        return parentNetwork.addUser(login, password);
    }

    //Метод создания нового блока
    public Block createBlock() {

        //Создать блок
        final Block block = new Block(blockchain);

        //Заполнить блок неподтвержденными транзакциями
        for (final Transaction transaction : parentNetwork.getUntrustedTransactions()) {
            if (block.hasEmptyPlacesForTransactions()) {
                block.addTransaction(transaction);
            }
            else {
                break;
            }
        }

        return block;
    }

    //Метод получения ip-адреса узла сети
    public String getIpAddress() {
        return ipAddress;
    }

    //Метод получения пользователя, прикрепленного к узлу
    public NetworkUser getNetworkUser() {
        return this.networkUser;
    }

    //Метод получения корректной версии блокчейна из сети
    public void updateBlockchain() {

        int maxChainLength = -1;
        Blockchain longestBlockchain = null;

        //Поиск самой длинной цепочки, представленной в сети
        for (final P2PNetworkNode networkNode : parentNetwork.getNetworkNodes()) {

            boolean isNotCurrentNode = !networkNode.getIpAddress().equals(this.getIpAddress());
            if (isNotCurrentNode) {

                int nodeBlockchainLength = networkNode.getBlockchainLength();

                if (nodeBlockchainLength > maxChainLength) {

                    maxChainLength = nodeBlockchainLength;
                    longestBlockchain = networkNode.getBlockchain();
                }
            }
        }

        //Если в сети есть блокчейн, то возвращаем его
        if (longestBlockchain != null) {
            this.blockchain = SerializationUtils.clone(longestBlockchain);
        }
        //В противном случае создаем новый
        else {
            this.blockchain = new Blockchain();
        }
    }

    //Метод получения блокчейна на узле
    private Blockchain getBlockchain() {
        return blockchain;
    }

    private int getBlockchainLength() {
        return blockchain.getChain().size();
    }


    private P2PNetwork getParentNetwork() {
        return parentNetwork;
    }
}

//TODO сделать периодическое обновление блокчейна