package com.example.javablockchainimplementation.util.network;

import com.example.javablockchainimplementation.util.blockchain.Transaction;
import com.example.javablockchainimplementation.util.hash.SHA256;
import com.example.javablockchainimplementation.util.network.P2PNetwork.P2PNetwork;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/*
  Класс пользователя сети
  Класс содержит информацию, необходимую для
  аутентификации пользователя в сети, а также
  для совершения внутрисетевых переводов

  Версия: 3.0
  Автор: Черномуров Семён
  Последнее изменение: 25.06.2023
*/
public class NetworkUser {
    private static final int WALLET_LENGTH = 40; //Длина кошелька в символах
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //Символы, используемые в имени кошелька
    private final P2PNetwork parentNetwork; //Сеть, которой принадлежит пользователь
    private final String wallet; //Кошелек пользователя
    private final String login; //Логин пользователя
    private final String passwordHash; //Хэш пароля пользователя
    private double balance; //Баланс пользователя


    //Конструктор
    public NetworkUser(
            String login,
            String password,
            P2PNetwork parentNetwork
    ) {
        this.parentNetwork = parentNetwork;
        this.login = login;
        this.passwordHash = SHA256.generateHash(password);
        this.wallet = generateWallet();
        this.balance = 0L;
    }

    //Метод генерации случайной строки
    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(WALLET_LENGTH);

        //Сгенерировать строку из символов строки CHARACTERS
        for (int i = 0; i < WALLET_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    //Метод изменения баланса кошелька
    public void changeBalance(double amount) {
        assert balance + amount >= 0;
        this.balance += amount;
    }

    //Метод получения баланса пользователя
    public double getBalance() {
        return balance;
    }

    //Метод получения логина пользователя
    public String getLogin() {
        return login;
    }

    //Метод получения кошелька пользователя
    public String getWallet() {
        return wallet;
    }

    //Метод перевода токенов другому пользователю
    public void sendCurrency(String recipient, double amount) {

        double transactionFee = 0;
        if (amount > 0) {
            //Расчет комиссии за перевод
            transactionFee = 1 / amount / 100;
        }

        //Создание неподтвержденной транзакции
        Transaction transaction = new Transaction(
                this.getWallet(),
                recipient,
                amount - transactionFee,
                SHA256.generateHash(String.valueOf(new Date().getTime())),
                transactionFee);

        //Добавление транзакции в сеть
        parentNetwork.addTransaction(transaction);
    }

    //Метод генерации кошелька
    private String generateWallet() {
        //Получить список пользователей сети
        Set<NetworkUser> setOfUsers = parentNetwork.getNetworksUsers();

        String wallet = generateRandomString();

        //Получить список кошельков в сети
        Set<String> usersWallets = setOfUsers
                .stream()
                .map(NetworkUser::getWallet)
                .collect(Collectors.toSet());

        //Пока кошелек не станет уникальным для сети
        while (usersWallets.contains(wallet)) {

            //Перегенерировать кошелек
            wallet = generateRandomString();
        }

        return wallet;
    }
}
