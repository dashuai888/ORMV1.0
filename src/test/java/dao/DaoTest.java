package dao;

import com.dashuai.framework.dao.Session;
import com.dashuai.framework.vo.Students;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DaoTest
 *
 * @author Wang Yishuai.
 * @date 2016/3/8 0008.
 * @Copyright(c) 2016 Wang Yishuai,USTC,SSE.
 */
public class DaoTest {
    private static final Logger LOG = LoggerFactory.getLogger(DaoTest.class);
    private Session session;

    @Before
    public void init(){
        this.session = Session.newInstance();
    }

    @Test
    public void saveObjTest() {
        Students students = new Students();
        students.setSid(0);
        students.setSname("dashuai");

        this.session.save(students);
    }
}
