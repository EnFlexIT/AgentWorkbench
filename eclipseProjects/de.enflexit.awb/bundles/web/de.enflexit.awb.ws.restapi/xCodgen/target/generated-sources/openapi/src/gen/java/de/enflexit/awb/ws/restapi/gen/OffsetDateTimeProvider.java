package de.enflexit.awb.ws.restapi.gen;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;

@Provider
public class OffsetDateTimeProvider implements ParamConverterProvider {

    public <T> ParamConverter<T> getConverter(Class<T> clazz, Type type, Annotation[] annotations) {
        if (clazz.getName().equals(OffsetDateTime.class.getName())) {
            return new ParamConverter<T>() {
                @SuppressWarnings("unchecked")
                public T fromString(String value) {
                    return value != null ? (T) OffsetDateTime.parse(value) : null;
                }

                public String toString(T bean) {
                    return bean != null ? bean.toString() : "";
                }
            };
        }
        return null;
    }
}