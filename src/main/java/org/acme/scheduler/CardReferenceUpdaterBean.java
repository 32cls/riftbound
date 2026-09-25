package org.acme.scheduler;

import java.util.stream.Stream;

import org.acme.CardReference;
import org.acme.dto.CardReferenceDto;
import org.acme.dto.CardRequestDto;
import org.acme.rest.CardReferenceService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CardReferenceUpdaterBean {

    @RestClient
    CardReferenceService cardReferenceService;

    @Scheduled(cron="0 0/10 * * * ?")
    @Transactional
    void cronJob(ScheduledExecution execution) {
        System.out.println("10 minutes");
        if (CardReference.count() > 0) {
            return;
        }
        int pageCounter = 1;
        CardRequestDto cardRequestDto = cardReferenceService.getBatchCards(100, pageCounter);
        persistRequestResult(cardRequestDto);
        while (pageCounter < cardRequestDto.pages) {
            CardRequestDto newRequest = cardReferenceService.getBatchCards(100, ++pageCounter);
            persistRequestResult(newRequest);
        }
    }

    private void persistRequestResult(CardRequestDto cardRequestDto){
        Stream<CardReference> cardReferences = cardRequestDto.items.stream().map((CardReferenceDto cardRefDto) -> {
            return CardReference.fromCardReferenceDto(cardRefDto);
        });
        CardReference.persist(cardReferences);
    }

}
