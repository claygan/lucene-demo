package com.quest.lucene.web.common.utils;

import com.quest.lucene.web.entity.FileInfo;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Quest on 2018/5/29.
 */
public class TikaUtil {
    public static FileInfo readFile(File file) throws IOException, TikaException {
        FileInfo info = new FileInfo();
        info.setName(file.getName().substring(0, (file.getName().lastIndexOf("."))));
        info.setType(new String[]{getMimeType(file)});
        info.setCreateTime(file.lastModified());
        info.setIntro(getContent(file).substring(0, 50) + "...");
        info.setDownUrl(file.getCanonicalPath());
        info.setTotalPage(1);
        info.setfId(String.valueOf(file.hashCode()));
        return info;
    }


    /**
     * 获取文件类型
     */
    private static String getMimeType(File file) {
        if (file.isDirectory()) {
            return "the target is a directory";
        }
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<MediaType, Parser>());

        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());

        InputStream stream;
        try {
            stream = new FileInputStream(file);
            parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
            stream.close();
        } catch (TikaException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return PropertiesUtil.getMimetypeValue(metadata.get(HttpHeaders.CONTENT_TYPE));
    }

    /**
     * 获取文件内容
     */
    private static String getContent(File file) throws IOException, TikaException {
        Tika tika = new Tika();
        return tika.parseToString(file);
    }
    public static void main(String[] args) throws IOException {
        File file = new File("F:\\Desktop\\Alison.xlsx");
        System.out.println(file.getName().substring(0, (file.getName().lastIndexOf("."))));
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getPath());
    }
}
