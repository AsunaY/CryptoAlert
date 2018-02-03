package net.snailz.cryptoalert;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.awt.*;
import java.io.IOException;
import java.math.MathContext;

public class PriceMessage {
    private MessageChannel channel;
    private CurrencyPair currencyPair;
    private Ticker ticker;
    private boolean reverse;

    public PriceMessage(MessageChannel channel, CurrencyPair currencyPair, boolean reverse) throws BinanceException{
        this.channel = channel;
        this.currencyPair = currencyPair;
        this.reverse = reverse;
        try {
            this.ticker = registerExtange();
        } catch (IOException e){
            channel.sendMessage("IO Exception! Stacktrace in terminal.").complete();
            e.printStackTrace();
        }
    }

    private Ticker registerExtange() throws IOException, BinanceException{
        ExchangeSpecification exchangeSpecification = new BinanceExchange().getDefaultExchangeSpecification();

        Exchange binance = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);

        MarketDataService marketDataService = binance.getMarketDataService();

        Ticker ticker = marketDataService.getTicker(currencyPair);

        return ticker;
    }

    public void sendMessage(){
        double ask = reverse ? 10/ticker.getAsk().doubleValue()/10 : ticker.getAsk().doubleValue();
        double high = reverse ? 10/ticker.getHigh().doubleValue()/10 : ticker.getHigh().doubleValue();
        double low = reverse ? 10/ticker.getLow().doubleValue()/10 : ticker.getLow().doubleValue();
        //double volume = reverse ? 10/ticker.getVolume().doubleValue()/10 : ticker.getVolume().doubleValue();
        String pairName = reverse ? currencyPair.counter.toString() + " to " + currencyPair.base.toString() :
                currencyPair.base.toString() + " to " + currencyPair.counter.toString();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(pairName);
        embedBuilder.addField("Asking Price", String.valueOf(ask), true);
        embedBuilder.addField("24 Hour Low", String.valueOf(high), true);
        embedBuilder.addField("24 Hour High", String.valueOf(low), true);
        //embedBuilder.addField("24 Hour Volume", String.valueOf(volume), true);
        embedBuilder.setAuthor("Crypto Bot");
        embedBuilder.setColor(Color.CYAN);
        channel.sendMessage(embedBuilder.build()).complete();
    }


}
