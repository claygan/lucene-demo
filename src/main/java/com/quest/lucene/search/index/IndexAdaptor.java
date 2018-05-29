package com.quest.lucene.search.index;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;

/**
 * Created by Quest on 2018/5/28.
 */
public class IndexAdaptor {
    /**
     *  名称：分词索引(词频+位置)+存储
     */
    protected FieldType nameType(){
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        fieldType.setTokenized(true);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.freeze();
        return fieldType;
    }
    /**
     * 简介：分词索引+存储+结果高亮
     */
    protected FieldType introType(){
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        fieldType.setTokenized(true);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        //词项向量
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorOffsets(true);
        fieldType.setStoreTermVectorPayloads(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.freeze();
        return fieldType;
    }

    /**
     * 类型：不存储，不分词，索引，分类排序
     */
    protected FieldType categoryType(){
        FieldType fieldType = new FieldType();
        fieldType.setStored(false);
        fieldType.setTokenized(false);
        fieldType.setIndexOptions(IndexOptions.DOCS);
        fieldType.setDocValuesType(DocValuesType.SORTED_SET);
        fieldType.freeze();
        return fieldType;
    }
    /**
     * 价格：不索引，存储(数值类型)，排序
     */
    protected FieldType priceType(){
        FieldType priceType = new FieldType();
        priceType.setStored(true);
        priceType.setTokenized(false);
        priceType.setIndexOptions(IndexOptions.DOCS);
        priceType.setDocValuesType(DocValuesType.NUMERIC);
        priceType.setDimensions(1, Double.BYTES);
        priceType.freeze();
        return priceType;
    }
}
