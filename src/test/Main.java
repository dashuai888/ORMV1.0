package test;

import dao.Session;
import vo.Students;

/**
 * Main
 *
 * @author Wang Yishuai.
 * @date 2016/3/8 0008.
 * @Copyright(c) 2016 Wang Yishuai,USTC,SSE.
 */
public class Main {
    public static void main(String[] args) {
        Session session = Session.newInstance();

        Students students = new Students();
        students.setSid(0);
        students.setSname("dashuai");

        session.save(students);
    }
}
