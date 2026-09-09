import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.acme.CardReference;
import org.acme.dto.CardReferenceDto;
import org.acme.dto.CardRequestDto;
import org.acme.rest.CardReferenceService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardReferenceUpdaterBean {

    @RestClient 
    CardReferenceService cardReferenceService;
    
    @Scheduled(cron="0 0 3 * * ?")
    void cronJob(ScheduledExecution execution) {
        int pageCounter = 1;
        CardRequestDto cardRequestDto = cardReferenceService.getBatchCards(100, pageCounter);
        do {
            CardRequestDto cardRequestDto = cardReferenceService.getBatchCards(100, pageCounter++);
        } while (pageCounter < cardRequestDto.pages);
        List<CardReference> cardReferences = cardRefDtos.stream().map((CardReferenceDto cardRefDto) -> {
            return CardReference.fromCardReferenceDto(cardRefDto);
        }).collect(Collectors.toList());
    }

}
