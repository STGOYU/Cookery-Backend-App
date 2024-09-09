
package com.cookerytech.service;

import com.cookerytech.domain.Currency;
import com.cookerytech.dto.CurrencyDTO;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.CurrencyMapper;
import com.cookerytech.repository.CurrencyRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final CurrencyService currencyService;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper, @Lazy CurrencyService currencyService) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.currencyService = currencyService;
    }


    @Scheduled(fixedRate = 60000) // Her 1 dakikada bir çalıştırılacak(60000 milisaniye)
    //@Scheduled(cron = "0 0 8 * * ?") // Her sabah saat 8'de çalıştırır (0)saniye (0)dakika (8)saat (*)hergun (*)heray (?)herhangi bir gun
    public void updateCurrencies() {
        currencyService.getCurrenciesPage(null);
    }

    public Page<CurrencyDTO> getCurrenciesPage(Pageable pageable) {

        Page<CurrencyDTO> currencyDTOPage = null;

        try {
            Document doc = Jsoup.connect("https://www.tcmb.gov.tr/kurlar/today.xml").get();
            Elements currencies = doc.select("Currency");

            Double price = 0.0;
            Double value = 0.0;

            Currency currencyUSD = getCurrency("USD");
            Currency currencyTRY = getCurrency("TRY");
            Currency currencyEUR = getCurrency("EUR");

            for (Element element : currencies) {
                String kod = element.attr("Kod");
                if (kod.equals("USD")) {
                    price = Double.valueOf(element.select("ForexBuying").text());//
                    currencyUSD.setCode("USD");
                    currencyUSD.setSymbol("$");
                    currencyUSD.setUpdateAt(LocalDateTime.now());
                    value=price;
                    currencyUSD.setValue(value/price);
                    currencyTRY.setCode("TRY");
                    currencyTRY.setSymbol("₺");
                    currencyTRY.setUpdateAt(LocalDateTime.now());
                    currencyTRY.setValue(value);

                    currencyRepository.save(currencyUSD);
                    currencyRepository.save(currencyTRY);

                }else if (kod.equals("EUR")){
                    price = Double.valueOf(element.select("ForexSelling").text());
                    currencyEUR.setCode("EUR");
                    currencyEUR.setSymbol("€");
                    currencyEUR.setUpdateAt(LocalDateTime.now());
                    currencyEUR.setValue(value/price);
                    currencyRepository.save(currencyEUR);
                }

            }
           Page<Currency> currencyPage = currencyRepository.findAll(pageable);

           currencyDTOPage = currencyMapper.currencyPageToCurrencyDTOPage(currencyPage);

            return currencyDTOPage;

        } catch (Exception e) {
            System.out.println("Siteye ulaşılamadı, lütfen internet bağlantınızı kontrol edin (Döviz): " + e.getMessage());
        }
        return currencyDTOPage;
    }

    public Page<CurrencyDTO> getCurrenciesPages(Pageable pageable) {
        Page<CurrencyDTO> currencyDTOPage = null;

        try {

            Page<Currency> currencyPage = currencyRepository.findAll(pageable);

            currencyDTOPage = currencyMapper.currencyPageToCurrencyDTOPage(currencyPage);

            return currencyDTOPage;

        } catch (Exception e) {
            System.out.println("Siteye ulaşılamadı, lütfen internet bağlantınızı kontrol edin (Döviz)");
        }
        return currencyDTOPage;
    }

    public Currency getCurrency(String code) {
        return currencyRepository.findByCode(code).orElseGet(() -> {
            Currency newCurrency = new Currency();
            return newCurrency;
        });
    }

    public Currency getCurrencyById(Long id) {
        return currencyRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id))
        );
    }
}
