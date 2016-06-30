import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationInfo {

    public void printAnnotation(Class<?> clazz){
        MyAnnotation ann = clazz.getAnnotation(MyAnnotation.class);  //Annotation 정보 얻기
        if(ann != null){
            int num = ann.num();
            String name = ann.name();
            System.out.println("num: " + num + ", name: " + name);
        }
    }

    public void onCreate(){
        Class clazz = MyData.class;
        printAnnotation(clazz);

        Method[] methods = clazz.getMethods();  //모든 메소드 획득

        for(Method m : methods){  //메소드 정보 출력
            printMethodInfo(m);
        }

        try {
            Method setMyInfoMethod = clazz.getMethod("setMyInfo", String.class);  //메소드 획득
            Object obj = clazz.newInstance();  //객체 생성
            setMyInfoMethod.invoke(obj, "My Info");  //메소드 호출
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void printMethodInfo(Method m){
        if(m != null){
            String methodName = m.getName();
            MyAnnotation ann = m.getAnnotation(MyAnnotation.class);

            //Annotation 정보 출력
            if(ann != null){
                int num = ann.num();
                String name = ann.name();
                System.out.println("method: " + methodName + ", " + num + ", " + name);
            }
        }
    }
}
