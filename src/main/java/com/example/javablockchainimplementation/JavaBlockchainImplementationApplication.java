package com.example.javablockchainimplementation;

import com.example.javablockchainimplementation.util.blockchain.blockchain_impl.Block;
import com.example.javablockchainimplementation.util.hash.SHA256;
import com.example.javablockchainimplementation.util.miner.Miner;
import com.example.javablockchainimplementation.util.miner.MinerImpl;
import com.example.javablockchainimplementation.util.network.NetworkUser;
import com.example.javablockchainimplementation.util.network.P2PNetwork.P2PNetwork;
import com.example.javablockchainimplementation.util.network.P2PNetwork.P2PNetworkNode;

import java.io.IOException;

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

        P2PNetworkNode node = new P2PNetworkNode(P2PNetwork.getPublicIpAddress(), 9999, network, "login1", "pass");
        P2PNetworkNode node1 = new P2PNetworkNode("1.1.1.1", 9999, network, "login2", "pass");
        P2PNetworkNode node2 = new P2PNetworkNode("2.2.2.2", 9999, network, "login3", "pass");
        P2PNetworkNode node3 = new P2PNetworkNode("3.3.3.3", 9999, network, "login4", "pass");

        NetworkUser user1 = node.addUser("login5", "password");
        NetworkUser user2 = node.addUser("login6", "password");

        Miner miner = new MinerImpl(network);

        for (int i = 0; i < 100; i++) {
            Block block = node1.createBlock();
            miner.mineBlock(block, user1);
            node1.addBlockToChain(block);
            network.updateNodes();
        }

        user1.sendCurrency(user2.getWallet(), 20);

        Block block = node1.createBlock();
        miner.mineBlock(block, user2);
        node1.addBlockToChain(block);
        network.updateNodes();

        System.out.println("\n\n");

        System.out.println(SHA256.generateHash(SHA256.generateHash("loginlogin")).substring(0, 15));
        System.out.println(SHA256.generateHash(SHA256.generateHash(SHA256.generateHash("pass"))).substring(0, 15));
    }


}