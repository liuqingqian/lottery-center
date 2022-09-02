package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.DownloadLotDomain;
import com.hermes.lotcenter.domain.FileOptDomain;
import com.hermes.lotcenter.domain.LotteryConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileOptDomainTest {

    @Autowired
    private DownloadLotDomain downloadLotDomain;

    @Autowired
    private FileOptDomain fileOptDomain;

    @Test
    public void testWriteFile() {
        String url = LotteryConfig.RESULT_85_URL;
        Map<String, Object> params = new HashMap<>();
        String lotCode = "FFK3";
        String startDate = "2022-09-02";
        params.put("lotCode", lotCode);
        params.put("startDate", startDate);
        String rawContent = downloadLotDomain.getHtmlRawContent(url, params);
        Assert.assertNotNull("获取开奖结果页面内容为空", rawContent);
        boolean writeLotFile = fileOptDomain.writeLotFile(rawContent, lotCode, startDate);
        Assert.assertTrue("写入开奖结果文件失败", writeLotFile);
    }


    @Test
    public void testReadLotFile() {
        String lotCode = "FFK3";
        String startDate = "2022-09-02";
        String rawContent = fileOptDomain.readLotFile(lotCode, startDate);
        Assert.assertNotNull("读取开奖结果文件内容为空", rawContent);
    }

}
