/*
 * $id$
 *
 */
package ru.vybornov.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ответ на запрос курса валют.
 *
 * @author vybornov
 * @version $Revision$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ValuteData")
public class ValuteData
{
    @XmlAttribute(name = "OnDate")
    private String onDate;

    @XmlElement(name = "ValuteCursOnDate")
    private List<ValuteCurs> valuteCursOnDate;

    public String getOnDate()
    {
        return onDate;
    }

    public void setOnDate(String onDate)
    {
        this.onDate = onDate;
    }

    public List<ValuteCurs> getValuteCursOnDate()
    {
        return valuteCursOnDate;
    }

    public void setValuteCursOnDate(List<ValuteCurs> valuteCursOnDate)
    {
        this.valuteCursOnDate = valuteCursOnDate;
    }

    @Override
    public String toString()
    {
        return "ValuteData{" +
                "onDate='" + onDate + '\'' +
                ", valuteCursOnDate=" + valuteCursOnDate +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ValuteCurs {
        @XmlElement(name = "Vname")
        private String name;

        @XmlElement(name = "Vnom")
        private int nom;

        @XmlElement(name = "Vcurs")
        private float curs;

        @XmlElement(name = "Vcode")
        private int code;

        @XmlElement(name = "VchCode")
        private String chCode;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getNom()
        {
            return nom;
        }

        public void setNom(int nom)
        {
            this.nom = nom;
        }

        public float getCurs()
        {
            return curs;
        }

        public void setCurs(float curs)
        {
            this.curs = curs;
        }

        public int getCode()
        {
            return code;
        }

        public void setCode(int code)
        {
            this.code = code;
        }

        public String getChCode()
        {
            return chCode;
        }

        public void setChCode(String chCode)
        {
            this.chCode = chCode;
        }

        @Override
        public String toString()
        {
            return "ValuteCurs{" +
                    "name='" + name + '\'' +
                    ", nom=" + nom +
                    ", curs=" + curs +
                    ", code=" + code +
                    ", chCode='" + chCode + '\'' +
                    '}';
        }
    }
}
