package com.example.javablockchainimplementation.util.blockchain.blockchain_impl;

import com.example.javablockchainimplementation.util.blockchain.Blockchain;

import java.util.ArrayList;
import java.util.List;

/*
  Реализация интерфейса Blockchain
  Блокчейн определяет максимальное число транзакций, подтверждаемых
  одним блоком, а также содержит цепочку блоков, добытых майнерами

  Версия: 1.0
  Автор: Черномуров Семён
  Последнее изменение: 14.06.2023
*/
public class BlockchainImpl implements Blockchain {

    private static final int MAX_BLOCK_SIZE = 10; //Максимальное число транзакций в блоке
    private final List<Block> chain; //Цепочка блоков

    //Конструктор
    public BlockchainImpl() {
        this.chain = new ArrayList<>();

        //Создать генезис-блок и добавить в цепочку
        final Block genesisBlock = createGenesisBlock();
        this.chain.add(genesisBlock);
    }

    @Override
    public boolean addBlock(final Block block) {

        assert block != null;

        if (isBlockValid(block) && !isBlockHashOccupied(block.getHash()) && block.isMined()) {
            getChain().add(block);
            return true;
        } else {
            System.out.println("block is invalid");
            return false;
        }
        //TODO validateBlockchain
    }

    @Override
    public Block getLastBlock() {
        return getChain().get(getChain().size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return getChain().isEmpty();
    }

    @Override
    public boolean isBlockHashOccupied(final String hash) {

        assert !hash.equals("");

        for (final Block block : getChain()) {
            if (block.getHash().equals(hash)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getChainLength() {
        return getChain().size();
    }

    @Override
    public int getMaxAmountOfTransactionsInBlock() {
        return MAX_BLOCK_SIZE;
    }

    @Override
    public List<Block> getChain() {
        return chain;
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

        for (int i = getChain().size() - 1; i >= 0; i--) {

            Block lastBlock = getChain().get(i);

            if (lastBlock.getHash().equals(currentBlock.getPreviousHash())) {
                currentBlock = lastBlock;
            } else {
                return false;
            }
        }
        return true;
    }
}
