package com.quest.lucene.web.service;

import com.quest.lucene.search.index.IndexPersist;
import com.quest.lucene.web.common.Constants;
import com.quest.lucene.web.common.utils.TikaUtil;
import com.quest.lucene.web.entity.FileInfo;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Quest on 2018/5/28.
 */
public class FileSearchService {
    /**
     * 文件索引持久化
     */
    public void fileIndex(String path) {
        listFile(new File(path));
    }

    /**
     * 递归遍历文件夹下面所有文件
     */
    private void listFile(File file){
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                listFile(f);
            }else if(f.isFile()){
                try {
                    FileInfo info = TikaUtil.readFile(f);
                    IndexPersist persist = new IndexPersist();
                    persist.persistFile(new File(Constants.FILE_INDEX_PATH).toPath(), info);
                    continue;
                } catch (IOException | TikaException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
