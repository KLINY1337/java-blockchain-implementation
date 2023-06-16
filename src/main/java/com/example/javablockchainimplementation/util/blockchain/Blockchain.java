package com.example.javablockchainimplementation.util.blockchain;

import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.network.NodeInformation;

import java.util.List;

/*
  Интерфейс Blockchain
  Определяет операции, производимые над блокчейном

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public interface Blockchain extends NodeInformation {

    //Метод добавления блока в блокчейн
    boolean addBlock(Block block);

    //Метод получения последнего блока в блокчейне
    Block getLastBlock();

    //Метод проверки заполненности блокчейна блоками
    boolean isEmpty();

    //Метод проверки занятости хэша блока уже созданными блоками
    boolean isBlockHashOccupied(String hash);

    //Метод получения длины блокчейна
    int getChainLength();

    //Метод получения максимального числа транзакций, подтверждаемых блоком
    int getMaxAmountOfTransactionsInBlock();

    //Метод получения цепочки блоков
    List<Block> getChain();

    //Метод получения количества оставшихся токенов в блокчейне
    double getTokensLeft();

    //Метод уменьшения оставшихся в сети токенов
    void decreaseTokensLeft(double miningReward);
}
