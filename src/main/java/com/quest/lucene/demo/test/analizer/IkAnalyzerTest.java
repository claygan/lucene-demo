package com.quest.lucene.demo.test.analizer;

import com.quest.lucene.demo.test.analizer.ik.IKAnalyer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by Quest on 2018/5/23.
 */
public class IkAnalyzerTest {
    public static void main(String[] args) throws IOException {
        String str = "市面上还有一些其他的分词器，那在 IK 有什么优势和特点";
        try (IKAnalyer ik = new IKAnalyer(true)) {
            TokenStream ts = ik.tokenStream("content", str);
            doToken(ts);
        }
    }

    private static void doToken(TokenStream ts) throws IOException {
        ts.reset();
        CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
        while (ts.incrementToken()) {
            System.out.print(cta.toString()+"|");
        }
        System.out.println();
        ts.end();
        ts.close();
    }
}
