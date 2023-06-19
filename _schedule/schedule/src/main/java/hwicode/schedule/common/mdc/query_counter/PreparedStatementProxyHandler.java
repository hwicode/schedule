package hwicode.schedule.common.mdc.query_counter;

import hwicode.schedule.common.mdc.MdcLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class PreparedStatementProxyHandler implements InvocationHandler {

    private final Object preparedStatement;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isExecuteQuery(method) && isQueryCountSet()) {
            increaseQueryCount();
        }
        return method.invoke(preparedStatement, args);
    }

    private boolean isExecuteQuery(Method method) {
        String methodName = method.getName();
        return methodName.equals("executeQuery") || methodName.equals("execute") || methodName.equals("executeUpdate");
    }

    private boolean isQueryCountSet() {
        return MDC.get(MdcLoggingFilter.QUERY_COUNT) != null;
    }

    private void increaseQueryCount() {
        String queryCountString = MDC.get(MdcLoggingFilter.QUERY_COUNT);
        int queryCount = Integer.parseInt(queryCountString);
        queryCount++;
        MDC.put(MdcLoggingFilter.QUERY_COUNT, String.valueOf(queryCount));
    }

}
