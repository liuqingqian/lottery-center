package com.hermes.lotcenter;

import com.hermes.lotcenter.infrastructure.utils.NetUtil2;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuqingqian on 2022/8/28.
 */
public class LotteryUtil {
    public static void main(String[] args) throws IOException {
        //TODO:1、批量获取html文件存储；3、转换成DTO->Entity入库

        String url = "http://8585055.cc/lotteryV2/resultList.do";
        Map<String, Object> params = new HashMap<>();
        String lotCode = "AZK3";
        String startDate = "2022-08-23";
        params.put("lotCode", lotCode);
        params.put("startDate", startDate);
        NetUtil2.Response res = NetUtil2.get(url, params);
//        System.out.println(res.rawContent);
//        params.put("lotCode", "FFK3");
        String baseDir = LotteryUtil.class.getResource("/").getPath() + "material/lottery/";

        String lotCodeDir = baseDir + lotCode;
        File lotCodeDirFile = new File(lotCodeDir);
        if (!lotCodeDirFile.exists()) {
            lotCodeDirFile.mkdirs();
        }

        String fileHtmlName = lotCodeDir + "/" + startDate + ".html";
        FileOutputStream fos = new FileOutputStream(fileHtmlName);
        byte[] bytes = res.rawContent.getBytes();
        fos.write(bytes);
        fos.close();

        //2、解析html表单字段；
        Parser mParser = null;
        NodeList nodeList = null;
        mParser = Parser.createParser(res.rawContent, "utf-8");
        NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
        OrFilter lastFilter = new OrFilter();
        lastFilter.setPredicates(new NodeFilter[]{tableFilter});
        try {
            nodeList = mParser.parse(lastFilter);
            //循环读取每一个Table
            for (int i = 0; i <= nodeList.size(); i++) {
                if (nodeList.elementAt(i) instanceof TableTag) {
                    TableTag tag = (TableTag) nodeList.elementAt(i);
                    TableRow[] rows = tag.getRows();
                    System.out.println("---------------- table " + i + "--------------");
                    for (int j = 0; j < rows.length; j++) {
                        TableRow tr = rows[j];
                        TableColumn[] tds = tr.getColumns();
                        //读取每一个单元格
                        for (int k = 0; k < tds.length; k++) {
                            String tdTxt = tds[k].getStringText();
                            System.out.println(tdTxt);


                        }

                    }
                }

            }
        } catch (ParserException e) {
            e.printStackTrace();
        }

        System.out.println("lastFilter = " + lastFilter);

    }
}

//---------------- table 0--------------
//        20220824001
//        2022-08-24 00:00:00
//<i style="margin-left: 40%;" class="ball-2"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//<em class="smallBlueball">14</em>
//<em class="big">大</em>
//<em class="even">双</em>
//<i style="margin-left: 40%;" class="ball-2"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//        20220823480
//        2022-08-23 23:57:00
//<i style="margin-left: 40%;" class="ball-2"></i>
//<i style="margin-left: 40%;" class="ball-3"></i>
//<i style="margin-left: 40%;" class="ball-3"></i>
//<em class="smallBlueball">8</em>
//<em class="small">小</em>
//<em class="even">双</em>
//<i style="margin-left: 40%;" class="ball-2"></i>
//<i style="margin-left: 40%;" class="ball-3"></i>
//<i style="margin-left: 40%;" class="ball-3"></i>
