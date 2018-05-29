package com.quest.lucene.search.search;

import com.quest.lucene.search.analizer.IKAnalyzer;
import com.quest.lucene.web.entity.FileInfo;
import com.quest.lucene.web.entity.PageInfo;
import com.quest.lucene.web.entity.QueryInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quest on 2018/5/28.
 */
public class IndexSearch {
    private IKAnalyzer analyzer = new IKAnalyzer(true);

    public PageInfo<FileInfo> findList(Path path, QueryInfo queryInfo) throws IOException, ParseException {
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(path));) {

            IndexSearcher searcher = new IndexSearcher(reader);
            PageInfo<FileInfo> infoList = (PageInfo<FileInfo>) doSearch(queryInfo, searcher, FileInfo.class);
            return infoList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PageInfo<?> doSearch(QueryInfo queryInfo, IndexSearcher searcher, Class<?> cls) throws NoSuchFieldException, ParseException {
        try {
            PageInfo pageInfo = new PageInfo();
            List list = new ArrayList();
            if (StringUtils.isNotBlank(queryInfo.getKeyword())) {
                Query query = (new QueryParser("name", analyzer)).parse(queryInfo.getKeyword());
                TopDocs topDocs = searcher.search(query, queryInfo.getPageSize());
                pageInfo.setTotal(topDocs.totalHits);

                for (ScoreDoc sdoc : topDocs.scoreDocs) {
                    Object obj = cls.newInstance();
                    // 根据文档id取存储的文档
                    Document hitDoc = searcher.doc(sdoc.doc);
                    cls.getDeclaredField("doc").set(obj,sdoc.doc);
                    cls.getDeclaredField("score").set(obj,sdoc.score);
                    for(Field field: cls.getDeclaredFields()){
                        field.setAccessible(true);
                        if(hitDoc.get(field.getName()) != null){
                            cls.getDeclaredField(field.getName()).set(obj,hitDoc.get(field.getName()));
                        }
                    }
                    list.add(obj);
                }
            }
            pageInfo.setData(list);

            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
