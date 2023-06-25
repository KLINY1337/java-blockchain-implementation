package com.example.javablockchainimplementation.util.blockchain.blockchain_impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
  Реализация интерфейса Blockchain
  Блокчейн определяет максимальное число транзакций, подтверждаемых
  одним блоком, а также содержит цепочку блоков, добытых майнерами

  Версия: 3.0
  Автор: Черномуров Семён
  Последнее изменение: 25.06.2023
*/
public class Blockchain implements Serializable {
    private final List<Block> chain; //Цепочка блоков
    private double tokensLeft = 10000000; // Максимальное число токенов в блокчейне

    //Конструктор
    public Blockchain() {
        this.chain = new ArrayList<>();

        //Создать генезис-блок и добавить в цепочку
        final Block genesisBlock = createGenesisBlock();
        this.chain.add(genesisBlock);
    }

    //Метод добавления блока в блокчейн
    public boolean addBlock(final Block block) {

        assert block != null;

        //Если блок можно добавить в цепочку
        if (isBlockValid(block) && !isBlockHashOccupied(block.getHash()) && block.isMined()) {
            chain.add(block);
            return true;
        }
        else {
            System.out.println("block is invalid");
            return false;
        }
        //TODO validateBlockchain
    }

    //Метод уменьшения кол-ва оставшихся для эмиссии монет
    public void decreaseTokensLeft(double miningReward) {
        assert tokensLeft - miningReward >= 0;
        tokensLeft -= miningReward;
    }

    //Метод получения цепочки блоков
    public List<Block> getChain() {
        return chain;
    }

    //Метод получения кол-ва монет, доступных для эмисии
    public double getTokensLeft() {
        return tokensLeft;
    }

    //Метод проверки, не занят ли хэш другими блоками
    public boolean isBlockHashOccupied(final String hash) {

        assert !hash.equals("");

        for (final Block block : getChain()) {
            if (block.getHash().equals(hash)) {
                return true;
            }
        }
        return false;
    }

    //Метод проверки заполненности цепочки
    public boolean isEmpty() {
        return chain.isEmpty();
    }

    //Метод создания генезис-блока
    private Block createGenesisBlock() {
        return new Block(this);
    }

    //Метод проверки корректности блока для вставки в цепочку
    private boolean isBlockValid(Block block) {

        assert block != null;

        //Проверка, чтобы блок при вставке не нарушал целостности цепочки
        Block currentBlock = block;

        for (int i = chain.size() - 1; i >= 0; i--) {

            Block lastBlock = chain.get(i);

            if (lastBlock.getHash().equals(currentBlock.getPreviousHash())) {
                currentBlock = lastBlock;
            }
            else {
                return false;
            }
        }
        return true;
    }
}
