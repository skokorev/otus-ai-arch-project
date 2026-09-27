package ru.yahoondex.archhelper.configurator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.configurator.repositories.NameSetRepository;
import ru.yahoondex.archhelper.configurator.services.dto.SetLite;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SetService {
    private final NameSetRepository setRepository;

    @Autowired
    public SetService(NameSetRepository setRepository) {
        this.setRepository = setRepository;
    }

    @Transactional
    public List<SetLite> getAllSets() {
        return setRepository.findAll().stream().map(ns -> new SetLite(ns.getId(), ns.getName())).collect(Collectors.toList());
    }
}
