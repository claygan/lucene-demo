package com.quest.lucene.web.controllers;

import com.quest.lucene.search.search.IndexSearch;
import com.quest.lucene.web.common.Constants;
import com.quest.lucene.web.entity.FileInfo;
import com.quest.lucene.web.entity.PageInfo;
import com.quest.lucene.web.entity.QueryInfo;
import com.quest.lucene.web.service.FileSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import sun.reflect.FieldInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;
import static thredds.featurecollection.FeatureCollectionConfig.PartitionType.file;
import static ucar.nc2.constants.ACDD.keywords;


/**
 * Created by Quest on 2018/5/28.
 */
@Controller
@RequestMapping("file")
public class FileSearchController {
    @Autowired
    private IndexSearch indexSearch;
    @Autowired
    private FileSearchService fileSearchService;

    @RequestMapping("index")
    public String toIndex(Model model) {
        model.addAttribute("path", Constants.FILE_PATH);
        return "index";
    }

    @RequestMapping("search")
    @ResponseBody
    public PageInfo<FileInfo> doSeacher(HttpServletRequest request, String keywords) throws IOException, ParseException {
        Path path = (new File(Constants.FILE_INDEX_PATH)).toPath();
        System.out.println("keywords:" + request.getParameter("keywords"));
        String keyword = new String(request.getParameter("keywords").getBytes("utf-8"),"utf-8");
        PageInfo<FileInfo> pageInfo = indexSearch.findList(path, new QueryInfo(keyword));
        return pageInfo;
    }
    @RequestMapping("store")
    @ResponseBody
    public Map doStore(String path){
        Map result = new HashMap();
        if (StringUtils.isBlank(path)) {
            path = Constants.FILE_PATH;
        }
        try {
            fileSearchService.fileIndex(path);
            result.put("code", "200");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "500");
        }
        return result;
    }
}
