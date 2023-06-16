package com.example.javablockchainimplementation.util.miner;

import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.network.NetworkUser;

/*
  Реализация интерфейса майнера блоков блокчейна
  Майнер содержит метод майнинга блока методом перебора
  изменяемого значения блока

  Версия: 2.0
  Автор: Черномуров Семён
  Последнее изменение: 16.06.2023
*/
public class MinerImpl implements Miner {

    @Override
    public Block mineBlock(final Block unminedBlock, final NetworkUser user) {

        assert unminedBlock != null;

        //Расчет награды за майнинг блока
        double miningReward = unminedBlock.getParentBlockchain().getTokensLeft() / 10000000;

        //Пока блок не замайнен, изменяем хэш блока
        while (!unminedBlock.isMined()) {
            unminedBlock.incrementNonce();
            System.out.println(unminedBlock.getHash());
        }

        //Если указан существующий в сети пользователь
        if (user != null ) {

            //Начисление награды за майнинг на кошелек
            user.changeBalance(miningReward);

            //Уменьшить число оставшихся недобытых токенов
            unminedBlock.getParentBlockchain().decreaseTokensLeft(miningReward);
        }
        else {
            System.out.println("zhiganie monet");
        }

        return unminedBlock;
    }
}
