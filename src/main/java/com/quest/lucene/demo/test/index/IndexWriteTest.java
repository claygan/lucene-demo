package com.quest.lucene.demo.test.index;

import com.quest.lucene.demo.common.Constants;
import com.quest.lucene.demo.test.analizer.ik.IKAnalyer;
import com.quest.lucene.demo.test.entity.ProInfo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

/**
 * Created by Quest on 2018/5/24.
 */
public class IndexWriteTest {
    public static void main(String[] args) throws IOException {
        //
        Analyzer analyer = new IKAnalyer(true);
        IndexWriterConfig config = new IndexWriterConfig(analyer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try(//
            Directory directory = FSDirectory.open((new File(Constants.DIRECTORY_FILE_PATH)).toPath());
            //
            IndexWriter writer = new IndexWriter(directory, config);){

            //
            Document doc = new Document();

            //模拟一个商城内容索引入库
            ProInfo proInfo = new ProInfo();
            proInfo.setProId("a00002");
            proInfo.setName("佳能（Canon）EOS 5D Mark IV 单反套机（EF 24-105mm f/4L IS II USM） 全画幅 3040万像素 61点对焦");
            proInfo.setIntro("品牌： 佳能（Canon）\n" +
                    "商品名称：佳能EOS 5D Mark IV 单反套机（EF 24-105mm f/4L IS USM）商品编号：3678501商品毛重：3.37kg商品产地：日本用途：人物摄影，风光摄影，高清拍摄，运动摄影，静物摄影，其他画幅：全画幅分类：专业套头：单镜头套机像素：3000-4999万");
            proInfo.setImageUrl("https://img11.360buyimg.com/n5/s54x54_jfs/t3871/250/209359195/162675/82a25e48/5844d591N39052a68.jpg");
            proInfo.setPrice(23999.00);
            proInfo.setShopName("佳能影像官方旗舰店");
            proInfo.setType(new String[]{"单反","Canon","佳能"});

            //1.商品编号-不索引，存储，字符串
            doc.add(new StringField("proId", proInfo.getProId(), Field.Store.YES));
            //2.商品名称-分词索引（词频、位置、偏移量），存储，字符串
            FieldType fieldType = new FieldType();
            fieldType.setStored(true);
            fieldType.setTokenized(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            fieldType.freeze();
            doc.add(new Field("name",proInfo.getName(),fieldType));
            //3.商品简介-分词索引（无需支持短语，临近查询），存储，字符串，文本高亮
            FieldType fieldType1 = new FieldType();
            fieldType1.setStored(true);
            fieldType1.setTokenized(true);
            fieldType1.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
            fieldType1.setStoreTermVectors(true);
            fieldType1.setStoreTermVectorOffsets(true);
            fieldType1.setStoreTermVectorPayloads(true);
            fieldType1.setStoreTermVectorPositions(true);
            fieldType1.freeze();
            doc.add(new Field("intro",proInfo.getIntro(),fieldType1));
            //4.商品图片-不索引，存储
            doc.add(new StoredField("imageUrl", proInfo.getImageUrl()));
            //5.商品分类-索引，不存储，分类统计查询，多值
            FieldType vectorType = new FieldType();
            vectorType.setTokenized(false);
            vectorType.setIndexOptions(IndexOptions.DOCS);
            vectorType.setDocValuesType(DocValuesType.SORTED_SET);
            vectorType.freeze();
            for (int i = 0; i < proInfo.getType().length; i++) {
                doc.add(new Field("type",proInfo.getType()[i],vectorType){
                    @Override
                    public BytesRef binaryValue() {
                        return new BytesRef((String)this.fieldsData);
                    }
                });
            }
            //6.商品价格-索引，存储，排序，数值
            FieldType priceType = new FieldType();
            priceType.setStored(true);
            priceType.setTokenized(false);
            priceType.setIndexOptions(IndexOptions.DOCS);
            priceType.setDocValuesType(DocValuesType.NUMERIC);
            priceType.setDimensions(1, Double.BYTES);
            priceType.freeze();
            doc.add(new DoubleField("price", proInfo.getPrice(), priceType));
            //7.商品上架时间-不索引，排序，数值
            long createTime = System.currentTimeMillis();
            doc.add(new NumericDocValuesField("createTime", createTime));
            //8.商家名称-索引，存储，分类查询
            doc.add(new StringField("shop",proInfo.getShopName(), Field.Store.YES));
            doc.add(new SortedDocValuesField("shop", new BytesRef(proInfo.getShopName())));

            writer.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static class DoubleField extends Field{
        public DoubleField(String name, double value, FieldType type) {
            super(name, type);
            this.fieldsData = Double.valueOf(value);
        }

        @Override
        public Number numericValue() {
            return (Double)this.fieldsData;
        }
    }

}
