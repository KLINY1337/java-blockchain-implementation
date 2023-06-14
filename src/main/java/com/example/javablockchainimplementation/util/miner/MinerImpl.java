package com.example.javablockchainimplementation.util.miner;

import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;

/*
  Реализация интерфейса майнера блоков блокчейна
  Майнер содержит метод майнинга блока методом перебора
  изменяемого значения блока

  Версия: 1.0
  Автор: Черномуров Семён
  Последнее изменение: 14.06.2023
*/
public class MinerImpl implements Miner {
    @Override
    public Block mineBlock(final Block unminedBlock) {

        assert unminedBlock != null;

        //Пока блок не замайнен, изменяем хэш блока
        while (!unminedBlock.isMined()) {
            unminedBlock.incrementNonce();
            System.out.println(unminedBlock.getHash());
        }

        return unminedBlock;
    }
}
