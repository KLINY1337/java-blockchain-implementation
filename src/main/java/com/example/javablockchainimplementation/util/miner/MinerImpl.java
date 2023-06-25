package com.example.javablockchainimplementation.util.miner;

import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Blockchain;
import com.example.javablockchainimplementation.util.network.NetworkUser;
import com.example.javablockchainimplementation.util.network.P2PNetwork.P2PNetwork;

/*
  Реализация интерфейса майнера блоков блокчейна
  Майнер содержит метод майнинга блока методом перебора
  изменяемого значения блока

  Версия: 3.0
  Автор: Черномуров Семён
  Последнее изменение: 25.06.2023
*/
public class MinerImpl implements Miner {

    private final P2PNetwork parentNetwork;

    public MinerImpl(P2PNetwork parentNetwork) {
        this.parentNetwork = parentNetwork;
    }

    @Override
    public Block mineBlock(final Block block, final NetworkUser user) {

        assert block != null;

        Blockchain parentBlockchain = block.getParentBlockchain();

        //Расчет награды за майнинг блока
        double miningReward = parentBlockchain.getTokensLeft() / 10000000;

        //Пока блок не замайнен, изменяем хэш блока
        while (!block.isMined()) {
            block.incrementNonce();
            System.out.println(block.getHash());
        }

        //Если указан существующий в сети пользователь
        if (user != null) {

            //Начисление награды за майнинг на кошелек
            NetworkUser feeDistributingUser = parentNetwork.getFeeDistributingUser();
            feeDistributingUser.changeBalance(miningReward);
            feeDistributingUser.sendCurrency(user.getWallet(), miningReward);

            //Уменьшить число оставшихся недобытых токенов
            parentBlockchain.decreaseTokensLeft(miningReward);
        }
        else {
            System.out.println("zhiganie monet");
        }

        return block;
    }
}
