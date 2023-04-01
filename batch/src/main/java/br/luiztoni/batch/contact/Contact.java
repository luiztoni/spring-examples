package br.luiztoni.batch.contact;

import java.util.Date;

public class Contact {
    private String name;
    private Date birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Contact(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
    }
}
