package com.bob.dao;

import java.util.List;

import com.bob.model.Tag;

public interface TagDao {

	public void saveOrUpdate(Tag tag);

	public List<Tag> getAll();
	
}
