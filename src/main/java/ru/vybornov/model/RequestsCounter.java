/*
 * $id$
 *
 */
package ru.vybornov.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Сущность счетчиков.
 *
 * @author vybornov
 * @version $Revision$
 */
@Entity
@Table(name = "RequestsCounter")
public class RequestsCounter
{
    @Id
    @Column
    private int id;

    @Column
    private long requests;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public long getRequests()
    {
        return requests;
    }

    public void setRequests(long requests)
    {
        this.requests = requests;
    }
}
