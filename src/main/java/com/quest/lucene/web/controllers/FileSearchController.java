package com.quest.lucene.web.controllers;

import com.quest.lucene.search.search.IndexSearch;
import com.quest.lucene.web.common.Constants;
import com.quest.lucene.web.entity.FileInfo;
import com.quest.lucene.web.entity.PageInfo;
import com.quest.lucene.web.entity.QueryInfo;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.reflect.FieldInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static thredds.featurecollection.FeatureCollectionConfig.PartitionType.file;


/**
 * Created by Quest on 2018/5/28.
 */
@Controller
@RequestMapping("file")
public class FileSearchController {
    @Autowired
    private IndexSearch indexSearch;

    @RequestMapping("index")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("search")
    @ResponseBody
    public PageInfo<FileInfo> doSeacher(String keywords) throws IOException, ParseException {
        Path path = (new File(Constants.FILE_INDEX_PATH)).toPath();
        PageInfo<FileInfo> pageInfo = indexSearch.findList(path, new QueryInfo(keywords));
        return pageInfo;
    }
    @RequestMapping("store")
    @ResponseBody
    public void doStore(){

    }
}
