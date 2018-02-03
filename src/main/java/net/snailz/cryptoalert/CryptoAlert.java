package net.snailz.cryptoalert;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class CryptoAlert {

    public static void main(String[] args){
        if (args.length == 0){
            System.out.println("Please run again with 1st argument bot token!");
        }
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(args[0])
                    .addEventListener(new MessageListener())
                    .buildBlocking();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
