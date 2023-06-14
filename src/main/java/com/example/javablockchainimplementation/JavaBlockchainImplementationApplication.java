package com.example.javablockchainimplementation;

import com.example.javablockchainimplementation.util.network.P2PNetwork.P2PNetworkNode;
import com.example.javablockchainimplementation.util.network.P2PNetwork.P2PNetwork;
import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.hash.SHA256;
import com.example.javablockchainimplementation.util.miner.Miner;
import com.example.javablockchainimplementation.util.miner.MinerImpl;

import java.io.IOException;
import java.util.Date;

//@SpringBootApplication
public class JavaBlockchainImplementationApplication {

    public static void main(String[] args) throws IOException {

/*
        String urlString = "http://checkip.amazonaws.com/";
        URL url = new URL(urlString);

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        System.out.println(br.readLine());

        Blockchain blockchain = new BlockchainImpl();

        Transaction transaction = new Transaction("sender", "reciever", 5);
        Transaction transaction1 = new Transaction("sender1", "reciever1", 100);
        blockchain.addTransaction(transaction);
        blockchain.addTransaction(transaction1);

        Block block = new Block(blockchain);

        for (Transaction t : blockchain.getUntrustedTransactions()) {
            block.addTransaction(t);
        }

        Miner miner = new MinerImpl();

        miner.mineBlock(block);

        blockchain.addBlock(block);

        System.out.println(block);
*/
        ///////////////////////////////////////////////////////////////

        P2PNetwork network = new P2PNetwork();

        P2PNetworkNode node = new P2PNetworkNode(P2PNetwork.getPublicIpAddress(), 9999, network);
        P2PNetworkNode node1 = new P2PNetworkNode("1.1.1.1", 9999, network);
        P2PNetworkNode node2 = new P2PNetworkNode("2.2.2.2", 9999, network);
        P2PNetworkNode node3 = new P2PNetworkNode("3.3.3.3", 9999, network);

        Transaction transaction1 = new Transaction("sender", "reciever", 5, SHA256.generateHash(String.valueOf(new Date().getTime())));

        for (int i = 0; i < 100; i++) {

            Transaction transaction = new Transaction("sender", "reciever", 5, SHA256.generateHash(String.valueOf(new Date().getTime())));
            node.addTransaction(transaction);
        }

        node.addTransaction(transaction1);

        Block block = node.createBlock();
        //node.addBlockToChain(block);

        Miner miner = new MinerImpl();
        miner.mineBlock(block);

        /*Block block1 = node.createBlock();
        Block block2 = node1.createBlock();
        Block block3 = node3.createBlock();
        Block block4 = node2.createBlock();

        miner.mineBlock(block1);
        miner.mineBlock(block2);
        miner.mineBlock(block3);
        miner.mineBlock(block4);

        node.addBlockToChain(block1);

        node1.addBlockToChain(block2);
        node3.addBlockToChain(block3);
        node2.addBlockToChain(block4);
        node.addBlockToChain(block);
*/
        Block block1 = node.createBlock();
        miner.mineBlock(block1);
        node.addBlockToChain(block1);

        Block block2 = node1.createBlock();
        miner.mineBlock(block2);
        node1.addBlockToChain(block2);

        Block block3 = node3.createBlock();
        miner.mineBlock(block3);
        node3.addBlockToChain(block3);

        Block block4 = node2.createBlock();
        miner.mineBlock(block4);
        node2.addBlockToChain(block4);
    }

}