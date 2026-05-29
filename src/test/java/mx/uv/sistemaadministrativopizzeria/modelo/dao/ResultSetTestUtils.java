package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

final class ResultSetTestUtils {

    private ResultSetTestUtils() {
    }

    static ResultSet resultSet(Map<String, Object> values) {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            String name = method.getName();

            if (args != null && args.length >= 1) {
                Object key = args[0];
                Object value = values.get(String.valueOf(key));

                switch (name) {
                    case "getInt":
                        return value instanceof Number ? ((Number) value).intValue() : 0;
                    case "getDouble":
                        return value instanceof Number ? ((Number) value).doubleValue() : 0d;
                    case "getString":
                        return value;
                    case "getObject":
                        return value;
                    case "getBytes":
                        return value;
                    case "getTimestamp":
                        return value;
                    case "getBlob":
                        return value;
                    default:
                        break;
                }
            }

            return defaultValue(method.getReturnType());
        };

        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class<?>[]{ResultSet.class},
                handler
        );
    }

    static Blob blob(byte[] bytes) {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            switch (method.getName()) {
                case "length":
                    return (long) bytes.length;
                case "getBytes":
                    long position = (long) args[0];
                    int length = (int) args[1];
                    int start = (int) position - 1;
                    byte[] copy = new byte[length];
                    System.arraycopy(bytes, start, copy, 0, length);
                    return copy;
                case "free":
                    return null;
                default:
                    throw new SQLException("Método no implementado en blob de prueba: " + method.getName());
            }
        };

        return (Blob) Proxy.newProxyInstance(
                Blob.class.getClassLoader(),
                new Class<?>[]{Blob.class},
                handler
        );
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0f;
        }
        if (type == double.class) {
            return 0d;
        }
        if (type == char.class) {
            return '\0';
        }
        return null;
    }
}
