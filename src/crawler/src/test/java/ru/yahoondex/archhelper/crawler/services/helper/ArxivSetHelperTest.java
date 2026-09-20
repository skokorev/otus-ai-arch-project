package ru.yahoondex.archhelper.crawler.services.helper;

import org.junit.jupiter.api.Test;
import ru.yahoondex.archhelper.crawler.repositories.dao.NameSet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArxivSetHelperTest {
    @Test
    public void shouldParseXmlSets() throws IOException {
        String setsXml = Files.readString(Path.of("src", "test", "resources", "oai-sets.xml"), StandardCharsets.UTF_8);
        List<NameSet> nameSets = ArxivSetHelper.getNameSets(setsXml);
        assertThat(nameSets).hasSize(183);
        NameSet firstSet = nameSets.get(0);
        assertThat(firstSet).isNotNull();
        assertThat(firstSet.getId()).isEqualTo("physics");
        assertThat(firstSet.getName()).isEqualTo("Physics");
    }
}
