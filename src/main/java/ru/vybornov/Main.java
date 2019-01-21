/*
 * $id$
 *
 */
package ru.vybornov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.jaxws.LocalJaxWsServiceFactoryBean;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Класс запуска приложения.
 *
 * @author vybornov
 * @version $Revision$
 */

@SpringBootApplication(scanBasePackages = "ru.vybornov")
public class Main extends SpringBootServletInitializer
{
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public LocalJaxWsServiceFactoryBean service() throws MalformedURLException
    {
        LocalJaxWsServiceFactoryBean bean = new LocalJaxWsServiceFactoryBean();
        bean.setNamespaceUri("http://web.cbr.ru/");
        bean.setServiceName("DailyInfo");
        bean.setWsdlDocumentUrl(new URL("http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx?wsdl"));
        return bean;
    }
}
