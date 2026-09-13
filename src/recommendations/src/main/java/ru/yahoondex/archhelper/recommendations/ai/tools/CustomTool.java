package ru.yahoondex.archhelper.recommendations.ai.tools;

@Component
public class CustomTool {

    @Tool(description="Получить собранные теги")
    public String[] getCollectedTags() {
        return ["Computer vision", "Vector search", "Artificial Intelligence", "Search process", "Nearest neighbor search"];
    }
}