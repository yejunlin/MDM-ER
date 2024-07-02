package org.example.aidemo;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * web 配置，设置 JSON 格式的自定义数据转换器
 *
 * @author Floki 2023-10-28
 */
@Configuration
public class JsonMessageConverterConfigurer implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);

        fastJsonConfig.setReaderFeatures(
                // 字段如 vBtn  会被转为 VBtn  处理这样的问题
                JSONReader.Feature.SupportSmartMatch,
                JSONReader.Feature.FieldBased,
                // 初始化String字段为空字符串""
                //JSONReader.Feature.InitStringFieldAsEmpty,
                // 对读取到的字符串值做trim处理
                JSONReader.Feature.TrimString);

        fastJsonConfig.setWriterFeatures(
                // 字段如 vBtn  会被转为 VBtn  处理这样的问题
                JSONWriter.Feature.FieldBased,
                // long 转 string 丢失精度问题
                JSONWriter.Feature.WriteLongAsString,
                // 保留 map 空的字段
                JSONWriter.Feature.WriteMapNullValue,
                // 将 List 类型的 null 转成 []
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 将 String 类型的 null 转成 ""
                JSONWriter.Feature.WriteNullStringAsEmpty,
                //将 Boolean 类型的 null 转成 false
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                // 将空置输出为缺省值，Number类型的null都输出为0，String类型的null输出为""，数组和Collection类型的输出为[]
                //JSONWriter.Feature.NullAsDefaultValue,
                // 日期格式转换
                JSONWriter.Feature.PrettyFormat);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonHttpMessageConverter);
    }

}
