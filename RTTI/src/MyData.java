

@MyAnnotation(num=0, name="MyData")
public class MyData {

    String info;

    @MyAnnotation(num=1, name="setMyInfo")
    public void setMyInfo(String info){

    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
