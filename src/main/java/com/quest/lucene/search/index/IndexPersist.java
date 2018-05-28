package com.quest.lucene.search.index;

import com.quest.lucene.search.analizer.IKAnalyzer;
import com.quest.lucene.web.entity.FileInfo;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Quest on 2018/5/28.
 */
public class IndexPersist extends IndexAdaptor{
    /**
     * 持久化文件索引
     */
    public void persistFile(Path path, FileInfo info) throws IOException {
        IKAnalyzer analyzer = new IKAnalyzer(true);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try (Directory directory = FSDirectory.open(path);
             IndexWriter writer = new IndexWriter(directory, config);
        ) {
            Document doc = new Document();
            //1.id
            doc.add(new StringField("fId", info.getfId(), Field.Store.YES));
            //2.name
            doc.add(new Field("name", info.getName(), nameType()));
            //3.type
            for (int i = 0; i < info.getType().length; i++) {
                doc.add(new Field("type", info.getType()[i], categoryType()) {
                    @Override
                    public BytesRef binaryValue() {
                        return new BytesRef((String) this.fieldsData);
                    }
                });
            }
            //4.intro
            doc.add(new Field("intro", info.getIntro(), introType()));
            //5.createTime
            doc.add(new SortedNumericDocValuesField("createTime", System.currentTimeMillis()));
            //6.totalPage
            doc.add(new NumericDocValuesField("totalPage", info.getTotalPage()));
            //7.downUrl
            doc.add(new StoredField("downUrl", info.getDownUrl()));


            writer.addDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
