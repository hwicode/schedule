package hwicode.schedule.common.mdc;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RequiredArgsConstructor
public class ConnectionProxyHandler implements InvocationHandler {

    private final Object connection;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invokeResult = method.invoke(connection, args);
        if (method.getName().equals("prepareStatement")) {
            return Proxy.newProxyInstance(
                    invokeResult.getClass().getClassLoader(),
                    invokeResult.getClass().getInterfaces(),
                    new PreparedStatementProxyHandler(invokeResult)
            );
        }
        return invokeResult;
    }

}
