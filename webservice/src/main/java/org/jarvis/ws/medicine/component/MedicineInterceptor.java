package org.jarvis.ws.medicine.component;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MedicineInterceptor implements Interceptor {

    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("db interceptor invoke");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
