import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class TestClass {
    @Autowired
    private HttpServletRequest request;
    @Test
    public void test(){
        File file = new File("./pyg_parent");
        System.out.println(file.getAbsolutePath());
        new File(file.getAbsolutePath() + "\\main\\webapp\\123.txt").delete();
    }
}
