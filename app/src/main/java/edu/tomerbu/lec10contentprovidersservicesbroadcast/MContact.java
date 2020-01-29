package edu.tomerbu.lec10contentprovidersservicesbroadcast;

import java.io.Serializable;
import java.util.List;

// implements Serializable
//now we can pass our object in putExtra (bundles)
public class MContact implements Serializable {
    private String name;
    private String id;
    private List<String> phones;
    private List<String> emails;

    //ctor
    public MContact(String name, String id, List<String> phones, List<String> emails) {
        this.name = name;
        this.id = id;
        this.phones = phones;
        this.emails = emails;
    }

    //getters & setters:
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<String> getPhones() {
        return phones;
    }
    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
    public List<String> getEmails() {
        return emails;
    }
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        return "MContact{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", phones=" + phones +
                ", emails=" + emails +
                '}';
    }
}
