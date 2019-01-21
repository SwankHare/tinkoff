/*
 * $id$
 *
 */
package ru.vybornov;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import ru.vybornov.model.Currency;
import ru.vybornov.model.RequestsCounter;

/**
 * Доступ к БД.
 *
 * @author vybornov
 * @version $Revision$
 */
@Service
public class Dao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void init()
    {
        // резервация счетчико для провальных и успешных запросов
        RequestsCounter success = new RequestsCounter();
        success.setId(0);
        success.setRequests(0);

        RequestsCounter failed = new RequestsCounter();
        failed.setId(-1);
        failed.setRequests(0);

        entityManager.persist(success);
        entityManager.persist(failed);
    }

    @Transactional
    public void updateCurrency(Currency currency)
    {
        Currency cur = entityManager.find(Currency.class, currency.getCode());
        if (cur == null)
        {
            entityManager.persist(currency);
            RequestsCounter counter = new RequestsCounter();
            counter.setId(currency.getCode());
            counter.setRequests(0);
            entityManager.persist(counter);
        }
        else
        {
            entityManager.merge(currency);
        }
    }

    @Transactional
    public void incrementSuccessRequests()
    {
        Query q = entityManager.createQuery("update RequestsCounter set requests = requests + 1 where id = 0");
        q.executeUpdate();
    }

    @Transactional
    public void incrementFailedRequests()
    {
        Query q = entityManager.createQuery("update RequestsCounter set requests = requests + 1 where id = -1");
        q.executeUpdate();
    }

    @Transactional
    public Currency getCurs(int code)
    {
        Currency currency = entityManager.find(Currency.class, code);
        if (currency != null)
        {
            synchronized (this)
            {
                RequestsCounter requestsCounter = entityManager.find(RequestsCounter.class, code);
                if (requestsCounter == null)
                {
                    requestsCounter = new RequestsCounter();
                    requestsCounter.setId(code);
                    requestsCounter.setRequests(1);
                    entityManager.persist(requestsCounter);
                }
                else
                {
                    requestsCounter.setRequests(requestsCounter.getRequests() + 1);
                    entityManager.merge(requestsCounter);
                }
            }
            incrementSuccessRequests();
        }
        else
        {
            incrementFailedRequests();
        }
        return currency;
    }

    public List<RequestsCounter> getCounters()
    {
        CriteriaQuery<RequestsCounter> query = entityManager.getCriteriaBuilder().createQuery(RequestsCounter.class);
        Root<RequestsCounter> from = query.from(RequestsCounter.class);
        query.select(from);
        return entityManager.createQuery(query).getResultList();
    }
}
