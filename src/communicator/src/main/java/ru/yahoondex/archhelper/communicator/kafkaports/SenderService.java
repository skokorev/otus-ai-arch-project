package ru.yahoondex.archhelper.communicator.kafkaports;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.yahoondex.archhelper.commons.contracts.kafka.RecommendationListDto;
import ru.yahoondex.archhelper.communicator.repositories.NameSetRepository;
import ru.yahoondex.archhelper.communicator.repositories.UsersRepository;
import ru.yahoondex.archhelper.communicator.repositories.dao.NameSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class SenderService {

    private final UsersRepository usersRepository;
    private final NameSetRepository nameSetRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    @Value("${communicator.mail-theme}")
    private String mailTheme;

    @Autowired
    public SenderService(UsersRepository usersRepository,
                         NameSetRepository nameSetRepository,
                         JavaMailSender mailSender,
                         SpringTemplateEngine thymeleafTemplateEngine) {
        this.usersRepository = usersRepository;
        this.nameSetRepository = nameSetRepository;
        this.mailSender = mailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    record Recommendation(String url, String title) {}
    record RecommendationSet(String name, List<Recommendation> recs) {}

    @KafkaListener(topics = {"recommendation-topic"},
            groupId = "mail-sender",
            containerFactory = "recommendationListenerContainerFactory"
    )
    @Transactional
    public void sendRecommendations(RecommendationListDto recommendation) {
        log.info("Recommendation: {}", recommendation);
        List<RecommendationSet> recommendationSets = new LinkedList<>();

        recommendation.getRecommendations().forEach((setId, recommendationsList) -> {
            List<Recommendation> recs = new LinkedList<>();
            String setName = nameSetRepository.findById(setId).map(NameSet::getName).orElse("Имя не найдено");
            recommendationsList.forEach(rec -> {
                String pdfUrl = String.format("https://arxiv.org/abs/%s", rec.getArticleId());
                recs.add(new Recommendation(pdfUrl, rec.getArticleTitle()));
            });
            recommendationSets.add(new RecommendationSet(setName, recs));
        });
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("sets", recommendationSets);
        final String mailText = thymeleafTemplateEngine.process("recommendation.html", thymeleafContext);

        final MimeMessage[] messages = usersRepository.findAllByGroupId(recommendation.getGroupId()).stream().map(user -> {
            MimeMessage message;
            try {
                message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper((MimeMessage) message, true, "UTF-8");
                //message.setFrom("noreply@yahoondex.ru");
                helper.setTo(user.getEmail());
                helper.setSubject(mailTheme);
                helper.setText(mailText, true);
                return message;
            } catch (MessagingException me) {
                log.error("Unexpected error while creating message", me);
            }
            return null;
        }).filter(Objects::nonNull).toArray(MimeMessage[]::new);

        mailSender.send(messages);
    }
}
