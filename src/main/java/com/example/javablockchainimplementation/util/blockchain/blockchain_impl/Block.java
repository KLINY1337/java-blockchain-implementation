package com.example.javablockchainimplementation.util.blockchain.blockchain_impl;

import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.hash.SHA256;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
  Класс блока блокчейна
  Блок содержит список транзакций сети, которые подтверждаются
  в случае успешного добавления блока в блокчейн после майнинга

  Версия: 3.0
  Автор: Черномуров Семён
  Последнее изменение: 25.06.2023
*/
public class Block implements Serializable {

    private static final int MAX_BLOCK_SIZE = 10; //Максимальное число транзакций в блоке
    private final int proofOfWorkDifficulty = 2; // Сложность майнинга блока (1 <= X < Длина хеша блока)
    private final Blockchain parentBlockchain; // Ссылка на блокчейн, к которому относится блок
    private final long timeStamp; // Время создания блока (миллисекунд от 1970 года)
    private final int index; // Индекс блока в блокчейне
    private final List<Transaction> transactions; // Список транзакций, подтверждаемых блоком
    private final String previousHash; // Хэш предыдущего блока в блокчейне

    private String merkleRoot; // Не испольузем
    private int nonce = 0; // Число, влияющее на хэш блока при майнинге

    //Конструктор блока
    public Block(final Blockchain parentBlockchain) {

        assert proofOfWorkDifficulty < SHA256.generateHash("1").length();
        assert parentBlockchain != null;

        this.parentBlockchain = parentBlockchain;
        this.timeStamp = new Date().getTime();

        //Если блокчейн не пустой (создан генезис-блок)
        if (!parentBlockchain.isEmpty()) {
            this.index = getLastBlockOfChain().getIndex() + 1;
            this.transactions = new ArrayList<>();
            this.previousHash = getLastBlockOfChain().getHash();
        }
        //Если блокчейн пустой (не создан генезис-блок)
        else {
            this.index = 1;
            this.transactions = new ArrayList<>();
            this.previousHash = "genesisBlockHash";
        }
    }

    //Метод добавления транзакции в блок
    public void addTransaction(final Transaction transaction) {

        assert transaction != null;

        //Если блок не заполнен, добавить транзакцию
        if (this.hasEmptyPlacesForTransactions()) {
            transactions.add(transaction);
        }
        else {
            System.out.println("block has no place for transaction");
        }

    }

    //Метод получения хэша блока
    public String getHash() {
        return SHA256.generateHash(timeStamp + index + new Gson().toJson(transactions) + previousHash + proofOfWorkDifficulty + nonce);
    }

    //Метод получения блокчейна, к которому относится блок
    public Blockchain getParentBlockchain() {
        return parentBlockchain;
    }

    //Метод получения хэша предыдущего блока
    public String getPreviousHash() {
        return previousHash;
    }

    //Метод получения транзакций, содержащихся в блоке
    public List<Transaction> getTransactions() {
        return transactions;
    }

    //Метод проверки наличия места в блоке (определяется переменной MAX_BLOCK_SIZE в BlockchainImpl)
    public boolean hasEmptyPlacesForTransactions() {
        return transactions.size() < MAX_BLOCK_SIZE;
    }

    //Метод изменения случайного числа (нужен для майнинга)
    public void incrementNonce() {
        this.nonce++;
    }

    //Метод проверки корректности блока (в отрыве от блокчейна), намайнен ли блок
    public boolean isMined() {
        //Создаем строку, с которой будет сравниваться часть хэша блока (для сложности 4 она будет "0000")
        final String prefixString = new String(new char[proofOfWorkDifficulty]).replace('\0', '0');

        //Находим префикс хэша блока
        final String blockHash = getHash();
        final String blockHashPrefix = blockHash.substring(0, proofOfWorkDifficulty);

        return blockHashPrefix.equals(prefixString);
    }

    //Метод получения индекса блока
    private int getIndex() {
        return index;
    }

    //Метод получения последнего блока в цепи
    private Block getLastBlockOfChain() {
        int indexOfLastBlock = parentBlockchain.getChain().size() - 1;
        return parentBlockchain.getChain().get(indexOfLastBlock);
    }
}
