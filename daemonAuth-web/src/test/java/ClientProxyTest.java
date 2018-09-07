import com.daemonauth.domain.Roles;
import com.daemonauth.domain.query.Query;
import com.daemonauth.export.rpc.AuthorityResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * User:
 * Date: 15-1-30
 * Time: 下午7:12
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-config.xml")
public class ClientProxyTest {


    @Resource(name = "authorityResource")
    AuthorityResourceService authorityResource;

    /**
     * 通过代理减少代码量
     */
    @Test
    public void requestByProxy() {
        Query<Roles> rolesQuery = new Query<Roles>();
        Roles roles = new Roles();
        roles.setRoleCode("admin");
        rolesQuery.setQuery(roles);
        List<Roles> rolesList = authorityResource.getRolesList(rolesQuery);
        System.out.println(rolesList);

    }
}
