package com.hermes.lotcenter.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@Component
public class FileOptDomain {

    public boolean writeLotFile(String content, String lotCode, String startDate) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        if (StringUtils.isBlank(lotCode)) {
            return false;
        }
        if (StringUtils.isBlank(startDate)) {
            return false;
        }
        String lotCodeDir = getLotCodePath(lotCode);
        File lotCodeDirFile = new File(lotCodeDir);
        if (!lotCodeDirFile.exists()) {
            lotCodeDirFile.mkdirs();
        }
        String fileHtmlName = getLotFilePath(lotCode, startDate);
        FileOutputStream fos = null;
        boolean flag = true;
        try {
            fos = new FileOutputStream(fileHtmlName);
            byte[] bytes = content.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException e) {
            flag = false;
            e.printStackTrace();
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            return flag;
        }
    }

    public String readLotFile(String lotCode, String startDate) {
        if (StringUtils.isBlank(lotCode)) {
            return null;
        }
        if (StringUtils.isBlank(startDate)) {
            return null;
        }
        String lotFilePathName = getLotFilePath(lotCode, startDate);
        File lotFile = new File(lotFilePathName);
        if (!lotFile.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        try {

            FileInputStream fis = new FileInputStream(lotFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String lineStr = null;
            while ((lineStr = br.readLine()) != null) {
                sb.append(lineStr);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getLotCodePath(String lotCode) {
        if (StringUtils.isBlank(lotCode)) {
            return null;
        }

        String lotCodePath = LotteryConfig.BASE_PATH + "/" + lotCode;
        return lotCodePath;
    }

    private String getLotFilePath(String lotCode, String startDate) {
        if (StringUtils.isBlank(lotCode)) {
            return null;
        }
        if (StringUtils.isBlank(startDate)) {
            return null;
        }
        String fileFilePath = getLotCodePath(lotCode) + "/" + startDate + ".html";
        return fileFilePath;
    }

}
