import com.alibaba.fastjson.JSON;
import message.LoginMes;
import message.Message;
import message.SmsMes;
import message.User;

/**
 * 将Java对象转换为Json的结构与golang的结构体序列化结构相同，只不过有些属性的顺序不同。
 * 并且官方的教程中有：
 * 我们还可以自定义输出，并控制字段的排序，日期显示格式，序列化标记等。
 * 接下来我们更新 bean 并添加几个字段：
 * @JSONField(name="AGE", serialize=false)
 * private int age;
 * @JSONField(name="LAST NAME", ordinal = 2)
 * private String lastName;
 * @JSONField(name="FIRST NAME", ordinal = 1)
 * private String firstName;
 * @JSONField(name="DATE OF BIRTH", format="dd/MM/yyyy", ordinal = 3)
 * private Date dateOfBirth;
 * 以上代码中我们列出了基本参数类别，并使用 @JSONField 注解，以便实现自定义转换：
 *     format 参数用于格式化 date 属性。
 *     默认情况下， FastJson 库可以序列化 Java bean 实体， 但我们可以使用 serialize 指定字段不序列化。
 *     使用 ordinal 参数指定字段的顺序
 */
public class FastJsonTest {
    public static void main(String[] args) {
        // 将Java对象转为Json格式
        String s1 = JSON.toJSONString(new LoginMes(1, "1", "Damon"));
        String s = JSON.toJSONString(new Message("LoginMes", s1));

        System.out.println(s);
        System.out.println("--------------------------");

        // 将s1转为Java对象。
        LoginMes lm = JSON.parseObject(s1, LoginMes.class);
        System.out.println(lm.getUserId());
        System.out.println(lm.getUserPwd());
        System.out.println(lm.getUserName());

        // 将Java对象转为Json格式 2
        String s2 = JSON.toJSONString(new SmsMes("-消息内容-", 1, "1", "Damon", 1,"11"));
        String s3 = JSON.toJSONString(new Message("LoginMes", s2));
        System.out.println(s3);

    }
}
