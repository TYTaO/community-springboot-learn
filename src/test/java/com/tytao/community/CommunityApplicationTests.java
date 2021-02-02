package com.tytao.community;

import com.tytao.community.enums.CommentTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testEnum(){
        Integer type = CommentTypeEnum.QUESTION.getType();
        System.out.println(type);
    }

    @Test
    void testSplits(){
        String[] s = StringUtils.split("aaa aa  b", " ");
        for (String s1 : s) {
            System.out.println(s1);
        }
    }



}
