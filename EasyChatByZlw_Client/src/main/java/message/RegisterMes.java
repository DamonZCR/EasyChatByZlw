package message;

/**
 * 因注册信息RegisterMes的结构只包含User类，但是不可以直接用User类来替代。
 * type RegisterMes struct {
 * 	User User `json:"user"`//服务器端RegisterMes类型就是User结构体.
 * }
 * 不可以直接封装User即可，需要再RegisterMes中再去封装User。
 */
public class RegisterMes {
    private User user;
    public RegisterMes(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
