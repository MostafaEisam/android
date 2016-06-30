
/**
 * RTTI(Run-Time Type Information)
 * Runtime중에 타입 정보를 알아낼 수 있으며 사용도 가능
 *
 * RunTime시에 객체와 클래스의 정보를 알아내는 방법
 * 1. 전통적 RTTI로 컴파일 시점에 사용가능한 모든 타입을 클래스에 내포
 * 2. Reflection 메커니즘으로 런타임시에 클래스 정보만을 알고 사용할 수 있는 것
 *    Annotation과 같이 사용한다.
 *
 * OOP의 다형성은 전통적 RTTI로 컴파일시 상속구조를 파악하여 동작하는 원리
 *
 * a.c -> a.o
 *         +    -- Link --> a.exe  --relocation--> Ram. 통째로 올린다
 * b.c -> b.o
 *
 * a.java -> a.class
 *              +    --> a.jar  ----> JVM. 참조시 메모리에 올린다.
 * b.java -> b.class                       (런타임시 코드를 만들고 실행) -> Reflection
 *
 * Annotation
 * Java class파일에 meta정보를 기술하여 활용하는 방식
 * ex) @Override, @Deprecated..
 * @Retention: SOURCE, CLASS, RUNTIME -> Annotation이 어디까지 정보를 가져갈꺼냐
 *
 * Target -> 어디에 정의할 수 있는가
 *      TYPE - class, interface, enum에서 사용 가능
METHOD - method에서 사용가능
 *      FIELD - field에 사용가능
 *      CONSTRUCTOR - 생성자에 사용가능
 *      PACKAGE - package에 사용가능
 *      PARAMETER - method의 parameter에 사용가능
 *      LOCAL_VARIABLE - method안의 local변수에 사용가능
 *      ANNOTATION_TYPE - Annotation을 정의할 때 사용가능
 *
 * Java Reflect란?
 *      동적으로 이름(String)을 이용하여 Java class를 다루는 방법
 *      Java class의 이름을 이용하여 class를 메모리에 불러와서 class의 객체를 생성
 *      class의 method나 field를 이름과 signature를 이용하여 획득한 다음, 함수 호출 또는 값의 설정 및 획득
 *
 * Proxy class란?
 *      Interface를 implement한 class나 객체를 동적으로 생성해 주기 위한 class
 *      reflect를 이용할 때 reflect로 생성한 object에 등록할 interface를 implements한 객체를 동적으로 생성
 * Proxy를 이용한 interface를 implements한 class 및 객체의 생성
 *      static Class<?> getProxyClass(ClassLoader loader, Class...<?> interfaces);
 *      static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler handler)
 * InvocationHandler
 *      newProxyInstance로 생성한 Object의 interface함수가 호출되면 InvocationHandler의 invoke가 호출됨
 *      Object invoke(Object proxy, Method method, Object[] args)
 *      Invoke함수에서는 method의 이름을 얻어와서 해당 method에 대한 처리후 결과를 return함
 */
public class Main {

    public static void main(String[] args) {
        AnnotationInfo annotationInfo = new AnnotationInfo();
        annotationInfo.onCreate();
    }
}
