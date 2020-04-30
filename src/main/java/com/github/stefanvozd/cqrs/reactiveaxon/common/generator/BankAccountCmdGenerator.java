package com.github.stefanvozd.cqrs.reactiveaxon.common.generator;


import com.github.stefanvozd.cqrs.reactiveaxon.common.api.*;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.fluttercode.datafactory.impl.DataFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Please note that this class is _not_ threadsafe. Threads are expected to have their own instance.
 */
public class BankAccountCmdGenerator {

    private static final String WORDLIST = "google-10000-english-usa-no-swears-long.txt";

    private final UUID[] ids;
    private final Random random;
    private final DataFactory dataFactory;
    private final String[] wordList;
    private final BigDecimal ONE_CENT = new BigDecimal("0.01");

    public BankAccountCmdGenerator(int idPoolSize) {
        ids = new UUID[idPoolSize];
        random = new Random();
        dataFactory = new DataFactory();
        dataFactory.randomize(random.nextInt());
        wordList = readWords();
    }

    public Object next() {
        val index = random.nextInt(ids.length);
        var id = ids[index];
        if(id == null) {
            id = UUID.randomUUID();
            ids[index] = id;
            return new OpenAccountCmd(id, new AccountHolder(
                    dataFactory.getFirstName(),
                    dataFactory.getLastName(),
                    LocalDate.now().minusYears(18).minusDays(random.nextInt(365*60))));
        } else {
            val dice = random.nextFloat();
            if(dice < 0.6f) {
                return new CreditAccountCmd(id,
                                            new BigDecimal(random.nextFloat() * 100)
                                .setScale(2, RoundingMode.HALF_UP)
                                .add(ONE_CENT),
                                            randomText());
            } else if(dice < 0.8f) {
                return new DebitAccountCmd(id,
                                           new BigDecimal(random.nextFloat() * 100)
                                .setScale(2, RoundingMode.HALF_UP)
                                .add(ONE_CENT),
                                           randomText());
            } else {
                ids[index] = null;
                return new CloseAccountCmd(id,
                                           AccountCloseReason.values()[random.nextInt(AccountCloseReason.values().length)]);
            }
        }
    }

    @SneakyThrows
    private String[] readWords() {
        val list = new ArrayList<String>(10000);
        val cl = Thread.currentThread().getContextClassLoader();
        try(val reader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(WORDLIST)))) {
            String line;
            while((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        val array = new String[list.size()];
        return list.toArray(array);
    }

    /* Something like this is supposed to be in Datafactory, but sadly doesn't work at all
    so we implemented ourselves. */
    private String randomText() {
        int wordCount = 1 + random.nextInt(4);
        List<String> words = new ArrayList<>(wordCount);
        for(int i = 0; i < wordCount; i++) {
            words.add(wordList[random.nextInt(wordList.length)]);
        }
        return String.join(" ", words);
    }

}
