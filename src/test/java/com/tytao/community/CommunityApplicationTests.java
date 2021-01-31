package com.tytao.community;

import com.tytao.community.enums.CommentTypeEnum;
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



}
