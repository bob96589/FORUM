package com.bob.dao.impl;

import org.springframework.stereotype.Repository;

import com.bob.dao.CommentDao;
import com.bob.model.Comment;

@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment> implements CommentDao {

}
