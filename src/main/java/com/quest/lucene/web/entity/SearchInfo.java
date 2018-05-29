package com.quest.lucene.web.entity;

import java.io.Serializable;

/**
 * Created by Quest on 2018/5/28.
 */
public class SearchInfo implements Serializable {
    private static final long serialVersionUID = -451079136023709080L;
    private float score;
    private int doc;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getDoc() {
        return doc;
    }

    public void setDoc(int doc) {
        this.doc = doc;
    }
}
