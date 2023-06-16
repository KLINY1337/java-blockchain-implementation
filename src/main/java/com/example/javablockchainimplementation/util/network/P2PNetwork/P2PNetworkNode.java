package com.example.javablockchainimplementation.util.network.P2PNetwork;

import com.example.javablockchainimplementation.util.blockchain.Blockchain;
import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.BlockchainImpl;
import com.example.javablockchainimplementation.util.network.Network;
import com.example.javablockchainimplementation.util.network.NetworkNode;
import com.example.javablockchainimplementation.util.network.NetworkUser;
import com.example.javablockchainimplementation.util.network.NodeInformation;
import org.apache.commons.lang3.SerializationUtils;

/*
  Реализация абстрактного класса NetworkNode
  Класс реализует узел одноранговой (P2P) сети, хранящей
  транзакции в своем пространстве. Узел сети хранит в
  себе копию информации о блокчейне

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public class P2PNetworkNode extends NetworkNode {

    //Конструктор
    public P2PNetworkNode(final String ipAddress, //IP-адрес узла
                          final int port, //Открытый порт узла
                          final P2PNetwork parentNetwork //Сеть, к которой относится узел
                            ) {

        assert 0 <= port && port <= 65535;
        assert parentNetwork != null;

        this.ipAddress = ipAddress;
        this.port = port;
        this.parentNetwork = parentNetwork;
        this.parentNetwork.addNode(this);

        //Получить корректную версию блокчейна
        updateNodeInformation();

    }

    @Override
    public void addTransaction(final Transaction transaction) {

        assert transaction != null;

        //Добавить транзакцию в сеть
        final Network parentNetwork = getParentNetwork();
        parentNetwork.addTransaction(transaction);

        //Распространить транзакцию по всем узлам
        for (final NetworkNode networkNode : getParentNetwork().getNetworkNodes()) {

            //Избегаем повторного добавления транзакции в изначальный узел
            if (!networkNode.getIpAddress().equals(this.getIpAddress())) {
                networkNode.getParentNetwork().addTransaction(SerializationUtils.clone(transaction)); //TODO сделать по сети а не локально
            }
        }
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public Network getParentNetwork() {
        return parentNetwork;
    }

    @Override
    public NodeInformation getNodeInformation() {
        return nodeInformation;
    }

    //Метод создания нового блока
    public Block createBlock() {

        //Создать блок
        final Block block = new Block(getCurrentBlockchainImplementation());

        //Заполнить блок неподтвержденными транзакциями
        for (final Transaction transaction : getParentNetwork().getUntrustedTransactions()) {
            if (block.hasEmptyPlacesForTransactions()) {
                block.addTransaction(transaction);
            } else {
                break;
            }
        }

        return block;
    }

    //Метод получения копии блокчейна, находящейся на узле
    private Blockchain getCurrentBlockchainImplementation() {
        return (Blockchain) getNodeInformation();
    }

    //Метод добавления блока в цепь
    public void addBlockToChain(final Block block) {

        assert block != null;

        //Добавление блока в цепь (true - если блок добавился в цепь, false - в противном случае)
        final boolean isBlockValid = getCurrentBlockchainImplementation().addBlock(block);

        //Если блок добавился в цепь корректно
        if (isBlockValid) {
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

                getParentNetwork().getUntrustedTransactions().remove(transaction);
            }
        } else {
            System.out.println("block is invalid");
        }
    }

    //Метод получения корректной версии блокчейна из сети
    public void updateNodeInformation() {

        int maxChainLength = -1;
        Blockchain longestBlockchain = null;

        //Поиск самой длинной цепочки, представленной в сети
        for (final NetworkNode networkNode : this.getParentNetwork().getNetworkNodes()) {

            if (!networkNode.getIpAddress().equals(this.getIpAddress())) {

                Blockchain nodeBlockchainImplementation = (Blockchain) networkNode.getNodeInformation();
                if (nodeBlockchainImplementation.getChainLength() > maxChainLength) {

                    maxChainLength = nodeBlockchainImplementation.getChainLength();
                    longestBlockchain = nodeBlockchainImplementation;
                }
            }
        }

        //Если в сети есть блокчейн, то возвращаем его
        if (longestBlockchain != null) {
            this.nodeInformation = SerializationUtils.clone(longestBlockchain);
        }
        //В противном случае создаем новый
        else {
            this.nodeInformation = new BlockchainImpl();
        }
    }
}

//TODO сделать периодическое обновление блокчейна