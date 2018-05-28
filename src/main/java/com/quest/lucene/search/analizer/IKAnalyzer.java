package com.quest.lucene.search.analizer;

import org.apache.lucene.analysis.Analyzer;

/**
 * Created by Quest on 2018/5/23.
 */
public class IKAnalyzer extends Analyzer {
    private boolean useSmart = false;

    public boolean isUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKAnalyzer() {
        this(false);
    }

    public IKAnalyzer(boolean useSmart) {
        this.useSmart = useSmart;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        IKTokenizer ik = new IKTokenizer(this.useSmart);
        return new TokenStreamComponents(ik);
    }
}
