package a311.college.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 敏感词过滤
 */
@Slf4j
public class FinderUtil {
    private final TrieNode root = new TrieNode();
    private boolean initialized = false;

    // 初始化DFA树
    public synchronized void initialize() {
        if (initialized) return;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sensitive/SensitiveLexicon.json")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(inputStream);

            // 提取所有敏感词（包括所有分类）
            Set<String> words = new HashSet<>();
            jsonNode.elements().forEachRemaining(node ->
                    node.elements().forEachRemaining(word ->
                            words.add(word.asText().toLowerCase())
                    )
            );

            // 构建DFA树
            words.forEach(this::addWord);
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sensitive words", e);
        }
    }

    // Trie节点结构
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd;
    }

    // 添加词语到DFA树
    private void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toLowerCase().toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEnd = true;
    }

    // 判断是否包含敏感词
    public boolean containsSensitiveWord(String text) {
        if (!initialized) initialize();
        if (text == null || text.isEmpty()) return false;

        String lowerText = text.toLowerCase();
        for (int i = 0; i < lowerText.length(); i++) {
            TrieNode node = root;
            for (int j = i; j < lowerText.length(); j++) {
                node = node.children.get(lowerText.charAt(j));
                if (node == null) break;
                if (node.isEnd) return true;
            }
        }
        return false;
    }
}
