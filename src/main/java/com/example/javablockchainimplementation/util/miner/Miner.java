package com.example.javablockchainimplementation.util.miner;

import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.network.NetworkUser;

/*
  Интерфейс майнера блоков блокчейна
  Майнер содержит метод майнинга блока методом перебора
  изменяемого значения блока

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public interface Miner {

    //Метод майнинга блока
    Block mineBlock(Block unminedBlock, NetworkUser user);
}
