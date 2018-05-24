package com.quest.lucene.demo.test.index;

import com.quest.lucene.demo.common.Constants;
import com.quest.lucene.demo.test.analizer.ik.IKAnalyer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.dic.Dictionary;

import java.io.File;
import java.io.IOException;

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

            //
//            doc.add(new StoredField("playerNo", "23"));
//            doc.add(new TextField("name", "詹姆斯",Field.Store.YES));
//            doc.add(new StringField("position","小前锋", Field.Store.YES));

            String content = "Lucene is a powerable tools";
            FieldType fieldType = new FieldType();
            fieldType.setStored(true);
            fieldType.setTokenized(false);
            fieldType.setOmitNorms(true);
            fieldType.setIndexOptions(IndexOptions.DOCS);

            //词项向量，前提是词项加入反向索引
            fieldType.setStoreTermVectors(true);
            fieldType.setStoreTermVectorOffsets(true);
            fieldType.setStoreTermVectorPayloads(true);
            fieldType.setStoreTermVectorPositions(true);


            fieldType.freeze();
            doc.add(new Field("content",content,fieldType));






            writer.addDocument(doc);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
