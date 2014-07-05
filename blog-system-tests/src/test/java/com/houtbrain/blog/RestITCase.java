package com.houtbrain.blog;

import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.outbrain.blog.app.BlogApplicationLoader;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.delete;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * User: maromg
 * Date: 05/07/2014
 * <p/>
 * This class tests the REST API of the application.
 * Only the APIs used in the application are tested, these are:
 * -Fetching blog posts (Specifically, the last 10 blog posts)
 * -Fetching a specific blog post
 * -Creating a new blog post
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BlogApplicationLoader.BlogApplicationContext.class)
@WebAppConfiguration
@IntegrationTest
public class RestITCase {

    private Gson gson = new Gson();

    @After
    public void cleanUp() {
        List<BlogPost> allBlogPosts = fetchLastBlogPosts(Integer.MAX_VALUE);
        for (BlogPost blogPost : allBlogPosts) {
            deleteBlogPost(blogPost.getSelfLink());
        }
    }

    private void deleteBlogPost(String selfLink) {
        delete(selfLink);
    }

    @Test
    public void testAddingAndFetching() {
        createRandomPosts(20);
        List<BlogPost> allBlogPosts = fetchLastBlogPosts(Integer.MAX_VALUE);
        assertThat(allBlogPosts, hasSize(20));
    }

    @Test
    public void testFetchingOfLast10() {
        createRandomPosts(20);
        List<BlogPost> blogPosts = fetchLastBlogPosts(10);

        assertThat(blogPosts, hasSize(10));
    }

    @Test
    public void testCreateAndFetchPost() {
        BlogPost expectedBlogPost = createOneBlogPost();

        //Fetch blog posts
        List<BlogPost> allBlogPosts = fetchLastBlogPosts(Integer.MAX_VALUE);
        BlogPost actualBlogPost = allBlogPosts.get(0);

        //Test
        assertThat(actualBlogPost.getAuthorEmail(), equalTo(expectedBlogPost.getAuthorEmail()));
        assertThat(actualBlogPost.getTitle(), equalTo(expectedBlogPost.getTitle()));
        assertThat(actualBlogPost.getContent(), equalTo(expectedBlogPost.getContent()));
    }

    private BlogPost createOneBlogPost() {
        BlogPost blogPost = createBlogPost("email@email.co.il", "tiasdfasdftle", "conasdfasdftent");
        String blogPostJson = blogPostToJson(blogPost);
        httpPostBlogPost(blogPostJson);
        return blogPost;
    }

    private BlogPost createBlogPost(String authorEmail, String title, String content) {
        BlogPost expectedBlogPost = new BlogPost();
        expectedBlogPost.setAuthorEmail(authorEmail);
        expectedBlogPost.setTitle(title);
        expectedBlogPost.setContent(content);
        return expectedBlogPost;
    }

    private void createRandomPosts(int amount) {
        for (int i = 0; i < amount; i++) {
            String prefix = UUID.randomUUID().toString();
            createRandomBlogPost(prefix);
        }
    }

    private List<BlogPost> fetchLastBlogPosts(int ammount) {
        Response response = given()
                .queryParam("page", "0")
                .queryParam("size", ammount)
                .queryParam("sort", "id,desc")
                .get(getUrl());
        ResponseBody body = response.getBody();
        if (body.jsonPath().get("_embedded") != null) {
            return convertToList(response.getBody().jsonPath().get("_embedded.blogposts"));
        } else {
            return new ArrayList<>();
        }
    }

    private List<BlogPost> convertToList(Object obj) {
        @SuppressWarnings("unchecked") List<Map<String, ?>> blogPostsRawData = (ArrayList) obj;
        List<BlogPost> result = new ArrayList<>(blogPostsRawData.size());
        for (Map<String, ?> blogPostValues : blogPostsRawData) {
            result.add(convertToBlogPost(blogPostValues));
        }
        return result;
    }

    private BlogPost convertToBlogPost(Map<String, ?> blogPostValues) {
        BlogPost blogPost = new BlogPost();
        blogPost.setContent((String) blogPostValues.get("content"));
        blogPost.setTitle((String) blogPostValues.get("title"));
        blogPost.setAuthorEmail((String) blogPostValues.get("authorEmail"));
        blogPost.setSelfLink(extractSelfLink(blogPostValues.get("_links")));
        return blogPost;
    }

    private String extractSelfLink(Object links) {
        @SuppressWarnings("unchecked") Map<String, Map> linksRawData = (Map) links;
        return (String) linksRawData.get("self").get("href");
    }

    private void createRandomBlogPost(String prefix) {
        BlogPost blogPost = createBlogPostEntity(prefix);
        String blogPostJson = blogPostToJson(blogPost);
        httpPostBlogPost(blogPostJson);
    }

    private void httpPostBlogPost(String blogPostJson) {
        with()
                .header("Content-Type", "application/json")
                .body(blogPostJson)
                .post(getUrl())
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    private String blogPostToJson(BlogPost blogPost) {
        return gson.toJson(blogPost);
    }

    private BlogPost createBlogPostEntity(String prefix) {
        BlogPost blogPost = new BlogPost();
        blogPost.setAuthorEmail("email@email.com");
        blogPost.setTitle(prefix + " title");
        blogPost.setContent(prefix + " content");
        return blogPost;
    }

    private URI getUrl() {
        try {
            String host = getHost();
            String port = getPort();
            return new URI("http://" + host + ":" + port + "/blogposts");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getPort() {
        return System.getProperty("server.port", "8080");
    }

    private String getHost() {
        return System.getProperty("server.host", "localhost");
    }

}
