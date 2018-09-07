package com.daemonauth;

import com.google.gson.Gson;
import com.daemonauth.domain.Roles;
import com.daemonauth.domain.query.Query;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Type;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        Query<Roles> rolesQuery = new Query<Roles>();
        Roles roles = new Roles();
        roles.setRoleCode("1232");
        roles.setRoleName("333");
        rolesQuery.setQuery(roles);
        Gson gson = new Gson();
        Type type = rolesQuery.getClass().getGenericSuperclass();
        System.out.println(gson.toJson(rolesQuery, type));
        System.out.println(gson.toJson(roles));
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}
