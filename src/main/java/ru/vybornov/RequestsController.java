/*
 * $id$
 *
 */
package ru.vybornov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.vybornov.model.Currency;
import ru.vybornov.model.Stats;

/**
 * Обработчик запросов.
 *
 * @author vybornov
 * @version $Revision$
 */
@Controller
public class RequestsController
{
    private final static Logger log = LogManager.getLogger(RequestsController.class);

    private CurrencyFacade currencyFacade;

    @Autowired
    public RequestsController(CurrencyFacade currencyFacade)
    {
        this.currencyFacade = currencyFacade;
    }

    @RequestMapping(value = "/action", method = RequestMethod.POST)
    public @ResponseBody Currency getCurrency(@RequestBody Currency cur)
    {
        log.debug("Requested currency with code: {}", cur.getCode());
        return currencyFacade.getCurrency(cur.getCode());
    }

    @RequestMapping(value = "/stat", method = RequestMethod.POST)
    public @ResponseBody Stats getStats()
    {
        log.debug("Stats requested");
        return currencyFacade.getStats();
    }
}
