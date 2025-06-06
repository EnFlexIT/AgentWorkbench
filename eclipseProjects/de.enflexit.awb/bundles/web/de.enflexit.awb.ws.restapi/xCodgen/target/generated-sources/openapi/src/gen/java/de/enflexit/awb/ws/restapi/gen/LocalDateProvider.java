package de.enflexit.awb.ws.restapi.gen;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

@Provider
public class LocalDateProvider implements ParamConverterProvider {

    public <T> ParamConverter<T> getConverter(Class<T> clazz, Type type, Annotation[] annotations) {
        if (clazz.getName().equals(LocalDate.class.getName())) {
            return new ParamConverter<T>() {
                @SuppressWarnings("unchecked")
                public T fromString(String value) {
                    return value!=null ? (T) LocalDate.parse(value) : null;
                }

                public String toString(T bean) {
                    return bean!=null ? bean.toString() : "";
                }
            };
        }
        return null;
    }
}