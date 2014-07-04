package com.outbrain.blog.app;

import com.google.gson.Gson;
import com.outbrain.blog.entities.BlogPost;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.core.Response;

/**
 * User: maromg
 * Date: 01/07/2014
 */
public class DemoDataPopulator {

    public void populate() {
        for (int i = 1; i <= 20; i++) {
            createBlogPost(i);
        }
    }

    private void createBlogPost(int i) {
        BlogPost blogPost = createBlogPostEntity(i);
        //todo replace with slf4j?
        System.out.println("================================");
        System.out.println("Posting to blog " + blogPost);
        postBlogPost(blogPost);
        System.out.println("Posted");
        System.out.println("================================");
    }

    private void postBlogPost(BlogPost blogPost) {
        String url = getUrl();
        String blogPostAsJson = new Gson().toJson(blogPost);

        ClientRequest request = prepareHttpRequest(url, blogPostAsJson);
        sendHttpRequest(request);
    }

    private ClientRequest prepareHttpRequest(String url, String body) {
        ClientRequest request = new ClientRequest(url);
        request.accept("application/json");
        request.body("application/json", body);
        return request;
    }

    private void sendHttpRequest(ClientRequest request) {
        try {
            ClientResponse response = request.post();
            if (response.getResponseStatus() != Response.Status.CREATED) {
                throw new RuntimeException("Could not post to blog " + request.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not post to blog " + request.getBody());
        }
    }

    private BlogPost createBlogPostEntity(int i) {
        BlogPost blogPost = new BlogPost();
        blogPost.setAuthorEmail("guy@marom.com");
        blogPost.setTitle("title " + i);
        blogPost.setContent("content " + i);
        return blogPost;
    }

    public String getServerPort() {
        return System.getProperty("server.port", "8080");
    }

    public String getUrl() {
        return "http://localhost:" + getServerPort() + "/blogposts";
    }
}
