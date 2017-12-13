package com.dashuai.framework.dao;

import com.dashuai.framework.common.Constants;
import com.dashuai.framework.common.util.ParseProCfg;
import com.dashuai.framework.vo.Students;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Session
 *
 * @author Wang Yishuai.
 * @date 2016/3/8 0008.
 * @Copyright(c) 2016 Wang Yishuai,USTC,SSE.
 */
public class Session {
    private final static Logger LOG = LoggerFactory.getLogger(Session.class);

    /**
     * 保存映射文件的属性和数据库字段的对应关系
     * <属性， 字段>，需要使用工具到xml中读取配置信息，也就是Students.xml的name属性和column属性
     */
    private Map<String, String> stringStringMap = new HashMap<>();

    /**
     * 保存数据库的表名
     */
    private String tableName;

    /**
     * 保存类的方法:暂时只存get方法，使能用反射机制调用
     */
    private String[] methodNames;

    private Session() {
        SAXReader saxReader = new SAXReader();
        URL confFile = Session.class.getClassLoader().getResource("Student.xml");

        try {
            // 解析为DOC文档对象
            String path = Optional.ofNullable(confFile).map(URL::getFile).orElse("");
            Document document = saxReader.read(path);
            // 获得配置文件的root节点
            Element rootElement = document.getRootElement();
            // 获得root的子节点集合list
            List list = rootElement.elements();

            for (Object l : list) {
                Element nodeElement = (Element) l;
                tableName = nodeElement.attributeValue("table");

                // 获得子节点的子节点集合 property
                List list1 = nodeElement.elements();

                for (Object object : list1) {
                    Element node = (Element) object;
                    // 保存类的属性和数据库字段
                    stringStringMap.put(node.attributeValue("name"), node.attributeValue("column"));
                }

                methodNames = new String[stringStringMap.size()];
            }
        } catch (DocumentException e) {
            LOG.error("saxReader.read(file)", e);
            throw new RuntimeException(e);
        }
    }

    public static Session newInstance() {
        return new Session();
    }

    /**
     * 拼接insert into students (sid,sname) values (?,?);
     *
     * @return String
     */
    private String createSQL() {
        String s = "";
        String s1 = "";
        int index = 0;

        for (String key : stringStringMap.keySet()) {
            String val = stringStringMap.get(key);
            // 拼接 sid, sname
            s += val + ",";
            // get方法名字保存
            methodNames[index++] = "get" + Character.toUpperCase(key.charAt(0)) + key.substring(1);
        }

        // 去掉最后多余的逗号
        s = s.substring(0, s.length() - 1);

        // 拼接后续的 ？,？
        for (String str : stringStringMap.keySet()) {
            s1 += "?,";
        }

        // 去掉最后多余的逗号
        s1 = s1.substring(0, s1.length() - 1);

        return "insert into " + tableName + "(" + s + ") values (" + s1 + ")";
    }

    /**
     * 封装JDBC
     *
     * @param students Students
     * @param sql      String
     */
    private void excuteJdbc(Students students, String sql) {
        // 简单的封装JDBC
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Properties properties = ParseProCfg.INSTANCE.parseFile();
        String url = "jdbc:mysql://" + properties.getProperty("dashuaiFramework.database.url", Constants.DefaultJDBCUrl.valueOf());
        String password = properties.getProperty("dashuaiFramework.database.password");
        String username = properties.getProperty("dashuaiFramework.database.username");

        try {
            Class.forName(properties.getProperty("dashuaiFramework.jdbc"));
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);

            // 反射调用get方法，解耦和
            for (int i = 0; i < methodNames.length; i++) {
                Method method = students.getClass().getMethod(methodNames[i]);
                Class clazz = method.getReturnType();

                if ("java.lang.Integer".equals(clazz.getName()) || "int".equals(clazz.getName())) {
                    int value = (int) method.invoke(students);
                    preparedStatement.setInt(i + 1, value);
                }

                if ("java.lang.String".equals(clazz.getName())) {
                    String value = (String) method.invoke(students);
                    preparedStatement.setString(i + 1, value);
                }
            }

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            LOG.error("Class.forName(\"com.mysql.jdbc.Driver\");", e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            LOG.error("DriverManager.getConnection(url)", e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            LOG.error("students.getClass().getMethod(methodNames[i]);", e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOG.error("method.invoke(students);", e);
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    LOG.error("preparedStatement.close();", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("connection.close();", e);
                }
            }
        }
    }

    /**
     * 保存对象到数据库
     *
     * @param students Students
     */
    public void save(Students students) {
        LOG.info("save");
        String sql = createSQL();
        LOG.info("SQL: {}", sql);
        excuteJdbc(students, sql);
    }
}
