package d.tsaplya.guidelead;

public class TheradMessage<T> {

    private String msg;
    private T load;

    public TheradMessage(String msg,T load){
        this.msg = msg;
        this.load = load;
    }

    public String getMsg() {
        return msg;
    }

    public T getLoad() {
        return load;
    }
}
