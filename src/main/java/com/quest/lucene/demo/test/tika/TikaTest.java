package com.quest.lucene.demo.test.tika;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Quest on 2018/5/25.
 */
public class TikaTest {
    public static void main(String[] args) throws IOException, TikaException {
        Tika tika = new Tika();
        System.out.println(tika.parseToString(new File("F:\\gsm\\Alison.xlsx")));
    }
}
