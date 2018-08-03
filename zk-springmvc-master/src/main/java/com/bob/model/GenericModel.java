package com.bob.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

public abstract class GenericModel {

    @Transient
    private List<Link> links = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String url, String rel) {
        Link link = new Link();
        link.setRel(rel);
        link.setLink(url);
        links.add(link);
    }

}
