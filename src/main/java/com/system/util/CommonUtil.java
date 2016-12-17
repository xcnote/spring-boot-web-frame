package com.system.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.http.converter.HttpMessageConversionException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

	private final static ObjectMapper objectMapper = new ObjectMapper();
	private final static ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>(64);
	
	static {
		objectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = new StringBuffer();
		buffer.append(stringWriter.getBuffer());
		return buffer.toString();
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param obj 准备转换的对象
	 * @return json字符串
	 * @throws Exception
	 */
	public static String object2json(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}
	
	/**
	 * 将json字符串转换成java对象
	 * 
	 * @param json
	 *            准备转换的json字符串
	 * @param cls
	 *            准备转换的类
	 * @return
	 * @throws Exception
	 */
	public static <T> T json2object(String _json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException {
        if (_json == null || _json.equals(""))
            _json = "{}";
        return (T) objectMapper.readValue(_json, cls);
    }
	
	public static String object2xml(Object obj) throws JAXBException{
		JAXBContext jaxbContext = getJaxbContext(obj.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		Writer writer = new StringWriter();
		marshaller.marshal(obj, writer);
		return writer.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T xml2object(String xml,Class<T> cls) throws JAXBException{
		JAXBContext jaxbContext = getJaxbContext(cls);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
	}
	
	private final static JAXBContext getJaxbContext(Class<?> clazz) {
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz);
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            } catch (JAXBException ex) {
                throw new HttpMessageConversionException(
                        "Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }
}
