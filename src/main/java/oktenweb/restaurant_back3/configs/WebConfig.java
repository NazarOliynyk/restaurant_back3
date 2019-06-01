//package oktenweb.restaurant_back3.configs;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.validation.MessageCodesResolver;
//import org.springframework.validation.Validator;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.config.annotation.*;
//
//import java.io.File;
//import java.util.List;
//import java.util.Properties;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    //this mailSender and Environment might be commented
//
//
//
////        @Autowired
////        Environment env;
//
////        @Bean
////    public JavaMailSenderImpl javaMailSender(){
////        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
////        mailSender.setHost(env.getProperty("email.host"));
////        mailSender.setPort(Integer.parseInt(env.getProperty("email.port")));
////        mailSender.setUsername(env.getProperty("email.username"));
////        mailSender.setPassword(env.getProperty("email.password"));
////        Properties properties = mailSender.getJavaMailProperties();
////        properties.put(env.getProperty("email.protocol"), env.getProperty("email.protocol.val"));
////        properties.put(env.getProperty("email.auth"), env.getProperty("email.auth.val"));
////        properties.put(env.getProperty("email.starttls"), env.getProperty("email.starttls.val"));
////        properties.put(env.getProperty("email.mail.debug"), env.getProperty("email.mail.debug.val"));
////
////        return mailSender;
////    }
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
//
//        String pathToFolder = "D:\\FotoSpringPreliminary1"+File.separator;
//
////        this "/ava/**" is used in the form to display the photo
//
//        resourceHandlerRegistry.addResourceHandler("/ava/**").addResourceLocations("file:///"+pathToFolder);
//
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//
//        registry.addViewController("/changePassword").setViewName("changePassword");
//        registry.addViewController("/restaurants").setViewName("restaurant");
//
//    }
//
//    @Override
//    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
//
//    }
//
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {
//
//    }
//
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {
//
//    }
//
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
//
//    }
//
//    @Override
//    public void addFormatters(FormatterRegistry formatterRegistry) {
//
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
//
//    }
//
//
//
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//
//    }
//
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {
//
//    }
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {
//
//    }
//
//    @Override
//    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {
//
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
//
//    }
//
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {
//
//    }
//
//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
//
//    }
//
//    @Override
//    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
//
//    }
//
//    @Override
//    public Validator getValidator() {
//        return null;
//    }
//
//    @Override
//    public MessageCodesResolver getMessageCodesResolver() {
//        return null;
//    }
//}
