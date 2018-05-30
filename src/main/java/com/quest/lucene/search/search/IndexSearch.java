package com.quest.lucene.search.search;

import com.quest.lucene.search.analizer.IKAnalyzer;
import com.quest.lucene.web.common.Constants;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ucar.nc2.constants.ACDD.keywords;

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
                System.out.println("query:" + query.toString());
                TopDocs topDocs = searcher.search(query, queryInfo.getPageSize());
                pageInfo.setTotal(topDocs.totalHits);
                System.out.println("totle:"+topDocs.totalHits);

                for (ScoreDoc sdoc : topDocs.scoreDocs) {
                    Object obj = cls.newInstance();
                    // 根据文档id取存储的文档
                    Document hitDoc = searcher.doc(sdoc.doc);
                    Field doc = cls.getSuperclass().getDeclaredField("doc");
                    doc.setAccessible(true);
                    doc.set(obj,sdoc.doc);
                    Field score = cls.getSuperclass().getDeclaredField("score");
                    score.setAccessible(true);
                    score.set(obj,sdoc.score);
                    for(Field field: cls.getDeclaredFields()){
                        field.setAccessible(true);
                        if(hitDoc.get(field.getName()) != null){
                            Field f = cls.getDeclaredField(field.getName());
                            f.setAccessible(true);
                            f.set(obj, hitDoc.get(field.getName()));
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

    public static void main(String[] args) throws IOException, ParseException {
        IndexSearch search = new IndexSearch();
        PageInfo<FileInfo> info = search.findList((new File(Constants.FILE_INDEX_PATH)).toPath(), new QueryInfo("详解"));

    }
}
