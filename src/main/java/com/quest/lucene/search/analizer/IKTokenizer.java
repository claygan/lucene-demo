package com.quest.lucene.search.analizer;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;

/**
 * Created by Quest on 2018/5/23.
 */
public class IKTokenizer extends Tokenizer {
    //ik实现类
    private IKSegmenter segmenter;
    //词元文本属性
    private final CharTermAttribute termAttr;
    //词元偏移量属性
    private final OffsetAttribute offsetAttr;
    //词元类型属性
    private final TypeAttribute typeAttr;
    //结束位
    private int endPosition;

    public IKTokenizer(boolean useSmart) {
        super();
        this.termAttr = addAttribute(CharTermAttribute.class);
        this.offsetAttr = addAttribute(OffsetAttribute.class);
        this.typeAttr = addAttribute(TypeAttribute.class);
        segmenter = new IKSegmenter(input, useSmart);
    }

    @Override
    public boolean incrementToken() throws IOException {
        //清除所有词元
        clearAttributes();
        Lexeme nextLexeme = segmenter.next();
        if (nextLexeme != null) {
            //将nextLexeme转换成attribute
            //设置文本
            termAttr.append(nextLexeme.getLexemeText());
            //设置长度
            termAttr.setLength(nextLexeme.getLength());
            //设置偏移量
            offsetAttr.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            //设置属性
            typeAttr.setType(nextLexeme.getLexemeTypeString());

            endPosition = nextLexeme.getEndPosition();
            //返回true，告知还有下个词元
            return true;
        }
        // 返会false告知词元输出完毕
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        segmenter.reset(input);
    }

    @Override
    public void end() throws IOException {
        int endOffset = correctOffset(this.endPosition);
        offsetAttr.setOffset(endOffset, endOffset);
    }
}
