package net.snailz.cryptoalert;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.HashMap;

public class MessageListener implements EventListener{

    public void onEvent(Event event){
        if (event instanceof MessageReceivedEvent){
            MessageReceivedEvent mEvent = (MessageReceivedEvent) event;
            if (!mEvent.getMessage().getContentRaw().startsWith("!crypto")){
                return;
            }
            String message = mEvent.getMessage().getContentRaw().replace("!crypto ", "");
            String[] args = message.split(" ");
            if (!Currency.getAvailableCurrencyCodes().contains(args[0]) || !Currency.getAvailableCurrencyCodes().contains(args[1])){
                mEvent.getChannel().sendMessage(mEvent.getAuthor().getAsMention() + " One of your currency codes is not valid!").complete();
                return;
            }



            PriceMessage priceMessage;
            try {
                priceMessage = new PriceMessage(mEvent.getChannel(), new CurrencyPair(args[0], args[1]), false);
            } catch (BinanceException e1){
                try{
                    priceMessage = new PriceMessage(mEvent.getChannel(), new CurrencyPair(args[1], args[0]), true);
                } catch (BinanceException e2){
                    mEvent.getChannel().sendMessage(mEvent.getAuthor().getAsMention() + " That exchange is not supported on Binance. " +
                            "This can usually be solved by reversing the currency codes.").complete();
                    return;
                }

            }
            priceMessage.sendMessage();
        }
    }
}
