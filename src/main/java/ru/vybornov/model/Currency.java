/*
 * $id$
 *
 */
package ru.vybornov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Сущность валюты.
 *
 * @author vybornov
 * @version $Revision$
 */
@Entity
@Table(name = "Currency")
public class Currency
{
    @JsonProperty("code")
    @Id
    @Column
    private int code;

    @JsonProperty("curs")
    @Column
    private float curs;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public float getCurs()
    {
        return curs;
    }

    public void setCurs(float curs)
    {
        this.curs = curs;
    }
}
