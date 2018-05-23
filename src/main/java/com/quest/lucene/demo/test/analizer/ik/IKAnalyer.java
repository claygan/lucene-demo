package com.quest.lucene.demo.test.analizer.ik;

import org.apache.lucene.analysis.Analyzer;

/**
 * Created by Quest on 2018/5/23.
 */
public class IKAnalyer extends Analyzer {
    private boolean useSmart = false;

    public boolean isUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKAnalyer() {
        this(false);
    }

    public IKAnalyer(boolean useSmart) {
        this.useSmart = useSmart;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        IKTokenizer ik = new IKTokenizer(this.useSmart);
        return new TokenStreamComponents(ik);
    }
}
