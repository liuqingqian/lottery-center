package com.hermes.lotcenter;

import com.hermes.lotcenter.domain.DoBetDomain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDoBetDomain {

    @Autowired
    private DoBetDomain doBetDomain;

    @Test
    public void testConvertToName() {
        String haoMa = "4,4,4";
        List<String> sidedNames = doBetDomain.convertToName(haoMa);
        System.out.println("sidedNames = " + sidedNames);
    }
}
