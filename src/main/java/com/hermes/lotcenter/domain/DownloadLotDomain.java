package com.hermes.lotcenter.domain;

import com.hermes.lotcenter.infrastructure.utils.NetUtil2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@Component
public class DownloadLotDomain {

    public String getHtmlRawContent(String url, Map<String, Object> params) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (Objects.isNull(params)) {
            params = new HashMap<>();
        }
        NetUtil2.Response res = NetUtil2.get(url, params);
        String rawContent = Objects.isNull(res) ? null : res.rawContent;
        return rawContent;
    }
}
