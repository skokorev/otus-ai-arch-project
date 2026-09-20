package ru.yahoondex.archhelper.recommendations.ai.tools;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Content;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
public class LangfuseObservationFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        if (!(context instanceof ChatModelObservationContext chatModelObservationContext)) {
            return context;
        }
        chatModelObservationContext.addHighCardinalityKeyValue(KeyValue.of("gen_ai.prompt", prompt(chatModelObservationContext)));
        chatModelObservationContext.addHighCardinalityKeyValue(KeyValue.of("gen_ai.completion", completion(chatModelObservationContext)));
        return chatModelObservationContext;
    }

    private String prompt(ChatModelObservationContext context) {
        return ofNullable(context.getRequest())
                .map(Prompt::getInstructions)
                .orElse(List.of())
                .stream()
                .map(Content::getText)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining("\n"));
    }

    private String completion(ChatModelObservationContext context) {
        return ofNullable(context.getResponse())
                .map(ChatResponse::getResults)
                .orElse(List.of())
                .stream()
                .filter(generation -> generation.getOutput() != null)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining("\n"));
    }
}
