package com.quest.lucene.demo.test.search;

import com.quest.lucene.demo.common.Constants;
import com.quest.lucene.demo.test.analizer.ik.IKAnalyer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Quest on 2018/5/25.
 */
public class SearchTest {
    public static void main(String[] args) throws IOException, ParseException {
        Analyzer analyzer = new IKAnalyer(true);
        Directory directory = FSDirectory.open(new File(Constants.DIRECTORY_FILE_PATH).toPath());
        //索引读取器
        IndexReader reader = DirectoryReader.open(directory);
        //索引搜索器
        IndexSearcher searcher = new IndexSearcher(reader);


        //--------------------------------------------------------------------
//        QueryParser parser = new QueryParser("intro",analyzer);
//        Query query = parser.parse("品牌");
        Query query = DoublePoint.newRangeQuery("price", 0, 200000);
        //--------------------------------------------------------------------

        long start = System.currentTimeMillis();
        doSearch(query, searcher);
        System.out.println("耗时："+(System.currentTimeMillis()-start)+"ms");

        reader.close();
        directory.close();
    }

    private static void doSearch(Query query, IndexSearcher searcher) throws IOException {
        System.out.println("query：" + query.toString());
        TopDocs docs = searcher.search(query, 10);
        System.out.println("总命中数：" + docs.totalHits);
        for (ScoreDoc sdoc : docs.scoreDocs) {
            Document doc = searcher.doc(sdoc.doc);
            System.out.println("docId:" + sdoc.doc + ",score:" + sdoc.score);
            //文档
            System.out.println("proId：" + doc.get("proId"));
            System.out.println("name：" + doc.get("name"));
            System.out.println("intro：" + doc.get("intro"));
            System.out.println("price：" + doc.get("price"));
            System.out.println("shopName：" + doc.get("shop"));
            System.out.println("type：" + doc.get("type"));
            System.out.println("createTime：" + doc.get("createTime"));
            System.out.println("------------------------------------------------------------------------");
        }
    }
}
