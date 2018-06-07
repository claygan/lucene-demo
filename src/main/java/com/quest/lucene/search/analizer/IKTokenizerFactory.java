package com.quest.lucene.search.analizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

/**
 * Created by Quest on 2018/6/7.
 */
public class IKTokenizerFactory extends TokenizerFactory {
    private static boolean useSmart = false;

    public IKTokenizerFactory(Map<String, String> args) {
        super(args);
        if ("true".equals(args.get("useSmart"))) {
            useSmart = true;
        }
    }

    @Override
    public Tokenizer create(AttributeFactory attributeFactory) {
        return new IKTokenizer(useSmart);
    }
}
