/*
 * $id$
 *
 */
package ru.vybornov;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.WebServiceRef;

import ru.vybornov.model.Currency;
import ru.vybornov.model.RequestsCounter;
import ru.vybornov.model.Stats;
import ru.vybornov.model.ValuteData;
import ru.vybornov.model.soap.DailyInfoSoap;
import ru.vybornov.model.soap.GetCursOnDateXMLResponse;

/**
 * Доступ к значениям валют.
 *
 * @author vybornov
 * @version $Revision$
 */
@Service
public class CurrencyFacade
{
    private final static Logger log = LogManager.getLogger(CurrencyFacade.class);

    @WebServiceRef(wsdlLocation="http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx?wsdl")
    private DailyInfoSoap service;

    @Autowired
    private Dao dao;

    @PostConstruct
    public void init()
    {
        dao.init();
        Thread thread = new Thread(() -> {
            updateCurs();
            long currTime = System.currentTimeMillis();
            long oneHourInMillis = TimeUnit.HOURS.toMillis(1);
            String nextHour = new SimpleDateFormat("yyyyMMdd HH:05").format(new Date(currTime + oneHourInMillis));
            long waitingForNextHour;
            try
            {
                Date nextUpdate = new SimpleDateFormat("yyyyMMdd HH:mm").parse(nextHour);
                waitingForNextHour = nextUpdate.getTime() - currTime;
                if (waitingForNextHour < 1)
                {
                    waitingForNextHour = oneHourInMillis;
                }
            }
            catch (ParseException e)
            {
                log.error("Can't calculate time to next hour.", e);
                waitingForNextHour = oneHourInMillis;
            }
            try
            {
                Thread.sleep(waitingForNextHour);
                while (true)
                {
                    updateCurs();
                    Thread.sleep(oneHourInMillis);
                }
            }
            catch (InterruptedException e)
            {
                return;
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void updateCurs()
    {
        try
        {
            XMLGregorianCalendarImpl onDate = new XMLGregorianCalendarImpl(new GregorianCalendar());
            GetCursOnDateXMLResponse.GetCursOnDateXMLResult cursOnDateXML = service.getCursOnDateXML(onDate);
            if (cursOnDateXML.getContent() != null)
            {
                Iterator<Object> iterator = cursOnDateXML.getContent().iterator();
                if (iterator.hasNext())
                {
                    ElementNSImpl next = (ElementNSImpl) iterator.next();
                    JAXBContext jaxbContext = JAXBContext.newInstance(ValuteData.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    ValuteData result = (ValuteData) unmarshaller.unmarshal(next);
                    List<ValuteData.ValuteCurs> valuteCursOnDate = result.getValuteCursOnDate();
                    if (valuteCursOnDate != null)
                    {
                        for (ValuteData.ValuteCurs curs : valuteCursOnDate)
                        {
                            Currency currency = new Currency();
                            currency.setCode(curs.getCode());
                            currency.setCurs(curs.getCurs());
                            dao.updateCurrency(currency);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("Fail on updating curs.", e);
        }
    }

    public Currency getCurrency(int code)
    {
        return dao.getCurs(code);
    }

    public Stats getStats()
    {
        List<RequestsCounter> counters = dao.getCounters();
        Stats stats = new Stats();
        for (RequestsCounter counter : counters)
        {
            if (counter.getId() == 0)
            {
                stats.setSuccessRequests(counter.getRequests());
            }
            else if (counter.getId() == -1)
            {
                stats.setFailedRequests(counter.getRequests());
            }
            else
            {
                stats.getCurrencies().put(counter.getId(), counter.getRequests());
            }
        }
        return stats;
    }
}
