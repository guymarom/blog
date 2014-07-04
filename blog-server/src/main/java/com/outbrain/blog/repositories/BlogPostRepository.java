package com.outbrain.blog.repositories;

import com.outbrain.blog.entities.BlogPost;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * User: maromg
 * Date: 30/06/2014
 */
@RepositoryRestResource(collectionResourceRel = "blogposts", path = "blogposts")
public interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, Long> {
}
